package com.hk.transformation.core.annotation;

import java.lang.annotation.*;

/**
 * @author : HK意境
 * @ClassName : Ignore
 * @date : 2023/7/31 22:48
 * @description : 当 @Dynamic 注解作用与 Type 类型上时，可以使用 @Ignore注解来排除不需要被设置为动态值的属性字段Field
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Ignore {
}
