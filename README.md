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
目前支持基本所有的数据类型，包括基本数据类型及其包装类，String，各种集合类型包括泛型类型，Map类型，数组类型，以及复合对象类型。
需要注意的是动态值对象相关的注解需要应用于Bean对象之上。

## 设置动态值对象
以下是动态值注解相关的使用方法， 用户将一个字段属性标识成为动态值对象
```java
@Component
// 动态属性类，表明其下的成员属性都将被声明为动态值对象
@DynamicValue
public class AllInOneTetBean {

    // ----------------------------基本类型及其包装类型----------------------------
    @DynamicValue(key = "baseTypeIntValue", defaultValue = "1000")
    private int anInt;

    @DynamicValue(key = "baseTypeLongValue", defaultValue = "111111111")
    private long longValue;

    @DynamicValue(key = "baseTypeDoubleValue", defaultValue = "1234525.767321")
    private double doubleValue;

    @DynamicValue(key = "baseTypeCharValue", defaultValue = "x")
    private char charValue;

    // ---------------------------动态开关-----------------------------------------
    @DynamicSwitch(key = "baseTypeBooleanValue", defaultValue = true)
    private boolean booleanValue;

    @DynamicSwitch(defaultValue = false)
    private boolean enableFeature;

    @DynamicSwitch
    private Boolean enableTransformation;
    

    // ----------------------------基本类型包装对象---------------------------------
    @DynamicValue(key = "wrapperTypeByteObject", defaultValue = "20")
    private Byte byteObject;

    @DynamicValue(key = "wrapperTypeShortObject", defaultValue = "30")
    private Short shortObject;


    // --------------------------------对象类型---------------------------
    @DynamicValue(key = "StringObject", defaultValue = "hello, transformation value!")
    private String stringValue;

    @DynamicValue(key = "NullValueStringObject")
    private String nullStringValue;

    @DynamicValue(key = "ObjectValue")
    private Object objectValue;

    @DynamicValue
    private AllInOneTetBean allInOneTetBean;


    // ----------------------------------数组类型--------------------------------
    @DynamicValue(key = "StringArray", defaultValue = "[a,b,c,bv,gregt,hethte.jy,hello,1,2,34,20]")
    private String[] stringArray;

    @DynamicValue(key = "IntegerArray", defaultValue = "[1,2,3,4,5,6,7,8,9]")
    private Integer[] integerArray;
    
    /**
     * 注意：此处如果确切需要char 类型的集合请使用如下两种形式进行初始化：
     * 方式一：a,b,c,d,e,f,g,h,i
     * 方式二：['a','b','c','d','e','f']
     * 因为他们将分别使用Spring ConverterService和Gson 进行转换成为Char数组。
     * 如果不按照这两种方式使用可能会造成field字段被初始化失败或者被初始化成为String[] 数组
     */
    @DynamicValue(key = "CharArray", defaultValue = "z,x,c,v,b,n,m,;,',=,*")
    private Character[] characterArray;

    @DynamicValue(key = "DoubleArray", defaultValue = "[12.14, 13.14, 520.13, 521.0, 1024.0]")
    private Double[] doubleArray;

    @DynamicValue(key = "IntSet", defaultValue = "[1,1,2,3,4,5,5,7,8]")
    private int[] intArray;
    
    @DynamicValue(key = "beanArray", defaultValue = "")
    private BaseTypeTestBean[] beans;


    // ----------------------------------集合类型--------------------------------
    @DynamicValue(key = "StringList", defaultValue = "[a,b,c,bv,gregt,hethte.jy,hello,1,2,34,20]")
    private List<String> stringList;
    
    @DynamicValue(key = "CharList", defaultValue = "['z','x','c','v','b','n','m',';','-','=','*']")
    private List<Character> characterList;

    @DynamicValue(key = "IntegerSet", defaultValue = "[1,1,2,3,4,5,5,7,8]")
    private Set<Integer> intSet;
    
    @DynamicValue(key = "StringSet", defaultValue = "[a,bc,d,e,f,g,g,g,f,a,z,x,我是HK,HK-hub,221384]")
    private Set<String> stringSet;
    
    
    // ----------------------------------多列集合类型--------------------------------
    @DynamicValue(key = "StringStringMap", defaultValue = "{k1:baaa, k2:bbbb, k3:ccc, k4:ddd, k5:1, k6:2, k7:3, k8:400.13}")
    private Map<String, String> stringStringMap;

    @DynamicValue(key = "StringIntegerMap", defaultValue = "{key1:100,key2:200,key3:600,key4:400}")
    private Map<String, Integer> stringIntegerMap;

    @DynamicValue(key = "StringListMap", defaultValue = "{hk:[12,13,14.07,16.87,543.77], hub:[12.3,87,45,4.87,64.8,6,4.5]}")
    private Map<String, List<Double>> stringListMap;
    
    
    // ----------------------------------匿名字段和忽略字段--------------------------------
    @DynamicValue
    private String[] testStringArray;

    // 忽略字段
    @IgnoreValue
    private Integer testInt;

    @DynamicValue(defaultValue = "[1,23,4,6,7,8,9,0,4365]")
    private List<Long> longList;

}
```

## 监听动态值对象
如果我们想对声明的动态值对象进行监听其值的变化，那么可以实现TransformableObserver 接口，
你可以对多个Key 进行监听，也可以设置监听的对象值类型
重写 `update(String key, T oldValue, T newValue)` 方法并且使用 @DynamicObserver 注解进行标注在该对象上声明成为
动态值监听者
```java
@Slf4j
@DynamicObserver(key = "baseTypeIntValue")
public class BaseValueChangeListener implements TransformableObserver<Integer> {

    /**
     * 监听动态值变化
     * @param oldValue
     * @param newValue
     */
    @Override
    public void update(String key, Integer oldValue, Integer newValue) {
        log.info("key={}, value has changed:old={}, new={}", "baseTypeIntValue", oldValue, newValue);
    }
}
```

# 更多使用方法
## 基本类型使用
```java
@Component
public class TestBean {

    // 基本数据类型、包装类型、常见数据类型和时间相关数据类型
    @DynamicValue(key = "baseTypeIntValue", defaultValue = "1000")
    private int anInt;

    @DynamicValue(key = "baseTypeByteValue", defaultValue = "120")
    private byte byteValue;

    @DynamicValue(key = "baseTypeShortValue", defaultValue = "520")
    private short shortValue;

    @DynamicValue(key = "baseTypeLongValue", defaultValue = "111111111")
    private long longValue;

    @DynamicValue(key = "baseTypeFloatValue", defaultValue = "1314.520")
    private float floatValue;

    @DynamicValue(key = "baseTypeDoubleValue", defaultValue = "1234525.767321")
    private double doubleValue;

    @DynamicValue(key = "baseTypeCharValue", defaultValue = "x")
    private char charValue;

    @DynamicValue(key = "baseTypeBooleanValue")
    private boolean booleanValue;

    @DynamicValue(key = "wrapperTypeByteObject", defaultValue = "20")
    private Byte byteObject;

    @DynamicValue(key = "wrapperTypeShortObject", defaultValue = "30")
    private Short shortObject;

    @DynamicValue(key = "wrapperTypeIntObject", defaultValue = "432895482")
    private Integer intObject;

    @DynamicValue(key = "wrapperTypeLongObject", defaultValue = "548357893276595674")
    private Long longObject;

    @DynamicValue(key = "wrapperTyeFloatObject", defaultValue = "328.654678")
    private Float floatObject;

    @DynamicValue(key = "wrapperTypeDoubleObject")
    private Double doubleObject;

    @DynamicValue(key = "wrapperTypeChatObject", defaultValue = "D")
    private Character charObject;

    @DynamicValue(key = "wrapperTypeBooleanObject", defaultValue = "false")
    private Boolean booleanObject;

    @DynamicValue(key = "StringObject", defaultValue = "hello, transformation value!")
    private String stringValue;

    @DynamicValue(key = "NullValueStringObject")
    private String nullStringValue;
}
```

## 数组类型使用
```java
@Component
public class ArrayTypeTestBean {

    @DynamicValue(key = "StringArray", defaultValue = "[a,b,c,bv,gregt,hethte.jy,hello,1,2,34,20]")
    private String[] stringList;

    @DynamicValue(key = "IntegerArray", defaultValue = "[1,2,3,4,5,6,7,8,9]")
    private Integer[] intList;

    @DynamicValue(key = "LongArray", defaultValue = "[100,2000,3000,4000,5000,6000]")
    private Long[] longList;

    /**
     * 注意：此处如果确切需要char 类型的集合请使用如下两种形式进行初始化：
     * 方式一：a,b,c,d,e,f,g,h,i
     * 方式二：['a','b','c','d','e','f']
     * 因为他们将分别使用Spring ConverterService和Gson 进行转换成为Char数组。
     * 如果不按照这两种方式使用可能会造成field字段被初始化失败或者被初始化成为String[] 数组
     */
    @DynamicValue(key = "CharArray", defaultValue = "z,x,c,v,b,n,m,;,',=,*")
    private Character[] characterList;

    @DynamicValue(key = "DoubleArray", defaultValue = "[12.14, 13.14, 520.13, 521.0, 1024.0]")
    private Double[] doubleList;

    @DynamicValue(key = "IntSet", defaultValue = "[1,1,2,3,4,5,5,7,8]")
    private int[] intArray;
    
    @DynamicValue(key = "beanArray", defaultValue = "")
    private BaseTypeTestBean[] beans;
}
```

## 集合类型使用
```java
@Component
public class CollectionTypeTestBean {
    
    @DynamicValue(key = "StringList", defaultValue = "[a,b,c,bv,gregt,hethte.jy,hello,1,2,34,20]")
    private List<String> stringList;

    @DynamicValue(key = "IntegerList", defaultValue = "[1,2,3,4,5,6,7,8,9]")
    private List<Integer> intList;

    @DynamicValue(key = "LongList", defaultValue = "[100,2000,3000,4000,5000,6000]")
    private List<Long> longList;

    /**
     * 注意：此处如果确切需要char 类型的集合请使用如下两种形式进行初始化：
     * 方式一：a,b,c,d,e,f,g,h,i
     * 方式二：['a','b','c','d','e','f']
     * 因为他们将分别使用Spring ConverterService和Gson 进行转换成为Char数组。
     * 如果不按照这两种方式使用可能会造成field字段被初始化失败或者被初始化成为String[] 数组
     */
    @DynamicValue(key = "CharList", defaultValue = "['z','x','c','v','b','n','m',';','-','=','*']")
    private List<Character> characterList;

    @DynamicValue(key = "DoubleList", defaultValue = "[12.14, 13.14, 520.13, 521.0, 1024.0]")
    private List<Double> doubleList;

    @DynamicValue(key = "IntegerSet", defaultValue = "[1,1,2,3,4,5,5,7,8]")
    private Set<Integer> intSet;
    
    @DynamicValue(key = "StringSet", defaultValue = "[a,bc,d,egg,f,girl,gkuyk,g67,f,a,z,x,我是HK,HK-hub,221384]")
    private Set<String> stringSet;
    
    // 多列集合
    @DynamicValue(key = "StringStringMap", defaultValue = "{k1:baaa, k2:bbbb, k3:ccc, k4:ddd, k5:1, k6:2, k7:3, k8:400.13}")
    private Map<String, String> stringStringMap;

    @DynamicValue(key = "StringIntegerMap", defaultValue = "{key1:100,key2:200,key3:600,key4:400}")
    private Map<String, Integer> stringIntegerMap;

    @DynamicValue(key = "StringListMap", defaultValue = "{hk:[12,13,14.07,16.87,543.77], hub:[12.3,87,45,4.87,64.8,6,4.5]}")
    private Map<String, List<Double>> stringListMap;


}
```

## 动态开关使用
```java
@Component
public class SwitchTypeTestBean {

    @DynamicSwitch(key = "baseTypeBooleanValue", defaultValue = true)
    private boolean booleanValue;

    @DynamicSwitch(defaultValue = false)
    private boolean enableFeature;

    @DynamicSwitch
    private Boolean enableTransformation;
    
}
```

## Class类对象和忽略字段使用
```java
@Component
@DynamicValue
public class ClassTypeTestBean {
    
    private int anInt;
    
    private long longValue;
    
    private double doubleValue;
    
    private boolean booleanValue;

    private Byte byteObject;

    private Short shortObject;


    /**
     * 忽略该字段
     */
    @IgnoreValue
    private Integer intObject;
    
    /**
     * 忽略该字段
     */
    @IgnoreValue
    private List<Character> characterList;
    
    private Set<String> stringSet;
}
```

## 匿名使用
当使用@DynamicValue 注解的时候如果不指定动态值属性的key 那么将会自动为你计算出该属性的全限定名：类全限定名.字段名称
```java
@Component
public class AnonymousTypeTestBean {
    
    // key=xxx.xxx.xxx.AnonymousTypeTestBean.testStringArray
    @DynamicValue
    private String[] testStringArray;

    @DynamicValue
    private Integer testInt;

    @DynamicValue(defaultValue = "[1,23,4,6,7,8,9,0,4365]")
    private List<Long> longList;

}
```

# 核心注解
### @DynamicValue
该注解可以标注于Class对象，成员属性字段，成员方法之上。
- 标注于Class对象上：表示该Bean对象内部的成员属性都将被扫描成为动态值属性。
- 标注于成员属性字段：表示该成员属性字段会被扫描成为动态属性值，将能够支持动态修改。
- 标注与成员方法之上：表示该成员方法会被扫描成为动态方法，可以对方法返回值进行动态修改。

```java
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicValue {

    /**
     * 等同于key: 如果为空默认，如果是属性字段=全类名.属性名，如果是方法=全类名#方法名
     */
    @AliasFor("key")
    String value() default "";

    /**
     * 动态值的Key, 用于后期获取和修改进行定位
     */
    @AliasFor("value")
    String key() default "";

    /**
     * 默认值,初始值
     * 数组和集合可以采用2中模式：item1,item2,item3,item4 或者 [item1,item2,item3,item4]
     * 分别是采用的 Spring Converter 和 Gson 进行的转换
     */
    String defaultValue() default "";

    /**
     * 动态值的Class类型
     */
    Class<?> valueClass() default void.class;

    /**
     * 动态表达式：简单的规则引擎，spEL，OGNL等
     */
    String expression() default "";

    /**
     * 动态表达式类型
     *
     */
    String elType() default "";

    /**
     * 备注
     */
    String comment() default "";

}
```

@DynamicValue 注解中的Key表示该动态值对象键，会用于后续的查询和修改的关键，他和value字段互为别名。

注解中的defaultValue 表示动态值对象的初始值和默认值，如果缺省不进行设置，那么将使用你的字段的显式赋值的值作为初始值。

valueClass 是指defaultValue的类型Class，当然一般情况下我们不建议你进行设置，因为我们会在运行时为你进行动态的计算，
当然如果你的Field字段类型是如Object等不确定类型，那么你可以通过valueClass进行指定值类型。

后续还会进行兼容@Value注解，能够支持解析表达式，能够使得您能够更加便捷的管理配置和动态值对象

## @DynamicSwitch
对没错，@DynamicSwitch 注解其实就是一个@DynamicValue 注解的特殊类别, 表示该字段是一个boolean,Boolean类型的属性字段，
并且默认值是false
```java
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicSwitch {

    /**
     * 等同于key: 如果为空默认，如果是属性字段=全类名.属性名，如果是方法=全类名#方法名
     */
    @AliasFor("key")
    String value() default "";

    /**
     * 动态值的Key, 用于后期获取和修改进行定位
     */
    @AliasFor("value")
    String key() default "";
    
    /**
     * 默认值,初始值
     */
    boolean defaultValue() default false;
    
    /**
     * 动态值的Class类型
     */
    Class<?> valueClass() default boolean.class;
    
    ......
}
```
@DynamicSwitch注解从语义上就十分明显，适用于动态开关，灰度开关此类业务场景的，你可以进行动态启用，关闭新特性；业务灰度测试等。

## @DynamicObserver
如果我们想对动态值对象进行监听其值的变化来进行一些业务操作，那么你就需要使用@DynamicObserver 注解来表示你的观察者对象，
当然你可以选择监听多个Key对应的动态值对象，并且可以指定泛型，来选择你感兴趣值类型。

# 未来特性
- [ ] 支持@Value注解标识的字段的兼容，能够无缝集成到Spring Boot中
- [ ] 未来将支持方法的动态参数控制，包括动态指定，表达式语言计算等
- [ ] 未来将支持方法的动态返回值控制，包括直接指定，表达式语言计算等
- [ ] 支持适配的灰度发布，测试环境等
- [ ] 未来将实现不依赖数据库等存储中间件的配置持久化和初始化功能

使用的数据结构考虑的场景不足：1.多配置中心 2.多环境 3.并发、JDK数据结构选型问题 4.性能问题 5.兼容性问题 6.偏CRUD 
7.设计上比较重，不够轻量级 8.动态感知
10.其他,多端、多节点配置同步

# 特别鸣谢
@冰河

# 参考文献
[企业级代码探究: @Value + Apollo动态刷新原理~](https://juejin.cn/post/7246049169306173495)

[Spring 注解（二）注解工具类 AnnotationUtils 和 AnnotatedElementUtils](https://www.cnblogs.com/binarylei/p/10415585.html)

[SpringBoot 中 @Value 源码解析](https://www.jianshu.com/p/933669270a9f)

[6 种方式读取 Springboot 的配置，老鸟都这么玩（原理+实战）](https://www.cnblogs.com/chengxy-nds/p/17484997.html)