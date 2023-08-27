package com.hk.transformation.core.context;

import com.google.common.collect.*;
import com.hk.transformation.core.listen.observation.observer.ValueObserver;
import com.hk.transformation.core.value.TransformableValue;
import lombok.Getter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.LinkedMultiValueMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public class TransformContext implements Transformable{

    /**
     * 注册中心
     */
    @Getter
    private final Map<BeanFactory, Multimap<String, TransformableValue>> registry = Maps.newConcurrentMap();


    /**
     * 存储由@DynamicValue 注解标字段Field，及其注解属性key的值
     */
    @Getter
    private final Map<String, List<Field>> transformableFieldMap = new ConcurrentHashMap<>();

    /**
     *  存储由@DynamicValue 注解标注方法Method，及其注解属性key的值
     */
    @Getter
    private final Map<String, List<Method>> transformableMethodMap = new ConcurrentHashMap<>();


    /**
     * Context实例
     */
    private static final TransformContext INSTANCE = new TransformContext();


    @Override
    public void add(String key, Field field) {

    }

    @Override
    public void update(String key, Object value) {

    }

    @Override
    public void remove(String key) {

    }

    @Override
    public void remove(String key, Class<?> clazz) {

    }


    public static TransformContext getInstance() {
        return INSTANCE;
    }

}
