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

## 第二章 实现 bean的定义以及注册 获取
关于设计模式的实现👇
> 我们在把系统设计的视角聚焦到具体代码实现上，你会有什么手段来实现你想要的设计模式呢？其实编码方式主要依托于：接口定义、类实现接口、抽象类实现接口、继承类、继承抽象类，而这些操作方式可以很好的隔离开每个类的基础功能、通用功能和业务功能，当类的职责清晰后，你的整个设计也会变得容易扩展和迭代。

在上一章实现了获取一个实例化好的bean,接下来我们尝试实现spring的scope功能也就是 prototype和singleton两种模式,同时将bean的创建交给容器,而不是自己去创建.
> 这一次我们把 Bean 的创建交给容器，而不是我们在调用时候传递一个实例化好的 Bean 对象，另外还需要考虑单例对象，在对象的二次获取时是可以从内存中获取对象的。此外不仅要实现功能还需要完善基础容器框架的类结构体，否则将来就很难扩容进去其他的功能了。


>首先非常重要的一点是在 Bean 注册的时候只注册一个类信息，而不会直接把实例化信息注册到 Spring 容器中。那么就需要修改 BeanDefinition 中的属性 Object 为 Class，接下来在需要做的就是在获取 Bean 对象时需要处理 Bean 对象的实例化操作以及判断当前单例对象在容器中是否已经缓存起来了

上面的是重点,毕竟你自己使用spring时也没有new 对象就能直接开始注入使用之.

这一次我们的目标是 实现单例模式,同时将BeanDefinition改成只有class类的变量,把生成实例甩给factory来实现,这样更有利于管理已经自定义化实例对象

![img](https://bugstack.cn/assets/images/spring/spring-3-01.png)

> 首先我们需要定义 BeanFactory 这样一个 Bean 工厂，提供 Bean 的获取方法 getBean(String name)，之后这个 Bean 工厂接口由抽象类 AbstractBeanFactory 实现。这样使用模板模式 (opens new window)的设计方式，可以统一收口通用核心方法的调用逻辑和标准定义，也就很好的控制了后续的实现者不用关心调用逻辑，按照统一方式执行。那么类的继承者只需要关心具体方法的逻辑实现即可。
那么在继承抽象类 AbstractBeanFactory 后的 AbstractAutowireCapableBeanFactory 就可以实现相应的抽象方法了，因为 AbstractAutowireCapableBeanFactory 本身也是一个抽象类，所以它只会实现属于自己的抽象方法，其他抽象方法由继承 AbstractAutowireCapableBeanFactory 的类实现。这里就体现了类实现过程中的各司其职，你只需要关心属于你的内容，不是你的内容，不要参与。这一部分内容我们会在代码里有具体的体现
另外这里还有块非常重要的知识点，就是关于单例 SingletonBeanRegistry 的接口定义实现，而 DefaultSingletonBeanRegistry 对接口实现后，会被抽象类 AbstractBeanFactory 继承。现在 AbstractBeanFactory 就是一个非常完整且强大的抽象类了，也能非常好的体现出它对模板模式的抽象定义。接下来我们就带着这些设计层面的思考，去看代码的具体实现结果

什么是模板模式?

在设计模式上,模板模式就是将要做的方法抽象化 然后将他们进行排序,实现转交给子类进行。
> 关于模版模式的核心点在于由抽象类定义抽象方法执行策略，也就是说父类规定了好一系列的执行标准，这些标准的串联成一整套业务流程。
举个例子，爬取过程分为；模拟登录、爬取信息、生成海报，这三个步骤，另外
因为有些商品只有登录后才可以爬取，并且登录可以看到一些特定的价格这与未登录用户看到的价格不同。
不同的电商网站爬取方式不同，解析方式也不同，因此可以作为每一个实现类中的特定实现。
生成海报的步骤基本一样，但会有特定的商品来源标识。所以这样三个步骤可以使用模版模式来设定，并有具体的场景做子类实现
他可以控制整套逻辑的执行顺序和统一的输入、输出，而对于实现方只需要关心好自己的业务逻辑即可。

![图 3-2](https://bugstack.cn/assets/images/spring/spring-3-02.png)

> - BeanFactory 的定义由 AbstractBeanFactory 抽象类实现接口的 getBean 方法
> - 而 AbstractBeanFactory 又继承了实现了 SingletonBeanRegistry 的DefaultSingletonBeanRegistry 类。这样 AbstractBeanFactory 抽象类就具备了单例 Bean 的注册功能。
> - AbstractBeanFactory 中又定义了两个抽象方法：getBeanDefinition(String beanName)、createBean(String beanName, BeanDefinition beanDefinition) ，而这两个抽象方法分别由 DefaultListableBeanFactory、AbstractAutowireCapableBeanFactory 实现。
> - 最终 DefaultListableBeanFactory 还会继承抽象类 AbstractAutowireCapableBeanFactory 也就可以调用抽象类中的 createBean 方法了。



我们从最顶层开始往下分析
首先是 BeanFactory接口,它的作用是给我们模板模式的模板 AbstractFactory一个getBean的方法,然后是关于单例模式的主件:SingletonRegistry
和实现它的DefaultSingletonRegistry.前者提供了getSingleton的方法,后者将其实现并且维护着一个存储着Singleton模式Bean的缓存Map,同时还实现了一个addSingletonBean的方法.

然后是"模板类" AbstractFactory 它是负责将getBean类实现的同时安排好抽象方法执行顺序的核心.他将creatBean和getBeanDefinition方法外包给子类来做.
比较有意思的是,它的子类都是分工合作各自实现不同的抽象方法 在一定程度上相互隔离,职责分明;
>综上这一部分的类关系和实现过程还是会有一些复杂的，因为所有的实现都以职责划分、共性分离以及调用关系定义为标准搭建的类关系。这部分内容的学习，可能会丰富你在复杂业务系统开发中的设计思路。

其中抽象类AbstractAutowireCapableBeanFactory实现的是createBean部分的内容,
DefaultListableBeanFactory是核心实现类,实现的是getBeanDefinition的方法并维护着一个关于BeanDefinition的Map,同时实现了接口BeanDefinitionRegistry的registerDefinition方法.
> DefaultListableBeanFactory 在 Spring 源码中也是一个非常核心的类，在我们目前的实现中也是逐步贴近于源码，与源码类名保持一致。
DefaultListableBeanFactory 继承了 AbstractAutowireCapableBeanFactory 类，也就具备了接口 BeanFactory 和 AbstractBeanFactory 等一连串的功能实现。所以有时候你会看到一些类的强转，调用某些方法，也是因为你强转的类实现接口或继承了某些类。
除此之外这个类还实现了接口 BeanDefinitionRegistry 中的 registerBeanDefinition(String beanName, BeanDefinition beanDefinition) 方法，当然你还会看到一个 getBeanDefinition 的实现，这个方法我们文中提到过它是抽象类 AbstractBeanFactory 中定义的抽象方法。现在注册Bean定义与获取Bean定义就可以同时使用了，是不感觉这个套路还蛮深的。接口定义了注册，抽象类定义了获取，都集中在 DefaultListableBeanFactory 中的 beanDefinitionMap 里

👆接口定义了注册(SingletonRegistry和BeanDefinitionRegistry)，抽象类定义了获取(getBean和getBeanDefinition)

其中AbstractAutowireCapableBeanFactory的create方法当中有缺陷,只能生成无参构造的bean,下一章写解决方案.
在BeanFactory当中我们发现getBean是优先去获取singletonMap里面的bean,如果没有才自己去创建.

总结:
学到了模板模式的应用 遵循开闭原则,将职责划分好 分好层,逐个去实现(比如"注册"用接口来定义,"获取"用类来实现)
>小傅哥的总结:相对于 前一章节 对 Spring Bean 容器的简单概念实现，本章节中加强了功能的完善。在实现的过程中也可以看到类的关系变得越来越多了，如果没有做过一些稍微复杂的系统类系统，那么即使现在这样9个类搭出来的容器工厂也可以给你绕晕。
在 Spring Bean 容器的实现类中要重点关注类之间的职责和关系，几乎所有的程序功能设计都离不开接口、抽象类、实现、继承，而这些不同特性类的使用就可以非常好的隔离开类的功能职责和作用范围。而这样的知识点也是在学习手写 Spring Bean 容器框架过程非常重要的知识。
最后要强调一下关于整个系列内容的学习，可能在学习的过程中会遇到像第二章节那样非常简单的代码实现，但要做一个有成长的程序员要记住代码实现只是最后的落地结果，而那些设计上的思考才是最有价值的地方。就像你是否遇到过，有人让你给一个内容做个描述、文档、说明，你总觉得太简单了没什么可写的，即使要动笔写了也不知道要从哪开始！其实这些知识内容都来源你对整体功能的理解，这就不只是代码开发还包括了需求目标、方案设计、技术实现、逻辑验证等等过程性的内容。所以，不要只是被看似简单的内容忽略了整体全局观，要学会放开视野，开放学习视角。
 ## 第三章 实现jdk和cglib的实例化对象补充
前置知识要求:明白cglib和jdk代理的原理以及使用方式.
在上一章当中我们在AbstractAutowireCapableBeanFactory当中实现的创建bean方法只能调用无参构造
### 设计
设计上面有两个问题,一,在**哪个阶段**合适地进行参数判断选择构造器? 二,怎么去实例化构造bean
>  Spring Bean 容器源码的实现方式，在 BeanFactory 中添加 Object getBean(String name, Object... args) 接口，这样就可以在获取 Bean 时把构造函数的入参信息传递进去了。

通过java的不定长参数 来做到输入不同参数来选择构造器(原本以为不定长参数只能针对一个类型,没想到配合Object特性可以海纳百川)

就引出了cglib和jdk实例化了

![图 4-2](https://bugstack.cn/assets/images/spring/spring-4-02.png)

> 本章节“填坑”主要是在现有工程中添加 InstantiationStrategy 实例化策略接口，以及补充相应的 getBean 入参信息，让外部调用时可以传递构造函数的入参并顺利实例化

通过源码,我们在abstractFactory里面实现了两个bean的获取,同时整合到doGetBean方法里面,利用模板模式进行集合.

其中关于cglib的实例化是建立代理类的基础上用NoOp的方法不代理进行实现。而jdk实例化则是利用了反射作为实现的方式

我们将getBean方法进行重载以及集合好doGetBean当中,并将AbstractAutowireCapableBeanFactory当中的createBean1方法进行
重写,将实现instantiateStrategy接口的cglib和jdk实例化作为createBean当中的创建实例的方法;

目前我们创建的bean还是存储在singleton当中;

我们默认使用的是Cglib的作为创建的方法.在createBeanInstance当中我们使用的是通过长度来判别选择的构造器,但实际上是不严谨的,因为可能会出现
类型不一致的状况,为此可以在原有基础上进行修改;

> 其中 Constructor 你可能会有一点陌生，它是 java.lang.reflect 包下的 Constructor 类，里面包含了一些必要的类信息，有这个参数的目的就是为了拿到符合入参信息相对应的构造函数
其实 Cglib 创建有构造函数的 Bean 也非常方便，在这里我们更加简化的处理了，如果你阅读 Spring 源码还会看到 CallbackFilter 等实现，不过我们目前的方式并不会影响创建。

> 接下来就需要循环比对出构造函数集合与入参信息 args 的匹配情况，这里我们对比的方式比较简单，只是一个数量对比，而实际 Spring 源码中还需要比对入参类型，否则相同数量不同入参类型的情况，就会抛异常了。

> 小傅哥总结:
> 本章节的主要以完善实例化操作，增加 InstantiationStrategy 实例化策略接口，并新增了两个实例化类。这部分类的名称与实现方式基本是 Spring 框架的一个缩小版，大家在学习过程中也可以从 Spring 源码找到对应的代码。
从我们不断的完善增加需求可以看到的，当你的代码结构设计的较为合理的时候，就可以非常容易且方便的进行扩展不同属性的类职责，而不会因为需求的增加导致类结构混乱。所以在我们自己业务需求实现的过程中，也要尽可能的去考虑一个良好的扩展性以及拆分好类的职责。







