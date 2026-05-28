-- 校园二手平台数据库 Schema

-- 创建数据库
CREATE DATABASE IF NOT EXISTS campus_marketplace DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE campus_marketplace;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL COMMENT '邮箱（作为登录账号）',
    nickname VARCHAR(20) NOT NULL COMMENT '昵称',
    password VARCHAR(100) NOT NULL COMMENT '密码（BCrypt加密）',
    avatar VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    campus VARCHAR(20) NOT NULL COMMENT '所属校区',
    real_name VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    verify_status TINYINT DEFAULT 0 COMMENT '认证状态：0未认证 1审核中 2已认证 3失败',
    verify_image VARCHAR(255) DEFAULT NULL COMMENT '认证图片URL',
    good_rate DECIMAL(5,2) DEFAULT NULL COMMENT '好评率',
    total_reviews INT DEFAULT 0 COMMENT '总评价数',
    good_reviews INT DEFAULT 0 COMMENT '好评数',
    role TINYINT DEFAULT 0 COMMENT '角色：0普通用户 1管理员',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用 1正常',
    lock_until DATETIME DEFAULT NULL COMMENT '锁定截止时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_email (email),
    INDEX idx_campus (campus),
    INDEX idx_status (status),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 商品表
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL COMMENT '卖家ID',
    title VARCHAR(100) NOT NULL COMMENT '商品标题',
    description TEXT NOT NULL COMMENT '商品描述',
    category VARCHAR(20) NOT NULL COMMENT '分类：books/electronics/daily/clothing',
    price DECIMAL(10,2) NOT NULL COMMENT '转让价格',
    original_price DECIMAL(10,2) DEFAULT NULL COMMENT '原价',
    `condition` VARCHAR(20) NOT NULL COMMENT '成色：new/like_new/used/minor_damage',
    location VARCHAR(100) NOT NULL COMMENT '交易地点',
    campus VARCHAR(20) NOT NULL COMMENT '所在校区',
    status TINYINT DEFAULT 0 COMMENT '状态：0草稿 1在售 2已售 3已下架 4被举报 5违规下架',
    view_count INT DEFAULT 0 COMMENT '浏览量',
    fav_count INT DEFAULT 0 COMMENT '收藏数',
    reject_reason VARCHAR(255) DEFAULT NULL COMMENT '拒绝原因',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_seller_id (seller_id),
    INDEX idx_category (category),
    INDEX idx_status (status),
    INDEX idx_campus (campus),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

-- 商品图片表
CREATE TABLE IF NOT EXISTS product_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL COMMENT '商品ID',
    url VARCHAR(255) NOT NULL COMMENT '图片URL',
    sort_order INT DEFAULT 0 COMMENT '排序',
    is_cover TINYINT DEFAULT 0 COMMENT '是否封面：0否 1是',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品图片表';

-- 会话表
CREATE TABLE IF NOT EXISTS conversations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user1_id BIGINT NOT NULL COMMENT '用户1 ID',
    user2_id BIGINT NOT NULL COMMENT '用户2 ID',
    product_id BIGINT NOT NULL COMMENT '关联商品',
    last_message VARCHAR(255) DEFAULT NULL COMMENT '最新消息预览',
    last_message_at DATETIME DEFAULT NULL COMMENT '最新消息时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_users_product (user1_id, user2_id, product_id),
    INDEX idx_user1 (user1_id),
    INDEX idx_user2 (user2_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会话表';

-- 消息表
CREATE TABLE IF NOT EXISTS messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversation_id BIGINT NOT NULL COMMENT '会话ID',
    sender_id BIGINT NOT NULL COMMENT '发送者ID',
    content TEXT NOT NULL COMMENT '消息内容',
    type TINYINT DEFAULT 1 COMMENT '类型：1文字 2图片 3出价 4还价',
    price_offer DECIMAL(10,2) DEFAULT NULL COMMENT '出价金额',
    offer_status TINYINT DEFAULT NULL COMMENT '出价状态：0待处理 1接受 2拒绝',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_conversation_id (conversation_id),
    INDEX idx_created_at (created_at),
    INDEX idx_conversation_time (conversation_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- 交易表
CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL COMMENT '商品ID',
    buyer_id BIGINT NOT NULL COMMENT '买家ID',
    seller_id BIGINT NOT NULL COMMENT '卖家ID',
    price DECIMAL(10,2) NOT NULL COMMENT '成交价格',
    status TINYINT DEFAULT 0 COMMENT '状态：0进行中 1卖家已交付 2已完成 3已取消 4待确认取消',
    cancel_reason VARCHAR(255) DEFAULT NULL COMMENT '取消原因',
    cancel_by BIGINT DEFAULT NULL COMMENT '取消发起人',
    delivered_at DATETIME DEFAULT NULL COMMENT '卖家标记交付时间',
    completed_at DATETIME DEFAULT NULL COMMENT '完成时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_product_id (product_id),
    INDEX idx_buyer_id (buyer_id),
    INDEX idx_seller_id (seller_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易表';

-- 评价表
CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id BIGINT NOT NULL COMMENT '交易ID',
    reviewer_id BIGINT NOT NULL COMMENT '评价者ID',
    target_id BIGINT NOT NULL COMMENT '被评价者ID',
    rating TINYINT NOT NULL COMMENT '评分：1-5星',
    content VARCHAR(500) DEFAULT NULL COMMENT '评价内容',
    tags VARCHAR(255) DEFAULT NULL COMMENT '标签（JSON数组）',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_transaction_reviewer (transaction_id, reviewer_id),
    INDEX idx_reviewer_id (reviewer_id),
    INDEX idx_target_id (target_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评价表';

-- 通知表
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '接收者ID',
    type TINYINT NOT NULL COMMENT '类型：1私信 2互动 3系统 4交易',
    title VARCHAR(100) NOT NULL COMMENT '通知标题',
    content VARCHAR(500) NOT NULL COMMENT '通知内容',
    related_id BIGINT DEFAULT NULL COMMENT '关联ID',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读：0否 1是',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_is_read (is_read),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知表';

-- 收藏表
CREATE TABLE IF NOT EXISTS favorites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_product (user_id, product_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';

-- 举报表
CREATE TABLE IF NOT EXISTS reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reporter_id BIGINT NOT NULL COMMENT '举报者ID',
    target_type TINYINT NOT NULL COMMENT '目标类型：1商品 2用户',
    target_id BIGINT NOT NULL COMMENT '目标ID',
    type TINYINT NOT NULL COMMENT '举报类型：1虚假信息 2违禁物品 3欺诈 4其他',
    reason VARCHAR(500) NOT NULL COMMENT '举报原因',
    status TINYINT DEFAULT 0 COMMENT '状态：0待处理 1已处理 2已驳回',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_reporter_id (reporter_id),
    INDEX idx_target (target_type, target_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='举报表';
