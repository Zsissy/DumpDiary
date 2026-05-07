# 飞牛 NAS 部署 Node 后端

## 目录

后端目录：

`/Users/zhengxiqiao/Desktop/DumpDiary/backend-node`

## 你需要准备

- 一台已启用 Docker / Compose 的飞牛 NAS
- 一个可以公网访问的域名（推荐）
- 邮箱 SMTP 参数
- 一个 release APK 放到 `downloads/`

## 推荐部署方式

把整个 `backend-node/` 上传到 NAS，然后执行：

```bash
cd backend-node
cp .env.example .env
docker compose up -d --build
```

## 关键挂载

- `./data:/app/data`
- `./uploads:/app/uploads`
- `./downloads:/app/downloads`

这样重启容器后：

- 用户数据还在
- 头像还在
- APK 下载文件还在

## 环境变量

至少需要填写：

- `SMTP_HOST`
- `SMTP_PORT`
- `SMTP_USERNAME`
- `SMTP_PASSWORD`
- `SMTP_FROM`
- `SMTP_SSL`
- `SMTP_STARTTLS`

版本更新相关：

- `DUMPDIARY_LATEST_VERSION_CODE`
- `DUMPDIARY_LATEST_VERSION_NAME`
- `DUMPDIARY_LATEST_VERSION_NOTES`
- `DUMPDIARY_DOWNLOAD_PATH`

## APK 下载

把你的正式包放到：

`backend-node/downloads/`

例如：

`backend-node/downloads/dumpdiary-1.1.apk`

然后在 `.env` 里写：

```env
DUMPDIARY_DOWNLOAD_PATH=/downloads/dumpdiary-1.1.apk
```

## Android 客户端切换到线上地址

重新打包时指定：

```bash
./gradlew :app:exportReleaseApk -PdumpDiaryApiBaseUrl=https://your-domain.com/
```

这样手机端就不会再依赖你电脑本地 `10.12.x.x:8080`。
