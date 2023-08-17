package com.hk.transformation.core.value;

import com.hk.transformation.core.annotation.DynamicValue;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : HK意境
 * @ClassName : TransformableValue
 * @date : 2023/8/17 21:29
 * @description : 包含动态值注解，属性，bean, field的复合对象
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@ToString
public class TransformableValue {

    /**
     * 值key
     */
    protected String key;


    /**
     * 注解
     */
    protected DynamicValue annotation;

    /**
     * bean 对象
     */
    protected Object bean;

    /**
     * bean 名称
     */
    protected String beanName;

    /**
     * bean工厂
     */
    protected BeanFactory beanFactory;


    /**
     * Field 字段或Method方法
     */
    protected Member member;


    /**
     * 是否初始化
     */
    protected AtomicBoolean initialized = new AtomicBoolean(false);


    public TransformableValue(DynamicValue annotation, Object bean, Field field, boolean initialized) {
        this.annotation = annotation;
        this.bean = bean;
        this.member = field;
        this.initialized.set(initialized);
    }

    public TransformableValue(DynamicValue annotation, Object bean, Method method, boolean initialized) {
        this.annotation = annotation;
        this.bean = bean;
        this.member = method;
        this.initialized.set(initialized);
    }
}
