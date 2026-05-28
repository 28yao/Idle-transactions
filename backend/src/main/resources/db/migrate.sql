-- 更新 transactions 表，支持待确认取消状态
ALTER TABLE transactions
    MODIFY COLUMN status TINYINT DEFAULT 0 COMMENT '状态：0进行中 1卖家已交付 2已完成 3已取消 4待确认取消',
    ADD INDEX idx_status (status);

-- 移除 product_id 的唯一约束，允许取消后重新购买
-- 先删除唯一索引，再添加普通索引
ALTER TABLE transactions
    DROP INDEX uk_product_id,
    ADD INDEX idx_product_id (product_id);

-- 更新商品状态注释
ALTER TABLE products
    MODIFY COLUMN status TINYINT DEFAULT 0 COMMENT '状态：0草稿 1在售 2已售 3已下架 4被举报 5违规下架';

-- 评价表添加 target_id 字段
ALTER TABLE reviews
    ADD COLUMN target_id BIGINT NOT NULL COMMENT '被评价者ID' AFTER reviewer_id,
    ADD INDEX idx_target_id (target_id);
