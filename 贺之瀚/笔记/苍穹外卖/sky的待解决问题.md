## Redis作为缓存的原因

- 内存IO性能远高于硬盘（原因？好像和寻址有关系）
- Java的Map也可以做缓存，但是在集群部署时，这样做就和cookie一样没法做到数据共享，而Redis相当于一个独立服务，支持服务器间共享；
- 数据结构丰富？
- TTL

## 静态资源映射

## 配置类的作用

## 菜品起售停售

这个补一下，没实现，Mapper层面应该是可以使用统一接口的；

这个用POST原因？

> **PUT语义用请求体里的完整数据，整体替换目标资源
> POST：更倾向于****执行一个动作**？


使用DTO作为Controller层对象的话，是不是Tomcat会自动解析

错误！！！

Tomcat的作用是将传递过来的字节流解析成HttpServletRequest对象，经过DispatcherServlet对象调度后，被**SpringMVC**解析并组装成DTO对象，传参给Controller；（SpringMVC 使用的是ModelAttribute解析器，）

从这里就可以看出DTO必须有set方法；

如果是POST请求的话，Spring MVC 负责解析的就是Jackson解析器；
