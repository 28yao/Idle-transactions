package com.campus.marketplace.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.marketplace.dto.response.ConversationResponse;
import com.campus.marketplace.entity.Conversation;
import com.campus.marketplace.entity.Product;
import com.campus.marketplace.entity.User;
import com.campus.marketplace.exception.BusinessException;
import com.campus.marketplace.exception.ErrorCode;
import com.campus.marketplace.mapper.ConversationMapper;
import com.campus.marketplace.mapper.ProductMapper;
import com.campus.marketplace.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationMapper conversationMapper;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;

    @Transactional
    public ConversationResponse createOrGetConversation(Long userId, Long productId) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        if (product.getSellerId().equals(userId)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST.getCode(), "不能与自己发起对话");
        }

        Long sellerId = product.getSellerId();
        Long user1 = Math.min(userId, sellerId);
        Long user2 = Math.max(userId, sellerId);

        // 查找已有会话
        LambdaQueryWrapper<Conversation> w = new LambdaQueryWrapper<>();
        w.eq(Conversation::getUser1Id, user1)
         .eq(Conversation::getUser2Id, user2)
         .eq(Conversation::getProductId, productId);
        Conversation conv = conversationMapper.selectOne(w);

        if (conv == null) {
            conv = new Conversation();
            conv.setUser1Id(user1);
            conv.setUser2Id(user2);
            conv.setProductId(productId);
            conversationMapper.insert(conv);
        }

        return buildResponse(conv, userId);
    }

    public List<ConversationResponse> listConversations(Long userId) {
        LambdaQueryWrapper<Conversation> w = new LambdaQueryWrapper<>();
        w.and(q -> q.eq(Conversation::getUser1Id, userId).or().eq(Conversation::getUser2Id, userId));
        w.orderByDesc(Conversation::getLastMessageAt);
        List<Conversation> list = conversationMapper.selectList(w);

        List<ConversationResponse> result = new ArrayList<>();
        for (Conversation conv : list) {
            result.add(buildResponse(conv, userId));
        }
        return result;
    }

    public ConversationResponse getConversation(Long conversationId, Long userId) {
        Conversation conv = conversationMapper.selectById(conversationId);
        if (conv == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        if (!conv.getUser1Id().equals(userId) && !conv.getUser2Id().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        return buildResponse(conv, userId);
    }

    @Transactional
    public void deleteConversation(Long conversationId, Long userId) {
        Conversation conv = conversationMapper.selectById(conversationId);
        if (conv == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        if (!conv.getUser1Id().equals(userId) && !conv.getUser2Id().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        conversationMapper.deleteById(conversationId);
    }

    @Transactional
    public void markAsRead(Long conversationId, Long userId) {
        Conversation conv = conversationMapper.selectById(conversationId);
        if (conv == null) return;
        if (conv.getUser1Id().equals(userId)) {
            conv.setUnreadCount1(0);
        } else {
            conv.setUnreadCount2(0);
        }
        conversationMapper.updateById(conv);
    }

    private ConversationResponse buildResponse(Conversation conv, Long currentUserId) {
        ConversationResponse r = new ConversationResponse();
        r.setId(conv.getId());
        r.setProductId(conv.getProductId());
        r.setLastMessage(conv.getLastMessage());
        r.setLastMessageAt(conv.getLastMessageAt());
        r.setCreatedAt(conv.getCreatedAt());

        // 未读数
        if (conv.getUser1Id().equals(currentUserId)) {
            r.setUnreadCount(conv.getUnreadCount1() != null ? conv.getUnreadCount1() : 0);
        } else {
            r.setUnreadCount(conv.getUnreadCount2() != null ? conv.getUnreadCount2() : 0);
        }

        // 对方用户
        Long otherUserId = conv.getUser1Id().equals(currentUserId) ? conv.getUser2Id() : conv.getUser1Id();
        User other = userMapper.selectById(otherUserId);
        if (other != null) {
            r.setOtherUserId(other.getId());
            r.setOtherNickname(other.getNickname());
            r.setOtherAvatar(other.getAvatar());
        }

        // 商品信息
        Product product = productMapper.selectById(conv.getProductId());
        if (product != null) {
            r.setSellerId(product.getSellerId());
            r.setProductTitle(product.getTitle());
            r.setProductPrice(product.getPrice());
        }

        return r;
    }
}
