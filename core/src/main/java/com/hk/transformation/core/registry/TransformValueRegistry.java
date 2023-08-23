package com.hk.transformation.core.registry;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.hk.transformation.core.context.TransformContext;
import com.hk.transformation.core.helper.DynamicValueHelper;
import com.hk.transformation.core.value.TransformableValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
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

        // 初始化注册中心
        synchronized (context) {

            // 是否包含该命名空间: beanFactory
            if (BooleanUtils.isFalse(registry.containsKey(beanFactory))) {
                registry.put(beanFactory, Multimaps.synchronizedListMultimap(LinkedListMultimap.create()));
            }
        }

        // 注册
        registry.get(beanFactory).put(key, value);

        // 执行初始化逻辑
        if (this.initialized.compareAndSet(false, true)) {
            this.initialize(value);
        }
    }


    /**
     * 获取值对象
     * @param beanFactory
     * @param key
     * @return
     */
    public List<TransformableValue> get(BeanFactory beanFactory, String key) {

        Map<BeanFactory, Multimap<String, TransformableValue>> registry = this.context.getRegistry();
        Multimap<String, TransformableValue> valueMultimap = registry.get(beanFactory);

        // 获取Key 下value 值对象集合
        Collection<TransformableValue> transformableValues = valueMultimap.get(key);

        // 这里是可以类型转换的，声明的时候使用的LinkedList
        return (List<TransformableValue>) transformableValues;
    }


    /**
     * 初始化
     * @param value
     */
    private void initialize(TransformableValue value) {

        // 为字段或方法赋值
        Member member = value.getMember();

        // 执行对于的初始化逻辑
        try {
            if (member instanceof Field field) {

                // 字段类型
                DynamicValueHelper.assignField(field, value.getDynamicValueBean().getValue(), value.getDynamicValueBean().getValueClass());
            } else if (member instanceof Method) {
                // 方法类型

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
