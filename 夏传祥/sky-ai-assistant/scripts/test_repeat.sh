#!/usr/bin/env bash
# 再来一单工具测试：bash scripts/test_repeat.sh
T=$(cat data/token.txt); B=http://localhost:8080
PY="/c/Users/14095/.conda/envs/code-python/python.exe"

echo "=== 0. 清空购物车 ==="
curl -s -X DELETE -H "authentication: $T" $B/user/shoppingCart/clean >/dev/null
curl -s -H "authentication: $T" $B/user/shoppingCart/list; echo

echo "=== 1. 再来一单 id=13（应成功并附购物车）==="
$PY -c "from app.tools_order import repeat_order; print(repeat_order.invoke({'order_id':13}))"

echo "=== 2. 购物车内容 ==="
curl -s -H "authentication: $T" $B/user/shoppingCart/list | grep -o '"name":"[^"]*"\|"number":[0-9]*'; echo

echo "=== 3. 同一单再来一次（应新增一行，不合并）==="
$PY -c "from app.tools_order import repeat_order; print(repeat_order.invoke({'order_id':13}))"
curl -s -H "authentication: $T" $B/user/shoppingCart/list | grep -o '"name":"[^"]*"\|"number":[0-9]*'; echo

echo "=== 4. 不存在的单（应被拦住，不发请求）==="
$PY -c "from app.tools_order import repeat_order; print(repeat_order.invoke({'order_id':999999}))"

echo "=== 5. 订单本身没被改动（status 应仍是 2）==="
curl -s -H "authentication: $T" $B/user/order/orderDetail/13 | grep -o '"status":[0-9]*'; echo

echo "=== 6. 已取消的单也能再来一单（id=12）==="
$PY -c "from app.tools_order import repeat_order; print(repeat_order.invoke({'order_id':12}))"
