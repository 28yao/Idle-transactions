package com.campus.marketplace.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.marketplace.dto.request.VerifyRequest;
import com.campus.marketplace.dto.response.ReviewResponse;
import com.campus.marketplace.dto.response.UserProfileResponse;
import com.campus.marketplace.entity.Product;
import com.campus.marketplace.entity.Review;
import com.campus.marketplace.entity.Transaction;
import com.campus.marketplace.entity.User;
import com.campus.marketplace.exception.BusinessException;
import com.campus.marketplace.exception.ErrorCode;
import com.campus.marketplace.mapper.ProductMapper;
import com.campus.marketplace.mapper.ReviewMapper;
import com.campus.marketplace.mapper.TransactionMapper;
import com.campus.marketplace.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final ProductMapper productMapper;
    private final TransactionMapper transactionMapper;
    private final ReviewMapper reviewMapper;

    /**
     * verifyStatus: 0未认证 1审核中 2已认证 3认证失败
     */
    public void submitVerify(Long userId, VerifyRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        Integer status = user.getVerifyStatus();
        if (status != null && (status == 1 || status == 2)) {
            // 审核中 或 已认证 不允许重复提交
            throw new BusinessException(ErrorCode.VERIFY_FAILED.getCode(),
                    status == 1 ? "已提交认证，请等待审核" : "您已通过实名认证");
        }
        user.setRealName(request.getRealName());
        user.setVerifyImage(request.getVerifyImage());
        user.setVerifyStatus(1);
        userMapper.updateById(user);
    }

    public UserProfileResponse getUserProfile(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        UserProfileResponse r = UserProfileResponse.fromEntity(user);

        // 发布数量
        LambdaQueryWrapper<Product> pw = new LambdaQueryWrapper<>();
        pw.eq(Product::getSellerId, userId);
        r.setPublishCount((int) productMapper.selectCount(pw));

        // 成交数量（状态=已完成）
        LambdaQueryWrapper<Transaction> tw = new LambdaQueryWrapper<>();
        tw.and(q -> q.eq(Transaction::getBuyerId, userId).or().eq(Transaction::getSellerId, userId));
        tw.eq(Transaction::getStatus, 2); // COMPLETED
        r.setTransactionCount((int) transactionMapper.selectCount(tw));

        // 最近评价
        LambdaQueryWrapper<Review> rw = new LambdaQueryWrapper<>();
        rw.eq(Review::getTargetId, userId).orderByDesc(Review::getCreatedAt).last("LIMIT 10");
        List<Review> reviews = reviewMapper.selectList(rw);
        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (Review review : reviews) {
            ReviewResponse rr = ReviewResponse.fromEntity(review);
            User reviewer = userMapper.selectById(review.getReviewerId());
            if (reviewer != null) {
                rr.setReviewerNickname(reviewer.getNickname());
                rr.setReviewerAvatar(reviewer.getAvatar());
            }
            reviewResponses.add(rr);
        }
        r.setRecentReviews(reviewResponses);

        return r;
    }

    public void updateProfile(Long userId, String nickname, String avatar, String campus) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if (nickname != null && !nickname.isBlank()) {
            if (nickname.length() > 50) {
                throw new BusinessException(ErrorCode.BAD_REQUEST.getCode(), "昵称过长");
            }
            user.setNickname(nickname);
        }
        if (avatar != null) {
            user.setAvatar(avatar);
        }
        if (campus != null) {
            user.setCampus(campus);
        }
        userMapper.updateById(user);
    }
}
