# sugar spring

## 第0章 感谢

感谢小傅哥分享的手搓spring容器的教程,该md百分之90文字都来自小傅哥的教程当中

## 第一章 spring 启动!

为什么起名为 sugar? 纯粹是想蹭hutool的甜甜热度.通过小傅哥的手写spring教程来让自己更好理解spring框架以及开发上的思路.
spring 是一个轻量级的非侵入式 ioc (inversion of control/控制翻转) 与 aop(Aspect Oriented Programming/面向切面编程)的容器框架,
ioc的简要概括就是将对象的控制权(对象的创建)交给容器来选择出来,在此基础上开发可以降低耦合,提高模块内聚.

> Spring 包含并管理应用对象的配置和生命周期，在这个意义上它是一种用于承载对象的容器，你可以配置你的每个 Bean 对象是如何被创建的，这些
> Bean 可以创建一个单独的实例或者每次需要时都生成一个新的实例，以及它们是如何相互关联构建和使用的。

在spring当中存储在容器里面的bean就好像零件一样被细化拆除，这样做的目的是有利于我们对bean管理以及 对其进行依赖注入等操作

本章节的目的是实现一个简单的spring容器（ioc的容器） 用来定义，存取和获取bean对象。

凡是可以存放数据的具体数据结构实现，都可以称之为容器。例如：ArrayList、LinkedList、HashSet等，在使用spring时
我们可以通过bean类型或者bean名称来获取bean，对此，HashMap应该是最合适的一个数据结构。

定义：BeanDefinition，可能这是你在查阅 Spring 源码时经常看到的一个类，例如它会包括 singleton、prototype、BeanClassName
等。但目前我们初步实现会更加简单的处理，只定义一个 Object 类型用于存放对象。
注册：这个过程就相当于我们把数据存放到 HashMap 中，只不过现在 HashMap 存放的是定义了的 Bean 的对象信息。
获取：最后就是获取对象，Bean 的名字就是key，Spring 容器初始化好 Bean 以后，就可以直接获取了。

刚开始时并没有多复杂,spring容器也就是由beanDefinition和factory组成.

BeanDefinition 相关属性的填充，例如：SCOPE_SINGLETON、SCOPE_PROTOTYPE、ROLE_APPLICATION、ROLE_SUPPORT、ROLE_INFRASTRUCTURE 以及
Bean Class 信息

## 第二章 实现 bean的定义以及注册 获取

关于设计模式的实现👇
>
我们在把系统设计的视角聚焦到具体代码实现上，你会有什么手段来实现你想要的设计模式呢？其实编码方式主要依托于：接口定义、类实现接口、抽象类实现接口、继承类、继承抽象类，而这些操作方式可以很好的隔离开每个类的基础功能、通用功能和业务功能，当类的职责清晰后，你的整个设计也会变得容易扩展和迭代。

在上一章实现了获取一个实例化好的bean,接下来我们尝试实现spring的scope功能也就是
prototype和singleton两种模式,同时将bean的创建交给容器,而不是自己去创建.
> 这一次我们把 Bean 的创建交给容器，而不是我们在调用时候传递一个实例化好的 Bean
> 对象，另外还需要考虑单例对象，在对象的二次获取时是可以从内存中获取对象的。此外不仅要实现功能还需要完善基础容器框架的类结构体，否则将来就很难扩容进去其他的功能了。


> 首先非常重要的一点是在 Bean 注册的时候只注册一个类信息，而不会直接把实例化信息注册到 Spring 容器中。那么就需要修改
> BeanDefinition 中的属性 Object 为 Class，接下来在需要做的就是在获取 Bean 对象时需要处理 Bean
> 对象的实例化操作以及判断当前单例对象在容器中是否已经缓存起来了

上面的是重点,毕竟你自己使用spring时也没有new 对象就能直接开始注入使用之.

这一次我们的目标是 实现单例模式,同时将BeanDefinition改成只有class类的变量,把生成实例甩给factory来实现,这样更有利于管理已经自定义化实例对象

![img](https://bugstack.cn/assets/images/spring/spring-3-01.png)

> 首先我们需要定义 BeanFactory 这样一个 Bean 工厂，提供 Bean 的获取方法 getBean(String name)，之后这个 Bean 工厂接口由抽象类
> AbstractBeanFactory 实现。这样使用模板模式 (opens new window)
> 的设计方式，可以统一收口通用核心方法的调用逻辑和标准定义，也就很好的控制了后续的实现者不用关心调用逻辑，按照统一方式执行。那么类的继承者只需要关心具体方法的逻辑实现即可。
> 那么在继承抽象类 AbstractBeanFactory 后的 AbstractAutowireCapableBeanFactory 就可以实现相应的抽象方法了，因为
> AbstractAutowireCapableBeanFactory 本身也是一个抽象类，所以它只会实现属于自己的抽象方法，其他抽象方法由继承
> AbstractAutowireCapableBeanFactory 的类实现。这里就体现了类实现过程中的各司其职，你只需要关心属于你的内容，不是你的内容，不要参与。这一部分内容我们会在代码里有具体的体现
> 另外这里还有块非常重要的知识点，就是关于单例 SingletonBeanRegistry 的接口定义实现，而 DefaultSingletonBeanRegistry
> 对接口实现后，会被抽象类 AbstractBeanFactory 继承。现在 AbstractBeanFactory
> 就是一个非常完整且强大的抽象类了，也能非常好的体现出它对模板模式的抽象定义。接下来我们就带着这些设计层面的思考，去看代码的具体实现结果

什么是模板模式?

在设计模式上,模板模式就是将要做的方法抽象化 然后将他们进行排序,实现转交给子类进行。
> 关于模版模式的核心点在于由抽象类定义抽象方法执行策略，也就是说父类规定了好一系列的执行标准，这些标准的串联成一整套业务流程。
> 举个例子，爬取过程分为；模拟登录、爬取信息、生成海报，这三个步骤，另外
> 因为有些商品只有登录后才可以爬取，并且登录可以看到一些特定的价格这与未登录用户看到的价格不同。
> 不同的电商网站爬取方式不同，解析方式也不同，因此可以作为每一个实现类中的特定实现。
> 生成海报的步骤基本一样，但会有特定的商品来源标识。所以这样三个步骤可以使用模版模式来设定，并有具体的场景做子类实现
> 他可以控制整套逻辑的执行顺序和统一的输入、输出，而对于实现方只需要关心好自己的业务逻辑即可。

![图 3-2](https://bugstack.cn/assets/images/spring/spring-3-02.png)

> - BeanFactory 的定义由 AbstractBeanFactory 抽象类实现接口的 getBean 方法
> - 而 AbstractBeanFactory 又继承了实现了 SingletonBeanRegistry 的DefaultSingletonBeanRegistry 类。这样
    AbstractBeanFactory 抽象类就具备了单例 Bean 的注册功能。
> - AbstractBeanFactory 中又定义了两个抽象方法：getBeanDefinition(String beanName)、createBean(String beanName,
    BeanDefinition beanDefinition) ，而这两个抽象方法分别由 DefaultListableBeanFactory、AbstractAutowireCapableBeanFactory
    实现。
> - 最终 DefaultListableBeanFactory 还会继承抽象类 AbstractAutowireCapableBeanFactory 也就可以调用抽象类中的 createBean
    方法了。



我们从最顶层开始往下分析
首先是 BeanFactory接口,它的作用是给我们模板模式的模板 AbstractFactory一个getBean的方法,然后是关于单例模式的主件:
SingletonRegistry
和实现它的DefaultSingletonRegistry.前者提供了getSingleton的方法,后者将其实现并且维护着一个存储着Singleton模式Bean的缓存Map,同时还实现了一个addSingletonBean的方法.

然后是"模板类" AbstractFactory 它是负责将getBean类实现的同时安排好抽象方法执行顺序的核心.他将creatBean和getBeanDefinition方法外包给子类来做.
比较有意思的是,它的子类都是分工合作各自实现不同的抽象方法 在一定程度上相互隔离,职责分明;
> 综上这一部分的类关系和实现过程还是会有一些复杂的，因为所有的实现都以职责划分、共性分离以及调用关系定义为标准搭建的类关系。这部分内容的学习，可能会丰富你在复杂业务系统开发中的设计思路。

其中抽象类AbstractAutowireCapableBeanFactory实现的是createBean部分的内容,
DefaultListableBeanFactory是核心实现类,实现的是getBeanDefinition的方法并维护着一个关于BeanDefinition的Map,同时实现了接口BeanDefinitionRegistry的registerDefinition方法.
> DefaultListableBeanFactory 在 Spring 源码中也是一个非常核心的类，在我们目前的实现中也是逐步贴近于源码，与源码类名保持一致。
> DefaultListableBeanFactory 继承了 AbstractAutowireCapableBeanFactory 类，也就具备了接口 BeanFactory 和
> AbstractBeanFactory 等一连串的功能实现。所以有时候你会看到一些类的强转，调用某些方法，也是因为你强转的类实现接口或继承了某些类。
> 除此之外这个类还实现了接口 BeanDefinitionRegistry 中的 registerBeanDefinition(String beanName, BeanDefinition
> beanDefinition) 方法，当然你还会看到一个 getBeanDefinition 的实现，这个方法我们文中提到过它是抽象类 AbstractBeanFactory
> 中定义的抽象方法。现在注册Bean定义与获取Bean定义就可以同时使用了，是不感觉这个套路还蛮深的。接口定义了注册，抽象类定义了获取，都集中在
> DefaultListableBeanFactory 中的 beanDefinitionMap 里

👆接口定义了注册(SingletonRegistry和BeanDefinitionRegistry)，抽象类定义了获取(getBean和getBeanDefinition)

其中AbstractAutowireCapableBeanFactory的create方法当中有缺陷,只能生成无参构造的bean,下一章写解决方案.
在BeanFactory当中我们发现getBean是优先去获取singletonMap里面的bean,如果没有才自己去创建.

总结:
学到了模板模式的应用 遵循开闭原则,将职责划分好 分好层,逐个去实现(比如"注册"用接口来定义,"获取"用类来实现)
> 小傅哥的总结:相对于 前一章节 对 Spring Bean
> 容器的简单概念实现，本章节中加强了功能的完善。在实现的过程中也可以看到类的关系变得越来越多了，如果没有做过一些稍微复杂的系统类系统，那么即使现在这样9个类搭出来的容器工厂也可以给你绕晕。
> 在 Spring Bean 容器的实现类中要重点关注类之间的职责和关系，几乎所有的程序功能设计都离不开接口、抽象类、实现、继承，而这些不同特性类的使用就可以非常好的隔离开类的功能职责和作用范围。而这样的知识点也是在学习手写
> Spring Bean 容器框架过程非常重要的知识。
>
最后要强调一下关于整个系列内容的学习，可能在学习的过程中会遇到像第二章节那样非常简单的代码实现，但要做一个有成长的程序员要记住代码实现只是最后的落地结果，而那些设计上的思考才是最有价值的地方。就像你是否遇到过，有人让你给一个内容做个描述、文档、说明，你总觉得太简单了没什么可写的，即使要动笔写了也不知道要从哪开始！其实这些知识内容都来源你对整体功能的理解，这就不只是代码开发还包括了需求目标、方案设计、技术实现、逻辑验证等等过程性的内容。所以，不要只是被看似简单的内容忽略了整体全局观，要学会放开视野，开放学习视角。

## 第三章 实现jdk和cglib的实例化对象补充

前置知识要求:明白cglib和jdk代理的原理以及使用方式.
在上一章当中我们在AbstractAutowireCapableBeanFactory当中实现的创建bean方法只能调用无参构造

### 设计

设计上面有两个问题,一,在**哪个阶段**合适地进行参数判断选择构造器? 二,怎么去实例化构造bean
> Spring Bean 容器源码的实现方式，在 BeanFactory 中添加 Object getBean(String name, Object... args) 接口，这样就可以在获取
> Bean 时把构造函数的入参信息传递进去了。

通过java的不定长参数 来做到输入不同参数来选择构造器(原本以为不定长参数只能针对一个类型,没想到配合Object特性可以海纳百川)

就引出了cglib和jdk实例化了

![图 4-2](https://bugstack.cn/assets/images/spring/spring-4-02.png)

> 本章节“填坑”主要是在现有工程中添加 InstantiationStrategy 实例化策略接口，以及补充相应的 getBean
> 入参信息，让外部调用时可以传递构造函数的入参并顺利实例化

通过源码,我们在abstractFactory里面实现了两个bean的获取,同时整合到doGetBean方法里面,利用模板模式进行集合.

其中关于cglib的实例化是建立代理类的基础上用NoOp的方法不代理进行实现。而jdk实例化则是利用了反射作为实现的方式

我们将getBean方法进行重载以及集合好doGetBean当中,并将AbstractAutowireCapableBeanFactory当中的createBean1方法进行
重写,将实现instantiateStrategy接口的cglib和jdk实例化作为createBean当中的创建实例的方法;

目前我们创建的bean还是存储在singleton当中;

我们默认使用的是Cglib的作为创建的方法.在createBeanInstance当中我们使用的是通过长度来判别选择的构造器,但实际上是不严谨的,因为可能会出现
类型不一致的状况,为此可以在原有基础上进行修改;

> 其中 Constructor 你可能会有一点陌生，它是 java.lang.reflect 包下的 Constructor
> 类，里面包含了一些必要的类信息，有这个参数的目的就是为了拿到符合入参信息相对应的构造函数
> 其实 Cglib 创建有构造函数的 Bean 也非常方便，在这里我们更加简化的处理了，如果你阅读 Spring 源码还会看到 CallbackFilter
> 等实现，不过我们目前的方式并不会影响创建。

> 接下来就需要循环比对出构造函数集合与入参信息 args 的匹配情况，这里我们对比的方式比较简单，只是一个数量对比，而实际 Spring
> 源码中还需要比对入参类型，否则相同数量不同入参类型的情况，就会抛异常了。

> 小傅哥总结:
> 本章节的主要以完善实例化操作，增加 InstantiationStrategy 实例化策略接口，并新增了两个实例化类。这部分类的名称与实现方式基本是
> Spring 框架的一个缩小版，大家在学习过程中也可以从 Spring 源码找到对应的代码。
>
从我们不断的完善增加需求可以看到的，当你的代码结构设计的较为合理的时候，就可以非常容易且方便的进行扩展不同属性的类职责，而不会因为需求的增加导致类结构混乱。所以在我们自己业务需求实现的过程中，也要尽可能的去考虑一个良好的扩展性以及拆分好类的职责。

## 第四章 注入属性和依赖bean注入的实现

> 在创建对象实例化这我们还缺少什么？其实还缺少一个关于类中是否有属性的问题，如果有类中包含属性那么在实例化的时候就需要把属性信息填充上，这样才是一个完整的对象创建。对于属性的填充不只是
> int、Long、String，还包括还没有实例化的对象属性，都需要在 Bean 创建时进行填充操作。不过这里我们暂时不会考虑 Bean
> 的循环依赖，否则会把整个功能实现撑大，这样新人学习时就把握不住了，待后续陆续先把核心功能实现后，再逐步完善

也就是类的默认属性和依赖注入.

![img](https://bugstack.cn/assets/images/spring/spring-5-01.png)

属性填充得在实例创建之后进行,也就是AbstractAutowireCapableBeanFactory当完成 createBean 中进行 applyPropertyValue;

> 由于我们需要在创建Bean时候填充属性操作，那么就需要在 bean 定义 BeanDefinition 类中，添加 PropertyValues 信息。
> 另外是填充属性信息还包括了 Bean 的对象类型，也就是需要再定义一个 BeanReference，里面其实就是一个简单的 Bean
> 名称，在具体的实例化操作时进行递归创建和填充，与 Spring 源码实现一样。Spring 源码中 BeanReference 是一个接口

![图 5-2](https://bugstack.cn/assets/images/spring/spring-5-02.png)

> 由于我们需要在创建Bean时候填充属性操作，那么就需要在 bean 定义 BeanDefinition 类中，添加 PropertyValues 信息。
> 另外是填充属性信息还包括了 Bean 的对象类型，也就是需要再定义一个 BeanReference，里面其实就是一个简单的 Bean
> 名称，在具体的实例化操作时进行递归创建和填充，与 Spring 源码实现一样。Spring 源码中 BeanReference 是一个接口

学到的小tip:

在foreach遍历当中,只要list为空列表和集合长度为0,就不会进入到代码块当中;

---
我们创建了PropertyValue和PropertyValues目的是为了将注入bean的属性字段收集起来处理 其中PropertyValue的value
包含了基本类型与引用类型,通过反射注入.同时将PropertyValues
设置为BeanDefinition的属性作为注入时的参数;
> 这两个类的作用就是创建出一个用于传递类中属性信息的类，因为属性可能会有很多，所以还需要定义一个集合包装下。我们并没有去处理循环依赖的问题，这部分内容较大，后续补充

创建BeanReference是为处理属性当中有bean的存在,为什么不直接通过反射识别出属性名字之后来注入而是通过BeanReference来作为引用注入?

答案是逻辑处理起来很麻烦,而且也不simple,可读性估计很shit;

## 第五章 设计与实现资源加载器，从Spring.xml解析和注册Bean对象

> 在我们实现的 Spring
>
框架中，每一个章节都会结合上一章节继续扩展功能，就像每一次产品都在加需求一样，那么在学习的过程中可以承上启下的对照和参考，看看每一个模块的添加都是用什么逻辑和技术细节实现的。这些内容的学习，会非常有利于你以后在设计和实现，自己承接产品需求时做的具体开发，代码的质量也会越来越高，越来越有扩展性和可维护性

![img](https://bugstack.cn/assets/images/spring/spring-6-02.png)

设计

我们要做的就是将三种类型的资源解析器进行实现，将数据读取之后进行解析和注册到spring的容器当中。

> - 资源加载器属于相对独立的部分，它位于 Spring 框架核心包下的IO实现内容，主要用于处理Class、本地和云环境中的文件信息。
> - 当资源可以加载后，接下来就是解析和注册 Bean 到 Spring 中的操作，这部分实现需要和 DefaultListableBeanFactory
    核心类结合起来，因为你所有的解析后的注册动作，都会把 Bean 定义信息放入到这个类中。
> - 那么在实现的时候就设计好接口的实现层级关系，包括我们需要定义出 Bean 定义的读取接口 `BeanDefinitionReader`
    以及做好对应的实现类，在实现类中完成对 Bean 对象的解析和注册

![图 6-3](https://bugstack.cn/assets/images/spring/spring-6-03.png)

资源解析器相对来说比较独立,我们将Loader和Reader进行分开实现,前者的功能为得到Resoure,resource作为接口规范了功能为获取Inputstream.

最终在 DefaultResourceLoader 中做具体的调用 三大实现的不同Resource.

后者作为读取解析inputstream的注册器来实现.那么在实现的时候就设计好接口的实现层级关系，包括我们需要定义出 Bean
定义的读取接口 `BeanDefinitionReader` 以及做好对应的实现类，在实现类中完成对 Bean 对象的解析和注册

> - 接口：BeanDefinitionReader、抽象类：AbstractBeanDefinitionReader、实现类：XmlBeanDefinitionReader，这三部分内容主要是合理清晰的处理了资源读取后的注册
    Bean 容器操作。*接口管定义，抽象类处理非接口功能外的注册Bean组件填充，最终实现类即可只关心具体的业务实现*

接口负责要实现的功能,抽象类负责初始化接口实现功能所需的变量或者方法,实现类负责实现接口功能的业务.职责划分明确.

👇另外本章节还参考 Spring 源码，做了相应接口的集成和实现的关系，虽然这些接口目前还并没有太大的作用，但随着框架的逐步完善，它们也会发挥作用。

![图 6-4](https://bugstack.cn/assets/images/spring/spring-6-04.png)

> - BeanFactory，已经存在的 Bean 工厂接口用于获取 Bean 对象，这次新增加了按照类型获取 Bean
    的方法：`<T> T getBean(String name, Class<T> requiredType)`
> - ListableBeanFactory，是一个扩展 Bean 工厂接口的接口，新增加了 `getBeansOfType`、`getBeanDefinitionNames()` 方法，在
    Spring 源码中还有其他扩展方法。
> - HierarchicalBeanFactory，在 Spring 源码中它提供了可以获取父类 BeanFactory 方法，属于是一种扩展工厂的层次子接口。*
    Sub-interface implemented by bean factories that can be part of a hierarchy.*
> - AutowireCapableBeanFactory，是一个自动化处理Bean工厂配置的接口，目前案例工程中还没有做相应的实现，后续逐步完善。
> - ConfigurableBeanFactory，可获取 BeanPostProcessor、BeanClassLoader等的一个配置化接口。
> - ConfigurableListableBeanFactory，提供分析和修改Bean以及预先实例化的操作接口，不过目前只有一个 getBeanDefinition 方法。

遵循接口提供方法,抽象类实现初始化
可能是给下一章context预热吧..

## 实现应用上下文

在对容器中 Bean 的实例化过程添加扩展机制的同时，需要把目前关于 Spring.xml 初始化和加载策略进行优化

> DefaultListableBeanFactory、XmlBeanDefinitionReader，是我们在目前 Spring 框架中对于服务功能测试的使用方式，它能很好的体现出
> Spring 是如何对 xml 加载以及注册Bean对象的操作过程，但这种方式是面向 Spring 本身的，还不具备一定的扩展性。
> 就像我们现在需要提供出一个可以在 Bean 初始化过程中，完成对 Bean 对象的扩展时，就很难做到自动化处理。所以我们要把 Bean
> 对象扩展机制功能和对 Spring 框架上下文的包装融合起来，对外提供完整的服务

设计：

![img](https://bugstack.cn/assets/images/spring/spring-7-02.png)

> 满足于对 Bean 对象扩展的两个接口，其实也是 Spring 框架中非常具有重量级的两个接口：BeanFactoryPostProcessor 和
> BeanPostProcessor，也几乎是大家在使用 Spring 框架额外新增开发自己组建需求的两个必备接口。
> BeanFactoryPostProcessor，是由 Spring 框架组建提供的容器扩展机制，允许在 Bean 对象注册后但未实例化之前，对 Bean 的定义信息
> BeanDefinition 执行修改操作。
> BeanPostProcessor，也是 Spring 提供的扩展机制，不过 BeanPostProcessor 是在 Bean 对象实例化之后修改 Bean 对象，也可以替换
> Bean 对象。这部分与后面要实现的 AOP 有着密切的关系。
> 同时如果只是添加这两个接口，不做任何包装，那么对于使用者来说还是非常麻烦的。我们希望于开发 Spring 的上下文操作类，把相应的
> XML 加载 、注册、实例化以及新增的修改和扩展都融合进去，让 Spring 可以自动扫描到我们的新增服务，便于用户使用。

![图 7-3](https://bugstack.cn/assets/images/spring/spring-7-03.png)

其中的BeanFactoryPostProcessor是在所有BeanDefinition加载完成之后，实例化Bean对象之前，提供修改BeanDefinition属性的机制，BeanPostProcessor是修改bean对象的拓展点，有着初始化前后执行的方法。

applicationContext是context的中心接口，ConfigurableApplicationContext是提供了核心方法refresh。

---

- AbstractApplicationContext 继承 DefaultResourceLoader 是为了处理 `spring.xml` 配置资源的加载。
- 之后是在 refresh() 定义实现过程，包括：
    -
        1. 创建 BeanFactory，并加载 BeanDefinition
    -
        1. 获取 BeanFactory
    -
        1. 在 Bean 实例化之前，执行 BeanFactoryPostProcessor (Invoke factory processors registered as beans in the
           context.)
    -
        1. BeanPostProcessor 需要提前于其他 Bean 对象实例化之前执行注册操作
    -
        1. 提前实例化单例Bean对象
- 另外把定义出来的抽象方法，refreshBeanFactory()、getBeanFactory() 由后面的继承此抽象类的其他抽象类实现。

---

- 在 refreshBeanFactory() 中主要是获取了 `DefaultListableBeanFactory`
  的实例化以及对资源配置的加载操作 `loadBeanDefinitions(beanFactory)`，在加载完成后即可完成对 spring.xml 配置文件中 Bean
  对象的定义和注册，同时也包括实现了接口 BeanFactoryPostProcessor、BeanPostProcessor 的配置 Bean 信息。
- 但此时资源加载还只是定义了一个抽象类方法 `loadBeanDefinitions(DefaultListableBeanFactory beanFactory)`，继续由其他抽象类继承实现。

---

在classpathXmlApplication当中做的是载入具体配置文档的路径

---

在abstractAutowireCapableFactory当中做的是在createBean过程当中添加初始化方法，即InitializeBean作用是对填充属性了的bean进行类似aop的操作。

>
> 实现 BeanPostProcessor 接口后，会涉及到两个接口方法，`postProcessBeforeInitialization`、`postProcessAfterInitialization`
> ，分别作用于 Bean 对象执行初始化前后的额外处理。
>
> 也就是需要在创建 Bean 对象时，在 createBean 方法中添加 `initializeBean(beanName, bean, beanDefinition);`
> 操作。而这个操作主要主要是对于方法 `applyBeanPostProcessorsBeforeInitialization`
> 、`applyBeanPostProcessorsAfterInitialization` 的使用。
>
> 另外需要提一下，applyBeanPostProcessorsBeforeInitialization、applyBeanPostProcessorsAfterInitialization
> 两个方法是在接口类 `AutowireCapableBeanFactory` 中新增加的。

代码逻辑较为复杂涉及到前几章的内容比较绕,可以尝试以入口类从高层模块到底层模块的方式捋清楚逻辑;

## 实现初始化方法和销毁方法

该实现很简单,得益于前几章所搭建好的项目结构可以很轻易地捋清楚代码思路;

那么除此之外我们还希望可以在 Bean
初始化过程，执行一些操作。比如帮我们做一些数据的加载执行，链接注册中心暴露RPC接口以及在Web程序关闭时执行链接断开，内存销毁等操作。*
如果说没有Spring我们也可以通过构造函数、静态方法以及手动调用的方式实现，但这样的处理方式终究不如把诸如此类的操作都交给
Spring 容器来管理更加合适。*

![img](https://bugstack.cn/assets/images/spring/spring-8-03.png)

> 可能面对像 Spring 这样庞大的框架，对外暴露的接口定义使用或者xml配置，完成的一系列扩展性操作，都让 Spring 框架看上去很神秘。其实对于这样在
> Bean 容器初始化过程中额外添加的处理操作，无非就是预先执行了一个定义好的接口方法或者是反射调用类中xml中配置的方法，最终你只要按照接口定义实现，就会有
> Spring 容器在处理的过程中进行调用而已。

![图 8-4](https://bugstack.cn/assets/images/spring/spring-8-04.png)
在 spring.xml 配置中添加 init-method、destroy-method 两个注解，在配置文件加载的过程中，把注解配置一并定义到 BeanDefinition
的属性当中。这样在 initializeBean 初始化操作的工程中，就可以通过反射的方式来调用配置在 Bean
定义属性当中的方法信息了。另外如果是接口实现的方式，那么直接可以通过 Bean 对象调用对应接口定义的方法即可，((
InitializingBean) bean).afterPropertiesSet()，两种方式达到的效果是一样的。

除了在初始化做的操作外，destroy-method 和 DisposableBean 接口的定义，都会在 Bean 对象初始化完成阶段，执行注册销毁方法的信息到
DefaultSingletonBeanRegistry 类中的 disposableBeans
属性里，这是为了后续统一进行操作。这里还有一段适配器的使用，因为反射调用和接口直接调用，是两种方式。所以需要使用适配器进行包装，下文代码讲解中参考
DisposableBeanAdapter 的具体实现
-关于销毁方法需要在虚拟机执行关闭之前进行操作，所以这里需要用到一个注册钩子的操作，如：Runtime.getRuntime()
.addShutdownHook(new Thread(() -> System.out.println("close！"))); 这段代码你可以执行测试，另外你可以使用手动调用
ApplicationContext.close 方法关闭容器。
