# 校园二手平台 - 技术实现方案

**文档版本：** v1.1
**创建日期：** 2026-05-27
**更新日期：** 2026-05-28
**状态：** 已补充

---

## 1. 技术栈总览

| 层级 | 技术选型 | 说明 |
|------|----------|------|
| **前端框架** | Vue 3 + Nuxt 3 | SSR/SSG支持，SEO友好，开发效率高 |
| **UI组件库** | Element Plus | 企业级组件库，文档完善 |
| **状态管理** | Pinia | Vue 3官方推荐，轻量级 |
| **HTTP客户端** | Axios | 拦截器支持，请求/响应处理 |
| **后端框架** | Spring Boot 3.x | 企业级框架，生态完善 |
| **ORM框架** | MyBatis-Plus | 简化CRUD，代码生成 |
| **数据库** | MySQL 8.0 | 本地部署，root/123. |
| **文件存储** | 本地文件系统 | MVP阶段，后续可迁移OSS |
| **实时通信** | WebSocket | 私信消息推送 |
| **接口文档** | Knife4j (Swagger) | 自动生成API文档 |
| **构建工具** | Maven + npm | 后端Maven，前端npm |

---

## 2. 项目结构

### 2.1 前端结构（Nuxt 3）

```
campus-marketplace/
├── assets/                    # 静态资源
│   ├── css/                   # 全局样式
│   └── images/                # 图片资源
├── components/                # 组件
│   ├── common/                # 公共组件
│   │   ├── AppHeader.vue      # 顶部导航
│   │   ├── AppFooter.vue      # 底部
│   │   ├── ImageUpload.vue    # 图片上传
│   │   └── Pagination.vue     # 分页
│   ├── product/               # 商品相关
│   │   ├── ProductCard.vue    # 商品卡片
│   │   ├── ProductGrid.vue    # 商品网格
│   │   └── ProductDetail.vue  # 商品详情
│   ├── user/                  # 用户相关
│   │   ├── UserCard.vue       # 用户卡片
│   │   └── ReviewList.vue     # 评价列表
│   └── chat/                  # 聊天相关
│       ├── ConversationList.vue
│       ├── ChatWindow.vue
│       └── MessageBubble.vue
├── composables/               # 组合式函数
│   ├── useAuth.ts             # 认证相关
│   ├── useProduct.ts          # 商品相关
│   └── useChat.ts             # 聊天相关
├── layouts/                   # 布局
│   ├── default.vue            # 默认布局
│   └── admin.vue              # 管理后台布局
├── middleware/                # 中间件
│   ├── auth.ts                # 登录验证
│   └── verified.ts            # 实名验证
├── pages/                     # 页面（路由）
│   ├── index.vue              # 首页
│   ├── login.vue              # 登录
│   ├── register.vue           # 注册
│   ├── product/
│   │   ├── [id].vue           # 商品详情
│   │   └── publish.vue        # 发布商品
│   ├── category/
│   │   └── [name].vue         # 分类页
│   ├── chat/
│   │   └── index.vue          # 私信
│   ├── user/
│   │   ├── profile.vue        # 个人主页
│   │   ├── listings.vue       # 我的发布
│   │   ├── favorites.vue      # 我的收藏
│   │   ├── transactions.vue   # 交易记录
│   │   ├── drafts.vue         # 草稿箱
│   │   └── verify.vue         # 实名认证
│   ├── notifications.vue      # 通知中心
│   └── admin/                 # 管理后台
│       ├── index.vue          # 后台首页
│       ├── users.vue          # 用户管理
│       ├── products.vue       # 商品管理
│       ├── reviews.vue        # 审核管理
│       └── reports.vue        # 举报管理
├── plugins/                   # 插件
│   ├── axios.ts               # Axios配置
│   └── websocket.ts           # WebSocket配置
├── stores/                    # Pinia状态
│   ├── auth.ts                # 认证状态
│   ├── product.ts             # 商品状态
│   └── chat.ts                # 聊天状态
├── types/                     # TypeScript类型
│   ├── user.ts
│   ├── product.ts
│   └── message.ts
├── utils/                     # 工具函数
│   ├── format.ts              # 格式化
│   └── validator.ts           # 验证
├── nuxt.config.ts             # Nuxt配置
└── package.json
```

### 2.2 后端结构（Spring Boot）

```
campus-marketplace-api/
├── src/main/java/com/campus/marketplace/
│   ├── CampusMarketplaceApplication.java
│   ├── config/                    # 配置类
│   │   ├── WebMvcConfig.java      # Web配置
│   │   ├── SecurityConfig.java    # 安全配置
│   │   ├── WebSocketConfig.java   # WebSocket配置
│   │   ├── MyBatisPlusConfig.java # MyBatis配置
│   │   └── CorsConfig.java        # 跨域配置
│   ├── controller/                # 控制器
│   │   ├── AuthController.java    # 认证
│   │   ├── UserController.java    # 用户
│   │   ├── ProductController.java # 商品
│   │   ├── MessageController.java # 消息
│   │   ├── TransactionController.java # 交易
│   │   ├── ReviewController.java  # 评价
│   │   ├── NotificationController.java # 通知
│   │   └── AdminController.java   # 管理后台
│   ├── service/                   # 服务层
│   │   ├── AuthService.java
│   │   ├── UserService.java
│   │   ├── ProductService.java
│   │   ├── MessageService.java
│   │   ├── TransactionService.java
│   │   ├── ReviewService.java
│   │   ├── NotificationService.java
│   │   ├── FileService.java       # 文件上传
│   │   └── AuditService.java      # 审核服务
│   ├── mapper/                    # MyBatis Mapper
│   │   ├── UserMapper.java
│   │   ├── ProductMapper.java
│   │   ├── MessageMapper.java
│   │   ├── TransactionMapper.java
│   │   ├── ReviewMapper.java
│   │   ├── NotificationMapper.java
│   │   ├── FavoriteMapper.java
│   │   └── ReportMapper.java
│   ├── entity/                    # 实体类
│   │   ├── User.java
│   │   ├── Product.java
│   │   ├── ProductImage.java
│   │   ├── Message.java
│   │   ├── Conversation.java
│   │   ├── Transaction.java
│   │   ├── Review.java
│   │   ├── Notification.java
│   │   ├── Favorite.java
│   │   └── Report.java
│   ├── dto/                       # 数据传输对象
│   │   ├── request/               # 请求DTO
│   │   │   ├── LoginRequest.java
│   │   │   ├── RegisterRequest.java
│   │   │   ├── ProductCreateRequest.java
│   │   │   ├── MessageSendRequest.java
│   │   │   └── ...
│   │   └── response/              # 响应DTO
│   │       ├── ApiResponse.java
│   │       ├── UserResponse.java
│   │       ├── ProductResponse.java
│   │       ├── PageResponse.java
│   │       └── ...
│   ├── vo/                        # 视图对象
│   │   ├── ProductVO.java
│   │   ├── UserVO.java
│   │   └── ...
│   ├── exception/                 # 异常处理
│   │   ├── BusinessException.java
│   │   ├── ErrorCode.java
│   │   └── GlobalExceptionHandler.java
│   ├── interceptor/               # 拦截器
│   │   └── AuthInterceptor.java
│   ├── util/                      # 工具类
│   │   ├── JwtUtil.java
│   │   ├── FileUtil.java
│   │   └── RedisUtil.java
│   └── websocket/                 # WebSocket
│       └── ChatWebSocketHandler.java
├── src/main/resources/
│   ├── application.yml            # 配置文件
│   ├── mapper/                    # MyBatis XML
│   │   ├── UserMapper.xml
│   │   └── ...
│   └── db/
│       └── schema.sql             # 数据库脚本
├── uploads/                       # 上传文件目录
├── pom.xml
└── README.md
```

---

## 3. 数据库设计

### 3.1 ER图

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              数据库ER图                                       │
└─────────────────────────────────────────────────────────────────────────────┘

    ┌──────────────┐         ┌──────────────┐         ┌──────────────┐
    │    users     │         │   products   │         │    images    │
    ├──────────────┤         ├──────────────┤         ├──────────────┤
    │ id (PK)      │────┐    │ id (PK)      │────┐    │ id (PK)      │
    │ student_id   │    │    │ seller_id(FK)│    │    │ product_id   │
    │ name         │    │    │ title        │    │    │ url          │
    │ password     │    │    │ description  │    │    │ sort_order   │
    │ nickname     │    │    │ category     │    │    │ is_cover     │
    │ avatar       │    │    │ price        │    │    │ created_at   │
    │ campus       │    │    │ original_price│   │    └──────────────┘
    │ verify_status│    │    │ condition    │    │
    │ good_rate    │    │    │ location     │    │    ┌──────────────┐
    │ role         │    │    │ status       │    │    │  favorites   │
    │ updated_at   │    │    │ view_count   │    │    ├──────────────┤
    └──────────────┘    │    │ fav_count    │    │    │ id (PK)      │
                        │    │ created_at   │    │    │ user_id (FK) │
                        │    │ updated_at   │    │    │ product_id   │
                        │    └──────────────┘    │    │ created_at   │
                        │                        │    └──────────────┘
                        │                        │
                        │    ┌──────────────┐    │    ┌──────────────┐
                        │    │conversations │    │    │  messages    │
                        │    ├──────────────┤    │    ├──────────────┤
                        ├───>│ id (PK)      │<───┼───>│ id (PK)      │
                        │    │ user1_id(FK) │    │    │ conversation │
                        │    │ user2_id(FK) │    │    │ sender_id    │
                        │    │ product_id   │    │    │ content      │
                        │    │ last_message │    │    │ type         │
                        │    │ updated_at   │    │    │ created_at   │
                        │    └──────────────┘    │    └──────────────┘
                        │                        │
                        │    ┌──────────────┐    │    ┌──────────────┐
                        │    │transactions  │    │    │   reviews    │
                        │    ├──────────────┤    │    ├──────────────┤
                        └───>│ id (PK)      │    └───>│ id (PK)      │
                             │ product_id   │         │ transaction  │
                             │ buyer_id(FK) │         │ reviewer_id  │
                             │ seller_id(FK)│         │ rating       │
                             │ price        │         │ content      │
                             │ status       │         │ tags         │
                             │ cancel_reason│         │ created_at   │
                             │ created_at   │         └──────────────┘
                             │ completed_at │
                             └──────────────┘

    ┌──────────────┐         ┌──────────────┐
    │notifications │         │   reports    │
    ├──────────────┤         ├──────────────┤
    │ id (PK)      │         │ id (PK)      │
    │ user_id (FK) │         │ reporter_id  │
    │ type         │         │ target_type  │
    │ title        │         │ target_id    │
    │ content      │         │ type         │
    │ is_read      │         │ reason       │
    │ created_at   │         │ status       │
    └──────────────┘         │ created_at   │
                             └──────────────┘
```

### 3.2 表结构定义

#### users 用户表

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | 是 | AUTO_INCREMENT | 主键 |
| email | VARCHAR(100) | 是 | - | 邮箱（作为登录账号），唯一 |
| nickname | VARCHAR(20) | 是 | - | 昵称（2-20位） |
| password | VARCHAR(100) | 是 | - | 密码（BCrypt加密） |
| avatar | VARCHAR(255) | 否 | NULL | 头像URL |
| campus | VARCHAR(20) | 是 | - | 所属校区 |
| real_name | VARCHAR(50) | 否 | NULL | 真实姓名（实名认证时填写） |
| verify_status | TINYINT | 否 | 0 | 认证状态：0未认证 1审核中 2已认证 3失败 |
| verify_image | VARCHAR(255) | 否 | NULL | 认证图片URL |
| good_rate | DECIMAL(5,2) | 否 | NULL | 好评率（0.00-100.00） |
| total_reviews | INT | 否 | 0 | 总评价数 |
| good_reviews | INT | 否 | 0 | 好评数 |
| role | TINYINT | 否 | 0 | 角色：0普通用户 1管理员 |
| status | TINYINT | 否 | 1 | 状态：0禁用 1正常 |
| lock_until | DATETIME | 否 | NULL | 锁定截止时间 |
| created_at | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引：**
- UNIQUE: `uk_email` (email)
- INDEX: `idx_campus` (campus)
- INDEX: `idx_status` (status)
- INDEX: `idx_role` (role)

#### products 商品表

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | 是 | AUTO_INCREMENT | 主键 |
| seller_id | BIGINT | 是 | - | 卖家ID |
| title | VARCHAR(100) | 是 | - | 商品标题 |
| description | TEXT | 是 | - | 商品描述 |
| category | VARCHAR(20) | 是 | - | 分类：books/electronics/daily/clothing |
| price | DECIMAL(10,2) | 是 | - | 转让价格 |
| original_price | DECIMAL(10,2) | 否 | NULL | 原价 |
| condition | VARCHAR(20) | 是 | - | 成色：new/like_new/used/minor_damage |
| location | VARCHAR(100) | 是 | - | 交易地点 |
| campus | VARCHAR(20) | 是 | - | 所在校区 |
| status | TINYINT | 否 | 0 | 状态：0草稿 1在售 2已售 3已下架 4被举报 5违规下架 |
| view_count | INT | 否 | 0 | 浏览量 |
| fav_count | INT | 否 | 0 | 收藏数 |
| reject_reason | VARCHAR(255) | 否 | NULL | 拒绝原因 |
| created_at | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引：**
- INDEX: `idx_seller_id` (seller_id)
- INDEX: `idx_category` (category)
- INDEX: `idx_status` (status)
- INDEX: `idx_campus` (campus)
- INDEX: `idx_created_at` (created_at)

#### product_images 商品图片表

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | 是 | AUTO_INCREMENT | 主键 |
| product_id | BIGINT | 是 | - | 商品ID |
| url | VARCHAR(255) | 是 | - | 图片URL |
| sort_order | INT | 否 | 0 | 排序 |
| is_cover | TINYINT | 否 | 0 | 是否封面：0否 1是 |
| created_at | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |

**索引：**
- INDEX: `idx_product_id` (product_id)

#### conversations 会话表

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | 是 | AUTO_INCREMENT | 主键 |
| user1_id | BIGINT | 是 | - | 用户1 ID |
| user2_id | BIGINT | 是 | - | 用户2 ID |
| product_id | BIGINT | 是 | - | 关联商品 |
| last_message | VARCHAR(255) | 否 | NULL | 最新消息预览 |
| last_message_at | DATETIME | 否 | NULL | 最新消息时间 |
| created_at | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |

**索引：**
- UNIQUE: `uk_users_product` (user1_id, user2_id, product_id)
- INDEX: `idx_user1` (user1_id)
- INDEX: `idx_user2` (user2_id)

#### messages 消息表

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | 是 | AUTO_INCREMENT | 主键 |
| conversation_id | BIGINT | 是 | - | 会话ID |
| sender_id | BIGINT | 是 | - | 发送者ID |
| content | TEXT | 是 | - | 消息内容 |
| type | TINYINT | 否 | 1 | 类型：1文字 2图片 3出价 4还价 |
| price_offer | DECIMAL(10,2) | 否 | NULL | 出价金额（type=3/4时） |
| offer_status | TINYINT | 否 | NULL | 出价状态：0待处理 1接受 2拒绝 |
| created_at | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |

**索引：**
- INDEX: `idx_conversation_id` (conversation_id)
- INDEX: `idx_created_at` (created_at)
- INDEX: `idx_conversation_time` (conversation_id, created_at)

#### transactions 交易表

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | 是 | AUTO_INCREMENT | 主键 |
| product_id | BIGINT | 是 | - | 商品ID |
| buyer_id | BIGINT | 是 | - | 买家ID |
| seller_id | BIGINT | 是 | - | 卖家ID |
| price | DECIMAL(10,2) | 是 | - | 成交价格 |
| status | TINYINT | 否 | 0 | 状态：0进行中 1卖家已交付 2已完成 3已取消 |
| cancel_reason | VARCHAR(255) | 否 | NULL | 取消原因 |
| cancel_by | BIGINT | 否 | NULL | 取消发起人 |
| delivered_at | DATETIME | 否 | NULL | 卖家标记交付时间 |
| completed_at | DATETIME | 否 | NULL | 完成时间 |
| created_at | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |

**索引：**
- UNIQUE: `uk_product_id` (product_id)
- INDEX: `idx_buyer_id` (buyer_id)
- INDEX: `idx_seller_id` (seller_id)

#### reviews 评价表

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | 是 | AUTO_INCREMENT | 主键 |
| transaction_id | BIGINT | 是 | - | 交易ID |
| reviewer_id | BIGINT | 是 | - | 评价者ID |
| rating | TINYINT | 是 | - | 评分：1-5星 |
| content | VARCHAR(500) | 否 | NULL | 评价内容 |
| tags | VARCHAR(255) | 否 | NULL | 标签（JSON数组） |
| created_at | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |

**索引：**
- UNIQUE: `uk_transaction_reviewer` (transaction_id, reviewer_id)
- INDEX: `idx_reviewer_id` (reviewer_id)

#### notifications 通知表

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | 是 | AUTO_INCREMENT | 主键 |
| user_id | BIGINT | 是 | - | 接收者ID |
| type | TINYINT | 是 | - | 类型：1私信 2互动 3系统 4交易 |
| title | VARCHAR(100) | 是 | - | 通知标题 |
| content | VARCHAR(500) | 是 | - | 通知内容 |
| related_id | BIGINT | 否 | NULL | 关联ID |
| is_read | TINYINT | 否 | 0 | 是否已读：0否 1是 |
| created_at | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |

**索引：**
- INDEX: `idx_user_id` (user_id)
- INDEX: `idx_is_read` (is_read)
- INDEX: `idx_created_at` (created_at)

#### favorites 收藏表

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | 是 | AUTO_INCREMENT | 主键 |
| user_id | BIGINT | 是 | - | 用户ID |
| product_id | BIGINT | 是 | - | 商品ID |
| created_at | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |

**索引：**
- UNIQUE: `uk_user_product` (user_id, product_id)
- INDEX: `idx_product_id` (product_id)

#### reports 举报表

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | 是 | AUTO_INCREMENT | 主键 |
| reporter_id | BIGINT | 是 | - | 举报者ID |
| target_type | TINYINT | 是 | - | 目标类型：1商品 2用户 3消息 |
| target_id | BIGINT | 是 | - | 目标ID |
| type | VARCHAR(20) | 是 | - | 举报类型：fake/prohibited/fraud/abuse/other |
| reason | VARCHAR(500) | 否 | NULL | 举报说明 |
| status | TINYINT | 否 | 0 | 状态：0待处理 1成立 2不成立 |
| admin_note | VARCHAR(500) | 否 | NULL | 管理员备注 |
| created_at | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |

**索引：**
- INDEX: `idx_reporter_id` (reporter_id)
- INDEX: `idx_target` (target_type, target_id)
- INDEX: `idx_status` (status)

---

## 3.3 实体类映射规范（重要）

**开发时必须严格对照以下映射关系，避免字段名不一致导致的 SQL 错误。**

### User 实体

| Java 字段 | 数据库列名 | 说明 |
|-----------|------------|------|
| email | email | 邮箱（作为登录账号） |
| nickname | nickname | 昵称（2-20位） |
| realName | real_name | 使用 `@TableField("real_name")` |
| verifyStatus | verify_status | 使用 `@TableField("verify_status")` |
| verifyImage | verify_image | 使用 `@TableField("verify_image")` |
| goodRate | good_rate | 使用 `@TableField("good_rate")` |
| totalReviews | total_reviews | 使用 `@TableField("total_reviews")` |
| goodReviews | good_reviews | 使用 `@TableField("good_reviews")` |
| lockUntil | lock_until | 使用 `@TableField("lock_until")` |

### Product 实体

| Java 字段 | 数据库列名 | 说明 |
|-----------|------------|------|
| userId | seller_id | 使用 `@TableField("seller_id")` |
| condition | `condition` | 保留字，使用反引号转义 `@TableField("\`condition\`")` |
| campus | campus | 必填字段，创建时从当前用户获取 |
| favoriteCount | fav_count | 使用 `@TableField("fav_count")` |

### ProductImage 实体

| Java 字段 | 数据库列名 | 说明 |
|-----------|------------|------|
| imageUrl | url | 使用 `@TableField("url")` |

### Transaction 实体

| Java 字段 | 数据库列名 | 说明 |
|-----------|------------|------|
| confirmedAt | completed_at | 使用 `@TableField("completed_at")` |
| updatedAt | - | 数据库无此字段，使用 `@TableField(exist = false)` |

### Notification 实体

| Java 字段 | 数据库列名 | 说明 |
|-----------|------------|------|
| 表名 | notifications | 使用 `@TableName("notifications")`（复数） |
| referenceId | related_id | 使用 `@TableField("related_id")` |
| referenceType | - | 数据库无此字段，使用 `@TableField(exist = false)` |
| readStatus | is_read | 使用 `@TableField("is_read")` |

### Review 实体

| Java 字段 | 数据库列名 | 说明 |
|-----------|------------|------|
| reviewedId | - | 数据库无此字段，使用 `@TableField(exist = false)` |

### Message 实体

| Java 字段 | 数据库列名 | 说明 |
|-----------|------------|------|
| readStatus | - | 数据库无此字段，使用 `@TableField(exist = false)` |

### Report 实体

| Java 字段 | 数据库列名 | 说明 |
|-----------|------------|------|
| userId | reporter_id | 使用 `@TableField("reporter_id")` |

---

## 4. API接口设计

### 4.1 接口规范

**基础URL：** `http://localhost:8080/api`

**请求格式：**
```
Content-Type: application/json
Authorization: Bearer {token}
```

**响应格式：**
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

**错误码：**
| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未登录 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 409 | 业务冲突 |
| 500 | 服务器错误 |

### 4.2 接口清单

#### 4.2.1 认证模块

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /auth/register | 用户注册 |
| POST | /auth/login | 用户登录 |
| POST | /auth/logout | 退出登录 |
| POST | /auth/reset-password | 重置密码 |
| GET | /auth/me | 获取当前用户信息 |

#### 4.2.2 用户模块

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /users/{id} | 获取用户信息 |
| PUT | /users/profile | 更新个人信息 |
| POST | /users/avatar | 上传头像 |
| POST | /users/verify | 提交实名认证 |
| GET | /users/{id}/listings | 获取用户发布的商品 |
| GET | /users/{id}/reviews | 获取用户收到的评价 |

#### 4.2.3 商品模块

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /products | 发布商品 |
| GET | /products | 商品列表（搜索） |
| GET | /products/{id} | 商品详情 |
| PUT | /products/{id} | 更新商品 |
| DELETE | /products/{id} | 删除商品 |
| PUT | /products/{id}/status | 更新商品状态 |
| GET | /products/recommended | 推荐商品 |
| GET | /products/latest | 最新商品 |
| POST | /products/{id}/images | 上传商品图片 |

#### 4.2.4 收藏模块

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /favorites/{productId} | 收藏商品 |
| DELETE | /favorites/{productId} | 取消收藏 |
| GET | /favorites | 我的收藏列表 |

#### 4.2.5 会话与消息模块

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /conversations | 会话列表 |
| POST | /conversations | 创建会话 |
| GET | /conversations/{id}/messages | 获取消息历史 |
| POST | /conversations/{id}/messages | 发送消息 |
| POST | /messages/{id}/offer | 出价/还价 |
| PUT | /messages/{id}/offer | 接受/拒绝出价 |

#### 4.2.6 交易模块

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /transactions | 创建交易 |
| GET | /transactions | 交易列表 |
| GET | /transactions/{id} | 交易详情 |
| PUT | /transactions/{id}/deliver | 卖家标记已交付 |
| PUT | /transactions/{id}/confirm | 买家确认收货 |
| POST | /transactions/{id}/cancel | 申请取消交易 |
| PUT | /transactions/{id}/cancel | 同意/拒绝取消 |

#### 4.2.7 评价模块

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /reviews | 提交评价 |
| GET | /reviews/{transactionId} | 获取交易评价 |

#### 4.2.8 通知模块

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /notifications | 通知列表 |
| PUT | /notifications/read-all | 全部已读 |
| PUT | /notifications/{id}/read | 标记已读 |
| GET | /notifications/unread-count | 未读数量 |

#### 4.2.9 举报模块

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /reports | 提交举报 |

#### 4.2.10 管理后台

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /admin/dashboard | 数据概览 |
| GET | /admin/users | 用户列表 |
| PUT | /admin/users/{id}/status | 封禁/解封用户 |
| GET | /admin/products | 商品列表 |
| PUT | /admin/products/{id}/status | 上架/下架商品 |
| GET | /admin/review/verifications | 实名认证审核列表 |
| PUT | /admin/review/verifications/{id} | 审核认证（通过/拒绝） |
| GET | /admin/review/products | 商品举报审查列表 |
| PUT | /admin/review/products/{id} | 审查商品（保留/下架） |
| GET | /admin/review/users | 用户举报审查列表 |
| PUT | /admin/review/users/{id} | 审查用户（警告/封禁） |

---

## 5. 页面路由映射

| 原型页面 | 路由路径 | 文件 | 需要登录 | 需要认证 |
|----------|----------|------|----------|----------|
| 01-登录注册 | /login | pages/login.vue | 否 | 否 |
| 02-注册 | /register | pages/register.vue | 否 | 否 |
| 03-首页 | / | pages/index.vue | 否 | 否 |
| 04-商品详情 | /product/:id | pages/product/[id].vue | 否 | 否 |
| 05-发布商品 | /product/publish | pages/product/publish.vue | 是 | 是 |
| 06-私信 | /chat | pages/chat/index.vue | 是 | 否 |
| 07-个人主页 | /user/profile | pages/user/profile.vue | 是 | 否 |
| 08-实名认证 | /user/verify | pages/user/verify.vue | 是 | 否 |
| 09-通知中心 | /notifications | pages/notifications.vue | 是 | 否 |
| 10-分类 | /category/:name | pages/category/[name].vue | 否 | 否 |
| 我的发布 | /user/listings | pages/user/listings.vue | 是 | 否 |
| 我的收藏 | /user/favorites | pages/user/favorites.vue | 是 | 否 |
| 交易记录 | /user/transactions | pages/user/transactions.vue | 是 | 否 |
| 草稿箱 | /user/drafts | pages/user/drafts.vue | 是 | 否 |
| 管理后台首页 | /admin | pages/admin/index.vue | 是 | 管理员 |
| 用户管理 | /admin/users | pages/admin/users.vue | 是 | 管理员 |
| 商品管理 | /admin/products | pages/admin/products.vue | 是 | 管理员 |
| 审核中心 | /admin/review | pages/admin/review.vue | 是 | 管理员 |
| 统计数据 | /admin/stats | pages/admin/stats.vue | 是 | 管理员 |

---

## 6. 核心业务流程实现

### 6.1 用户注册登录流程

```
┌─────────────────────────────────────────────────────────────────┐
│                        注册登录时序图                              │
└─────────────────────────────────────────────────────────────────┘

前端                          后端                          数据库
 │                             │                             │
 │  1. POST /auth/register     │                             │
 │  {studentId, name, pwd}     │                             │
 │────────────────────────────>│                             │
 │                             │  2. 校验学号格式             │
 │                             │────────────────────────────>│
 │                             │  3. 检查学号是否已注册       │
 │                             │────────────────────────────>│
 │                             │  4. BCrypt加密密码           │
 │                             │  5. INSERT用户               │
 │                             │────────────────────────────>│
 │                             │  6. 生成JWT Token            │
 │  7. 返回token + userInfo    │                             │
 │<────────────────────────────│                             │
 │                             │                             │
 │  8. POST /auth/login        │                             │
 │  {studentId, password}      │                             │
 │────────────────────────────>│                             │
 │                             │  9. 查询用户                 │
 │                             │────────────────────────────>│
 │                             │  10. 验证密码                │
 │                             │  11. 检查账号状态/锁定       │
 │                             │  12. 生成JWT Token           │
 │  13. 返回token + userInfo   │                             │
 │<────────────────────────────│                             │
 │                             │                             │
```

### 6.2 商品发布流程

```
┌─────────────────────────────────────────────────────────────────┐
│                        商品发布时序图                              │
└─────────────────────────────────────────────────────────────────┘

前端                          后端                          数据库
 │                             │                             │
 │  1. POST /products/images   │                             │
 │  multipart/form-data        │                             │
 │────────────────────────────>│                             │
 │                             │  2. 保存图片到本地           │
 │                             │  3. 返回图片URL列表          │
 │  4. 返回imageUrls[]         │                             │
 │<────────────────────────────│                             │
 │                             │                             │
 │  5. POST /products          │                             │
 │  {title, desc, price, ...}  │                             │
 │────────────────────────────>│                             │
 │                             │  6. 参数校验                 │
 │                             │  7. 检查用户认证状态         │
 │                             │  8. 检查发布数量限制         │
 │                             │  9. INSERT商品(status=1)     │
 │                             │────────────────────────────>│
 │                             │  10. INSERT商品图片          │
 │                             │────────────────────────────>│
 │  11. 返回商品信息            │                             │
 │<────────────────────────────│                             │
 │                             │                             │
```

### 6.3 私信与议价流程

```
┌─────────────────────────────────────────────────────────────────┐
│                        私信议价时序图                              │
└─────────────────────────────────────────────────────────────────┘

买家前端        后端        卖家前端        数据库
 │              │              │              │
 │  1. 创建会话  │              │              │
 │  POST /conv  │              │              │
 │─────────────>│              │              │
 │              │  2. INSERT会话│              │
 │              │─────────────────────────────>│
 │              │              │              │
 │  3. 发送消息  │              │              │
 │  POST /msg   │              │              │
 │─────────────>│              │              │
 │              │  4. INSERT消息│              │
 │              │─────────────────────────────>│
 │              │  5. WebSocket推送            │
 │              │─────────────>│              │
 │              │              │  6. 显示消息  │
 │              │              │              │
 │  7. 出价     │              │              │
 │  type=3      │              │              │
 │─────────────>│              │              │
 │              │  8. INSERT出价消息            │
 │              │─────────────────────────────>│
 │              │  9. 推送+创建通知             │
 │              │─────────────>│              │
 │              │              │              │
 │              │  10. 卖家还价 │              │
 │              │<─────────────│              │
 │              │  11. INSERT还价消息           │
 │              │─────────────────────────────>│
 │              │  12. 推送                     │
 │  13. 显示还价│<────────────│              │
 │<─────────────│              │              │
 │              │              │              │
```

### 6.4 交易确认流程

```
┌─────────────────────────────────────────────────────────────────┐
│                        交易确认时序图                              │
└─────────────────────────────────────────────────────────────────┘

买家前端        后端        卖家前端        数据库
 │              │              │              │
 │  1. 确认购买  │              │              │
 │  POST /txn   │              │              │
 │─────────────>│              │              │
 │              │  2. INSERT交易记录           │
 │              │─────────────────────────────>│
 │              │  3. 更新商品状态(status=3)   │
 │              │─────────────────────────────>│
 │              │  4. 通知卖家                 │
 │              │─────────────>│              │
 │              │              │              │
 │              │  5. 卖家标记已交付            │
 │              │<─────────────│              │
 │              │  6. 更新交易状态             │
 │              │─────────────────────────────>│
 │              │  7. 通知买家                 │
 │  8. 显示通知  │<────────────│              │
 │<─────────────│              │              │
 │              │              │              │
 │  9. 确认收货  │              │              │
 │─────────────>│              │              │
 │              │  10. 更新交易完成            │
 │              │─────────────────────────────>│
 │              │  11. 更新商品status=3(已售)  │
 │              │─────────────────────────────>│
 │              │  12. 通知双方评价            │
 │─────────────>│─────────────>│              │
 │              │              │              │
```

---

## 7. 任务拆分与开发计划

### 7.1 阶段一：基础框架搭建（第1周）

| 任务ID | 任务 | 依赖 | 预计工时 |
|--------|------|------|----------|
| T1.1 | 后端项目初始化（Spring Boot + MyBatis-Plus） | 无 | 4h |
| T1.2 | 数据库表创建（执行schema.sql） | 无 | 2h |
| T1.3 | 前端项目初始化（Nuxt 3 + Element Plus） | 无 | 4h |
| T1.4 | 基础配置（跨域、拦截器、全局异常处理） | T1.1 | 4h |
| T1.5 | JWT认证实现 | T1.1 | 6h |
| T1.6 | 前端登录状态管理（Pinia） | T1.3, T1.5 | 4h |

### 7.2 阶段二：用户模块（第2周）

| 任务ID | 任务 | 依赖 | 预计工时 |
|--------|------|------|----------|
| T2.1 | 注册接口实现 | T1.1 | 4h |
| T2.2 | 登录接口实现 | T1.5 | 4h |
| T2.3 | 用户信息接口 | T1.5 | 4h |
| T2.4 | 头像上传接口 | T1.1 | 4h |
| T2.5 | 实名认证接口 | T1.1 | 4h |
| T2.6 | 登录页面 | T1.3 | 6h |
| T2.7 | 注册页面 | T1.3 | 6h |
| T2.8 | 个人主页 | T2.6 | 6h |
| T2.9 | 实名认证页面 | T2.6 | 4h |

### 7.3 阶段三：商品模块（第3周）

| 任务ID | 任务 | 依赖 | 预计工时 |
|--------|------|------|----------|
| T3.1 | 商品发布接口 | T1.5 | 6h |
| T3.2 | 商品图片上传接口 | T1.1 | 4h |
| T3.3 | 商品列表/搜索接口 | T1.1 | 6h |
| T3.4 | 商品详情接口 | T1.1 | 4h |
| T3.5 | 首页 | T1.3 | 8h |
| T3.6 | 商品详情页 | T1.3 | 8h |
| T3.7 | 发布商品页 | T1.3 | 8h |
| T3.8 | 分类页 | T1.3 | 6h |
| T3.9 | 收藏接口实现 | T1.1 | 4h |
| T3.10 | 我的发布/收藏页面 | T2.8 | 6h |

### 7.4 阶段四：私信与交易（第4周）

| 任务ID | 任务 | 依赖 | 预计工时 |
|--------|------|------|----------|
| T4.1 | WebSocket配置 | T1.1 | 4h |
| T4.2 | 会话管理接口 | T1.1 | 6h |
| T4.3 | 消息发送/接收接口 | T4.1, T4.2 | 6h |
| T4.4 | 出价/还价接口 | T4.3 | 6h |
| T4.5 | 交易创建/确认接口 | T1.1 | 8h |
| T4.6 | 私信页面 | T1.3 | 10h |
| T4.7 | 交易记录页面 | T2.8 | 6h |
| T4.8 | 评价接口实现 | T1.1 | 4h |
| T4.9 | 评价展示 | T2.8 | 4h |

### 7.5 阶段五：系统功能（第5周）

| 任务ID | 任务 | 依赖 | 预计工时 |
|--------|------|------|----------|
| T5.1 | 通知接口实现 | T1.1 | 6h |
| T5.2 | 通知中心页面 | T1.3 | 6h |
| T5.3 | 举报接口实现 | T1.1 | 4h |
| T5.4 | 举报弹窗组件 | T1.3 | 4h |
| T5.5 | 草稿功能实现 | T3.1 | 4h |
| T5.6 | 草稿箱页面 | T2.8 | 4h |

### 7.6 阶段六：管理后台（第6周）

| 任务ID | 任务 | 依赖 | 预计工时 |
|--------|------|------|----------|
| T6.1 | 管理后台布局 | T1.3 | 4h |
| T6.2 | 数据概览接口 | T1.1 | 4h |
| T6.3 | 用户管理接口+页面 | T1.1 | 8h |
| T6.4 | 商品审核接口+页面 | T1.1 | 8h |
| T6.5 | 认证审核接口+页面 | T1.1 | 6h |
| T6.6 | 举报处理接口+页面 | T1.1 | 6h |

### 7.7 阶段七：测试与优化（第7周）

| 任务ID | 任务 | 依赖 | 预计工时 |
|--------|------|------|----------|
| T7.1 | 接口联调测试 | 所有接口 | 8h |
| T7.2 | 边缘场景测试 | T7.1 | 6h |
| T7.3 | 性能优化 | T7.1 | 6h |
| T7.4 | UI/UX优化 | T7.1 | 6h |
| T7.5 | 部署文档编写 | T7.1 | 4h |

---

## 8. 关键实现细节

### 8.1 JWT认证实现

```java
// JwtUtil.java
public class JwtUtil {
    private static final String SECRET = "your-secret-key";
    private static final long EXPIRE = 7 * 24 * 60 * 60 * 1000; // 7天

    public static String generateToken(Long userId) {
        return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();
    }

    public static Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(SECRET)
            .parseClaimsJws(token)
            .getBody();
        return Long.parseLong(claims.getSubject());
    }
}
```

### 8.2 WebSocket配置

```java
// WebSocketConfig.java
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler(), "/ws/chat")
                .addInterceptors(new WebSocketAuthInterceptor())
                .setAllowedOrigins("*");
    }
}
```

### 8.3 图片上传处理

```java
// FileService.java
@Service
public class FileService {
    @Value("${upload.path}")
    private String uploadPath;

    public String upload(MultipartFile file) {
        // 1. 验证文件类型和大小
        // 2. 生成唯一文件名
        // 3. 保存到本地
        // 4. 返回访问URL
    }
}
```

**注意事项：**
- 使用 `Files.copy()` 而非 `file.transferTo()`，避免 Tomcat 临时目录问题
- 路径必须转为绝对路径：`filePath.toAbsolutePath()`
- 前端 `el-upload` 组件需设置 `name="files"` 匹配后端参数名

### 8.4 分页查询封装

```java
// PageResponse.java
@Data
public class PageResponse<T> {
    private List<T> records;
    private Long total;
    private Integer page;
    private Integer size;
    private Integer totalPages;
}
```

---

## 9. 配置文件示例

### 9.1 application.yml

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/campus_marketplace?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8&allowPublicKeyRetrieval=true
    username: root
    password: "123"
    driver-class-name: com.mysql.cj.jdbc.Driver

  servlet:
    multipart:
      max-file-size: 5MB
      max-request-size: 50MB

mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
  configuration:
    map-underscore-to-camel-case: true

jwt:
  secret: your-secret-key-here
  expiration: 604800000  # 7 days

upload:
  path: ./uploads
  url-prefix: http://localhost:8080/uploads/
```

### 9.2 nuxt.config.ts

```typescript
export default defineNuxtConfig({
  devtools: { enabled: true },
  modules: ['@element-plus/nuxt', '@pinia/nuxt'],
  runtimeConfig: {
    public: {
      apiBase: 'http://localhost:8080/api'
    }
  },
  // 开发环境 API 代理配置（必须）
  nitro: {
    devProxy: {
      '/api': {
        target: 'http://localhost:8080/api',
        changeOrigin: true
      }
    }
  },
  app: {
    head: {
      title: '校园二手平台'
    }
  }
})
```

---

## 10. 验收检查清单

### 10.1 功能验收

- [ ] 用户注册（学号格式校验）
- [ ] 用户登录（JWT Token生成）
- [ ] 实名认证（图片上传、状态流转）
- [ ] 商品发布（图片上传、表单验证）
- [ ] 商品搜索（关键词、分类、排序）
- [ ] 商品详情（图片轮播、卖家信息）
- [ ] 收藏功能（收藏/取消收藏）
- [ ] 私信功能（WebSocket实时推送）
- [ ] 议价功能（出价/还价/接受/拒绝）
- [ ] 交易确认（卖家标记交付、买家确认收货）
- [ ] 信用评价（评分、文字、标签）
- [ ] 通知中心（未读提醒、全部已读）
- [ ] 举报功能（选择类型、提交说明）
- [ ] 管理后台（用户管理、商品审核）

### 10.2 非功能验收

- [ ] 首屏加载 <2秒
- [ ] 搜索响应 <1秒
- [ ] 图片上传成功
- [ ] WebSocket连接稳定
- [ ] 接口错误处理完善
- [ ] 无SQL注入漏洞
- [ ] 无XSS漏洞

---

**文档版本记录**

| 版本 | 日期 | 修改内容 | 修改人 |
|------|------|----------|--------|
| v1.0 | 2026-05-27 | 初稿创建 | Proma Agent |
