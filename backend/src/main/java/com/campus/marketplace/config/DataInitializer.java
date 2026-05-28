package com.campus.marketplace.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.marketplace.entity.User;
import com.campus.marketplace.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) {
        initAdmin();
    }

    private void initAdmin() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, "admin@campus.edu.cn");
        if (userMapper.selectCount(wrapper) > 0) {
            log.info("管理员账号已存在，跳过初始化");
            return;
        }

        User admin = new User();
        admin.setEmail("admin@campus.edu.cn");
        admin.setNickname("管理员");
        admin.setPassword(passwordEncoder.encode("Admin123"));
        admin.setCampus("系统");
        admin.setRole(1);
        admin.setStatus(1);
        admin.setVerifyStatus(2);
        admin.setTotalReviews(0);
        admin.setGoodReviews(0);

        userMapper.insert(admin);
        log.info("管理员账号创建成功: admin@campus.edu.cn");
    }
}
