package com.campus.marketplace.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.marketplace.dto.request.ReviewRequest;
import com.campus.marketplace.dto.response.ReviewResponse;
import com.campus.marketplace.entity.Review;
import com.campus.marketplace.entity.Transaction;
import com.campus.marketplace.entity.User;
import com.campus.marketplace.exception.BusinessException;
import com.campus.marketplace.exception.ErrorCode;
import com.campus.marketplace.mapper.ReviewMapper;
import com.campus.marketplace.mapper.TransactionMapper;
import com.campus.marketplace.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewMapper reviewMapper;
    private final TransactionMapper transactionMapper;
    private final UserMapper userMapper;

    private static final int STATUS_COMPLETED = 2;

    @Transactional
    public ReviewResponse submitReview(Long reviewerId, ReviewRequest request) {
        Transaction transaction = transactionMapper.selectById(request.getTransactionId());
        if (transaction == null) {
            throw new BusinessException(ErrorCode.TRANSACTION_NOT_FOUND);
        }
        if (transaction.getStatus() != STATUS_COMPLETED) {
            throw new BusinessException(ErrorCode.TRANSACTION_STATUS_ERROR.getCode(), "交易未完成，不能评价");
        }

        // 只有买家或卖家可以评价
        if (!transaction.getBuyerId().equals(reviewerId) && !transaction.getSellerId().equals(reviewerId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // 检查是否已评价
        LambdaQueryWrapper<Review> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(Review::getTransactionId, request.getTransactionId())
                .eq(Review::getReviewerId, reviewerId);
        Long existCount = reviewMapper.selectCount(checkWrapper);
        if (existCount > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST.getCode(), "已评价过该交易");
        }

        // 确定被评价人
        Long targetId = transaction.getBuyerId().equals(reviewerId)
                ? transaction.getSellerId()
                : transaction.getBuyerId();

        Review review = new Review();
        review.setTransactionId(request.getTransactionId());
        review.setReviewerId(reviewerId);
        review.setTargetId(targetId);
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setTags(request.getTags());
        reviewMapper.insert(review);

        // 更新用户好评率
        updateUserRating(targetId);

        return buildResponse(review);
    }

    public List<ReviewResponse> getUserReviews(Long userId, int limit) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getTargetId, userId)
                .orderByDesc(Review::getCreatedAt)
                .last("LIMIT " + limit);
        List<Review> reviews = reviewMapper.selectList(wrapper);

        List<ReviewResponse> result = new ArrayList<>();
        for (Review review : reviews) {
            result.add(buildResponse(review));
        }
        return result;
    }

    public ReviewResponse getTransactionReview(Long transactionId, Long userId) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getTransactionId, transactionId)
                .eq(Review::getReviewerId, userId);
        Review review = reviewMapper.selectOne(wrapper);
        if (review == null) {
            return null;
        }
        return buildResponse(review);
    }

    private void updateUserRating(Long userId) {
        // 计算用户好评率
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getTargetId, userId);
        List<Review> reviews = reviewMapper.selectList(wrapper);

        if (reviews.isEmpty()) {
            return;
        }

        long goodReviews = reviews.stream().filter(r -> r.getRating() >= 4).count();
        int totalReviews = reviews.size();
        double goodRate = (double) goodReviews / totalReviews * 100;

        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setGoodRate(new java.math.BigDecimal(goodRate).setScale(2, java.math.RoundingMode.HALF_UP));
            user.setTotalReviews(totalReviews);
            user.setGoodReviews((int) goodReviews);
            userMapper.updateById(user);
        }
    }

    private ReviewResponse buildResponse(Review review) {
        ReviewResponse r = new ReviewResponse();
        r.setId(review.getId());
        r.setTransactionId(review.getTransactionId());
        r.setReviewerId(review.getReviewerId());
        r.setTargetId(review.getTargetId());
        r.setRating(review.getRating());
        r.setContent(review.getContent());
        r.setTags(review.getTags());
        r.setCreatedAt(review.getCreatedAt());

        // 评价人信息
        User reviewer = userMapper.selectById(review.getReviewerId());
        if (reviewer != null) {
            r.setReviewerNickname(reviewer.getNickname());
            r.setReviewerAvatar(reviewer.getAvatar());
        }

        // 被评价人信息
        User target = userMapper.selectById(review.getTargetId());
        if (target != null) {
            r.setTargetNickname(target.getNickname());
        }

        return r;
    }
}
