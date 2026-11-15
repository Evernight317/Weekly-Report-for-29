# Day6

## HttpClient

> HttpClient是ApacheJakartaCommon下的子项目，可以用来提供高效的、最新的、功能丰富的支持HTTP协议的客户端编程工具包，并且它支持HTTP协议最新的版本和建议；

- 可以在java程序中通过编码的方式发送http请求；

核心API：

- HttpClient
- HttpClients
- CloseableHttpClient
- HttpGet
- HttpPost

发送请求步骤：

1. 创建HttpClient对象
2. 创建Http请求对象
3. 调用HttpClient的execute方法发送请求


小程序做起来涉及到注册的问题，先跳了好了


## 商品浏览代码

- 查询分类：GET 查询参数
- 根据分类id查询菜品：GET 查询参数
- 根据分类id查询套餐：GET 查询参数
- 根据套餐id查询包含的菜品：GET 路径参数


- 这里导入XML出现问题，无法识别Setmeal，推测是Entity包下的，扩展成全类名了；
