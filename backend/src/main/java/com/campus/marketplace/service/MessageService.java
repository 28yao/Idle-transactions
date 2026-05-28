package com.campus.marketplace.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.marketplace.dto.request.MessageSendRequest;
import com.campus.marketplace.dto.response.MessageResponse;
import com.campus.marketplace.entity.Conversation;
import com.campus.marketplace.entity.Message;
import com.campus.marketplace.entity.User;
import com.campus.marketplace.exception.BusinessException;
import com.campus.marketplace.exception.ErrorCode;
import com.campus.marketplace.mapper.ConversationMapper;
import com.campus.marketplace.mapper.MessageMapper;
import com.campus.marketplace.mapper.UserMapper;
import com.campus.marketplace.websocket.ChatWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageMapper messageMapper;
    private final ConversationMapper conversationMapper;
    private final UserMapper userMapper;
    private final ChatWebSocketHandler chatWebSocketHandler;

    @Transactional
    public MessageResponse sendMessage(Long senderId, MessageSendRequest request) {
        Conversation conv = conversationMapper.selectById(request.getConversationId());
        if (conv == null) {
            throw new BusinessException(ErrorCode.CONVERSATION_NOT_FOUND);
        }
        if (!conv.getUser1Id().equals(senderId) && !conv.getUser2Id().equals(senderId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        Message msg = new Message();
        msg.setConversationId(request.getConversationId());
        msg.setSenderId(senderId);
        msg.setContent(request.getContent());
        msg.setType(request.getType() == null ? 1 : request.getType());
        msg.setPriceOffer(request.getPriceOffer());
        msg.setOfferStatus(null);
        messageMapper.insert(msg);

        // 更新会话最后消息
        conv.setLastMessage(request.getContent());
        conv.setLastMessageAt(msg.getCreatedAt());
        conversationMapper.updateById(conv);

        // 通过 WebSocket 通知对方
        Long receiverId = conv.getUser1Id().equals(senderId) ? conv.getUser2Id() : conv.getUser1Id();
        MessageResponse response = buildResponse(msg);
        Map<String, Object> wsMessage = new HashMap<>();
        wsMessage.put("type", "new_message");
        wsMessage.put("data", response);
        chatWebSocketHandler.sendToUser(receiverId, wsMessage);

        return response;
    }

    public List<MessageResponse> getMessages(Long conversationId, Long userId, int limit) {
        Conversation conv = conversationMapper.selectById(conversationId);
        if (conv == null) {
            throw new BusinessException(ErrorCode.CONVERSATION_NOT_FOUND);
        }
        if (!conv.getUser1Id().equals(userId) && !conv.getUser2Id().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        LambdaQueryWrapper<Message> w = new LambdaQueryWrapper<>();
        w.eq(Message::getConversationId, conversationId)
         .orderByDesc(Message::getCreatedAt)
         .last("LIMIT " + Math.min(limit, 100));

        List<Message> list = messageMapper.selectList(w);
        List<MessageResponse> result = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            result.add(buildResponse(list.get(i)));
        }
        return result;
    }

    @Transactional
    public MessageResponse handleOffer(Long messageId, Long userId, Integer action) {
        Message msg = messageMapper.selectById(messageId);
        if (msg == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        if (msg.getType() != 3 && msg.getType() != 4) {
            throw new BusinessException(ErrorCode.BAD_REQUEST.getCode(), "该消息不是出价/还价消息");
        }
        if (msg.getOfferStatus() != null && msg.getOfferStatus() != 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST.getCode(), "该出价已处理");
        }

        Conversation conv = conversationMapper.selectById(msg.getConversationId());
        if (conv == null) {
            throw new BusinessException(ErrorCode.CONVERSATION_NOT_FOUND);
        }

        // 只有对方可以处理出价
        Long otherUserId = conv.getUser1Id().equals(msg.getSenderId()) ? conv.getUser2Id() : conv.getUser1Id();
        if (!otherUserId.equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        msg.setOfferStatus(action); // 1接受 2拒绝
        messageMapper.updateById(msg);

        return buildResponse(msg);
    }

    private MessageResponse buildResponse(Message msg) {
        MessageResponse r = new MessageResponse();
        r.setId(msg.getId());
        r.setConversationId(msg.getConversationId());
        r.setSenderId(msg.getSenderId());
        r.setContent(msg.getContent());
        r.setType(msg.getType());
        r.setPriceOffer(msg.getPriceOffer());
        r.setOfferStatus(msg.getOfferStatus());
        r.setCreatedAt(msg.getCreatedAt());

        User sender = userMapper.selectById(msg.getSenderId());
        if (sender != null) {
            r.setSenderNickname(sender.getNickname());
            r.setSenderAvatar(sender.getAvatar());
        }

        return r;
    }
}
