"""
Day 2 验证脚本：三级阶梯验证苍穹外卖后端连通性与 token 合法性

第 1 级：/user/shop/status —— 拦截器放行路径（免 token），验证后端与放行配置
第 2 级：/user/category/list —— 需要 token，验证签发格式正确
第 3 级：/user/dish/list —— 用第 2 级拿到的真实分类 id 查菜品，验证业务数据

运行前先安装依赖：pip install requests
运行前先执行：python scripts/gen_token.py
运行方式（在项目根目录）：python scripts/test_api.py
"""
from pathlib import Path

import requests

BASE_URL = "http://localhost:8080"

# 从项目根目录的 data/token.txt 读取（无论从哪个目录运行都定位准确）
TOKEN_PATH = Path(__file__).resolve().parent.parent / "data" / "token.txt"
token = TOKEN_PATH.read_text(encoding="utf-8").strip()

# 请求头名称对应 application.yml 的 user-token-name: authentication
headers = {"authentication": token}


def show(step_name, resp):
    """安全打印响应：非 JSON 响应不会让脚本崩溃"""
    print("=" * 50)
    print(step_name)
    print("状态码:", resp.status_code)
    try:
        print("响应:", resp.json())
    except ValueError:
        print("响应体(非JSON):", repr(resp.text[:200]))
    return resp.status_code


# ---------- 第 1 级：免 token 访问店铺状态 ----------
r1 = requests.get(f"{BASE_URL}/user/shop/status", timeout=5)
code1 = show("[1] 店铺营业状态（免 token，验证拦截器放行）", r1)
if code1 == 401:
    print(">>> 401 = 放行路径没生效。检查 WebMvcConfiguration 里 excludePathPatterns 是否带开头的 / ，改完要重启后端")
    exit(1)
if code1 == 500:
    print(">>> 500 = 后端连 Redis 有问题。检查 Redis 是否带密码启动（redis-server.exe redis.windows.conf），密码要与 application-dev.yml 一致")
    exit(1)

# ---------- 第 2 级：token 是否合法 ----------
r2 = requests.get(f"{BASE_URL}/user/category/list", headers=headers, timeout=5)
code2 = show("[2] 分类列表（验证 token）", r2)
if code2 == 401:
    print(">>> token 被拒绝：重新跑 gen_token.py，确认密钥 itcast 与后端 user-secret-key 一致")
    exit(1)
categories = r2.json()["data"]
print("分类:", [(c["id"], c["name"]) for c in categories])

# ---------- 第 3 级：真实业务数据 ----------
if categories:
    cid = categories[0]["id"]
    r3 = requests.get(f"{BASE_URL}/user/dish/list", params={"categoryId": cid},
                      headers=headers, timeout=5)
    code3 = show(f"[3] 分类 {cid} 下的菜品", r3)
    if code3 == 200:
        for dish in r3.json()["data"] or []:
            print(f"  - {dish['name']}  ￥{dish['price']}  状态:{dish['status']}")
        print("=" * 50)
        print("三级全部通过 = Day 2 完成 ✅")
else:
    print(">>> 分类列表为空，先在管理端添加几个分类再重试")
