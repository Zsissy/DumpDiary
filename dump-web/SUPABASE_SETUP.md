# DumpDiary Supabase 配置指南

## 1. 创建 Supabase 项目

1. 前往 [supabase.com](https://supabase.com) 注册/登录
2. 创建新项目，记下项目 URL 和 anon key
3. 进入 SQL Editor，依次执行以下 SQL

## 2. 创建表

### app_users 表

```sql
create table if not exists public.app_users (
  id uuid primary key default gen_random_uuid(),
  username text not null unique,
  nickname text not null default '',
  password text not null,
  role text not null default 'member',
  status text not null default 'pending',
  avatar text not null default '',
  match_code text not null default '',
  created_at timestamptz not null default now(),
  reviewed_at timestamptz,
  reviewed_by text
);
```

### app_sync_rooms 表

```sql
create table if not exists public.app_sync_rooms (
  room_code text primary key,
  bowel_logs jsonb not null default '[]'::jsonb,
  updated_at timestamptz not null default now()
);
```

## 3. 启用 RLS 并创建策略

```sql
alter table public.app_users enable row level security;
alter table public.app_sync_rooms enable row level security;

-- app_users 策略（匿名访问用于客户端直连）
create policy "public_select_app_users" on public.app_users for select to anon using (true);
create policy "public_insert_app_users" on public.app_users for insert to anon with check (true);
create policy "public_update_app_users" on public.app_users for update to anon using (true) with check (true);

-- app_sync_rooms 策略
create policy "public_select_sync_rooms" on public.app_sync_rooms for select to anon using (true);
create policy "public_insert_sync_rooms" on public.app_sync_rooms for insert to anon with check (true);
create policy "public_update_sync_rooms" on public.app_sync_rooms for update to anon using (true) with check (true);
```

## 4. 配置环境变量

1. 在项目根目录创建 `.env` 文件（从 `.env.example` 复制）：
```
VITE_SUPABASE_URL=https://your-project-id.supabase.co
VITE_SUPABASE_ANON_KEY=your-anon-key
```

2. 在 GitHub 仓库 Settings > Secrets and variables > Actions 中添加：
   - `VITE_SUPABASE_URL`
   - `VITE_SUPABASE_ANON_KEY`

## 5. 管理员账号

应用内置管理员账号，可在 `src/context/AuthContext.jsx` 中修改：
- 默认用户名：`小茭`
- 默认密码：`zxq121800`

管理员登录后可在 `/admin` 页面审核新注册用户。

## 6. 部署

推送 `main` 分支后，GitHub Actions 自动构建并部署到 `https://zsissy.github.io/dump-diary/`。
