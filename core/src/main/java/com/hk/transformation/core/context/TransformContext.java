package com.hk.transformation.core.context;

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
public class TransformContext<T> implements Transformable{

    /**
     * 存储由@DynamicValue 注解标字段Field，及其注解属性key的值
     */
    private final Map<String, List<Field>> transformableFieldMap = new ConcurrentHashMap<>();

    /**
     *  存储由@DynamicValue 注解标注方法Method，及其注解属性key的值
     */
    private final Map<String, List<Method>> transformableMethodMap = new ConcurrentHashMap<>();


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
}
