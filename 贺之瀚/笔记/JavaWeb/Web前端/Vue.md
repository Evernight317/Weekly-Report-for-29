<script>
newVue(
el：“#app”,
data: (
message: "Hello Vue!"
}
</script>

# Vue

框架：是一个半成品软件，一套可重用的软件基础代码模型。基于框架进行开发，更加快捷、更加高效；

Vue是一套前端框架，免除原生JavaScript中的DOM操作，简化书写；基于MVVM(Model-View-ViewModel)思想，实现数据的**双向绑定** ，将编程的关注点放在数据上。[官网地址](https://v2.cn.vuejs.org/)

---

## 一、入门

- 新建HTML页面，引入Vue.js文件
- 在JS代码区域，创建Vue核心对象，定义数据模型
- 编写视图

```HTML
<script src="js/vye.js"></script>

<script>
	new Vue({
      	// 代表控制区域 id选择器
		el："#app",
      	// 数据模型
		data: {
			message: "Hello Vue!"
		}
  	})
</script>

<div id="app">
	<input type="text" v-model="message">
	{{ message }}
</div>
```
