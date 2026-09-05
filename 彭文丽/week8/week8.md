# week8
## 学习内容
1. 苍穹外卖day01中的1-8
2. javaweb文件上传 1-4
## 内容大概回忆&遇到的问题和解决方法
1. 苍穹外卖总体流程
客户端请求-》ngnix-》jwt-》tomcat-》controller-》service——》mapper-》mysql
2. 文件上传-登录校验
filter-》interceptor（jwt）-》controller（~~异常~~ oss直接上传）-》service（异常）-》本地存储
3. 打开ngnix时有问题-》端口号被占用 就是记得是从前端访问打开ngnix之后
4. 苍穹外卖看后端代码时 找不到log-》之前的mvnd不对应该是mvn才对+lombok和jdk不适配-》下次多多看看弹幕
5. 前后端联调登录不上-》自己导入老师的yml 之前没有了解yml与properties同样作用要连接自己的mysql改username password
