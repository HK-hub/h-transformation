package com.hk.transformation.core.context;

import java.lang.reflect.Field;

/**
 * @author : HK意境
 * @ClassName : Transformable
 * @date : 2023/7/31 22:32
 * @description : 用于操作动态值的接口规范
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface TransformableEnvironment{


    /**
     * 添加可变字段
     * @param key 注解标注key 属性
     * @param field 成员字段
     */
    public void add(String key, Field field);


    /**
     * 更新key 下的动态值
     * @param key
     * @param value
     */
    public void update(String key, Object value);


    /**
     * 移除key 下全部 Field字段
     * @param key
     */
    public void remove(String key);


    /**
     * 移除key 下指定Class 对象下的Field 字段
     * @param key
     * @param clazz
     */
    public void remove(String key, Class<?> clazz);



}
