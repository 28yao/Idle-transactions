# 校园二手平台 - 问题修复记录

## 本次任务出现的问题（2026-05-28）

### 9.1 导航栏链接 404

**现象**: 点击导航栏"消息"、"我的发布"、"收藏"、"个人主页"返回 404
**原因**: 导航栏链接指向的页面不存在（`/messages`、`/user/listings`、`/user/favorites`、`/user/profile`）
**修复**:
- 修正"消息"链接：`/messages` → `/chat`
- 创建缺失页面：`user/profile.vue`、`user/listings.vue`、`user/favorites.vue`

### 9.2 登录状态容易丢失

**现象**: 频繁出现"未登录或登录已过期"
**原因**: `useCookie('token')` 在 axios 插件和 auth store 中没有统一设置 `maxAge`，导致 cookie 在浏览器关闭后丢失
**修复**: 所有 `useCookie('token')` 调用统一添加 `maxAge: 60 * 60 * 24 * 7`（7天）

```typescript
// 修复前
const token = useCookie('token')

// 修复后
const token = useCookie('token', { maxAge: 60 * 60 * 24 * 7 })
```

### 9.3 联系卖家后会话列表为空

**现象**: 点击"联系卖家"跳转到消息页，但会话列表为空
**原因**: `listConversations` 过滤掉了 `lastMessageAt` 为 null 的会话，而新创建的会话还没有消息
**修复**: 移除过滤条件，显示所有会话

```java
// 修复前
for (Conversation conv : list) {
    if (conv.getLastMessageAt() != null) {
        result.add(buildResponse(conv, userId));
    }
}

// 修复后
for (Conversation conv : list) {
    result.add(buildResponse(conv, userId));
}
```

### 9.4 从商品详情页点击联系卖家后不会自动选中会话

**现象**: 点击"联系卖家"跳转到 `/chat?id=123`，但聊天页面不会自动选中该会话
**原因**: 聊天页面没有读取 URL 中的 `id` 查询参数
**修复**: 在 `onMounted` 中读取 `route.query.id` 并自动选中对应会话

### 9.5 聊天窗口消息过多时输入框被顶出可视区域

**现象**: 消息很多时，输入框被推到页面底部，需要滚动才能看到
**原因**: 聊天窗口的布局没有正确设置高度约束
**修复**: 添加 `min-height: 0` 和 `flex-shrink: 0`，确保消息列表可滚动，输入框固定在底部

### 9.6 发送方头像位置错误

**现象**: 发送方（自己）的头像显示在消息气泡左侧
**原因**: CSS 使用 `flex-direction: row-reverse` 导致头像位置反转
**修复**: 改用 `justify-content: flex-end`，保持头像在右侧

### 9.7 WebSocket 实时消息不显示在聊天窗口

**现象**: 收到新消息时，会话列表更新了，但聊天窗口没有显示新消息
**原因**: WebSocket 消息处理只刷新了会话列表，没有将新消息添加到当前聊天窗口
**修复**:
- ChatWindow 组件暴露 `addMessage` 方法
- 父组件收到 WebSocket 消息时，调用 `chatWindowRef.value.addMessage()` 添加到聊天窗口

---

## 上一次任务出现的问题

## 一、数据库与连接问题

### 1.1 JDBC 字符编码错误

**错误**: `Unsupported character encoding 'utf8mb4'`**原因**: JDBC URL 中 `characterEncoding=utf8mb4` 不被 Java 驱动支持 **修复**: 改为 `characterEncoding=utf8`

### 1.2 公钥检索错误

**错误**: `Public Key Retrieval is not allowed`**原因**: MySQL 8.0 默认使用 `caching_sha2_password`，JDBC 需要显式允许公钥检索 **修复**: JDBC URL 添加 `allowPublicKeyRetrieval=true`

### 1.3 数据库密码错误

**错误**: `Access denied for user 'root'@'localhost'`**原因**: `application.yml` 中密码配置为 `"123."`，实际密码是 `"123"`**修复**: 改为 `password: "123"`

---

## 二、实体类字段映射问题

### 2.1 Product 实体字段缺失/映射错误

**错误**: `Unknown column 'user_id'`、`Field 'campus' doesn't have a default value`**原因**: 实体字段名与数据库列名不一致，缺少 `campus` 字段 **修复**:

```java
@TableField("seller_id")
private Long userId;

@TableField("`condition`")  // condition 是 SQL 保留字
private String condition;

private String campus;  // 新增字段

@TableField("fav_count")
private Integer favoriteCount;
```

### 2.2 ProductImage 实体映射错误

**错误**: `Unknown column 'image_url'`**原因**: 数据库列名是 `url`，实体用的是 `imageUrl`**修复**:

```java
@TableField("url")
private String imageUrl;
```

### 2.3 Transaction 实体映射错误

**错误**: `Unknown column 'updated_at'`、`Unknown column 'confirmed_at'`**原因**: 数据库没有 `updated_at` 列，`confirmed_at` 实际列名是 `completed_at`**修复**:

```java
@TableField("completed_at")
private LocalDateTime confirmedAt;

@TableField(exist = false)
private LocalDateTime updatedAt;
```

### 2.4 Notification 实体表名和字段错误

**错误**: `Table 'campus_marketplace.notification' doesn't exist`**原因**: 数据库表名是 `notifications`（复数），实体用的是 `notification`**修复**:

```java
@TableName("notifications")
public class Notification {
    @TableField("related_id")
    private Long referenceId;

    @TableField(exist = false)
    private Integer referenceType;

    @TableField("is_read")
    private Integer readStatus;
}
```

### 2.5 Review/Message 实体多余字段

**错误**: `Unknown column 'reviewed_id'`、`Unknown column 'read_status'`**原因**: 实体中存在数据库没有的字段 **修复**: 标记为 `@TableField(exist = false)`

### 2.6 Report 实体映射错误

**错误**: `Unknown column 'user_id' in 'where clause'`**原因**: 数据库列名是 `reporter_id`，实体用的是 `userId`**修复**:

```java
@TableField("reporter_id")
private Long userId;
```

---

## 三、文件上传问题

### 3.1 图片上传 500 错误

**错误**: `java.io.FileNotFoundException` - 系统找不到指定的路径 **原因**: `file.transferTo()` 将文件保存到了 Tomcat 临时目录（`AppData\Local\Temp\tomcat.xxx`），而非应用工作目录 **修复**: 改用 `Files.copy()` 并转为绝对路径

```java
// 修复前
file.transferTo(filePath.toFile());

// 修复后
Path filePath = dirPath.resolve(filename).toAbsolutePath();
Files.copy(file.getInputStream(), filePath);
```

### 3.2 前端上传参数名不匹配

**现象**: 后端接收不到文件 **原因**: `el-upload` 默认参数名是 `file`，后端用的是 `@RequestParam("files")`**修复**: 前端添加 `name="files"`

```vue
<el-upload name="files" ...>
```

---

## 四、前端组件与路由问题

### 4.1 商品详情页图片不显示

**错误**: `Failed to resolve component: ImageCarousel`**原因**: Nuxt 3 自动导入组件时会加目录前缀。`components/product/ImageCarousel.vue` 自动注册为 `ProductImageCarousel`，而非 `ImageCarousel`**修复**:

```vue
<!-- 修复前 -->
<ImageCarousel :images="product.images" />

<!-- 修复后 -->
<ProductImageCarousel :images="product.images" />
```

同理修复:

- `SellerInfo` → `UserSellerInfo`（位于 `components/user/`）
- `ReportDialog` → `CommonReportDialog`（位于 `components/common/`）

### 4.2 卖家主页 404

**现象**: 点击"查看主页"跳转 `/user/63` 返回 404 **原因**: 缺少 `/user/[id].vue` 动态路由页面 **修复**: 创建 `pages/user/[id].vue`，展示卖家信息、在售商品、评价

---

## 五、接口与鉴权问题

### 5.1 登录接口返回 null 角色

**现象**: 前端无法判断用户是否为管理员 **原因**: `AuthService.convertToUserResponse()` 未设置 `role` 字段 **修复**: 添加 `response.setRole(user.getRole())`

### 5.2 商品创建时 userId 为 null

**错误**: `用户不存在`（userId=null） **原因**: `excludePathPatterns("/api/products")` 排除了所有 HTTP 方法（包括 POST），导致拦截器不执行 **修复**: 改为在拦截器内部判断公开 GET 接口，不再使用 `excludePathPatterns` 排除 `/api/products`

### 5.3 商品创建缺少 campus 字段

**错误**: `Field 'campus' doesn't have a default value`**原因**: 创建商品时未设置 `campus` 字段 **修复**: 从当前用户信息获取 `product.setCampus(user.getCampus())`

### 5.4 前端 API 404

**现象**: 前端请求 `/api/products` 返回 404 **原因**: Nuxt 开发服务器默认不代理 `/api` 请求到后端 **修复**: `nuxt.config.ts` 添加 `nitro.devProxy`

```typescript
nitro: {
  devProxy: {
    '/api': {
      target: 'http://localhost:8080/api',
      changeOrigin: true
    }
  }
}
```

---

## 六、数据校验问题

### 6.1 商品描述字数限制过严

**现象**: 描述最少需要 10 个字符 **修复**: 前后端统一改为 1-1000 字

```java
@Size(min = 1, max = 1000, message = "描述长度应为1-1000个字符")
```

### 6.2 商品标题字数限制过严

**现象**: 标题最少需要 5 个字符 **修复**: 前后端统一改为 1-50 字

### 6.3 图片反序列化错误

**错误**: `Cannot deserialize value of type ArrayList<String> from Array`**原因**: 前端发送的 images 数组可能包含非字符串元素 **修复**: 提交时过滤

```typescript
images: form.images.filter(img => typeof img === 'string' && img.length > 0)
```

### 6.4 图片改为非必填

**修复**:

- 后端: 去掉 `@NotEmpty` 注解
- 前端: 去掉 `required` 验证规则，详情页无图片时隐藏轮播

---

## 七、数据溢出问题

### 7.1 好评率字段溢出

**错误**: `Out of range value for 'good_rate'`**原因**: 数据库字段为 `DECIMAL(3,1)`，最大值 99.9 **修复**: 生成测试数据时限制 `Math.min(rate, 99.9)`

---

## 八、其他问题

### 8 草稿编辑需重新填写

**现象**: 编辑草稿时表单为空，需要重新填写 **修复**: 发布页支持 `?edit=ID` 参数，自动加载已有商品数据填充表单

- 后端新增 `PUT /api/products/{id}` 编辑接口
- 前端 `publish.vue` 检测 `edit` 参数，调用 GET 加载数据