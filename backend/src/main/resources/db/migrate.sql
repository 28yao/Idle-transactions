-- 更新 transactions 表，支持待确认取消状态
ALTER TABLE transactions
    MODIFY COLUMN status TINYINT DEFAULT 0 COMMENT '状态：0进行中 1卖家已交付 2已完成 3已取消 4待确认取消',
    ADD INDEX idx_status (status);

-- 更新商品状态注释
ALTER TABLE products
    MODIFY COLUMN status TINYINT DEFAULT 0 COMMENT '状态：0草稿 1在售 2已售 3已下架 4被举报 5违规下架';
