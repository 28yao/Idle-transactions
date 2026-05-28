package com.campus.marketplace.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.marketplace.dto.response.PageResponse;
import com.campus.marketplace.dto.response.TransactionResponse;
import com.campus.marketplace.entity.Product;
import com.campus.marketplace.entity.Transaction;
import com.campus.marketplace.entity.User;
import com.campus.marketplace.exception.BusinessException;
import com.campus.marketplace.exception.ErrorCode;
import com.campus.marketplace.mapper.ProductMapper;
import com.campus.marketplace.mapper.TransactionMapper;
import com.campus.marketplace.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionMapper transactionMapper;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;

    // 状态常量
    private static final int STATUS_ONGOING = 0;      // 进行中
    private static final int STATUS_DELIVERED = 1;    // 卖家已交付
    private static final int STATUS_COMPLETED = 2;    // 已完成
    private static final int STATUS_CANCELLED = 3;    // 已取消
    private static final int STATUS_PENDING_CANCEL = 4; // 待确认取消

    // 商品状态
    private static final int PRODUCT_ON_SALE = 1;
    private static final int PRODUCT_SOLD = 2;

    @Transactional
    public TransactionResponse createTransaction(Long buyerId, Long productId) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        if (product.getStatus() == null || product.getStatus() != PRODUCT_ON_SALE) {
            throw new BusinessException(ErrorCode.PRODUCT_STATUS_ERROR.getCode(), "商品已售出或不可购买");
        }
        if (buyerId.equals(product.getSellerId())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST.getCode(), "不能购买自己的商品");
        }

        // 检查是否已有进行中或待确认取消的交易
        LambdaQueryWrapper<Transaction> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(Transaction::getProductId, productId)
                .in(Transaction::getStatus, STATUS_ONGOING, STATUS_DELIVERED, STATUS_PENDING_CANCEL);
        Long existCount = transactionMapper.selectCount(checkWrapper);
        if (existCount > 0) {
            throw new BusinessException(ErrorCode.TRANSACTION_STATUS_ERROR.getCode(), "该商品已有进行中的交易");
        }

        Transaction transaction = new Transaction();
        transaction.setProductId(productId);
        transaction.setBuyerId(buyerId);
        transaction.setSellerId(product.getSellerId());
        transaction.setPrice(product.getPrice());
        transaction.setStatus(STATUS_ONGOING);
        transactionMapper.insert(transaction);

        // 更新商品状态为已售
        product.setStatus(PRODUCT_SOLD);
        productMapper.updateById(product);

        return buildResponse(transaction);
    }

    @Transactional
    public TransactionResponse deliver(Long transactionId, Long sellerId) {
        Transaction transaction = transactionMapper.selectById(transactionId);
        if (transaction == null) {
            throw new BusinessException(ErrorCode.TRANSACTION_NOT_FOUND);
        }
        if (!transaction.getSellerId().equals(sellerId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        if (transaction.getStatus() != STATUS_ONGOING) {
            throw new BusinessException(ErrorCode.TRANSACTION_STATUS_ERROR.getCode(), "当前状态不允许标记交付");
        }

        transaction.setStatus(STATUS_DELIVERED);
        transaction.setDeliveredAt(LocalDateTime.now());
        transactionMapper.updateById(transaction);

        return buildResponse(transaction);
    }

    @Transactional
    public TransactionResponse confirm(Long transactionId, Long buyerId) {
        Transaction transaction = transactionMapper.selectById(transactionId);
        if (transaction == null) {
            throw new BusinessException(ErrorCode.TRANSACTION_NOT_FOUND);
        }
        if (!transaction.getBuyerId().equals(buyerId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        if (transaction.getStatus() != STATUS_DELIVERED) {
            throw new BusinessException(ErrorCode.TRANSACTION_STATUS_ERROR.getCode(), "卖家尚未标记交付");
        }

        transaction.setStatus(STATUS_COMPLETED);
        transaction.setCompletedAt(LocalDateTime.now());
        transactionMapper.updateById(transaction);

        return buildResponse(transaction);
    }

    @Transactional
    public TransactionResponse requestCancel(Long transactionId, Long userId, String reason) {
        Transaction transaction = transactionMapper.selectById(transactionId);
        if (transaction == null) {
            throw new BusinessException(ErrorCode.TRANSACTION_NOT_FOUND);
        }
        // 只有买家或卖家可以申请取消
        if (!transaction.getBuyerId().equals(userId) && !transaction.getSellerId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        if (transaction.getStatus() == STATUS_COMPLETED || transaction.getStatus() == STATUS_CANCELLED || transaction.getStatus() == STATUS_PENDING_CANCEL) {
            throw new BusinessException(ErrorCode.TRANSACTION_STATUS_ERROR.getCode(), "当前状态不允许取消");
        }

        // 设置为待确认取消状态
        transaction.setStatus(STATUS_PENDING_CANCEL);
        transaction.setCancelReason(reason);
        transaction.setCancelBy(userId);
        transactionMapper.updateById(transaction);

        return buildResponse(transaction);
    }

    @Transactional
    public TransactionResponse confirmCancel(Long transactionId, Long userId) {
        Transaction transaction = transactionMapper.selectById(transactionId);
        if (transaction == null) {
            throw new BusinessException(ErrorCode.TRANSACTION_NOT_FOUND);
        }
        // 只有对方可以确认取消
        if (transaction.getCancelBy().equals(userId)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST.getCode(), "不能确认自己发起的取消请求");
        }
        // 必须是交易的买家或卖家
        if (!transaction.getBuyerId().equals(userId) && !transaction.getSellerId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        if (transaction.getStatus() != STATUS_PENDING_CANCEL) {
            throw new BusinessException(ErrorCode.TRANSACTION_STATUS_ERROR.getCode(), "当前状态不是待确认取消");
        }

        // 确认取消
        transaction.setStatus(STATUS_CANCELLED);
        transactionMapper.updateById(transaction);

        // 商品重新上架
        Product product = productMapper.selectById(transaction.getProductId());
        if (product != null) {
            product.setStatus(PRODUCT_ON_SALE);
            productMapper.updateById(product);
        }

        return buildResponse(transaction);
    }

    @Transactional
    public TransactionResponse rejectCancel(Long transactionId, Long userId) {
        Transaction transaction = transactionMapper.selectById(transactionId);
        if (transaction == null) {
            throw new BusinessException(ErrorCode.TRANSACTION_NOT_FOUND);
        }
        // 只有对方可以拒绝取消
        if (transaction.getCancelBy().equals(userId)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST.getCode(), "不能拒绝自己发起的取消请求");
        }
        // 必须是交易的买家或卖家
        if (!transaction.getBuyerId().equals(userId) && !transaction.getSellerId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        if (transaction.getStatus() != STATUS_PENDING_CANCEL) {
            throw new BusinessException(ErrorCode.TRANSACTION_STATUS_ERROR.getCode(), "当前状态不是待确认取消");
        }

        // 拒绝取消，恢复原状态
        if (transaction.getDeliveredAt() != null) {
            transaction.setStatus(STATUS_DELIVERED);
        } else {
            transaction.setStatus(STATUS_ONGOING);
        }
        transaction.setCancelReason(null);
        transaction.setCancelBy(null);
        transactionMapper.updateById(transaction);

        return buildResponse(transaction);
    }

    public PageResponse<TransactionResponse> listTransactions(Long userId, Integer status, int page, int size) {
        LambdaQueryWrapper<Transaction> w = new LambdaQueryWrapper<>();
        w.and(q -> q.eq(Transaction::getBuyerId, userId).or().eq(Transaction::getSellerId, userId));

        if (status != null) {
            w.eq(Transaction::getStatus, status);
        }
        w.orderByDesc(Transaction::getCreatedAt);

        IPage<Transaction> result = transactionMapper.selectPage(new Page<>(page, size), w);

        List<TransactionResponse> items = new ArrayList<>();
        for (Transaction t : result.getRecords()) {
            items.add(buildResponse(t));
        }
        return PageResponse.of(items, result.getTotal(), size, page);
    }

    public TransactionResponse getTransactionDetail(Long transactionId, Long userId) {
        Transaction transaction = transactionMapper.selectById(transactionId);
        if (transaction == null) {
            throw new BusinessException(ErrorCode.TRANSACTION_NOT_FOUND);
        }
        if (!transaction.getBuyerId().equals(userId) && !transaction.getSellerId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        return buildResponse(transaction);
    }

    private TransactionResponse buildResponse(Transaction t) {
        TransactionResponse r = TransactionResponse.fromEntity(t);

        // 商品信息
        Product product = productMapper.selectById(t.getProductId());
        if (product != null) {
            r.setProductTitle(product.getTitle());
            // 获取商品封面图（简化处理，取第一张）
            r.setProductImage(null);
        }

        // 买家信息
        User buyer = userMapper.selectById(t.getBuyerId());
        if (buyer != null) {
            r.setBuyerNickname(buyer.getNickname());
            r.setBuyerAvatar(buyer.getAvatar());
        }

        // 卖家信息
        User seller = userMapper.selectById(t.getSellerId());
        if (seller != null) {
            r.setSellerNickname(seller.getNickname());
            r.setSellerAvatar(seller.getAvatar());
        }

        return r;
    }
}
