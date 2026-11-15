"""
生成苍穹外卖用户端 JWT token（Day 2 用）

与后端签发逻辑完全对齐（已核对 JwtUtil.java / JwtTokenUserInterceptor.java）：
- 算法：HS256
- 密钥：application.yml 里的 user-secret-key，即 itcast
- claims：userId（对应 JwtClaimsConstant.USER_ID）
- exp：过期时间，jjwt 的 parseClaimsJws 会校验，必须带

运行前先安装依赖：pip install pyjwt
运行方式（在项目根目录）：python scripts/gen_token.py
"""
import time
from pathlib import Path

import jwt  # 注意：包名是 pyjwt，导入名是 jwt

SECRET = "itcast"       # 与 sky-server/src/main/resources/application.yml 的 user-secret-key 一致
USER_ID = 4             # 测试用户 id，需确认 sky_take_out 库 user 表里存在这条记录

payload = {
    "userId": USER_ID,
    "exp": int(time.time()) + 7 * 24 * 3600,   # 7 天有效期，够整个项目用
}

token = jwt.encode(payload, SECRET, algorithm="HS256")

# 写到项目根目录的 data/token.txt（无论从哪个目录运行都定位准确）
TOKEN_PATH = Path(__file__).resolve().parent.parent / "data" / "token.txt"
TOKEN_PATH.parent.mkdir(parents=True, exist_ok=True)
TOKEN_PATH.write_text(token)

print(f"token 已生成并写入 {TOKEN_PATH}：")
print(token)
