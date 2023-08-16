package com.hk.transformation.core.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author : HK意境
 * @ClassName : DynamicValue
 * @date : 2023/7/30 23:11
 * @description : 动态值注解。当标注在Type 类上的时候，表明其类下所有非Bean的对象类的Field字段属性都会被作为动态值进行解析，
 *                  如果不希望某个字段被解析成为动态值可以使用 {@link IgnoreValue} 注解进行修饰该字段进行忽略
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicValue {

    /**
     * 等同于key: 如果为空默认，如果是属性字段=全类名.属性名，如果是方法=全类名#方法名
     */
    String value() default "";

    /**
     * 动态值的Key, 用于后期获取和修改进行定位
     */
    @AliasFor()
    String key() default "";


    /**
     * 默认值,初始值
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
     */
    String elType() default "";

}
