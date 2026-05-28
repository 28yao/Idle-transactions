package com.campus.marketplace.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.marketplace.dto.response.PageResponse;
import com.campus.marketplace.entity.*;
import com.campus.marketplace.exception.BusinessException;
import com.campus.marketplace.exception.ErrorCode;
import com.campus.marketplace.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserMapper userMapper;
    private final ProductMapper productMapper;
    private final TransactionMapper transactionMapper;
    private final ReportMapper reportMapper;

    public Map<String, Object> getDashboard() {
        Map<String, Object> data = new HashMap<>();

        // 今日新增用户
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LambdaQueryWrapper<User> newUserQ = new LambdaQueryWrapper<>();
        newUserQ.ge(User::getCreatedAt, todayStart);
        data.put("todayNewUsers", userMapper.selectCount(newUserQ));

        // 在售商品数
        LambdaQueryWrapper<Product> onSaleQ = new LambdaQueryWrapper<>();
        onSaleQ.eq(Product::getStatus, 1);
        data.put("onSaleProducts", productMapper.selectCount(onSaleQ));

        // 今日成交数
        LambdaQueryWrapper<Transaction> todayTxQ = new LambdaQueryWrapper<>();
        todayTxQ.eq(Transaction::getStatus, 2).ge(Transaction::getCompletedAt, todayStart);
        data.put("todayTransactions", transactionMapper.selectCount(todayTxQ));

        // 待审核数（待处理举报 + 待审核认证）
        LambdaQueryWrapper<Report> pendingReportQ = new LambdaQueryWrapper<>();
        pendingReportQ.eq(Report::getStatus, 0);
        data.put("pendingReports", reportMapper.selectCount(pendingReportQ));

        LambdaQueryWrapper<User> pendingVerifyQ = new LambdaQueryWrapper<>();
        pendingVerifyQ.eq(User::getVerifyStatus, 1);
        data.put("pendingVerifications", userMapper.selectCount(pendingVerifyQ));

        return data;
    }

    public PageResponse<Product> getProducts(Integer status, int page, int size) {
        LambdaQueryWrapper<Product> w = new LambdaQueryWrapper<>();
        if (status != null) {
            w.eq(Product::getStatus, status);
        }
        w.orderByDesc(Product::getCreatedAt);
        IPage<Product> result = productMapper.selectPage(new Page<>(page, size), w);
        return PageResponse.of(result.getRecords(), result.getTotal(), size, page);
    }

    public PageResponse<Map<String, Object>> getUsers(int page, int size) {
        IPage<User> result = userMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<User>().orderByDesc(User::getCreatedAt));

        List<Map<String, Object>> items = result.getRecords().stream().map(u -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", u.getId());
            m.put("email", u.getEmail());
            m.put("nickname", u.getNickname());
            m.put("avatar", u.getAvatar());
            m.put("campus", u.getCampus());
            m.put("verifyStatus", u.getVerifyStatus());
            m.put("role", u.getRole());
            m.put("status", u.getStatus());
            m.put("createdAt", u.getCreatedAt());
            return m;
        }).toList();

        return PageResponse.of(items, result.getTotal(), size, page);
    }

    @Transactional
    public void updateUserStatus(Long userId, Integer status) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if (user.getRole() != null && user.getRole() == 1) {
            throw new BusinessException(ErrorCode.BAD_REQUEST.getCode(), "不能修改管理员状态");
        }
        user.setStatus(status);
        userMapper.updateById(user);
    }

    @Transactional
    public void updateProductStatus(Long productId, Integer status) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        product.setStatus(status);
        productMapper.updateById(product);
    }

    @Transactional
    public void reviewVerification(Long userId, boolean approved) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if (user.getVerifyStatus() == null || user.getVerifyStatus() != 1) {
            throw new BusinessException(ErrorCode.BAD_REQUEST.getCode(), "该用户不在审核中");
        }
        user.setVerifyStatus(approved ? 2 : 3);
        userMapper.updateById(user);
    }

    @Transactional
    public void reviewReport(Long reportId, boolean approved) {
        Report report = reportMapper.selectById(reportId);
        if (report == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        if (report.getStatus() != 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST.getCode(), "该举报已处理");
        }

        if (approved) {
            // 通过举报：处理目标
            if (report.getTargetType() == 1) {
                // 商品举报 → 下架
                Product product = productMapper.selectById(report.getTargetId());
                if (product != null) {
                    product.setStatus(5); // 违规下架
                    productMapper.updateById(product);
                }
            } else if (report.getTargetType() == 2) {
                // 用户举报 → 封禁
                User user = userMapper.selectById(report.getTargetId());
                if (user != null && (user.getRole() == null || user.getRole() != 1)) {
                    user.setStatus(0); // 封禁
                    userMapper.updateById(user);
                }
            }
            report.setStatus(1); // 已处理
        } else {
            report.setStatus(2); // 已驳回
        }
        reportMapper.updateById(report);
    }

    public PageResponse<Map<String, Object>> getReports(Integer status, int page, int size) {
        LambdaQueryWrapper<Report> w = new LambdaQueryWrapper<>();
        if (status != null) {
            w.eq(Report::getStatus, status);
        }
        w.orderByDesc(Report::getCreatedAt);

        IPage<Report> result = reportMapper.selectPage(new Page<>(page, size), w);

        List<Map<String, Object>> items = result.getRecords().stream().map(r -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", r.getId());
            m.put("reporterId", r.getReporterId());
            m.put("targetType", r.getTargetType());
            m.put("targetId", r.getTargetId());
            m.put("type", r.getType());
            m.put("reason", r.getReason());
            m.put("status", r.getStatus());
            m.put("createdAt", r.getCreatedAt());

            // 举报者信息
            User reporter = userMapper.selectById(r.getReporterId());
            if (reporter != null) {
                m.put("reporterNickname", reporter.getNickname());
            }

            // 目标信息
            if (r.getTargetType() == 1) {
                Product product = productMapper.selectById(r.getTargetId());
                if (product != null) {
                    m.put("targetTitle", product.getTitle());
                }
            } else {
                User target = userMapper.selectById(r.getTargetId());
                if (target != null) {
                    m.put("targetNickname", target.getNickname());
                }
            }

            return m;
        }).toList();

        return PageResponse.of(items, result.getTotal(), size, page);
    }

    public PageResponse<Map<String, Object>> getPendingVerifications(int page, int size) {
        LambdaQueryWrapper<User> w = new LambdaQueryWrapper<>();
        w.eq(User::getVerifyStatus, 1).orderByDesc(User::getUpdatedAt);

        IPage<User> result = userMapper.selectPage(new Page<>(page, size), w);

        List<Map<String, Object>> items = result.getRecords().stream().map(u -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", u.getId());
            m.put("nickname", u.getNickname());
            m.put("realName", u.getRealName());
            m.put("verifyImage", u.getVerifyImage());
            m.put("createdAt", u.getUpdatedAt());
            return m;
        }).toList();

        return PageResponse.of(items, result.getTotal(), size, page);
    }
}
