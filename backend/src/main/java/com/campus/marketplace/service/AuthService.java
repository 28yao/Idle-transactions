package com.campus.marketplace.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.marketplace.dto.request.LoginRequest;
import com.campus.marketplace.dto.request.RegisterRequest;
import com.campus.marketplace.dto.response.UserResponse;
import com.campus.marketplace.entity.User;
import com.campus.marketplace.exception.BusinessException;
import com.campus.marketplace.exception.ErrorCode;
import com.campus.marketplace.mapper.UserMapper;
import com.campus.marketplace.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserResponse register(RegisterRequest request) {
        // 检查邮箱是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, request.getEmail());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 创建用户
        User user = new User();
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCampus(request.getCampus());
        user.setRole(0);
        user.setStatus(1);
        user.setVerifyStatus(0);
        user.setTotalReviews(0);
        user.setGoodReviews(0);

        userMapper.insert(user);

        // 生成 Token
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());

        return UserResponse.fromEntity(user, token);
    }

    public UserResponse login(LoginRequest request) {
        // 查找用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, request.getEmail());
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 检查账号状态
        if (user.getStatus() == 0) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }

        // 检查是否被锁定
        if (user.getLockUntil() != null && user.getLockUntil().isAfter(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKED);
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }

        // 生成 Token
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());

        return UserResponse.fromEntity(user, token);
    }

    public UserResponse getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return UserResponse.fromEntity(user);
    }
}
