package com.campus.marketplace.service;

import com.campus.marketplace.dto.request.VerifyRequest;
import com.campus.marketplace.entity.User;
import com.campus.marketplace.exception.BusinessException;
import com.campus.marketplace.exception.ErrorCode;
import com.campus.marketplace.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

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
}
