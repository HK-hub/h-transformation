package com.hk.transformation.core.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author : HK意境
 * @ClassName : DynamicValueProcessor
 * @date : 2023/7/31 17:17
 * @description : 动态之处理器，用于获取标注了@DynamicValue 注解Bean的字段
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class DynamicValueProcessor extends TransformationProcessor implements BeanFactoryPostProcessor, BeanFactoryAware {


    /**
     * 处理动态方法
     * @param bean
     * @param beanName
     * @param method
     */
    @Override
    protected void processMethod(Object bean, String beanName, Method method) {

    }


    /**
     * 处理动态字段
     * @param bean
     * @param beanName
     * @param field
     */
    @Override
    protected void processField(Object bean, String beanName, Field field) {

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
