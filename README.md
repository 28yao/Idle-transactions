# 校园二手平台

一个面向在校学生的二手物品交易平台，支持商品发布、搜索、议价、私信沟通等功能。

---

## 项目简介

校园二手平台是一个PC端Web应用，旨在为校园用户提供安全、便捷的二手物品交易渠道。

**核心价值：**
- 连接校园内的买卖双方
- 支持实名认证，增加信任度
- 议价模式，灵活交易
- 站内私信，沟通便捷

---

## 适合谁使用

| 用户类型 | 使用场景 |
|----------|----------|
| 在校学生 | 出售闲置物品、购买二手教材/电子产品 |
| 毕业生 | 毕业季集中处理闲置物品 |
| 管理员 | 审核商品、处理举报、管理用户 |

---

## 项目主要功能

### 用户功能
- 注册/登录（学号+密码）
- 实名认证（上传学生证）
- 个人主页（好评率、交易数）
- 我的发布（管理商品）
- 我的收藏
- 交易记录
- 草稿箱

### 商品功能
- 发布商品（图片上传、信息填写）
- 搜索商品（关键词、分类、筛选）
- 商品详情（图片轮播、卖家信息）
- 收藏/取消收藏

### 交易功能
- 私信沟通（文字、图片）
- 议价协商（出价、还价、接受、拒绝）
- 交易确认（卖家标记交付、买家确认收货）
- 信用评价（1-5星评分、文字评价）

### 系统功能
- 通知中心（私信、互动、系统、交易通知）
- 举报机制（虚假商品、欺诈行为等）
- 管理后台（用户管理、商品审核、举报处理）

---

## 项目目录结构说明

```
D:\31689\Documents\code\AI CODING\SDD\Idle transactions\
├── PRD.md                    # 产品需求文档
├── plan.md                   # 技术实现方案
├── README.md                 # 项目说明（本文件）
├── todo.md                   # 开发任务跟踪
├── Idle transactions.pen     # 页面原型文件
└── campus-marketplace/       # 项目代码（待创建）
    ├── frontend/             # 前端项目（Nuxt 3）
    └── backend/              # 后端项目（Spring Boot）
```

---

## 本地运行方式

### 环境要求

| 软件 | 版本 | 说明 |
|------|------|------|
| JDK | 17+ | Java开发环境 |
| Maven | 3.8+ | 后端构建工具 |
| Node.js | 18+ | 前端运行环境 |
| npm | 9+ | 前端包管理 |
| MySQL | 8.0 | 数据库 |

### 第一步：启动数据库

1. 确保MySQL已安装并运行
2. 创建数据库：
   ```sql
   CREATE DATABASE campus_marketplace DEFAULT CHARACTER SET utf8mb4;
   ```
3. 导入表结构：
   ```bash
   mysql -u root -p campus_marketplace < backend/src/main/resources/db/schema.sql
   ```

### 第二步：启动后端

```bash
cd backend
mvn spring-boot:run
```

后端启动后访问：http://localhost:8080

验证后端是否启动成功：
- 访问 http://localhost:8080/doc.html 查看API文档

### 第三步：启动前端

```bash
cd frontend
npm install
npm run dev
```

前端启动后访问：http://localhost:3000

### 第四步：初始化管理员账号和测试数据

启动后端后，访问以下接口初始化系统：

1. **自动创建管理员账号**（应用启动时自动执行）
   - 邮箱：`admin@campus.edu.cn`
   - 密码：`Admin123`
   - 角色：管理员

2. **生成测试数据**（可选）
   - 调用接口：`POST /api/admin/seed`（需管理员登录）
   - 生成内容：8个用户、20个商品、5笔交易、21个收藏、13条通知、4个会话

### 第五步：访问页面

打开浏览器访问 http://localhost:3000，可以看到：
- 首页：商品列表
- 登录页：http://localhost:3000/login
- 注册页：http://localhost:3000/register
- 卖家主页：http://localhost:3000/user/{id}（需要创建 `pages/user/[id].vue`）
- 管理后台：http://localhost:3000/admin（需管理员登录）

---

## 环境变量说明

### 后端配置（application.yml）

```yaml
# 数据库配置
spring.datasource.url: jdbc:mysql://localhost:3306/campus_marketplace?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8&allowPublicKeyRetrieval=true
spring.datasource.username: root
spring.datasource.password: "123"

# JWT密钥
jwt.secret: your-secret-key-here

# 文件上传路径
upload.path: ./uploads
```

**重要配置说明：**
- `characterEncoding=utf8`：JDBC 不支持 `utf8mb4`，必须用 `utf8`
- `allowPublicKeyRetrieval=true`：MySQL 8.0 需要显式允许公钥检索
- 数据库密码是 `123`，不是 `123.`

### 前端配置（.env）

```bash
# API地址
NUXT_PUBLIC_API_BASE=http://localhost:8080/api
```

### Nuxt 3 组件命名规则（重要）

Nuxt 3 自动导入组件时会添加目录前缀，必须按以下规则引用组件：

| 文件路径 | 自动注册名称 | 错误写法 |
|----------|--------------|----------|
| `components/product/ImageCarousel.vue` | `ProductImageCarousel` | ~~ImageCarousel~~ |
| `components/user/SellerInfo.vue` | `UserSellerInfo` | ~~SellerInfo~~ |
| `components/common/ReportDialog.vue` | `CommonReportDialog` | ~~ReportDialog~~ |
| `components/product/ProductCard.vue` | `ProductProductCard` | ~~ProductCard~~ |

**规则：** 目录名 + 组件文件名（PascalCase）

### 文件上传配置

前端 `el-upload` 组件必须设置 `name="files"` 匹配后端参数名：

```vue
<el-upload
  name="files"
  action="/api/products/images"
  :on-success="handleSuccess"
>
```

---

## 常用命令

### 后端命令

| 命令 | 说明 |
|------|------|
| `mvn spring-boot:run` | 启动后端服务 |
| `mvn package` | 打包项目 |
| `mvn test` | 运行测试 |

### 前端命令

| 命令 | 说明 |
|------|------|
| `npm run dev` | 启动开发服务器 |
| `npm run build` | 构建生产版本 |
| `npm run preview` | 预览生产构建 |

### 数据库命令

| 命令 | 说明 |
|------|------|
| `mysql -u root -p` | 登录MySQL |
| `SHOW DATABASES;` | 查看数据库列表 |
| `USE campus_marketplace;` | 切换到项目数据库 |
| `SHOW TABLES;` | 查看所有表 |

---

## 文档索引

| 文档 | 说明 |
|------|------|
| [PRD.md](./PRD.md) | 产品需求文档，包含所有功能需求 |
| [plan.md](./plan.md) | 技术实现方案，包含数据库设计、API设计、任务拆分 |
| [todo.md](./todo.md) | 开发任务跟踪，按功能模块拆分 |
| [Idle transactions.pen](./Idle transactions.pen) | 页面原型文件，用Pencil打开 |

---

## 开发流程说明

### 推荐开发顺序

1. **第一阶段：基础框架搭建**
   - 初始化前后端项目
   - 创建数据库表
   - 实现JWT认证

2. **第二阶段：用户模块**
   - 注册/登录功能
   - 实名认证功能
   - 个人主页

3. **第三阶段：商品模块**
   - 商品发布
   - 商品搜索
   - 商品详情
   - 收藏功能

4. **第四阶段：私信与交易**
   - 私信功能
   - 议价功能
   - 交易确认
   - 信用评价

5. **第五阶段：系统功能**
   - 通知中心
   - 举报功能
   - 管理后台

### 开发规范

- 每个功能模块完成后，先进行本地测试
- 测试通过后再进入下一个模块
- 遇到问题及时记录到todo.md的"阻塞"状态

---

## 如何测试功能是否正常

### 测试登录功能

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 打开 http://localhost:3000/login | 显示登录页面 |
| 2 | 输入学号和密码 | 输入框可正常输入 |
| 3 | 点击"登录"按钮 | 跳转到首页，显示用户信息 |

### 测试注册功能

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 打开 http://localhost:3000/register | 显示注册页面 |
| 2 | 填写学号、姓名、密码、选择校区 | 输入框可正常输入 |
| 3 | 点击"注册"按钮 | 注册成功，自动跳转到首页 |

### 测试商品发布功能

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 登录后点击"发布商品" | 显示发布页面 |
| 2 | 填写商品信息、上传图片 | 表单可正常填写 |
| 3 | 点击"发布"按钮 | 提示发布成功，商品进入审核 |

### 测试草稿编辑功能

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 在草稿箱点击"编辑" | 跳转到 `/product/publish?edit={id}` |
| 2 | 页面自动加载已有商品数据 | 表单填充原有内容 |
| 3 | 修改后点击"发布" | 商品更新成功 |

### 测试搜索功能

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 在首页搜索框输入关键词 | 搜索框可正常输入 |
| 2 | 点击搜索或按回车 | 显示搜索结果列表 |
| 3 | 点击分类标签 | 显示该分类下的商品 |

### 测试私信功能

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 在商品详情页点击"联系卖家" | 打开私信窗口 |
| 2 | 输入消息内容 | 输入框可正常输入 |
| 3 | 点击"发送"按钮 | 消息发送成功，对方收到通知 |

### 出错时先看哪里

| 问题 | 检查位置 |
|------|----------|
| 页面打不开 | 检查前端是否启动，端口3000是否被占用 |
| 接口报错 | 检查后端是否启动，查看控制台日志 |
| 数据库连接失败 | 检查MySQL是否运行，账号密码是否正确 |
| 图片上传失败 | 检查uploads目录是否存在，权限是否正确 |

---

## 常见问题

### Q: 忘记管理员账号密码怎么办？

A: 管理员账号是预设的，账号密码在数据库中直接配置。如需修改，请联系项目负责人。

### Q: 如何修改数据库配置？

A: 编辑 `backend/src/main/resources/application.yml` 文件中的数据库配置部分。

### Q: 前端页面显示空白怎么办？

A: 检查浏览器控制台是否有报错，通常是API请求失败导致。确认后端是否正常启动。

### Q: 图片上传失败怎么办？

A: 检查 `uploads` 目录是否存在，以及目录权限是否正确。

---

## 联系方式

如有问题，请联系项目负责人或查看项目文档。

---

**最后更新：** 2026-05-28
