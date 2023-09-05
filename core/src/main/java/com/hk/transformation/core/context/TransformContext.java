package com.hk.transformation.core.context;

import com.google.common.collect.*;
import com.hk.transformation.core.value.TransformableValue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ClassUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
     * 获取值指定key和beanFactory 下动态值对象列表
     * @param beanFactory
     * @param key
     * @return
     */
    @Override
    public List<TransformableValue> get(BeanFactory beanFactory, String key) {

        Map<BeanFactory, Multimap<String, TransformableValue>> registry = this.registry;
        Multimap<String, TransformableValue> valueMultimap = registry.get(beanFactory);

        // 获取Key 下value 值对象集合
        Collection<TransformableValue> transformableValues = valueMultimap.get(key);

        // 这里是可以类型转换的，声明的时候使用的LinkedList
        return (List<TransformableValue>) transformableValues;
    }


    /**
     * 获取指定key下动态值列表
     * @param key
     * @return
     */
    @Override
    public List<TransformableValue> get(String key) {

        List<TransformableValue> transformValueList = Lists.newArrayList();

        // 取出所有key下对应集合
        Collection<Multimap<String, TransformableValue>> values = this.registry.values();
        for (Multimap<String, TransformableValue> multimap : values) {
            Collection<TransformableValue> valueCollection = multimap.get(key);
            // 添加到集合中
            transformValueList.addAll(valueCollection);
        }

        return transformValueList;
    }



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


    /**
     * 更新
     * @param key
     * @param value
     */
    @Override
    public void update(String key, Object value) {

        Collection<Multimap<String, TransformableValue>> values = this.registry.values();
        for (Multimap<String, TransformableValue> multimap : values) {
            Collection<TransformableValue> transformableValueCollection = multimap.get(key);

            // key下集合不为空
            if (CollectionUtils.isNotEmpty(transformableValueCollection)) {
                // TODO 后续抽取成为异步更新
                for (TransformableValue transformableValue : transformableValueCollection) {
                    // 进行更新
                    transformableValue.update(value);
                }
            }
        }
    }


    /**
     * 移除key 下所有动态值对象
     * @param key
     */
    @Override
    public List<TransformableValue> remove(String key) {

        // 将需要被重置的对象采集出来
        List<TransformableValue> needRemoveValueList = new ArrayList<>();

        for (Multimap<String, TransformableValue> multimap : registry.values()) {
            // 移除Key下对应值对象列表
            if (multimap.containsKey(key)) {
                // 移除值对象并返回
                Collection<TransformableValue> removeValueList = multimap.removeAll(key);
                needRemoveValueList.addAll(removeValueList);
            }
        }

        // 返回被移除的key 对应的动态值对象
        return needRemoveValueList;
    }


    /**
     * 删除指定key， 指定类下的属性字段动态值
     * @param key
     * @param clazz
     */
    @Override
    public List<TransformableValue> remove(String key, Class<?> clazz) {

        // 返回被移除的动态值对象集合
        List<TransformableValue> removedTransformableValues = Lists.newArrayList();
        for (Multimap<String, TransformableValue> multimap : this.registry.values()) {

            // 获取key 下集合
            Collection<TransformableValue> valueCollection = multimap.get(key);
            if (CollectionUtils.isEmpty(valueCollection)) {
                continue;
            }

            // 删除key 下符合Class类型的值对象
            for (TransformableValue transformableValue : valueCollection) {

                // 代理对象
                Object bean = transformableValue.getBean();

                // 判断直接类型
                if (ClassUtils.isAssignable(clazz, bean.getClass())) {
                    valueCollection.remove(transformableValue);
                    removedTransformableValues.add(transformableValue);

                } else if (AopProxyUtils.ultimateTargetClass(bean).isAssignableFrom(clazz)) {
                    // 判断代理目标对象类型
                    valueCollection.remove(transformableValue);
                    removedTransformableValues.add(transformableValue);
                }
            }
        }

        return removedTransformableValues;
    }


    public static TransformContext getInstance() {
        return INSTANCE;
    }

}
