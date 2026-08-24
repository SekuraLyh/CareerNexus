# CareerNexus 中间件部署

使用服务器上已有的 `docker-compose.yml` 部署 MySQL 和 Redis。

## 前置条件

在服务器上确认 Docker 和 Compose 可用：

```bash
docker --version
docker compose version
```

进入 `docker-compose.yml` 所在目录：

```bash
cd /你的/Compose文件所在目录
```

## 启动

先检查 Compose 文件格式：

```bash
docker compose config
```

启动两个中间件：

```bash
docker compose up -d
```

`-d` 表示后台运行。首次执行时，Docker 会拉取镜像、创建容器、创建 `CareerNexus` 网络，并挂载 Compose 文件中配置的数据目录和 Redis 配置文件。

查看运行状态：

```bash
docker compose ps
```

状态为 `Up` 表示容器正在运行。

## 服务地址

假设服务器 IP 为 `192.168.100.10`：

| 服务 | 服务器访问地址 | Compose 网络内地址 |
| --- | --- | --- |
| MySQL | `192.168.100.10:13306` | `mysql:3306` |
| Redis | `192.168.100.10:16379` | `redis:6379` |

服务器外部的应用使用服务器 IP 和左侧端口；以后加入同一 Compose 网络的容器，使用右侧服务名和容器端口。

## 查看日志

```bash
# 查看 MySQL 日志
docker compose logs mysql

# 持续查看 Redis 日志
docker compose logs -f redis
```

Redis 连通性测试：

```bash
docker compose exec redis redis-cli ping
```

如果 `redis.conf` 配置了密码：

```bash
docker compose exec redis redis-cli -a 'Redis密码' ping
```

返回 `PONG` 表示 Redis 正常。

MySQL 登录测试：

```bash
docker compose exec mysql mysql -uroot -p
```

## 停止和重启

```bash
# 停止并删除容器，保留数据目录
docker compose down

# 重新启动已有容器
docker compose start

# 重启服务
docker compose restart mysql redis
```

不要使用 `docker compose down -v`，当前 Compose 使用宿主机目录挂载，删除数据目录才会真正删除数据；不要手动删除 `mysql/data` 或 `redis/data`。

## 修改配置后重新应用

编辑 `docker-compose.yml` 或 `redis/redis.conf` 后执行：

```bash
docker compose up -d
```

Compose 会比较配置变化：没有变化的容器继续复用，发生变化的容器会被重新创建。宿主机挂载的 MySQL 和 Redis 数据不会因容器重建而删除。

拉取最新镜像并应用：

```bash
docker compose pull
docker compose up -d
```

## 自动重启

Compose 中的：

```yaml
restart: unless-stopped
```

表示 Docker 服务或服务器重启后自动启动容器；如果手动执行 `docker compose stop` 或 `docker stop`，容器不会自动恢复，手动启动即可：

```bash
docker compose start
```

## 常见问题

### 端口被占用

```bash
sudo ss -lntp | grep -E '13306|16379'
```

修改 Compose 中左侧端口即可，例如：

```yaml
ports:
  - "23306:3306"
```

容器内部端口仍然是 `3306`。

### 查看容器和网络详情

```bash
docker compose ps
docker inspect $(docker compose ps -q mysql)
docker network inspect CareerNexus
```
