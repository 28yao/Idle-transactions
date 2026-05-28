# 校园二手平台 - 开发任务跟踪

**最后更新：** 2026-05-28
**状态说明：** 未开始 | 进行中 | 已完成 | 阻塞 | 待确认
**验收状态：** 待验收 | 自动验收通过 | 人工验收通过 | 验收驳回 | 重新提交 | 无需验收

---

## 模块一：基础框架搭建

### 1.1 模块目标

搭建前后端项目基础框架，实现数据库连接、JWT认证等基础设施，为后续功能开发提供支撑。

### 1.2 用户可见内容

当前阶段用户看不到具体业务功能。该模块主要用于支撑后续功能开发。

可以通过以下方式验证：
- 启动前端项目，访问 http://localhost:3000 能看到默认页面
- 启动后端项目，访问 http://localhost:8080/doc.html 能看到API文档
- 调用健康检查接口 http://localhost:8080/api/health 返回成功

### 1.3 用户操作流程

无具体业务操作流程，属于基础架构搭建。

### 1.4 前端页面任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| F1.1 | 初始化Nuxt 3项目 | frontend/package.json, frontend/nuxt.config.ts | 无 | 项目能正常启动 | 已完成 |
| F1.2 | 安装Element Plus | frontend/package.json | F1.1 | 组件能正常使用 | 已完成 |
| F1.3 | 安装Pinia状态管理 | frontend/package.json | F1.1 | 状态能正常存储 | 已完成 |
| F1.4 | 安装Axios | frontend/package.json | F1.1 | HTTP请求能正常发送 | 已完成 |
| F1.5 | 配置Axios拦截器 | frontend/plugins/axios.ts | F1.4 | 请求自动携带Token | 已完成 |
| F1.6 | 创建默认布局 | frontend/layouts/default.vue | F1.2 | 页面有统一的导航栏 | 已完成 |
| F1.7 | 创建Pinia认证状态 | frontend/stores/auth.ts | F1.3 | 登录状态能持久化 | 已完成 |
| F1.8 | 创建认证中间件 | frontend/middleware/auth.ts | F1.7 | 未登录自动跳转登录页 | 已完成 |

### 1.5 Controller 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| B1.1 | 创建Spring Boot项目 | backend/pom.xml | 无 | 项目能正常编译 | 已完成 |
| B1.2 | 配置数据库连接 | backend/src/main/resources/application.yml | B1.1 | 数据库连接成功 | 已完成 |
| B1.3 | 配置MyBatis-Plus | backend/src/main/java/.../config/MyBatisPlusConfig.java | B1.2 | Mapper能正常注入 | 已完成 |
| B1.4 | 配置跨域 | backend/src/main/java/.../config/WebMvcConfig.java | B1.1 | 前端能正常请求后端 | 已完成 |
| B1.5 | 创建全局异常处理器 | backend/src/main/java/.../exception/GlobalExceptionHandler.java | B1.1 | 异常返回统一格式 | 已完成 |
| B1.6 | 创建统一响应类 | backend/src/main/java/.../dto/response/ApiResponse.java | B1.1 | 响应格式统一 | 已完成 |
| B1.7 | 创建健康检查接口 | backend/src/main/java/.../controller/HealthController.java | B1.1 | GET /api/health 返回成功 | 已完成 |

### 1.6 Service 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| S1.1 | 创建JWT工具类 | backend/src/main/java/.../util/JwtUtil.java | B1.1 | Token能正常生成和解析 | 已完成 |
| S1.2 | 创建认证拦截器 | backend/src/main/java/.../interceptor/AuthInterceptor.java | S1.1 | 未登录请求返回401 | 已完成 |
| S1.3 | 配置WebMvc拦截器 | backend/src/main/java/.../config/WebMvcConfig.java | S1.2 | 拦截器生效 | 已完成 |
| S1.4 | 创建管理员账号初始化器 | backend/src/main/java/.../config/DataInitializer.java | B1.1 | 应用启动时自动创建管理员账号（admin@campus.edu.cn/Admin123） | 已完成 |
| S1.5 | 创建测试数据生成器 | backend/src/main/java/.../config/DataSeeder.java | S1.4 | POST /api/admin/seed 生成测试数据 | 未开始 |

### 1.7 Mapper 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| M1.1 | 创建数据库Schema | backend/src/main/resources/db/schema.sql | 无 | 表结构创建成功 | 已完成 |

### 1.8 Repository / 数据保存任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| D1.1 | 创建uploads目录 | backend/uploads/ | 无 | 目录存在且可写 | 已完成 |

### 1.9 页面测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 运行 `cd frontend && npm run dev` | 控制台显示启动成功 |
| 2 | 打开 http://localhost:3000 | 页面能正常显示 |
| 3 | 打开浏览器控制台 | 无报错信息 |

### 1.10 接口测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 运行 `cd backend && mvn spring-boot:run` | 控制台显示启动成功 |
| 2 | 访问 http://localhost:8080/api/health | 返回 `{"code":200,"message":"success"}` |
| 3 | 访问 http://localhost:8080/doc.html | 显示API文档页面 |

### 1.11 异常情况测试

| 场景 | 操作 | 预期结果 |
|------|------|----------|
| 数据库未启动 | 启动后端项目 | 控制台显示数据库连接失败错误 |
| 端口被占用 | 启动项目 | 控制台显示端口占用错误 |

### 1.12 当前状态

- 已完成

---

## 模块二：用户登录注册

### 2.1 模块目标

实现用户注册、登录、退出登录功能，支持邮箱+密码登录方式。

### 2.2 用户可见内容

**登录页面（/login）：**
- 邮箱输入框
- 密码输入框
- "记住我"复选框
- "忘记密码"链接
- "登录"按钮
- "去注册"链接

**注册页面（/register）：**
- 邮箱输入框
- 昵称输入框（2-20位）
- 密码输入框（8-20位，包含字母和数字）
- 确认密码输入框
- 校区选择下拉框
- "注册"按钮
- "去登录"链接

### 2.3 用户操作流程

**注册流程：**
1. 访问 http://localhost:3000/register
2. 填写邮箱、昵称（2-20位）、密码（8-20位，包含字母和数字）、确认密码
3. 选择所属校区（东校区/西校区/南校区等）
4. 点击"注册"按钮
5. 注册成功后自动跳转到首页

**登录流程：**
1. 访问 http://localhost:3000/login
2. 输入邮箱和密码
3. 点击"登录"按钮
4. 登录成功后跳转到首页
5. 页面右上角显示用户昵称

**退出登录流程：**
1. 点击页面右上角用户头像
2. 点击"退出登录"
3. 跳转到登录页面

### 2.4 前端页面任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| F2.1 | 创建登录页面 | frontend/pages/login.vue | F1.6 | 页面能正常显示 | 已完成 |
| F2.2 | 创建注册页面 | frontend/pages/register.vue | F1.6 | 页面能正常显示 | 已完成 |
| F2.3 | 实现登录表单验证 | frontend/pages/login.vue | F2.1 | 表单验证生效 | 已完成 |
| F2.4 | 实现注册表单验证 | frontend/pages/register.vue | F2.2 | 表单验证生效 | 已完成 |
| F2.5 | 调用登录API | frontend/pages/login.vue | F2.1, B2.2 | 登录成功跳转首页 | 已完成 |
| F2.6 | 调用注册API | frontend/pages/register.vue | F2.2, B2.1 | 注册成功跳转首页 | 已完成 |
| F2.7 | 实现退出登录功能 | frontend/layouts/default.vue | F2.5 | 退出后跳转登录页 | 已完成 |

### 2.5 Controller 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| B2.1 | 创建注册接口 | backend/src/main/java/.../controller/AuthController.java | S1.1 | POST /api/auth/register 返回成功 | 已完成 |
| B2.2 | 创建登录接口 | backend/src/main/java/.../controller/AuthController.java | S1.1 | POST /api/auth/login 返回Token | 已完成 |
| B2.3 | 创建退出登录接口 | backend/src/main/java/.../controller/AuthController.java | S1.1 | POST /api/auth/logout 返回成功 | 已完成 |
| B2.4 | 创建获取当前用户接口 | backend/src/main/java/.../controller/AuthController.java | S1.2 | GET /api/auth/me 返回用户信息 | 已完成 |

### 2.6 Service 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| S2.1 | 实现注册业务逻辑 | backend/src/main/java/.../service/AuthService.java | M2.1 | 邮箱重复时返回错误 | 已完成 |
| S2.2 | 实现登录业务逻辑 | backend/src/main/java/.../service/AuthService.java | M2.1 | 密码错误时返回错误 | 已完成 |
| S2.3 | 实现密码加密 | backend/src/main/java/.../service/AuthService.java | 无 | 密码BCrypt加密存储 | 已完成 |

### 2.7 Mapper 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| M2.1 | 创建UserMapper | backend/src/main/java/.../mapper/UserMapper.java | M1.1 | 能正常查询用户 | 已完成 |
| M2.2 | 创建UserMapper.xml | backend/src/main/resources/mapper/UserMapper.xml | M2.1 | 自定义SQL能执行 | 无需验收 |

### 2.8 Repository / 数据保存任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| D2.1 | 创建User实体类 | backend/src/main/java/.../entity/User.java | M1.1 | 字段与数据库对应 | 已完成 |
| D2.2 | 创建注册请求DTO | backend/src/main/java/.../dto/request/RegisterRequest.java | 无 | 字段验证生效 | 已完成 |
| D2.3 | 创建登录请求DTO | backend/src/main/java/.../dto/request/LoginRequest.java | 无 | 字段验证生效 | 已完成 |
| D2.4 | 创建用户响应DTO | backend/src/main/java/.../dto/response/UserResponse.java | 无 | 返回字段正确 | 已完成 |

### 2.9 页面测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 打开 http://localhost:3000/register | 显示注册页面，有学号、姓名、密码、校区输入框 |
| 2 | 填写学号"20220001"、姓名"张三"、密码"Test1234"、选择"东校区" | 输入框可正常输入 |
| 3 | 点击"注册"按钮 | 跳转到首页，右上角显示用户信息 |
| 4 | 点击"退出登录" | 跳转到登录页面 |
| 5 | 打开 http://localhost:3000/login | 显示登录页面 |
| 6 | 输入学号"20220001"和密码"Test1234" | 输入框可正常输入 |
| 7 | 点击"登录"按钮 | 跳转到首页，右上角显示用户信息 |

### 2.10 接口测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | POST /api/auth/register 请求体：`{"studentId":"20220001","name":"张三","password":"Test1234","campus":"东校区"}` | 返回 `{"code":200,"data":{"token":"..."}}` |
| 2 | POST /api/auth/login 请求体：`{"studentId":"20220001","password":"Test1234"}` | 返回 `{"code":200,"data":{"token":"...","user":{...}}}` |
| 3 | GET /api/auth/me 请求头：`Authorization: Bearer {token}` | 返回 `{"code":200,"data":{"id":1,"studentId":"20220001",...}}` |

### 2.11 异常情况测试

| 场景 | 操作 | 预期结果 |
|------|------|----------|
| 学号已存在 | 使用已注册学号注册 | 返回错误提示"学号已被注册" |
| 密码错误 | 输入错误密码登录 | 返回错误提示"密码错误" |
| 学号格式错误 | 输入非8位数字学号 | 返回错误提示"学号格式错误" |
| 密码格式错误 | 输入不符合要求的密码 | 返回错误提示"密码格式错误" |
| 连续登录失败 | 连续5次输入错误密码 | 返回错误提示"账号已锁定" |

### 2.12 当前状态

- 已完成

---

## 模块三：实名认证（可选）

### 3.1 模块目标

实现用户实名认证功能（可选），用户上传学生证照片，管理员审核后更新认证状态。已认证用户显示认证标识，增加买家信任度。

### 3.2 用户可见内容

**实名认证页面（/user/verify）：**
- 认证状态显示（未认证/审核中/已认证/认证失败）
- 认证好处说明（显示认证标识，增加信任度）
- 上传区域（支持JPG、PNG格式）
- 提交按钮
- 认证失败原因显示

### 3.3 用户操作流程

1. 登录后访问 http://localhost:3000/user/verify
2. 查看当前认证状态和认证好处
3. 点击上传区域，选择学生证照片
4. 点击"提交认证"按钮
5. 等待管理员审核（24小时内）
6. 审核结果通过通知中心告知
7. 认证通过后，个人主页和商品详情页显示认证标识

### 3.4 前端页面任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| F3.1 | 创建实名认证页面 | frontend/pages/user/verify.vue | F1.6 | 页面能正常显示 | 已完成 |
| F3.2 | 实现图片上传组件 | frontend/components/common/ImageUpload.vue | F1.2 | 图片能正常上传 | 已完成 |
| F3.3 | 调用认证提交API | frontend/pages/user/verify.vue | F3.1, B3.1 | 提交成功显示提示 | 已完成 |
| F3.4 | 显示认证状态 | frontend/pages/user/verify.vue | F3.1 | 状态显示正确 | 已完成 |

### 3.5 Controller 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| B3.1 | 创建提交认证接口 | backend/src/main/java/.../controller/UserController.java | S1.2 | POST /api/users/verify 返回成功 | 已完成 |
| B3.2 | 创建上传认证图片接口 | backend/src/main/java/.../controller/UserController.java | S1.2 | POST /api/users/verify/image 返回图片URL | 已完成 |

### 3.6 Service 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| S3.1 | 实现认证提交逻辑 | backend/src/main/java/.../service/UserService.java | M2.1 | 状态更新为"审核中" | 已完成 |
| S3.2 | 实现图片上传逻辑 | backend/src/main/java/.../service/FileService.java | 无 | 图片保存成功 | 已完成 |

### 3.7 Mapper 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| M3.1 | 更新UserMapper添加认证相关方法 | backend/src/main/java/.../mapper/UserMapper.java | M2.1 | 能更新认证状态 | 已完成 |

### 3.8 Repository / 数据保存任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| D3.1 | 创建认证请求DTO | backend/src/main/java/.../dto/request/VerifyRequest.java | 无 | 字段验证生效 | 已完成 |

### 3.9 页面测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 登录后访问 http://localhost:3000/user/verify | 显示认证页面，状态为"未认证" |
| 2 | 点击上传区域，选择一张图片 | 图片预览显示 |
| 3 | 点击"提交认证"按钮 | 提示"提交成功，等待审核" |
| 4 | 刷新页面 | 状态变为"审核中" |

### 3.10 接口测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | POST /api/users/verify/image 上传图片 | 返回图片URL |
| 2 | POST /api/users/verify 请求体：`{"imageUrl":"..."}` | 返回 `{"code":200,"message":"提交成功"}` |
| 3 | GET /api/auth/me 查看认证状态 | 返回 `verifyStatus: 1`（审核中） |

### 3.11 异常情况测试

| 场景 | 操作 | 预期结果 |
|------|------|----------|
| 上传非图片文件 | 上传一个txt文件 | 返回错误提示"仅支持图片格式" |
| 图片过大 | 上传超过5MB的图片 | 返回错误提示"图片大小不能超过5MB" |
| 已认证用户重复提交 | 已认证用户再次提交 | 返回错误提示"未开始认证" |

### 3.12 当前状态

- 已完成

---

## 模块四：商品发布

### 4.1 模块目标

实现商品发布功能，支持图片上传、商品信息填写、草稿保存等。

### 4.2 用户可见内容

**发布商品页面（/product/publish）：**
- 商品标题输入框（1-50字）
- 商品分类下拉选择（书籍教材/电子数码/生活用品/服饰美妆）
- 成色下拉选择（全新/几乎全新/轻微使用痕迹/明显使用痕迹）
- 交易地点输入框
- 转让价格输入框
- 原价输入框（选填）
- 商品描述文本框（1-1000字）
- 图片上传区域（最多9张，非必填）
- "立即发布"按钮
- "存为草稿"按钮
- "取消"按钮

**编辑模式（/product/publish?edit={id}）：**
- 自动加载已有商品数据填充表单
- 支持修改后重新提交

### 4.3 用户操作流程

1. 登录后，点击"发布商品"（无需实名认证）
2. 填写商品标题、选择分类和成色
3. 填写交易地点和价格
4. 填写商品描述
5. 上传商品图片（第一张为封面）
6. 选择"立即发布"或"存为草稿"
7. 发布成功后商品立即上架（无需审核）
8. 如被举报，商品进入审核队列

### 4.4 前端页面任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| F4.1 | 创建发布商品页面 | frontend/pages/product/publish.vue | F1.6 | 页面能正常显示 | 已完成 |
| F4.2 | 实现商品图片上传组件 | frontend/components/product/ProductImageUpload.vue | F3.2 | 图片能正常上传 | 已完成 |
| F4.3 | 实现表单验证 | frontend/pages/product/publish.vue | F4.1 | 表单验证生效 | 已完成 |
| F4.4 | 调用发布API | frontend/pages/product/publish.vue | F4.1, B4.1 | 发布成功跳转详情页 | 已完成 |
| F4.5 | 调用保存草稿API | frontend/pages/product/publish.vue | F4.1, B4.1 | 保存成功显示提示 | 已完成 |
| F4.6 | 支持编辑模式加载数据 | frontend/pages/product/publish.vue | F4.1, B5.3 | 访问 ?edit={id} 自动填充表单 | 已完成 |

### 4.5 Controller 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| B4.1 | 创建发布商品接口 | backend/src/main/java/.../controller/ProductController.java | S1.2 | POST /api/products 返回商品信息 | 已完成 |
| B4.2 | 创建上传商品图片接口 | backend/src/main/java/.../controller/ProductController.java | S1.2 | POST /api/products/images 返回图片URL列表 | 已完成 |
| B4.3 | 创建更新商品接口 | backend/src/main/java/.../controller/ProductController.java | S1.2 | PUT /api/products/{id} 返回更新成功 | 已完成 |

### 4.6 Service 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| S4.1 | 实现商品发布逻辑 | backend/src/main/java/.../service/ProductService.java | M4.1 | 商品状态为"在售" | 已完成 |
| S4.2 | 实现草稿保存逻辑 | backend/src/main/java/.../service/ProductService.java | M4.1 | 商品状态为"草稿" | 已完成 |
| S4.3 | 实现批量图片上传 | backend/src/main/java/.../service/FileService.java | S3.2 | 多张图片上传成功 | 已完成 |
| S4.4 | 实现商品编辑逻辑 | backend/src/main/java/.../service/ProductService.java | M4.1 | 商品更新成功 | 已完成 |

### 4.7 Mapper 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| M4.1 | 创建ProductMapper | backend/src/main/java/.../mapper/ProductMapper.java | M1.1 | 能正常查询商品 | 已完成 |
| M4.2 | 创建ProductMapper.xml | backend/src/main/resources/mapper/ProductMapper.xml | M4.1 | 自定义SQL能执行 | 无需验收 |
| M4.3 | 创建ProductImageMapper | backend/src/main/java/.../mapper/ProductImageMapper.java | M1.1 | 能正常查询图片 | 已完成 |

### 4.8 Repository / 数据保存任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| D4.1 | 创建Product实体类 | backend/src/main/java/.../entity/Product.java | M1.1 | 字段与数据库对应 | 已完成 |
| D4.2 | 创建ProductImage实体类 | backend/src/main/java/.../entity/ProductImage.java | M1.1 | 字段与数据库对应 | 已完成 |
| D4.3 | 创建商品发布请求DTO | backend/src/main/java/.../dto/request/ProductCreateRequest.java | 无 | 字段验证生效 | 已完成 |
| D4.4 | 创建商品响应DTO | backend/src/main/java/.../dto/response/ProductResponse.java | 无 | 返回字段正确 | 已完成 |

### 4.9 页面测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 登录后点击"发布商品" | 显示发布页面 |
| 2 | 填写标题"iPad 2024款"、选择分类"电子数码"、成色"几乎全新" | 输入框可正常输入 |
| 3 | 填写地点"图书馆附近"、价格"128"、原价"399" | 输入框可正常输入 |
| 4 | 填写描述"买来只用了几次，一直闲置" | 输入框可正常输入 |
| 5 | 点击上传区域，选择3张图片 | 图片预览显示，可拖拽排序 |
| 6 | 点击"立即发布"按钮 | 提示"发布成功，等待审核"，跳转到商品详情页 |

### 4.10 接口测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | POST /api/products/images 上传3张图片 | 返回3个图片URL |
| 2 | POST /api/products 请求体包含商品信息和图片URL | 返回 `{"code":200,"data":{"id":1,"status":1,...}}` |
| 3 | GET /api/products/1 查看商品详情 | 返回完整的商品信息 |

### 4.11 异常情况测试

| 场景 | 操作 | 预期结果 |
|------|------|----------|
| 超过发布限制 | 发布第21件商品 | 返回错误提示"最多同时发布20件商品" |
| 标题为空 | 不输入标题直接提交 | 返回错误提示"请输入商品标题" |
| 标题过长 | 输入超过50字的标题 | 返回错误提示"标题不能超过50个字" |
| 描述为空 | 不输入描述直接提交 | 返回错误提示"请输入商品描述" |
| 描述过长 | 输入超过1000字的描述 | 返回错误提示"描述不能超过1000个字" |
| 图片过多 | 上传超过9张图片 | 返回错误提示"最多上传9张图片" |

### 4.12 当前状态

- 已完成

---

## 模块五：商品搜索与浏览

### 5.1 模块目标

实现商品搜索、分类筛选、排序功能，支持首页展示和分类页浏览。

### 5.2 用户可见内容

**首页（/）：**
- 搜索框（输入关键词搜索）
- 分类标签栏（全部/书籍教材/电子数码/生活用品/服饰美妆）
- 推荐商品区域（5个商品卡片）
- 最新发布区域（商品网格）

**分类页（/category/:name）：**
- 分类侧边栏
- 面包屑导航
- 筛选栏（价格/成色/校区/排序）
- 商品网格
- 分页

### 5.3 用户操作流程

**首页浏览：**
1. 访问 http://localhost:3000
2. 看到推荐商品和最新发布
3. 点击分类标签筛选
4. 点击商品卡片进入详情

**搜索商品：**
1. 在搜索框输入关键词
2. 按回车或点击搜索
3. 查看搜索结果
4. 使用筛选条件缩小范围

### 5.4 前端页面任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| F5.1 | 创建首页 | frontend/pages/index.vue | F1.6 | 页面能正常显示 | 已完成 |
| F5.2 | 创建商品卡片组件 | frontend/components/ProductCard.vue | F1.2 | 卡片显示商品信息 | 已完成 |
| F5.3 | 创建商品网格组件 | frontend/components/product/ProductGrid.vue | F5.2 | 网格显示多个商品 | 已完成 |
| F5.4 | 实现搜索功能 | frontend/pages/index.vue | F5.1, B5.2 | 搜索结果正确显示 | 已完成 |
| F5.5 | 实现分类筛选 | frontend/pages/index.vue | F5.1, B5.2 | 分类筛选生效 | 已完成 |
| F5.6 | 创建分类页 | frontend/pages/category/[name].vue | F1.6 | 页面能正常显示 | 已完成 |
| F5.7 | 实现分页功能 | frontend/components/common/Pagination.vue | F1.2 | 分页切换正常 | 已完成 |

### 5.5 Controller 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| B5.1 | 创建商品列表接口 | backend/src/main/java/.../controller/ProductController.java | S1.2 | GET /api/products 返回商品列表 | 已完成 |
| B5.2 | 创建商品搜索接口 | backend/src/main/java/.../controller/ProductController.java | S5.1 | GET /api/products?keyword=xxx 返回搜索结果 | 已完成 |
| B5.3 | 创建商品详情接口 | backend/src/main/java/.../controller/ProductController.java | S1.2 | GET /api/products/{id} 返回商品详情 | 已完成 |
| B5.4 | 创建推荐商品接口 | backend/src/main/java/.../controller/ProductController.java | S5.2 | GET /api/products/recommended 返回推荐列表 | 已完成 |
| B5.5 | 创建最新商品接口 | backend/src/main/java/.../controller/ProductController.java | S5.2 | GET /api/products/latest 返回最新列表 | 已完成 |

### 5.6 Service 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| S5.1 | 实现商品列表查询 | backend/src/main/java/.../service/ProductService.java | M4.1 | 分页查询正确 | 已完成 |
| S5.2 | 实现商品搜索逻辑 | backend/src/main/java/.../service/ProductService.java | M4.1 | 关键词搜索正确 | 已完成 |
| S5.3 | 实现分类筛选逻辑 | backend/src/main/java/.../service/ProductService.java | M4.1 | 分类筛选正确 | 已完成 |
| S5.4 | 实现排序逻辑 | backend/src/main/java/.../service/ProductService.java | M4.1 | 排序结果正确 | 已完成 |

### 5.7 Mapper 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| M5.1 | 添加商品搜索SQL | backend/src/main/resources/mapper/ProductMapper.xml | M4.2 | 搜索SQL正确执行 | 已完成 |
| M5.2 | 添加商品统计SQL | backend/src/main/resources/mapper/ProductMapper.xml | M4.2 | 统计SQL正确执行 | 已完成 |

### 5.8 Repository / 数据保存任务

无新增任务。

### 5.9 页面测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 访问 http://localhost:3000 | 显示首页，有搜索框、分类标签、商品列表 |
| 2 | 点击"书籍教材"分类标签 | 显示该分类下的商品 |
| 3 | 在搜索框输入"iPad"，按回车 | 显示包含"iPad"的商品 |
| 4 | 点击商品卡片 | 跳转到商品详情页 |
| 5 | 点击"下一页" | 显示下一页商品 |

### 5.10 接口测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | GET /api/products?page=1&size=20 | 返回商品列表，包含分页信息 |
| 2 | GET /api/products?keyword=iPad | 返回搜索结果 |
| 3 | GET /api/products?category=books | 返回书籍教材分类商品 |
| 4 | GET /api/products?sort=price_asc | 返回按价格升序排列的商品 |
| 5 | GET /api/products/recommended | 返回推荐商品列表 |

### 5.11 异常情况测试

| 场景 | 操作 | 预期结果 |
|------|------|----------|
| 搜索无结果 | 搜索一个不存在的关键词 | 显示"暂无搜索结果"，显示推荐商品 |
| 页码超出范围 | 访问第100页 | 返回最后一页数据 |
| 分类不存在 | 访问不存在的分类 | 显示404页面 |

### 5.12 当前状态

- 已完成

---

## 模块六：商品详情

### 6.1 模块目标

实现商品详情页，展示商品完整信息、卖家信息、支持收藏和联系卖家。

### 6.2 用户可见内容

**商品详情页（/product/:id）：**
- 图片轮播区域（支持左右切换、放大查看）
- 商品标题
- 价格（转让价、原价、折扣）
- 元信息（分类、成色、发布时间、交易地点）
- 商品描述
- 卖家信息（头像、昵称、认证状态、好评率、发布数、成交数）
- 操作按钮（联系卖家、收藏、举报）

### 6.3 用户操作流程

1. 在首页或搜索结果中点击商品卡片
2. 进入商品详情页，查看商品信息
3. 点击图片可放大查看
4. 点击"联系卖家"发起私信
5. 点击"收藏"添加到收藏夹
6. 点击"举报"举报商品问题

### 6.4 前端页面任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| F6.1 | 创建商品详情页 | frontend/pages/product/[id].vue | F1.6 | 页面能正常显示 | 已完成 |
| F6.2 | 实现图片轮播组件 | frontend/components/product/ImageCarousel.vue | F1.2 | 图片可左右切换 | 已完成 |
| F6.3 | 实现收藏功能 | frontend/pages/product/[id].vue | F6.1, B6.2 | 收藏状态切换正常 | 已完成 |
| F6.4 | 实现举报弹窗 | frontend/components/common/ReportDialog.vue | F1.2 | 弹窗正常显示 | 已完成 |
| F6.5 | 创建卖家信息组件 | frontend/components/user/SellerInfo.vue | F1.2 | 信息显示正确 | 已完成 |

### 6.5 Controller 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| B6.1 | 创建收藏接口 | backend/src/main/java/.../controller/FavoriteController.java | S1.2 | POST /api/favorites/{productId} 返回成功 | 已完成 |
| B6.2 | 创建取消收藏接口 | backend/src/main/java/.../controller/FavoriteController.java | S1.2 | DELETE /api/favorites/{productId} 返回成功 | 已完成 |
| B6.3 | 创建举报接口 | backend/src/main/java/.../controller/ReportController.java | S1.2 | POST /api/reports 返回成功 | 已完成 |
| B6.4 | 更新商品浏览量 | backend/src/main/java/.../controller/ProductController.java | S1.2 | 浏览量+1 | 已完成 |

### 6.6 Service 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| S6.1 | 实现收藏逻辑 | backend/src/main/java/.../service/FavoriteService.java | M6.1 | 收藏成功 | 已完成 |
| S6.2 | 实现举报逻辑 | backend/src/main/java/.../service/ReportService.java | M6.2 | 举报成功 | 已完成 |
| S6.3 | 实现浏览量统计 | backend/src/main/java/.../service/ProductService.java | M4.1 | 浏览量正确累加 | 已完成 |

### 6.7 Mapper 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| M6.1 | 创建FavoriteMapper | backend/src/main/java/.../mapper/FavoriteMapper.java | M1.1 | 能正常查询收藏 | 已完成 |
| M6.2 | 创建ReportMapper | backend/src/main/java/.../mapper/ReportMapper.java | M1.1 | 能正常查询举报 | 已完成 |

### 6.8 Repository / 数据保存任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| D6.1 | 创建Favorite实体类 | backend/src/main/java/.../entity/Favorite.java | M1.1 | 字段与数据库对应 | 已完成 |
| D6.2 | 创建Report实体类 | backend/src/main/java/.../entity/Report.java | M1.1 | 字段与数据库对应 | 已完成 |
| D6.3 | 创建举报请求DTO | backend/src/main/java/.../dto/request/ReportRequest.java | 无 | 字段验证生效 | 已完成 |

### 6.9 页面测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 点击商品卡片进入详情页 | 显示商品完整信息 |
| 2 | 点击图片左右箭头 | 图片切换正常 |
| 3 | 点击图片 | 弹出大图预览 |
| 4 | 点击"收藏"按钮 | 按钮变为"已收藏"状态 |
| 5 | 再次点击"收藏"按钮 | 按钮变为"收藏"状态 |
| 6 | 点击"举报"按钮 | 弹出举报弹窗 |

### 6.10 接口测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | GET /api/products/1 | 返回商品详情，包含图片、卖家信息 |
| 2 | POST /api/favorites/1 | 返回 `{"code":200,"message":"收藏成功"}` |
| 3 | DELETE /api/favorites/1 | 返回 `{"code":200,"message":"取消收藏成功"}` |
| 4 | POST /api/reports 请求体：`{"targetType":1,"targetId":1,"type":"fake","reason":"虚假商品"}` | 返回 `{"code":200,"message":"举报成功"}` |

### 6.11 异常情况测试

| 场景 | 操作 | 预期结果 |
|------|------|----------|
| 商品不存在 | 访问不存在的商品ID | 显示404页面 |
| 收藏已满 | 收藏第101件商品 | 返回错误提示"收藏数量已达上限" |
| 收藏自己的商品 | 收藏自己发布的商品 | 返回错误提示"不能收藏自己的商品" |
| 举报重复 | 对同一商品重复举报 | 返回错误提示"已举报过该商品" |

### 6.12 当前状态

- 已完成

---

## 模块七：私信沟通

### 7.1 模块目标

实现站内私信功能，支持文字消息、图片消息、实时推送。

### 7.2 用户可见内容

**私信页面（/chat）：**
- 左侧会话列表
  - 会话头像
  - 会话名称
  - 最新消息预览
  - 最新消息时间
  - 未读红点
- 右侧聊天窗口
  - 关联商品卡片
  - 消息气泡（对方消息靠左，自己消息靠右）
  - 输入区域（图片按钮、输入框、发送按钮）

### 7.3 用户操作流程

1. 在商品详情页点击"联系卖家"
2. 跳转到私信页面，自动创建会话
3. 在输入框输入消息内容
4. 点击"发送"按钮
5. 消息实时显示在聊天窗口
6. 对方收到消息通知

### 7.4 前端页面任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| F7.1 | 创建私信页面 | frontend/pages/chat/index.vue | F1.6 | 页面能正常显示 | 已完成 |
| F7.2 | 创建会话列表组件 | frontend/components/chat/ConversationList.vue | F1.2 | 列表显示正常 | 已完成 |
| F7.3 | 创建聊天窗口组件 | frontend/components/chat/ChatWindow.vue | F1.2 | 窗口显示正常 | 已完成 |
| F7.4 | 创建消息气泡组件 | frontend/components/chat/MessageBubble.vue | F1.2 | 气泡样式正确 | 已完成 |
| F7.5 | 实现WebSocket连接 | frontend/plugins/websocket.ts | F1.1 | 连接成功 | 已完成 |
| F7.6 | 实现实时消息接收 | frontend/composables/useChat.ts | F7.5 | 消息实时显示 | 已完成 |
| F7.7 | 实现图片消息发送 | frontend/components/chat/ChatWindow.vue | F7.3 | 图片发送成功 | 已完成 |

### 7.5 Controller 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| B7.1 | 创建会话列表接口 | backend/src/main/java/.../controller/MessageController.java | S1.2 | GET /api/conversations 返回会话列表 | 已完成 |
| B7.2 | 创建会话详情接口 | backend/src/main/java/.../controller/MessageController.java | S1.2 | GET /api/conversations/{id} 返回会话详情 | 已完成 |
| B7.3 | 创建消息历史接口 | backend/src/main/java/.../controller/MessageController.java | S1.2 | GET /api/conversations/{id}/messages 返回消息列表 | 已完成 |
| B7.4 | 创建发送消息接口 | backend/src/main/java/.../controller/MessageController.java | S1.2 | POST /api/conversations/{id}/messages 返回消息 | 已完成 |
| B7.5 | 创建WebSocket端点 | backend/src/main/java/.../websocket/ChatWebSocketHandler.java | S1.1 | WebSocket连接成功 | 已完成 |

### 7.6 Service 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| S7.1 | 实现会话管理逻辑 | backend/src/main/java/.../service/MessageService.java | M7.1 | 会话创建成功 | 已完成 |
| S7.2 | 实现消息发送逻辑 | backend/src/main/java/.../service/MessageService.java | M7.2 | 消息保存成功 | 已完成 |
| S7.3 | 实现WebSocket推送 | backend/src/main/java/.../websocket/ChatWebSocketHandler.java | S7.2 | 消息实时推送 | 已完成 |
| S7.4 | 实现未读消息统计 | backend/src/main/java/.../service/MessageService.java | M7.2 | 统计正确 | 已完成 |

### 7.7 Mapper 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| M7.1 | 创建ConversationMapper | backend/src/main/java/.../mapper/ConversationMapper.java | M1.1 | 能正常查询会话 | 已完成 |
| M7.2 | 创建MessageMapper | backend/src/main/java/.../mapper/MessageMapper.java | M1.1 | 能正常查询消息 | 已完成 |

### 7.8 Repository / 数据保存任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| D7.1 | 创建Conversation实体类 | backend/src/main/java/.../entity/Conversation.java | M1.1 | 字段与数据库对应 | 已完成 |
| D7.2 | 创建Message实体类 | backend/src/main/java/.../entity/Message.java | M1.1 | 字段与数据库对应 | 已完成 |
| D7.3 | 创建消息发送请求DTO | backend/src/main/java/.../dto/request/MessageSendRequest.java | 无 | 字段验证生效 | 已完成 |

### 7.9 页面测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 登录后访问 http://localhost:3000/chat | 显示私信页面 |
| 2 | 点击一个会话 | 右侧显示聊天记录 |
| 3 | 在输入框输入"你好"，点击发送 | 消息显示在聊天窗口 |
| 4 | 用另一个账号登录，发送消息 | 原账号实时收到消息 |

### 7.10 接口测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | GET /api/conversations | 返回会话列表 |
| 2 | GET /api/conversations/1/messages | 返回消息历史 |
| 3 | POST /api/conversations/1/messages 请求体：`{"content":"你好","type":1}` | 返回消息信息 |

### 7.11 异常情况测试

| 场景 | 操作 | 预期结果 |
|------|------|----------|
| 消息为空 | 发送空消息 | 返回错误提示"消息内容不能为空" |
| 消息过长 | 发送超过1000字的消息 | 返回错误提示"消息内容过长" |
| 会话不存在 | 访问不存在的会话 | 返回错误提示"会话不存在" |

### 7.12 当前状态

- 已完成

---

## 模块八：议价协商

### 8.1 模块目标

实现出价、还价、接受、拒绝功能，支持多轮议价。

### 8.2 用户可见内容

**在聊天窗口中：**
- 出价消息（显示出价金额、接受/拒绝按钮）
- 还价消息（显示还价金额、接受/拒绝按钮）
- 出价状态（待处理/已接受/已拒绝）

**在商品详情页：**
- "出价"按钮（买家可见）

### 8.3 用户操作流程

**买家出价流程：**
1. 在商品详情页点击"出价"
2. 输入期望价格
3. 点击"发送出价"
4. 卖家收到出价通知

**卖家处理出价流程：**
1. 在聊天窗口看到出价消息
2. 点击"接受"、"拒绝"或"还价"
3. 如果还价，输入新的价格
4. 买家收到处理结果

### 8.4 前端页面任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| F8.1 | 实现出价弹窗 | frontend/components/chat/ChatWindow.vue | F1.2 | 弹窗正常显示 | 已完成 |
| F8.2 | 实现出价消息显示 | frontend/components/chat/MessageBubble.vue | F7.4 | 出价消息样式正确 | 已完成 |
| F8.3 | 实现接受/拒绝按钮 | frontend/components/chat/MessageBubble.vue | F8.2 | 按钮可点击 | 已完成 |
| F8.4 | 实现还价弹窗 | frontend/components/chat/ChatWindow.vue | F1.2 | 弹窗正常显示 | 已完成 |
| F8.5 | 实现"确认购买"按钮 | frontend/components/chat/ChatWindow.vue | F7.3 | 按钮可点击 | 未开始 |

### 8.5 Controller 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| B8.1 | 创建出价接口 | backend/src/main/java/.../controller/MessageController.java | S1.2 | POST /api/messages 返回成功 | 已完成 |
| B8.2 | 创建处理出价接口 | backend/src/main/java/.../controller/MessageController.java | S1.2 | POST /api/messages/{id}/offer 返回成功 | 已完成 |

### 8.6 Service 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| S8.1 | 实现出价逻辑 | backend/src/main/java/.../service/MessageService.java | S7.2 | 出价消息保存成功 | 已完成 |
| S8.2 | 实现处理出价逻辑 | backend/src/main/java/.../service/MessageService.java | S8.1 | 状态更新成功 | 已完成 |

### 8.7 Mapper 任务

无新增任务，复用M7.2。

### 8.8 Repository / 数据保存任务

无新增任务。

### 8.9 页面测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 在商品详情页点击"出价" | 弹出出价弹窗 |
| 2 | 输入价格"100"，点击"发送" | 出价消息显示在聊天窗口 |
| 3 | 用卖家账号登录，查看聊天 | 看到出价消息，有接受/拒绝按钮 |
| 4 | 点击"还价"，输入"120"，点击"发送" | 还价消息显示在聊天窗口 |

### 8.10 接口测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | POST /api/messages/{id}/offer 请求体：`{"price":100}` | 返回出价消息 |
| 2 | PUT /api/messages/{id}/offer 请求体：`{"action":"accept"}` | 返回接受结果 |

### 8.11 异常情况测试

| 场景 | 操作 | 预期结果 |
|------|------|----------|
| 出价为负数 | 输入-100出价 | 返回错误提示"价格不能为负数" |
| 出价为0 | 输入0出价 | 返回错误提示"价格必须大于0" |
| 重复出价 | 对同一商品多次出价 | 允许出价，显示最新出价 |
| 拒绝后出价 | 拒绝出价后再次出价 | 允许出价 |

### 8.12 当前状态

- 已完成（"确认购买"按钮属于模块九：交易确认）

---

## 模块九：交易确认

### 9.1 模块目标

实现交易确认流程，支持卖家标记交付、买家确认收货、交易取消。

### 9.2 用户可见内容

**交易记录页面（/user/transactions）：**
- 交易列表（进行中/未开始/已取消）
- 交易详情（商品信息、价格、交易对象、状态）
- 操作按钮（标记已交付/确认收货/申请取消）

### 9.3 用户操作流程

**创建交易流程：**
1. 买家在聊天窗口点击"确认购买"
2. 系统创建交易记录
3. 卖家收到通知

**卖家标记交付流程：**
1. 卖家在交易记录中找到进行中的交易
2. 点击"标记已交付"
3. 买家收到通知

**买家确认收货流程：**
1. 买家在交易记录中找到待确认的交易
2. 点击"确认收货"
3. 交易完成，双方进入评价

**取消交易流程：**
1. 在交易记录中点击"申请取消"
2. 对方收到通知
3. 对方同意后，交易取消
4. 商品自动重新上架

### 9.4 前端页面任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| F9.1 | 创建交易记录页面 | frontend/pages/user/transactions.vue | F1.6 | 页面能正常显示 | 未开始 |
| F9.2 | 实现交易列表组件 | frontend/components/transaction/TransactionList.vue | F1.2 | 列表显示正常 | 未开始 |
| F9.3 | 实现交易详情组件 | frontend/components/transaction/TransactionDetail.vue | F1.2 | 详情显示正确 | 未开始 |
| F9.4 | 实现确认购买功能 | frontend/components/chat/ChatWindow.vue | F8.5 | 交易创建成功 | 未开始 |
| F9.5 | 实现取消交易弹窗 | frontend/components/transaction/CancelDialog.vue | F1.2 | 弹窗正常显示 | 未开始 |

### 9.5 Controller 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| B9.1 | 创建交易列表接口 | backend/src/main/java/.../controller/TransactionController.java | S1.2 | GET /api/transactions 返回交易列表 | 未开始 |
| B9.2 | 创建交易详情接口 | backend/src/main/java/.../controller/TransactionController.java | S1.2 | GET /api/transactions/{id} 返回交易详情 | 未开始 |
| B9.3 | 创建标记交付接口 | backend/src/main/java/.../controller/TransactionController.java | S1.2 | PUT /api/transactions/{id}/deliver 返回成功 | 未开始 |
| B9.4 | 创建确认收货接口 | backend/src/main/java/.../controller/TransactionController.java | S1.2 | PUT /api/transactions/{id}/confirm 返回成功 | 未开始 |
| B9.5 | 创建申请取消接口 | backend/src/main/java/.../controller/TransactionController.java | S1.2 | POST /api/transactions/{id}/cancel 返回成功 | 未开始 |
| B9.6 | 创建同意取消接口 | backend/src/main/java/.../controller/TransactionController.java | S1.2 | PUT /api/transactions/{id}/cancel 返回成功 | 未开始 |

### 9.6 Service 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| S9.1 | 实现创建交易逻辑 | backend/src/main/java/.../service/TransactionService.java | M9.1 | 交易记录创建成功 | 未开始 |
| S9.2 | 实现标记交付逻辑 | backend/src/main/java/.../service/TransactionService.java | M9.1 | 状态更新为"卖家已交付" | 未开始 |
| S9.3 | 实现确认收货逻辑 | backend/src/main/java/.../service/TransactionService.java | M9.1 | 状态更新为"未开始" | 未开始 |
| S9.4 | 实现取消交易逻辑 | backend/src/main/java/.../service/TransactionService.java | M9.1 | 状态更新为"已取消" | 未开始 |
| S9.5 | 实现超时自动完成 | backend/src/main/java/.../service/TransactionService.java | M9.1 | 7天后自动完成 | 未开始 |

### 9.7 Mapper 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| M9.1 | 创建TransactionMapper | backend/src/main/java/.../mapper/TransactionMapper.java | M1.1 | 能正常查询交易 | 未开始 |

### 9.8 Repository / 数据保存任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| D9.1 | 创建Transaction实体类 | backend/src/main/java/.../entity/Transaction.java | M1.1 | 字段与数据库对应 | 未开始 |
| D9.2 | 创建交易响应DTO | backend/src/main/java/.../dto/response/TransactionResponse.java | 无 | 返回字段正确 | 未开始 |

### 9.9 页面测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 登录后访问 http://localhost:3000/user/transactions | 显示交易记录页面 |
| 2 | 点击"进行中"标签 | 显示进行中的交易 |
| 3 | 点击"未开始"标签 | 显示未开始的交易 |
| 4 | 点击"已取消"标签 | 显示已取消的交易 |

### 9.10 接口测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | POST /api/transactions 请求体：`{"productId":1}` | 返回交易信息 |
| 2 | PUT /api/transactions/1/deliver | 返回 `{"code":200,"message":"标记成功"}` |
| 3 | PUT /api/transactions/1/confirm | 返回 `{"code":200,"message":"确认成功"}` |

### 9.11 异常情况测试

| 场景 | 操作 | 预期结果 |
|------|------|----------|
| 商品已售 | 对已售商品创建交易 | 返回错误提示"商品已售出" |
| 重复确认 | 已确认后再次确认 | 返回错误提示"交易未开始" |
| 非卖家标记 | 买家尝试标记交付 | 返回错误提示"无权限" |
| 超时确认 | 超过7天未确认 | 自动完成交易 |

### 9.12 当前状态

- 未开始

---

## 模块十：信用评价

### 10.1 模块目标

实现交易完成后双方互评功能，支持评分、文字评价、标签。

### 10.2 用户可见内容

**评价弹窗：**
- 评分选择（1-5星）
- 评价内容输入框
- 评价标签选择（描述准确/沟通顺畅/交易守时等）
- 提交按钮

**个人主页评价展示：**
- 好评率显示
- 评价列表（最近10条）

### 10.3 用户操作流程

1. 交易完成后，收到评价提醒通知
2. 点击通知或在交易记录中点击"去评价"
3. 选择评分（1-5星）
4. 输入评价内容（选填）
5. 选择评价标签（选填）
6. 点击"提交评价"
7. 评价显示在对方个人主页

### 10.4 前端页面任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| F10.1 | 创建评价弹窗组件 | frontend/components/review/ReviewDialog.vue | F1.2 | 弹窗正常显示 | 未开始 |
| F10.2 | 实现评分选择组件 | frontend/components/review/RatingSelect.vue | F1.2 | 评分选择正常 | 未开始 |
| F10.3 | 实现标签选择组件 | frontend/components/review/TagSelect.vue | F1.2 | 标签选择正常 | 未开始 |
| F10.4 | 创建评价列表组件 | frontend/components/review/ReviewList.vue | F1.2 | 列表显示正常 | 未开始 |
| F10.5 | 在个人主页显示评价 | frontend/pages/user/profile.vue | F10.4 | 评价显示正确 | 未开始 |

### 10.5 Controller 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| B10.1 | 创建提交评价接口 | backend/src/main/java/.../controller/ReviewController.java | S1.2 | POST /api/reviews 返回成功 | 未开始 |
| B10.2 | 创建获取评价接口 | backend/src/main/java/.../controller/ReviewController.java | S1.2 | GET /api/reviews/{transactionId} 返回评价 | 未开始 |
| B10.3 | 创建用户评价列表接口 | backend/src/main/java/.../controller/UserController.java | S1.2 | GET /api/users/{id}/reviews 返回评价列表 | 未开始 |

### 10.6 Service 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| S10.1 | 实现评价提交逻辑 | backend/src/main/java/.../service/ReviewService.java | M10.1 | 评价保存成功 | 未开始 |
| S10.2 | 实现好评率计算 | backend/src/main/java/.../service/ReviewService.java | M10.1 | 好评率计算正确 | 未开始 |
| S10.3 | 实现超时默认好评 | backend/src/main/java/.../service/ReviewService.java | M10.1 | 7天后自动好评 | 未开始 |

### 10.7 Mapper 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| M10.1 | 创建ReviewMapper | backend/src/main/java/.../mapper/ReviewMapper.java | M1.1 | 能正常查询评价 | 未开始 |

### 10.8 Repository / 数据保存任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| D10.1 | 创建Review实体类 | backend/src/main/java/.../entity/Review.java | M1.1 | 字段与数据库对应 | 未开始 |
| D10.2 | 创建评价请求DTO | backend/src/main/java/.../dto/request/ReviewRequest.java | 无 | 字段验证生效 | 未开始 |
| D10.3 | 创建评价响应DTO | backend/src/main/java/.../dto/response/ReviewResponse.java | 无 | 返回字段正确 | 未开始 |

### 10.9 页面测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 交易完成后，收到评价提醒 | 通知中心显示评价提醒 |
| 2 | 点击"去评价"按钮 | 弹出评价弹窗 |
| 3 | 选择5星评分，输入"交易顺利" | 评分和内容显示正确 |
| 4 | 选择"描述准确"标签 | 标签选中 |
| 5 | 点击"提交评价" | 提示"评价成功" |

### 10.10 接口测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | POST /api/reviews 请求体：`{"transactionId":1,"rating":5,"content":"交易顺利","tags":["描述准确"]}` | 返回评价信息 |
| 2 | GET /api/users/1/reviews | 返回用户评价列表 |

### 10.11 异常情况测试

| 场景 | 操作 | 预期结果 |
|------|------|----------|
| 重复评价 | 对同一交易重复评价 | 返回错误提示"已评价过该交易" |
| 评分为空 | 不选择评分提交 | 返回错误提示"请选择评分" |
| 非交易方评价 | 非交易双方尝试评价 | 返回错误提示"无权限" |

### 10.12 当前状态

- 未开始

---

## 模块十一：通知中心

### 11.1 模块目标

实现消息通知功能，支持私信通知、互动通知、系统通知、交易通知。

### 11.2 用户可见内容

**通知中心页面（/notifications）：**
- 通知列表
  - 通知标签（私信/互动/系统/交易）
  - 通知内容
  - 通知时间
  - 未读红点
- "全部已读"按钮

**导航栏：**
- 通知图标
- 未读数量角标

### 11.3 用户操作流程

1. 点击导航栏通知图标
2. 跳转到通知中心页面
3. 查看通知列表
4. 点击通知可跳转到相关页面
5. 点击"全部已读"标记所有通知为已读

### 11.4 前端页面任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| F11.1 | 创建通知中心页面 | frontend/pages/notifications.vue | F1.6 | 页面能正常显示 | 未开始 |
| F11.2 | 创建通知列表组件 | frontend/components/notification/NotificationList.vue | F1.2 | 列表显示正常 | 未开始 |
| F11.3 | 创建通知项组件 | frontend/components/notification/NotificationItem.vue | F1.2 | 通知项样式正确 | 未开始 |
| F11.4 | 实现未读角标 | frontend/layouts/default.vue | F11.1 | 角标显示正确 | 未开始 |

### 11.5 Controller 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| B11.1 | 创建通知列表接口 | backend/src/main/java/.../controller/NotificationController.java | S1.2 | GET /api/notifications 返回通知列表 | 未开始 |
| B11.2 | 创建全部已读接口 | backend/src/main/java/.../controller/NotificationController.java | S1.2 | PUT /api/notifications/read-all 返回成功 | 未开始 |
| B11.3 | 创建标记已读接口 | backend/src/main/java/.../controller/NotificationController.java | S1.2 | PUT /api/notifications/{id}/read 返回成功 | 未开始 |
| B11.4 | 创建未读数量接口 | backend/src/main/java/.../controller/NotificationController.java | S1.2 | GET /api/notifications/unread-count 返回数量 | 未开始 |

### 11.6 Service 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| S11.1 | 实现通知创建逻辑 | backend/src/main/java/.../service/NotificationService.java | M11.1 | 通知创建成功 | 未开始 |
| S11.2 | 实现通知列表查询 | backend/src/main/java/.../service/NotificationService.java | M11.1 | 列表查询正确 | 未开始 |
| S11.3 | 实现已读标记逻辑 | backend/src/main/java/.../service/NotificationService.java | M11.1 | 标记成功 | 未开始 |

### 11.7 Mapper 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| M11.1 | 创建NotificationMapper | backend/src/main/java/.../mapper/NotificationMapper.java | M1.1 | 能正常查询通知 | 未开始 |

### 11.8 Repository / 数据保存任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| D11.1 | 创建Notification实体类 | backend/src/main/java/.../entity/Notification.java | M1.1 | 字段与数据库对应 | 未开始 |

### 11.9 页面测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 点击导航栏通知图标 | 跳转到通知中心页面 |
| 2 | 查看通知列表 | 显示各类通知 |
| 3 | 点击"全部已读" | 所有通知变为已读状态 |
| 4 | 刷新页面 | 未读角标消失 |

### 11.10 接口测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | GET /api/notifications | 返回通知列表 |
| 2 | PUT /api/notifications/read-all | 返回 `{"code":200}` |
| 3 | GET /api/notifications/unread-count | 返回未读数量 |

### 11.11 异常情况测试

| 场景 | 操作 | 预期结果 |
|------|------|----------|
| 通知为空 | 查看通知列表 | 显示"暂无通知" |
| 通知已读 | 点击已读通知 | 不做任何操作 |

### 11.12 当前状态

- 未开始

---

## 模块十二：我的发布管理

### 12.1 模块目标

实现用户管理已发布商品的功能，支持查看、编辑、上架/下架、删除、标记已售。

### 12.2 用户可见内容

**我的发布页面（/user/listings）：**
- 状态筛选标签（在售/已售/已下架/审核中/草稿）
- 商品列表
  - 商品图片
  - 商品标题
  - 商品价格
  - 商品状态
  - 操作按钮（编辑/上架/下架/删除）

### 12.3 用户操作流程

1. 登录后访问 http://localhost:3000/user/listings
2. 查看已发布的商品列表
3. 点击状态标签筛选
4. 点击"编辑"修改商品信息
5. 点击"上架"/"下架"切换状态
6. 点击"删除"删除商品

### 12.4 前端页面任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| F12.1 | 创建我的发布页面 | frontend/pages/user/listings.vue | F1.6 | 页面能正常显示 | 未开始 |
| F12.2 | 实现状态筛选功能 | frontend/pages/user/listings.vue | F12.1 | 筛选生效 | 未开始 |
| F12.3 | 实现编辑功能 | frontend/pages/product/publish.vue | F4.1 | 编辑页面显示正确 | 未开始 |
| F12.4 | 实现删除确认弹窗 | frontend/components/common/ConfirmDialog.vue | F1.2 | 弹窗正常显示 | 未开始 |

### 12.5 Controller 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| B12.1 | 创建我的发布列表接口 | backend/src/main/java/.../controller/ProductController.java | S1.2 | GET /api/products/user 返回列表 | 未开始 |
| B12.2 | 创建更新商品状态接口 | backend/src/main/java/.../controller/ProductController.java | S1.2 | PUT /api/products/{id}/status 返回成功 | 未开始 |
| B12.3 | 创建删除商品接口 | backend/src/main/java/.../controller/ProductController.java | S1.2 | DELETE /api/products/{id} 返回成功 | 未开始 |

### 12.6 Service 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| S12.1 | 实现商品状态更新逻辑 | backend/src/main/java/.../service/ProductService.java | M4.1 | 状态更新成功 | 未开始 |
| S12.2 | 实现商品删除逻辑 | backend/src/main/java/.../service/ProductService.java | M4.1 | 删除成功 | 未开始 |

### 12.7 Mapper 任务

无新增任务，复用M4.1。

### 12.8 Repository / 数据保存任务

无新增任务。

### 12.9 页面测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 登录后访问 http://localhost:3000/user/listings | 显示我的发布页面 |
| 2 | 点击"在售"标签 | 显示在售商品 |
| 3 | 点击"已售"标签 | 显示已售商品 |
| 4 | 点击"编辑"按钮 | 跳转到编辑页面 |
| 5 | 点击"下架"按钮 | 商品状态变为"已下架" |
| 6 | 点击"删除"按钮 | 弹出确认弹窗 |

### 12.10 接口测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | GET /api/users/1/listings?status=2 | 返回在售商品列表 |
| 2 | PUT /api/products/1/status 请求体：`{"status":4}` | 返回成功 |
| 3 | DELETE /api/products/1 | 返回成功 |

### 12.11 异常情况测试

| 场景 | 操作 | 预期结果 |
|------|------|----------|
| 删除已售商品 | 删除有交易的商品 | 返回错误提示"该商品有未完成的交易" |
| 下架已售商品 | 下架已售商品 | 返回错误提示"已售商品不能下架" |

### 12.12 当前状态

- 未开始

---

## 模块十三：我的收藏

### 13.1 模块目标

实现收藏列表查看功能，支持取消收藏。

### 13.2 用户可见内容

**我的收藏页面（/user/favorites）：**
- 收藏商品列表
  - 商品图片
  - 商品标题
  - 商品价格
  - 商品状态（在售/已售）
  - 取消收藏按钮

### 13.3 用户操作流程

1. 登录后访问 http://localhost:3000/user/favorites
2. 查看收藏的商品列表
3. 点击商品卡片进入详情
4. 点击"取消收藏"移除收藏

### 13.4 前端页面任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| F13.1 | 创建我的收藏页面 | frontend/pages/user/favorites.vue | F1.6 | 页面能正常显示 | 未开始 |
| F13.2 | 实现取消收藏功能 | frontend/pages/user/favorites.vue | F13.1, B6.2 | 取消成功 | 未开始 |

### 13.5 Controller 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| B13.1 | 创建收藏列表接口 | backend/src/main/java/.../controller/FavoriteController.java | S1.2 | GET /api/favorites 返回收藏列表 | 未开始 |

### 13.6 Service 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| S13.1 | 实现收藏列表查询 | backend/src/main/java/.../service/FavoriteService.java | M6.1 | 列表查询正确 | 未开始 |

### 13.7 Mapper 任务

无新增任务，复用M6.1。

### 13.8 Repository / 数据保存任务

无新增任务。

### 13.9 页面测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 登录后访问 http://localhost:3000/user/favorites | 显示我的收藏页面 |
| 2 | 查看收藏列表 | 显示收藏的商品 |
| 3 | 点击商品卡片 | 跳转到商品详情页 |
| 4 | 点击"取消收藏"按钮 | 商品从列表中移除 |

### 13.10 接口测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | GET /api/favorites | 返回收藏列表 |

### 13.11 异常情况测试

| 场景 | 操作 | 预期结果 |
|------|------|----------|
| 收藏为空 | 查看收藏列表 | 显示"暂无收藏" |

### 13.12 当前状态

- 未开始

---

## 模块十四：草稿箱

### 14.1 模块目标

实现草稿管理功能，支持查看、编辑、发布、删除草稿。

### 14.2 用户可见内容

**草稿箱页面（/user/drafts）：**
- 草稿列表
  - 商品图片
  - 商品标题
  - 保存时间
  - 操作按钮（编辑/发布/删除）

### 14.3 用户操作流程

1. 登录后访问 http://localhost:3000/user/drafts
2. 查看草稿列表
3. 点击"编辑"修改草稿
4. 点击"发布"发布草稿
5. 点击"删除"删除草稿

### 14.4 前端页面任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| F14.1 | 创建草稿箱页面 | frontend/pages/user/drafts.vue | F1.6 | 页面能正常显示 | 未开始 |
| F14.2 | 实现发布草稿功能 | frontend/pages/user/drafts.vue | F14.1, B4.3 | 发布成功 | 未开始 |
| F14.3 | 实现删除草稿功能 | frontend/pages/user/drafts.vue | F14.1, B12.3 | 删除成功 | 未开始 |

### 14.5 Controller 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| B14.1 | 创建草稿列表接口 | backend/src/main/java/.../controller/ProductController.java | S1.2 | GET /api/products/user?status=0 返回草稿列表 | 未开始 |

### 14.6 Service 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| S14.1 | 实现草稿列表查询 | backend/src/main/java/.../service/ProductService.java | M4.1 | 列表查询正确 | 未开始 |
| S14.2 | 实现草稿发布逻辑 | backend/src/main/java/.../service/ProductService.java | M4.1 | 状态更新为"审核中" | 未开始 |

### 14.7 Mapper 任务

无新增任务，复用M4.1。

### 14.8 Repository / 数据保存任务

无新增任务。

### 14.9 页面测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 登录后访问 http://localhost:3000/user/drafts | 显示草稿箱页面 |
| 2 | 查看草稿列表 | 显示保存的草稿 |
| 3 | 点击"编辑"按钮 | 跳转到编辑页面 |
| 4 | 点击"发布"按钮 | 草稿发布成功 |
| 5 | 点击"删除"按钮 | 草稿从列表中移除 |

### 14.10 接口测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | GET /api/products?status=0 | 返回草稿列表 |

### 14.11 异常情况测试

| 场景 | 操作 | 预期结果 |
|------|------|----------|
| 草稿为空 | 查看草稿列表 | 显示"暂无草稿" |
| 草稿过期 | 超过30天未发布 | 草稿自动删除 |

### 14.12 当前状态

- 未开始

---

## 模块十五：个人主页

### 15.1 模块目标

实现个人主页功能，展示用户信息、好评率、评价列表。

### 15.2 用户可见内容

**个人主页页面（/user/profile）：**
- 用户卡片
  - 头像
  - 昵称
  - 学号（部分隐藏）
  - 认证状态标识
  - 好评率
- 统计信息
  - 发布数量
  - 成交数量
  - 好评率
- 评价列表（最近10条）
- 侧边导航
  - 我的发布
  - 交易记录
  - 我的收藏
  - 草稿箱
  - 实名认证
  - 账号设置

### 15.3 用户操作流程

1. 登录后点击右上角用户头像
2. 跳转到个人主页
3. 查看个人信息和统计数据
4. 查看收到的评价
5. 点击侧边导航切换功能

### 15.4 前端页面任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| F15.1 | 创建个人主页页面 | frontend/pages/user/profile.vue | F1.6 | 页面能正常显示 | 未开始 |
| F15.2 | 创建用户卡片组件 | frontend/components/user/UserCard.vue | F1.2 | 卡片显示正确 | 未开始 |
| F15.3 | 创建侧边导航组件 | frontend/components/user/SidebarNav.vue | F1.2 | 导航切换正常 | 未开始 |
| F15.4 | 实现好评率显示 | frontend/pages/user/profile.vue | F15.1 | 好评率显示正确 | 未开始 |
| F15.5 | 创建卖家主页动态路由页面 | frontend/pages/user/[id].vue | F1.6, B15.1 | 访问 /user/{id} 显示卖家信息 | 未开始 |

### 15.5 Controller 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| B15.1 | 创建用户信息接口 | backend/src/main/java/.../controller/UserController.java | S1.2 | GET /api/users/{id} 返回用户信息 | 未开始 |
| B15.2 | 创建更新用户信息接口 | backend/src/main/java/.../controller/UserController.java | S1.2 | PUT /api/users/profile 返回成功 | 未开始 |

### 15.6 Service 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| S15.1 | 实现用户信息查询 | backend/src/main/java/.../service/UserService.java | M2.1 | 信息查询正确 | 未开始 |
| S15.2 | 实现用户信息更新 | backend/src/main/java/.../service/UserService.java | M2.1 | 更新成功 | 未开始 |

### 15.7 Mapper 任务

无新增任务，复用M2.1。

### 15.8 Repository / 数据保存任务

无新增任务。

### 15.9 页面测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 登录后点击用户头像 | 跳转到个人主页 |
| 2 | 查看用户信息 | 显示头像、昵称、学号、认证状态 |
| 3 | 查看统计数据 | 显示发布数、成交数、好评率 |
| 4 | 查看评价列表 | 显示最近10条评价 |
| 5 | 点击侧边导航 | 切换到对应功能页面 |

### 15.10 接口测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | GET /api/users/1 | 返回用户信息 |
| 2 | PUT /api/users/profile 请求体：`{"nickname":"新昵称"}` | 返回成功 |

### 15.11 异常情况测试

| 场景 | 操作 | 预期结果 |
|------|------|----------|
| 用户不存在 | 访问不存在的用户 | 显示404页面 |
| 昵称过长 | 输入超过50字的昵称 | 返回错误提示"昵称过长" |

### 15.12 当前状态

- 未开始

---

## 模块十六：管理后台

### 16.1 模块目标

实现管理后台功能，支持用户管理、商品审核、认证审核、举报处理。

### 16.2 用户可见内容

**管理后台首页（/admin）：**
- 数据概览
  - 今日新增用户数
  - 在售商品数
  - 今日成交数
  - 待审核数
- 待审核列表

**用户管理页面（/admin/users）：**
- 用户列表
- 封禁/解封操作

**商品管理页面（/admin/products）：**
- 商品列表
- 上架/下架操作

**审核中心页面（/admin/review）：**
- 实名认证审核（待审核列表，通过/拒绝）
- 商品举报审查（被举报商品，保留/下架）
- 用户举报审查（被举报用户，警告/封禁）

### 16.3 用户操作流程

1. 管理员登录后访问 http://localhost:3000/admin
2. 查看数据概览
3. 点击"用户管理"管理用户
4. 点击"商品管理"管理商品
5. 点击"审核中心"处理审核任务
   - 实名认证审核
   - 商品举报审查
   - 用户举报审查

### 16.4 前端页面任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| F16.1 | 创建管理后台布局 | frontend/layouts/admin.vue | F1.6 | 布局显示正常 | 未开始 |
| F16.2 | 创建后台首页 | frontend/pages/admin/index.vue | F16.1 | 页面能正常显示 | 未开始 |
| F16.3 | 创建用户管理页面 | frontend/pages/admin/users.vue | F16.1 | 页面能正常显示 | 未开始 |
| F16.4 | 创建商品管理页面 | frontend/pages/admin/products.vue | F16.1 | 页面能正常显示 | 未开始 |
| F16.5 | 创建审核中心页面 | frontend/pages/admin/review.vue | F16.1 | 页面能正常显示 | 未开始 |
| F16.6 | 创建管理员登录中间件 | frontend/middleware/admin.ts | F1.7 | 非管理员无法访问 | 未开始 |

### 16.5 Controller 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| B16.1 | 创建数据概览接口 | backend/src/main/java/.../controller/AdminController.java | S1.2 | GET /api/admin/dashboard 返回统计数据 | 未开始 |
| B16.2 | 创建用户管理接口 | backend/src/main/java/.../controller/AdminController.java | S1.2 | GET /api/admin/users 返回用户列表 | 未开始 |
| B16.3 | 创建封禁/解封接口 | backend/src/main/java/.../controller/AdminController.java | S1.2 | PUT /api/admin/users/{id}/status 返回成功 | 未开始 |
| B16.4 | 创建商品状态接口 | backend/src/main/java/.../controller/AdminController.java | S1.2 | PUT /api/admin/products/{id}/status 返回成功 | 未开始 |
| B16.5 | 创建认证审核接口 | backend/src/main/java/.../controller/AdminController.java | S1.2 | PUT /api/admin/review/verifications/{id} 返回成功 | 未开始 |
| B16.6 | 创建商品举报审查接口 | backend/src/main/java/.../controller/AdminController.java | S1.2 | PUT /api/admin/review/products/{id} 返回成功 | 未开始 |
| B16.7 | 创建用户举报审查接口 | backend/src/main/java/.../controller/AdminController.java | S1.2 | PUT /api/admin/review/users/{id} 返回成功 | 未开始 |

### 16.6 Service 任务

| 任务ID | 任务描述 | 涉及文件 | 前置依赖 | 验收方式 | 状态 |
|--------|----------|----------|----------|----------|------|
| S16.1 | 实现数据概览逻辑 | backend/src/main/java/.../service/AdminService.java | M2.1 | 统计数据正确 | 未开始 |
| S16.2 | 实现用户管理逻辑 | backend/src/main/java/.../service/AdminService.java | M2.1 | 封禁/解封成功 | 未开始 |
| S16.3 | 实现商品状态管理逻辑 | backend/src/main/java/.../service/AdminService.java | M4.1 | 上架/下架成功 | 未开始 |
| S16.4 | 实现认证审核逻辑 | backend/src/main/java/.../service/AdminService.java | M2.1 | 认证状态更新成功 | 未开始 |
| S16.5 | 实现商品举报审查逻辑 | backend/src/main/java/.../service/AdminService.java | M6.2 | 举报状态更新成功 | 未开始 |
| S16.6 | 实现用户举报审查逻辑 | backend/src/main/java/.../service/AdminService.java | M2.1 | 用户状态更新成功 | 未开始 |

### 16.7 Mapper 任务

无新增任务，复用之前的Mapper。

### 16.8 Repository / 数据保存任务

无新增任务。

### 16.9 页面测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | 管理员登录后访问 http://localhost:3000/admin | 显示后台首页 |
| 2 | 查看数据概览 | 显示统计数据 |
| 3 | 点击"用户管理" | 显示用户列表 |
| 4 | 点击"商品审核" | 显示待审核商品 |
| 5 | 点击"认证审核" | 显示待审核认证 |
| 6 | 点击"举报处理" | 显示待处理举报 |

### 16.10 接口测试方法

| 步骤 | 操作 | 预期结果 |
|------|------|----------|
| 1 | GET /api/admin/dashboard | 返回统计数据 |
| 2 | GET /api/admin/users | 返回用户列表 |
| 3 | PUT /api/admin/users/1/status 请求体：`{"status":0}` | 返回成功 |
| 4 | PUT /api/admin/products/1/audit 请求体：`{"status":2}` | 返回成功 |

### 16.11 异常情况测试

| 场景 | 操作 | 预期结果 |
|------|------|----------|
| 非管理员访问 | 普通用户访问后台 | 返回403错误 |
| 审核不存在的商品 | 审核不存在的商品ID | 返回404错误 |

### 16.12 当前状态

- 未开始

---

## 任务依赖关系图

```
基础框架搭建 (模块一)
    ↓
用户登录注册 (模块二)
    ↓
    ├── 实名认证 (模块三)
    │       ↓
    │   商品发布 (模块四)
    │       ↓
    │   商品搜索与浏览 (模块五)
    │       ↓
    │   商品详情 (模块六)
    │       ↓
    │   我的发布管理 (模块十二)
    │   我的收藏 (模块十三)
    │   草稿箱 (模块十四)
    │   个人主页 (模块十五)
    │
    └── 私信沟通 (模块七)
            ↓
        议价协商 (模块八)
            ↓
        交易确认 (模块九)
            ↓
        信用评价 (模块十)
            ↓
        通知中心 (模块十一)
            ↓
        管理后台 (模块十六)
```

---

**最后更新：** 2026-05-28
