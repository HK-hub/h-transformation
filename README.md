# h-transformation
一个简单快捷的动态值技术小工具。功能类似于配置中心，但是无需额外部署服务，轻量级，能够非常轻松的集成进入Spring Boot开发环境，
核心实现原理简单易理解，能够快速进行二次扩展开发。

# 核心功能
- [x] 实现单一属性值的动态控制
- [x] 实现bean对象整体属性列表的动态值控制
- [x] 对于不必要的属性字段可以进行配置忽略
- [x] 针对Boolean开关变量类型提供单独注解
- [x] 简单易扩展的动态值变更监听，订阅机制
- [x] 支持几乎所有数据类型之间的动态值转换，赋值等操作
- [x] 支持表达式语言，规则引擎等动态解析模式
- [x] Spring Boot应用隔离，全局应用开关

# 起源
起源于大三在飞猪实习期间接触到的一个动态值管理平台，我在使用的时候没有看到引入第三方配置中心，只需要引入一个jar包依赖便可对应用中的所有属性动态值和方法进行动态的控制
，包括值，参数，返回值等，可以做到灰度开关，灰度名单，灰度发布的能力，以及及时的线上异常控制。

当时弱小的自己，感觉此项目功能非常的奇妙很有趣，但是不知道如何实现，一直困惑着我，直到看到了Apollo配置中心Java 开发包的源码解析
**没错你没听错，此项目灵感来自于飞猪动态值监控平台，实现思想来自于Apollo-java客户端**


# 快速入门
目前支持基本所有的数据类型，包括基本数据类型及其包装类，String，各种集合类型包括泛型类型，Map类型，数组类型，一起复合对象类型。
```java
public class TestBean {

    // 基本数据类型、包装类型、常见数据类型和时间相关数据类型
    private byte byteValue;
    private short shortValue;
    private int intValue;
    private long longValue;
    private float floatValue;
    private double doubleValue;
    private char charValue;
    private boolean booleanValue;

    private Byte byteObject;
    private Short shortObject;
    private Integer intObject;
    private Long longObject;
    private Float floatObject;
    private Double doubleObject;
    private Character charObject;
    private Boolean booleanObject;

    private String stringValue;

    // 日期类型
    private Date dateValue;
    private java.sql.Date sqlDateValue;
    private BigDecimal bigDecimalValue;
    private BigInteger bigIntegerValue;
    private LocalDate localDateValue;
    private LocalDateTime localDateTimeValue;

    // 集合类型
    private List<String> stringList;
    private Set<Integer> integerSet;
    private Map<String, Double> stringToDoubleMap;

    // 数组类型
    // 数组类型
    private String[] stringArray;
    private int[] intArray;
    private Integer[] integerArray;
    private Date[] dateArray;
    private LocalDate[] localDateArray;
}
```

... 未完待续

# 未来特性
- [ ] 未来将支持方法的动态参数控制，包括动态指定，表达式语言计算等
- [ ] 未来将支持方法的动态返回值控制，包括直接指定，表达式语言计算等
- [ ] 支持适配的灰度发布，测试环境等
- [ ] 未来将实现不依赖数据库等存储中间件的配置持久化和初始化功能

## 参考文献
[企业级代码探究: @Value + Apollo动态刷新原理~](https://juejin.cn/post/7246049169306173495)
[Spring 注解（二）注解工具类 AnnotationUtils 和 AnnotatedElementUtils](https://www.cnblogs.com/binarylei/p/10415585.html)