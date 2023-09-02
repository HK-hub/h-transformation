package com.hk.transformation.core.context;

import com.google.common.collect.*;
import com.hk.transformation.core.value.TransformableValue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : TransformContext
 * @date : 2023/7/31 22:25
 * @description : 值转换上下文: 用户记录和变更动态值
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Getter
public class TransformContext extends AbstractTransformableEnvironment {

    /**
     * Context实例
     */
    private static final TransformContext INSTANCE = new TransformContext();


    /**
     * 放入registry中
     * @param beanFactory
     * @param key
     * @param value
     * @return 返回是否放入
     */
    @Override
    public boolean add(BeanFactory beanFactory, String key, TransformableValue value) {
        // 初始化注册中心
        synchronized (this.registry) {
            // 是否包含该命名空间: beanFactory
            if (BooleanUtils.isFalse(registry.containsKey(beanFactory))) {
                registry.put(beanFactory, Multimaps.synchronizedListMultimap(LinkedListMultimap.create()));
            }
        }

        // 注册
        registry.get(beanFactory).put(key, value);

        // 放入字段列表中
        try {
            if (BooleanUtils.isFalse(this.transformableFieldMap.containsKey(key))) {
                this.transformableFieldMap.put(key, Lists.newArrayList());
            }

            List<Field> fieldList = this.transformableFieldMap.get(key);
            fieldList.add((Field) value.getMember());
            this.transformableFieldMap.put(key, fieldList);
        } catch (Exception e) {
            log.warn("Failed to put transform field map: {}:{}, on:{}", key, value, e);
        }

        return true;
    }


    @Override
    public void update(String key, Object value) {


    }


    /**
     * 移除key 下所有动态值对象
     * @param key
     */
    @Override
    public void remove(String key) {
        Collection<Multimap<String, TransformableValue>> values = this.registry.values();
        for (Multimap<String, TransformableValue> multimap : values) {
            if (multimap.containsKey(key)) {
                multimap.removeAll(key);
            }
        }
    }



    @Override
    public void remove(String key, Class<?> clazz) {
        Collection<Multimap<String, TransformableValue>> values = this.registry.values();
        for (Multimap<String, TransformableValue> multimap : values) {

            // 获取key 下集合
            Collection<TransformableValue> valueCollection = multimap.get(key);
            if (CollectionUtils.isEmpty(valueCollection)) {
                continue;
            }

            // 删除key 下符合类型的值对象
            for (TransformableValue transformableValue : valueCollection) {

                // 代理对象
                Object bean = transformableValue.getBean();

                // 判断直接类型
                if (ClassUtils.isAssignable(clazz, bean.getClass())) {
                    valueCollection.remove(transformableValue);

                } else if (AopProxyUtils.ultimateTargetClass(bean).isAssignableFrom(clazz)) {
                    // 判断代理目标对象类型
                    valueCollection.remove(transformableValue);
                }
            }
        }
    }


    public static TransformContext getInstance() {
        return INSTANCE;
    }

}
