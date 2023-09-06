package com.hk.transformation.core.annotation.dynamic;

import com.hk.transformation.core.config.TransformationAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author : HK意境
 * @ClassName : EnableDynamicValue
 * @date : 2023/7/31 17:20
 * @description : 启用
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(TransformationAutoConfiguration.class)
public @interface EnableDynamicValue {

    /**
     * 指定需要开启动态值所在的包路径
     */
    String[] packages() default {};

}
