"""
苍穹外卖后端 HTTP 客户端封装

所有工具函数都通过这里调后端接口，统一处理：
- base_url 与 token 读取（data/token.txt 由 scripts/gen_token.py 生成）
- 请求头（authentication）
- 网络异常兜底（连不上 / 超时，返回可读提示而不是堆栈）
- 业务码检查（code != 1 视为失败）

分层：_call 负责"发请求"这一层的异常，_parse 负责"拿到响应之后"的解析。
谁离异常最近谁处理，两层不交叉。
"""
import os
from pathlib import Path

import requests

BASE_URL = os.getenv("SKY_BASE_URL", "http://localhost:8080")
TIMEOUT = 10
_TOKEN_PATH = Path(__file__).resolve().parent.parent / "data" / "token.txt"


def _token() -> str:
    with open(_TOKEN_PATH, encoding="utf-8") as f:
        return f.read().strip()


def _headers() -> dict:
    # 请求头名对应后端 application.yml 的 user-token-name: authentication
    return {"authentication": _token()}


def get(path: str, params: dict | None = None) -> dict:
    return _call("GET", path, params=params)


def post(path: str, json_body: dict | None = None) -> dict:
    return _call("POST", path, body=json_body)


def put(path: str, json_body: dict | None = None) -> dict:
    return _call("PUT", path, body=json_body)


def delete(path: str) -> dict:
    return _call("DELETE", path)


def _call(method: str, path: str, params: dict | None = None,
          body: dict | None = None) -> dict:
    """唯一发请求的出口，把网络层异常翻译成人话。

    requests 的异常继承关系决定了 except 的顺序，子类必须写在父类前面：
        ConnectTimeout(ConnectionError, Timeout)  ← 必须最先
        ReadTimeout(Timeout)
        Timeout
        ConnectionError
        RequestException                          ← 兜底

    两种超时的语义不同，写操作的提示不能混：
    - ConnectTimeout：连接都没建立，请求肯定没发出去，可以安全重试
    - ReadTimeout   ：请求已发出但没等到响应，可能已经生效，必须先查询再决定
    """
    url = BASE_URL + path
    try:
        r = requests.request(method, url, params=params, json=body,
                             headers=_headers(), timeout=TIMEOUT)
    except requests.ConnectTimeout:
        return {"ok": False,
                "error": f"连接后端超时，请确认 SkyApplication 已启动（{BASE_URL}）"}
    except requests.ReadTimeout:
        return {"ok": False,
                "error": f"请求已发出但等待响应超时（{TIMEOUT}s）：操作可能已生效，"
                         f"请先用查询类工具确认结果，不要直接重试"}
    except requests.Timeout:
        return {"ok": False, "error": f"请求超时（{TIMEOUT}s），请稍后重试"}
    except requests.ConnectionError:
        return {"ok": False,
                "error": f"连不上后端，请确认 SkyApplication 已启动（{BASE_URL}）"}
    except requests.RequestException as e:
        return {"ok": False, "error": f"请求失败（{e.__class__.__name__}）：{e}"}
    return _parse(r)


def _parse(r: requests.Response) -> dict:
    """把苍穹外卖的统一响应 {code, msg, data} 转成 {ok, data/error}

    顺序上先按 HTTP 状态码定基础错误，再尽力从响应体补细节。
    状态码是第一手证据，响应体只是补充——body 解析失败不该覆盖 401 这类
    结论（网关的 401 错误页是 HTML，不是 JSON）。
    """
    base_error = None
    if r.status_code == 401:
        base_error = "token 无效或过期，请重新运行 scripts/gen_token.py"
    elif r.status_code != 200:
        base_error = f"接口异常 HTTP {r.status_code}"

    try:
        body = r.json()
        parsed = True
    except ValueError:
        body, parsed = None, False

    if base_error:
        detail = ""
        if isinstance(body, dict) and body.get("msg"):
            detail = f"（{body['msg']}）"
        return {"ok": False, "error": base_error + detail}

    if not parsed:
        return {"ok": False,
                "error": f"响应不是 JSON（HTTP {r.status_code}）：{r.text[:60]!r}"}

    if not isinstance(body, dict):
        return {"ok": False,
                "error": f"响应结构异常（HTTP {r.status_code}），期望 JSON 对象：{str(body)[:60]}"}

    if body.get("code") != 1:
        return {"ok": False, "error": body.get("msg") or "业务处理失败"}

    return {"ok": True, "data": body.get("data")}
