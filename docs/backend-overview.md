# DumpDiary Backend 说明文档

## 1. 后端是做什么的

这个后端是 `DumpDiary` 安卓 App 的服务端，当前用 `Kotlin + Ktor` 实现，主要负责 6 类事情：

1. 账号注册、登录、刷新 token、退出登录
2. 邮箱验证码发送与校验
3. 个人资料读取与更新
4. 排便日志的增删改查
5. 好友关系和共享数据
6. 统计数据与 App 版本更新信息

当前入口文件是：
[Application.kt](/Users/zhengxiqiao/Desktop/DumpDiary/backend/src/main/kotlin/com/dumpdiary/backend/Application.kt)

## 2. 当前技术栈

- Web 框架：`Ktor`
- 运行引擎：`Netty`
- 序列化：`kotlinx.serialization`
- 密码哈希：`BCrypt`
- 邮件发送：`jakarta.mail`
- 数据存储：`InMemoryStore`

依赖定义在：
[backend/build.gradle.kts](/Users/zhengxiqiao/Desktop/DumpDiary/backend/build.gradle.kts)

## 3. 当前后端的核心限制

这个后端现在是 **MVP 开发版后端**，不是正式生产后端。

最重要的限制有 3 个：

1. 数据已落盘，但不是数据库
   账号、资料、好友、日志、验证码、refresh token 会保存到本地 JSON 文件，服务重启后会自动恢复。

2. 没有数据库
   目前没有接 PostgreSQL / MySQL / Redis，而是使用本地文件持久化。

3. access token 仍然是内存态
   服务重启后旧 access token 会失效，但 refresh token 已持久化，客户端可通过 refresh 重新拿到新的 access token。

对应存储实现文件：
[InMemoryStore.kt](/Users/zhengxiqiao/Desktop/DumpDiary/backend/src/main/kotlin/com/dumpdiary/backend/repository/InMemoryStore.kt)

## 4. 启动时做了什么

后端启动时主要做这些初始化：

1. 创建 `InMemoryStore`
2. 创建 `TokenService`
3. 创建 `EmailSender`
4. 创建 `AuthService`
5. 创建头像上传目录 `uploads/`
6. 安装 Ktor 插件
7. 注册认证与业务路由
8. 暴露静态资源目录

已经启用的 Ktor 能力包括：

- `DefaultHeaders`
- `Compression`
- `PartialContent`
- `CallLogging`
- `CORS`
- `ContentNegotiation`
- `StatusPages`
- `Authentication`

## 5. 路由总览

### 公开路由

- `GET /`
  服务健康检查

- `GET /app/version`
  返回最新 App 版本信息，给登录页弹窗更新用

- `POST /auth/register`
  注册账号

- `POST /auth/login`
  邮箱密码登录

- `POST /auth/send-email-code`
  发送邮箱验证码

- `POST /auth/verify-email-code`
  校验邮箱验证码

- `POST /auth/reset-password`
  重置密码

- `POST /auth/refresh`
  刷新 access token

- `POST /auth/logout`
  退出登录

### 需要登录的路由

- `GET /me/profile`
- `PUT /me/profile`
- `POST /me/avatar`
- `GET /friends`
- `POST /friends`
- `GET /logs`
- `POST /logs`
- `PUT /logs/{id}`
- `DELETE /logs/{id}`
- `GET /stats/monthly`
- `GET /stats/streak`
- `GET /stats/yearly`

### 静态资源路由

- `GET /uploads/...`
  头像文件访问

- `GET /downloads/app-debug.apk`
  给客户端更新弹窗下载 APK 用

## 6. 账号与鉴权是怎么做的

### 注册

注册流程在：
[AuthService.kt](/Users/zhengxiqiao/Desktop/DumpDiary/backend/src/main/kotlin/com/dumpdiary/backend/service/AuthService.kt)

核心逻辑：

1. 校验邮箱不能为空
2. 校验密码至少 8 位
3. 校验注册码是否正确且未过期
4. 检查邮箱是否已注册
5. 用 `BCrypt` 哈希密码
6. 创建 `UserAccount`
7. 创建默认 `UserProfile`
8. 签发 access token + refresh token

### 登录

登录逻辑：

1. 按邮箱查用户
2. 用 `BCrypt` 校验密码
3. 找到该用户 profile
4. 返回新的 access token / refresh token / profile

### Refresh Token

Refresh 逻辑在：
[TokenService.kt](/Users/zhengxiqiao/Desktop/DumpDiary/backend/src/main/kotlin/com/dumpdiary/backend/security/TokenService.kt)

当前实现比较轻量：

- access token：随机 UUID 字符串
- refresh token：随机 UUID 字符串
- token 与 userId 的映射存在内存里

### 鉴权方式

Ktor 使用 `bearer auth`：

- 客户端把 `Authorization: Bearer xxx` 带上
- 后端从 token 反查当前用户 `userId`
- 成功后注入 `AuthenticatedUser`

## 7. 邮箱验证码做了什么

验证码逻辑也在：
[AuthService.kt](/Users/zhengxiqiao/Desktop/DumpDiary/backend/src/main/kotlin/com/dumpdiary/backend/service/AuthService.kt)

### 当前支持两种用途

- `REGISTER`
- `RESET_PASSWORD`

### 发送验证码时的流程

1. 生成 6 位随机数字
2. 存入 `verificationCodes`
3. 设置 10 分钟过期
4. 调用 `EmailSender` 发邮件

### 校验验证码时的流程

1. 用 `purpose + email` 找记录
2. 校验验证码是否一致
3. 校验是否过期
4. 成功后删除这条验证码记录，避免重复使用

### SMTP 做了什么

邮件发送在：
[EmailSender.kt](/Users/zhengxiqiao/Desktop/DumpDiary/backend/src/main/kotlin/com/dumpdiary/backend/service/EmailSender.kt)

支持的配置包括：

- `SMTP_HOST`
- `SMTP_PORT`
- `SMTP_USERNAME`
- `SMTP_PASSWORD`
- `SMTP_FROM`
- `SMTP_STARTTLS`
- `SMTP_SSL`

如果 SMTP 没配，后端会返回开发提示：

- `SMTP is not configured. Development code: xxxxxx`

你现在已经把 163 SMTP 接上了，所以可以真实发邮件。

## 8. 个人资料做了什么

资料路由在：
[ProfileRoutes.kt](/Users/zhengxiqiao/Desktop/DumpDiary/backend/src/main/kotlin/com/dumpdiary/backend/routes/ProfileRoutes.kt)

### `GET /me/profile`

返回当前登录用户资料：

- `userId`
- `displayName`
- `avatarUrl`
- `updatedAt`

### `PUT /me/profile`

更新昵称：

- 只更新 `displayName`
- 自动刷新 `updatedAt`

### `POST /me/avatar`

上传头像：

1. 接收 multipart 文件
2. 写入 `backend/uploads/`
3. 生成 `/uploads/xxx` 路径
4. 把这个路径写回 profile

## 9. 日志做了什么

日志路由在：
[LogRoutes.kt](/Users/zhengxiqiao/Desktop/DumpDiary/backend/src/main/kotlin/com/dumpdiary/backend/routes/LogRoutes.kt)

### `GET /logs`

不是只返回自己的日志，而是返回“当前用户可见日志”：

- 自己的日志
- 已添加好友的日志

这个可见性逻辑在：
[InMemoryStore.kt](/Users/zhengxiqiao/Desktop/DumpDiary/backend/src/main/kotlin/com/dumpdiary/backend/repository/InMemoryStore.kt)

### `POST /logs`

创建日志时会检查：

- `log.userId` 必须等于当前登录用户

### `PUT /logs/{id}`

更新日志时会检查：

- 路径 id 和 body id 必须一致
- `log.userId` 必须等于当前登录用户
- 自动更新 `updatedAt`

### `DELETE /logs/{id}`

这里不是物理删除，而是：

- 把 `isDeleted = true`
- 更新 `updatedAt`

也就是“软删除”。

## 10. 好友共享做了什么

好友逻辑在：
[FriendRoutes.kt](/Users/zhengxiqiao/Desktop/DumpDiary/backend/src/main/kotlin/com/dumpdiary/backend/routes/FriendRoutes.kt)

### `GET /friends`

返回当前用户的可见好友资料：

- `userId`
- `email`
- `displayName`
- `avatarUrl`

### `POST /friends`

添加好友流程：

1. 按邮箱找目标用户
2. 不能添加自己
3. 建立双向好友关系

当前是双向共享：

- A 加了 B
- B 也会自动看到 A

## 11. 统计做了什么

统计路由在：
[StatsRoutes.kt](/Users/zhengxiqiao/Desktop/DumpDiary/backend/src/main/kotlin/com/dumpdiary/backend/routes/StatsRoutes.kt)

### 月统计 `GET /stats/monthly`

输入：

- `month=YYYY-MM`

输出：

- `month`
- `totalCount`
- `activeDays`

这里统计的是“当前用户可见日志”，也就是自己 + 好友。

### 连续统计 `GET /stats/streak`

输出：

- `currentStreakDays`
- `maxStreakDays`

规则：

- 连续天数按“连续多少天至少有 1 条记录”计算
- 如果最后一次记录是今天或昨天，才认为当前连续还在继续

### 年统计 `GET /stats/yearly`

输入：

- `year=YYYY`

输出：

- 1 到 12 月每个月的记录数

## 12. App 更新接口做了什么

这块在：
[Application.kt](/Users/zhengxiqiao/Desktop/DumpDiary/backend/src/main/kotlin/com/dumpdiary/backend/Application.kt)

`GET /app/version` 会返回：

- `versionCode`
- `versionName`
- `downloadPath`
- `notes`

客户端登录页拿这个接口做“发现新版本弹窗”。

当前默认值来自环境变量：

- `DUMPDIARY_LATEST_VERSION_CODE`
- `DUMPDIARY_LATEST_VERSION_NAME`
- `DUMPDIARY_LATEST_VERSION_NOTES`

如果没配，就用默认：

- `versionCode = 2`
- `versionName = 1.1`

## 13. 数据持久化做了什么

后端现在会把核心数据保存到本地文件：

- `backend/data/store.json`

这里会持久化：

- 用户账号
- 用户资料
- 好友关系
- 排便日志
- 验证码
- refresh token

也就是说：

- App 重启后，本地 Room 数据还在
- 后端重启后，服务端数据也还在
- 客户端重新启动时会先同步本地待上传日志，再从服务端拉最新数据

## 14. 配置文件做了什么

你当前本地最重要的配置文件是：
[backend/.env](/Users/zhengxiqiao/Desktop/DumpDiary/backend/.env)

它现在主要承担两件事：

1. 配 SMTP 发信参数
2. 配 App 更新信息

模板文件在：
[backend/.env.example](/Users/zhengxiqiao/Desktop/DumpDiary/backend/.env.example)

## 15. 启停脚本做了什么

### 启动

[start-backend.sh](/Users/zhengxiqiao/Desktop/DumpDiary/scripts/start-backend.sh)

作用：

1. 读取 `backend/.env`
2. 导出环境变量
3. 启动 `./gradlew :backend:run`

### 停止

[stop-backend.sh](/Users/zhengxiqiao/Desktop/DumpDiary/scripts/stop-backend.sh)

作用：

1. 读取 `backend/backend.pid`
2. 终止后端进程

## 16. 现在这套后端最适合干什么

适合：

- 你自己本机开发
- 模拟器调试
- 同一 Wi‑Fi 下真机联调
- MVP 演示

还不适合：

- 正式上线
- 多人长期稳定使用
- 重启后数据还要保留
- 高并发访问

## 17. 如果后面要升级，我建议优先做什么

优先级最高的升级项有：

1. 接入数据库
   用 PostgreSQL 替掉 `InMemoryStore`

2. 持久化 token 与验证码
   避免服务重启后全部丢失

3. 增加日志与监控
   比如结构化日志、错误告警

4. 补权限与频控
   比如验证码限流、登录失败频控

5. 改成真正的发布更新链路
   比如 OSS / COS / GitHub Releases / Google Play

## 18. 一句话总结

这个 backend 现在本质上是一个：

**支持邮箱登录、验证码、资料、日志、好友共享、统计和版本更新的 Ktor MVP 开发后端，并且已经支持本地文件持久化恢复。**
