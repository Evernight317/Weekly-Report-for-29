# Ajax

Asynchronous JavaScript And XML，异步的JavaScript和XML；

数据交换：通过Ajax可以给服务器发送请求，并获取服务器响应的数据。
异步交互：可以在 *不重新加载整个页面* 的情况下，与服务器交换数据并*更新部分网页* 的技术，如：搜索联想、用户名是否可用的校验等等。

#### 同步与异步

同步：用户向服务器发送请求，客户端必须等待服务器处理完成才能进行其他操作；

异步：无需等待处理完成，等待过程中*可以进行其他操作* ；

---

## 原生Ajax

1、准备数据地址；

2、创建`XMLHttpRequest`对象，用于和服务器交换数据；

3、向服务器发送请求；

4、获取服务器响应数据；

这种方法存在兼容性问题，现在用的是基于Ajax的Axios；

## Axios

封装了原生的Ajax，[官网地址](https://www.axios-http.cn/) ；

### 入门

1、引入Axios的js文件

2、使用Axios发送请求，并获取响应结果

```HTML
axios( {
	<!-- method: "get",
	url:"" -->
	method:"post",
	url:"",
	data:"",
}).then((result) => { <!-- 成功回调函数 -->
	console.log(result.data);
});
```

请求方式别名

`axios.get(url [, config])`
`axios.delete(url [, config])`
`axios.post(url [, data[, config]])`
`axios.put(url [, data[, config]])`

```HTML
axios.get("url").then((result) => {
	console.log(result.data);
});
```
