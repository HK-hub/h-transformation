package com.hk.transformation.core.processor;

import com.hk.transformation.core.helper.DynamicValueHelper;
import com.hk.transformation.core.registry.TransformValueRegistry;
import com.hk.transformation.core.value.DynamicValueBean;
import com.hk.transformation.core.value.TransformableValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Objects;

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
@Slf4j
public class DynamicValueProcessor extends TransformationProcessor implements BeanFactoryPostProcessor, BeanFactoryAware {


    private BeanFactory beanFactory;

    private final TransformValueRegistry transformValueRegistry;

    public DynamicValueProcessor(TransformValueRegistry transformValueRegistry) {
        this.transformValueRegistry = transformValueRegistry;
    }

    /**
     * 处理动态方法
     * @param bean
     * @param beanName
     * @param method
     */
    @Override
    protected void processMethod(Object bean, String beanName, Method method) {

        log.info("暂不支持方法更新，敬请期待...");
    }


    /**
     * 处理动态字段
     * @param bean
     * @param beanName
     * @param field
     */
    @Override
    protected void processField(Object bean, String beanName, Field field) {

        // 将注解计算出来
        DynamicValueBean dynamicValue = DynamicValueHelper.computeDynamicFieldAnnotation(bean, field);

        // 是否能够计算出来注解对象
        if (Objects.isNull(dynamicValue)) {
            // 不能成功得出，无法注册进入
            log.warn("object:{}, field:{}, failed to build dynamicValueBean, can not to register to context", bean, field);
            return;
        }

        // 注册到Context中
        this.doRegister(bean, beanName, field, dynamicValue);
    }

    /**
     * 注册Field字段或Method方法
     * @param bean
     * @param beanName
     * @param member
     * @param dynamicValue
     */
    private void doRegister(Object bean, String beanName, Member member, DynamicValueBean dynamicValue) {

        // 包装为TransformableValue 对象
        TransformableValue value = null;
        if (member instanceof Field field) {
            // 字段
            value = new TransformableValue(dynamicValue, bean, field, false);
        } else if (member instanceof Method method) {
            // 方法
            value = new TransformableValue(dynamicValue, bean, method, false);
        } else {
            // 暂不支持的类型：构造函数等
            log.debug("H-Transformation @DynamicValue annotation currently only support to be used on fields or methods," +
                    "not supported on {}", member.getClass());
            return;
        }

        // 执行注册逻辑
        this.transformValueRegistry.registry(beanFactory, value.getKey(), value);
        log.info("H-Transformation starting to monitoring dynamic value:{}", value);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
