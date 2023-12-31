package com.hk.transformation.core.annotation.dynamic;

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
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
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
     * TODO 暂时全部采用JSON 数据进行赋值，转换，调整等
     */
    String defaultValue() default "";


    /**
     * 动态值的Class类型
     */
    Class<?> valueClass() default void.class;


    /**
     * 动态表达式：简单的规则引擎，spEL，OGNL等
     * 注意：如果你设置为表达式的方式来进行初始设置，后续更新的方式需要确保配置环境中存在该配置
     *      - 当配置文件的值被更新的时候，您的动态值对象也将会被同步更新；
     *      - 当通过transformation的api(仅仅只有通过更新表达式) 主动更新动态表达式值的时候，相关联的配置文件值也会被同步更新
     */
    String expression() default "";


    /**
     * 动态表达式类型
     * {@link ExpressionLanguageEnum}
     */
    String elType() default "";


    /**
     * 备注
     * @return
     */
    String comment() default "";

}
