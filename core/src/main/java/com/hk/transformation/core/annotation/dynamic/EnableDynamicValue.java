package com.hk.transformation.core.annotation.dynamic;

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
public @interface EnableDynamicValue {

    /**
     * 指定需要开启动态值所在的包路径
     */
    String[] packages() default {};

}
