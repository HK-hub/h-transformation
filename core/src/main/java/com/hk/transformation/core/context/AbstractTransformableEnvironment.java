package com.hk.transformation.core.context;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.hk.transformation.core.value.TransformableValue;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : HK意境
 * @ClassName : AbstractTransformableEnvironment
 * @date : 2023/9/2 17:48
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Getter
public abstract class AbstractTransformableEnvironment implements TransformableEnvironment{

    /**
     * 注册中心
     */
    protected final Map<BeanFactory, Multimap<String, TransformableValue>> registry = Maps.newConcurrentMap();

    /**
     * 存储由@DynamicValue 注解标字段Field，及其注解属性key的值
     */
    protected final Map<String, List<Field>> transformableFieldMap = new ConcurrentHashMap<>();

    /**
     *  存储由@DynamicValue 注解标注方法Method，及其注解属性key的值
     */
    protected final Map<String, List<Method>> transformableMethodMap = new ConcurrentHashMap<>();


    /**
     * 获取全部值对象集合
     * @return
     */
    @Override
    public List<TransformableValue> all() {

        // 获取全部值集合
        Collection<Multimap<String, TransformableValue>> transformMultimapCollection = registry.values();
        if (CollectionUtils.isEmpty(transformMultimapCollection)) {
            return Collections.emptyList();
        }

        // 将集合合并到一个List中
        List<TransformableValue> transformableValues = Lists.newArrayList();
        for (Multimap<String, TransformableValue> multimap : transformMultimapCollection) {
            Collection<TransformableValue> transformableValueCollection = multimap.values();
            // 添加到集合中
            transformableValues.addAll(transformableValueCollection);
        }

        // 返回集合
        return transformableValues;
    }
}
