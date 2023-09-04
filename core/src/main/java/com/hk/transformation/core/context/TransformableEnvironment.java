package com.hk.transformation.core.context;

import com.hk.transformation.core.value.TransformableValue;
import org.springframework.beans.factory.BeanFactory;

import java.util.List;

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
     * 获取指定beanFactory 和 key下动态值列表
     * @param beanFactory
     * @param key
     * @return
     */
    public List<TransformableValue> get(BeanFactory beanFactory, String key);


    /**
     * 获取指定key下所有动态值列表
     * @param key
     * @return
     */
    public List<TransformableValue> get(String key);


    /**
     * 添加可变字段
     * @param beanFactory
     * @param key 解标注key 属性
     * @param value
     */
    public boolean add(BeanFactory beanFactory, String key, TransformableValue value);


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
