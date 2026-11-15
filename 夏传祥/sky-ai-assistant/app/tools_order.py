"""
点餐工具集：封装苍穹外卖 /user/** 接口为 LangChain @tool

改造自课程的 QueryWeatherTool.py：
- get_weather 调 OpenWeather API  →  这里的工具调苍穹外卖后端接口
- 返回值都裁剪成 LLM 好读的纯文本（只留名称/价格/id 等关键字段）

第 2 周将在此基础上新增：地址簿、下单、历史订单、取消、催单等工具
"""
from datetime import datetime, timedelta
from langchain_core.tools import tool
from app import client as sky_client

STATUS_MAP = {
    1: "待付款",
    2: "待接单",
    3: "已接单",
    4: "派送中",
    5: "已完成",
    6: "已取消",
}

@tool
def query_categories() -> str:
    """查询外卖平台的全部菜品分类（如蜀味烤鱼、特色蒸菜、传统主食、酒水饮料等）。
    当用户想浏览有什么类别的菜、或者没有说明想吃什么时，先调用本工具。
    返回分类的 id 和名称列表。"""
    res = sky_client.get("/user/category/list")
    if not res["ok"]:
        return f"查询分类失败：{res['error']}"
    lines = [f"{c['id']}:{c['name']}" for c in res["data"] if c.get("status") == 1]
    return "在售分类 -> " + "、".join(lines)

@tool
def query_address() -> str:
    """查询当前用户收货地址列表。
    返回地址 id、名称、手机号、地址。用户确认收货地址时调用。"""
    res = sky_client.get("/user/addressBook/list")
    if not res["ok"]:
        return f"查询地址失败：{res['error']}"
    address = res['data'] or []
    if not address:
        return "没有收货地址"
    lines = [
        f"{a['id']}|{a['consignee']}|{a['phone']}|{'默认' if a['isDefault'] == 1 else ''}"
        f"|{a['provinceName']}|{a['cityName']}|{a['districtName']}|{a['detail']}"
        for a in address
    ]
    return "收货地址 -> " + "、".join(lines)

@tool
def query_orders(page: int = 1, pageSize: int = 10, status: str| None = None) -> str:
    """查询当前用户的历史订单列表。
        当用户问自己下过哪些单、有没有待付款的、上次点的什么时，先调用本工具
        参数可取（全部/待付款/待接单/已接单/派送中/已完成/已取消）
        返回每行一单，含 id、状态、时间、金额、菜品；id 可用于后续取消/催单
    """

    STATUS_TO_CODE = {v: k for k,v in STATUS_MAP.items()}
    params = {"page": page, "pageSize": pageSize}
    if status is not None:
        status_code=STATUS_TO_CODE.get(status)
        if status_code is None:
            return "无法识别的订单状态:"+status+"，可用：待付款、待接单、已接单、派送中、已完成、已取消"
        params["status"] = status_code

    res = sky_client.get("/user/order/historyOrders", params=params)
    if not res["ok"]:
        return f"查询订单失败：{res['error']}"

    total = res["data"]["total"]
    orders = res["data"]['records']


    if total==0 :
        return "没有订单"
    if len(orders)==0 :
        return f"共{total}单,该页没有订单"

    header=f"订单 ->共{total}单"
    if len(orders)<total:
        header+=f",本页显示{len(orders)}单"

    rows=[header]
    for a in orders:
        # 每条订单各自取 status，取不到就给空
        status_text = STATUS_MAP.get(a.get("status"), "")

        # 明细字段（注意拼写，用 get 防 KeyError）
        detail = a.get("orderDetailList")

        dishes_text="、".join(f"{d['name']}*{d['number']}" for d in detail)
        rows.append(
            f"id={a['id']}|{status_text}|{a['orderTime']}|{float(a['amount']):.2f}|{dishes_text}"
        )

    return "\n".join(rows)



@tool
def cancel_orders(order_id:int) -> str:
    """取消当前用户的未完成订单
    当用户想要取消订单，退款时调用此工具
    成功时会返回已取消的订单，失败则会解释原因
    调用前必须向用户复述订单并得到明确确认
    """
    path=f"/user/order/cancel/{order_id}"
    res=sky_client.put(path)
    if not res["ok"]:
        error=res.get("error","")

        if "不存在" in error:
            return f"取消失败：订单 id={order_id}不存在，请确认id是否正确"
        else:
            detail=_get_detail(order_id)
            if not detail:
                return f"取消失败：{error}；同时查询订单详情失败"

            status_str=STATUS_MAP[detail['status']]
            return f"取消失败：id={order_id}的订单当前状态是{status_str},不能重复取消"

    detail = _get_detail(order_id)
    if not detail:
        return f"已取消订单 id={order_id}，但查询最新订单详情失败"

    status_str = STATUS_MAP[detail['status']]
    return (
        f"已取消订单 id={detail.get('id', order_id)}"
        f"（下单 {detail.get('orderTime', '未知')}，"
        f"应付 {float(detail['amount']):.2f}；"
        f"当前状态：{status_str}）"
    )

@tool
def query_dishes(category_id: int) -> str:
    """按分类 id 查询该分类下在售的菜品列表。
    category_id 是 query_categories 返回的分类 id（整数）。
    返回菜品 id、名称、价格、描述。用户想看某个分类的菜时调用。"""
    res = sky_client.get("/user/dish/list", params={"categoryId": category_id})
    if not res["ok"]:
        return f"查询菜品失败：{res['error']}"
    dishes = res["data"] or []
    if not dishes:
        return f"分类 {category_id} 下没有在售菜品"
    lines = []
    for d in dishes:
        if d.get("status") != 1:
            continue
        flavors = ""
        if d.get("flavors"):
            flavors = "，可选口味:" + ";".join(
                f"{f['name']}{f['value']}" for f in d["flavors"]
            )
        lines.append(f"id={d['id']} {d['name']} ￥{d['price']}（{d.get('description') or ''}{flavors}）")
    return "\n".join(lines)


@tool
def add_to_cart(dish_id: int, dish_flavor: str = "") -> str:
    """把指定菜品加入当前用户的购物车。
    dish_id 是 query_dishes 返回的菜品 id（整数）；
    dish_flavor 是口味（可选，如 "微辣"，没有口味要求的菜品传空字符串）。
    加购成功后返回购物车当前内容。"""
    res = sky_client.post("/user/shoppingCart/add",
                          {"dishId": dish_id, "dishFlavor": dish_flavor})
    if not res["ok"]:
        return f"加购失败：{res['error']}"
    return "已加入购物车。" + _cart_text()

@tool
def submit_order(address_book_id:int,remark: str="",tableware_number:int=0) -> str:
    """
    用户要下单、确认点这些菜、问"帮我下单"时；
    address_book_id是地址簿id(先查地址簿),remark是用户备注,tableware_number是餐具数量
    下单会清空购物车并生成真实订单，调用前必须向用户复述菜品和金额并得到确认。
    """
    res = sky_client.get("/user/shoppingCart/list")
    if not res["ok"]:
        return f"查询购物车失败：{res['error']}"
    items = res["data"] or []
    if not items:
        return "购物车是空的"

    addresses = sky_client.get("/user/addressBook/list")
    ids = [a["id"] for a in addresses["data"] or []]
    if address_book_id not in ids:
        return f"地址 id={address_book_id} 不存在，可选：{ids}"

    #时间
    estimatedDeliveryTime=datetime.now() + timedelta(minutes=30)
    formatted = estimatedDeliveryTime.strftime("%Y-%m-%d %H:%M:%S")

    #餐具数量
    if tableware_number==0:
        tableware_status=1 #按餐具提供
    else:
        tableware_status=0 #具体数量

    #应付金额
    amount=_cart_items(items)[1]

    params={
        "addressBookId": address_book_id,
        "payMethod": 1,
        "remark":remark,
        "estimatedDeliveryTime":formatted,
        "deliveryStatus": 1,
        "tablewareNumber": tableware_number,
        "tablewareStatus": tableware_status,
        "packAmount": 0,
        "amount":amount
    }
    order_res=sky_client.post("/user/order/submit",params)
    if not order_res['ok'] :
        return f"下单失败：{order_res['error']}"
    order_data=order_res['data']

    dishs="".join(f"{d['name']}*{d['number']} "for d in items)
    lines=f"下单成功：订单号 {order_data['orderNumber']},应付{amount}({dishs}),当前状态：待付款"
    return lines


@tool
def remind_order(order_id : int ) -> str:
    """
    当用户想要催单，问饭菜什么时候做好，调用此工具
    order_id是订单id
    返回语句是催单的结果

    """
    order_detail=_get_detail(order_id)
    if not order_detail:
        return f"订单id={order_id}不存在"
    status=order_detail['status']
    ans = STATUS_MAP[status]
    if status not in (2,3,4):
        return f"当前订单状态为{ans},无法催单"

    res=sky_client.get(f"/user/order/reminder/{order_id}")
    if  not res['ok']:
        return f"催单失败:{res['error']}"
    return f"已发送提醒,订单号{order_detail['number']},当前状态：{ans}"


@tool
def repeat_order(order_id:int)-> str:
    """
    用户说明再来一份，照上次的点，把上次的菜加回来时，调用此工具
    传入的数据是订单id
    返回给用户目前购物车中的菜品
    """
    order_detail = _get_detail(order_id)
    if not order_detail:
        return f"订单id={order_id}不存在"
    res=sky_client.post(f"/user/order/repetition/{order_id}")
    if not res['ok']:
        return f"添加购物车失败：{res['error']}"
    return f"已把订单 {order_detail['number']} 的菜品加回购物车\n"+_cart_text()


@tool
def view_cart() -> str:
    """查看当前用户购物车里的所有商品（名称、数量、单价、总额）。
    用户问"购物车有什么/多少钱"或下单前确认时调用。"""
    return _cart_text()


def _cart_text() -> str:
    res = sky_client.get("/user/shoppingCart/list")
    if not res["ok"]:
        return f"查询购物车失败：{res['error']}"
    items = res["data"] or []
    if not items:
        return "购物车是空的"
    List=_cart_items(items)
    return "\n".join(List[0])

def _cart_items(items: list) -> list:
    lines, total = [], 0.0
    for it in items:
        amount = float(it["amount"]) * it["number"]
        total += amount
        flavor = f"（{it['dishFlavor']}）" if it.get("dishFlavor") else ""
        lines.append(f"{it['name']}{flavor} x{it['number']} 小计￥{amount:.2f}")
    lines.append(f"合计：￥{total:.2f}")
    return [lines,total]

def _get_detail(order_id) ->dict | None:
    res=sky_client.get(f"/user/order/orderDetail/{order_id}")
    if not res['ok']:
        return None
    return res['data']



ALL_TOOLS = [query_categories,query_address,query_orders,submit_order,
             remind_order,repeat_order,cancel_orders,query_dishes, add_to_cart, view_cart]
