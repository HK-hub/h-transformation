package com.hk.transformation.core.annotation.observation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author : HK意境
 * @ClassName : DynamicObserver
 * @date : 2023/8/23 8:37
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Component
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface DynamicObserver {

    /**
     * 需要监听变化的Key对应的动态值对象，可以传入多个key，同时监听多个
     * @return
     */
    String[] key();

}
