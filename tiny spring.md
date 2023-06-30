# sugar spring
## 第0章 感谢
感谢小傅哥分享的手搓spring容器的教程,该md百分之90文字都来自小傅哥的教程当中
## 第一章 spring 启动!
为什么起名为 sugar? 纯粹是想蹭hutool的甜甜热度.通过小傅哥的手写spring教程来让自己更好理解spring框架以及开发上的思路.
spring 是一个轻量级的非侵入式 ioc (inversion of control/控制翻转) 与 aop(Aspect Oriented Programming/面向切面编程)的容器框架,
ioc的简要概括就是将对象的控制权(对象的创建)交给容器来选择出来,在此基础上开发可以降低耦合,提高模块内聚.
> Spring 包含并管理应用对象的配置和生命周期，在这个意义上它是一种用于承载对象的容器，你可以配置你的每个 Bean 对象是如何被创建的，这些 Bean 可以创建一个单独的实例或者每次需要时都生成一个新的实例，以及它们是如何相互关联构建和使用的。

在spring当中存储在容器里面的bean就好像零件一样被细化拆除，这样做的目的是有利于我们对bean管理以及 对其进行依赖注入等操作

本章节的目的是实现一个简单的spring容器（ioc的容器） 用来定义，存取和获取bean对象。

凡是可以存放数据的具体数据结构实现，都可以称之为容器。例如：ArrayList、LinkedList、HashSet等，在使用spring时
我们可以通过bean类型或者bean名称来获取bean，对此，HashMap应该是最合适的一个数据结构。

定义：BeanDefinition，可能这是你在查阅 Spring 源码时经常看到的一个类，例如它会包括 singleton、prototype、BeanClassName 等。但目前我们初步实现会更加简单的处理，只定义一个 Object 类型用于存放对象。
注册：这个过程就相当于我们把数据存放到 HashMap 中，只不过现在 HashMap 存放的是定义了的 Bean 的对象信息。
获取：最后就是获取对象，Bean 的名字就是key，Spring 容器初始化好 Bean 以后，就可以直接获取了。

刚开始时并没有多复杂,spring容器也就是由beanDefinition和factory组成.

BeanDefinition 相关属性的填充，例如：SCOPE_SINGLETON、SCOPE_PROTOTYPE、ROLE_APPLICATION、ROLE_SUPPORT、ROLE_INFRASTRUCTURE 以及 Bean Class 信息




