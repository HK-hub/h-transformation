package com.hk.transformation.core.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author : HK意境
 * @ClassName : DynamicSwitch
 * @date : 2023/8/16 16:16
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicSwitch {

    /**
     * 动态值的Key, 用于后期获取和修改进行定位
     */
    @AliasFor("value")
    String key() default "";


    /**
     * 默认值,初始值
     */
    boolean defaultValue();


    /**
     * 动态值的Class类型
     */
    Class<?> valueClass() default boolean.class;


    /**
     * 动态表达式：简单的规则引擎，spEL，OGNL等
     */
    String expression() default "";


    /**
     * 动态表达式类型
     */
    String elType() default "";

}
