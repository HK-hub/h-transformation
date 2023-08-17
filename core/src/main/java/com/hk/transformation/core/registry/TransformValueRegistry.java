package com.hk.transformation.core.registry;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.hk.transformation.core.context.TransformContext;
import com.hk.transformation.core.helper.DynamicValueHelper;
import com.hk.transformation.core.value.TransformableValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : HK意境
 * @ClassName : TransformValueRegistry
 * @date : 2023/8/17 21:19
 * @description : 注册进入Context中
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class TransformValueRegistry {


    /**
     * 初始化标志
     */
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    /**
     * 环境
     */
    private final TransformContext context = TransformContext.getInstance();

    /**
     * 注册到Context环境，Spring 环境
     * @param beanFactory
     * @param key
     * @param value
     */
    public void registry(BeanFactory beanFactory, String key, TransformableValue value) {

        Map<BeanFactory, Multimap<String, TransformableValue>> registry = context.getRegistry();

        // 初始化

    }



}
