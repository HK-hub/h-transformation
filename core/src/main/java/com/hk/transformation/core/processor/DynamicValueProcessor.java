package com.hk.transformation.core.processor;

import com.hk.transformation.core.helper.DynamicValueHelper;
import com.hk.transformation.core.registry.TransformValueRegistry;
import com.hk.transformation.core.value.DynamicValueBean;
import com.hk.transformation.core.value.TransformableValue;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
@NoArgsConstructor
public class DynamicValueProcessor extends AbstractTransformationProcessor implements BeanFactoryPostProcessor, BeanFactoryAware {


    private BeanFactory beanFactory;

    /**
     * 通过依赖注入方式注入注册中心
     */
    private TransformValueRegistry transformValueRegistry;

    /**
     * 工具助手
     */
    private DynamicValueHelper dynamicValueHelper;


    /**
     * 构造器注入
     * @param transformValueRegistry
     */
    public DynamicValueProcessor(TransformValueRegistry transformValueRegistry, DynamicValueHelper dynamicValueHelper) {
        this.transformValueRegistry = transformValueRegistry;
        this.dynamicValueHelper = dynamicValueHelper;
    }

    /**
     * 处理动态方法
     * @param bean
     * @param beanName
     * @param method
     */
    @Override
    protected void processMethod(Object bean, String beanName, Method method) {

        log.info("start to process method:{}#{}, parameters={}", bean.getClass().getName(), method.getName(), method.getParameterTypes());

        // 获取参数列表
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            // 将注解计算出来
            DynamicValueBean dynamicValueBean = this.dynamicValueHelper.computeDynamicMethodAnnotation(bean, method, parameter, beanName);

            // 进行注册动态参数
            if (Objects.nonNull(dynamicValueBean)) {
                this.doRegister(bean, beanName, method, parameter, dynamicValueBean);
            }
        }

    }


    /**
     * 处理动态字段
     * 此时使用@Value注解标注的字段已经被解析赋值了
     * @param bean
     * @param beanName
     * @param field
     */
    @Override
    protected void processField(Object bean, String beanName, Field field) {

        // 将注解计算出来
        DynamicValueBean dynamicValue = this.dynamicValueHelper.computeDynamicFieldAnnotation(bean, field);

        // 能够计算出来注解对象
        if (Objects.nonNull(dynamicValue)) {
            // 注册到Context中
            this.doRegister(bean, beanName, field, null, dynamicValue);
        }
    }



    /**
     * 注册Field字段或Method方法
     * @param bean
     * @param beanName
     * @param member field 成员属性或 method 成员方法
     * @param dynamicValue
     */
    private void doRegister(Object bean, String beanName, Member member, Parameter parameter, DynamicValueBean dynamicValue) {

        // 包装为TransformableValue 对象
        TransformableValue value = null;
        if (member instanceof Field field) {
            // 字段
            value = new TransformableValue(dynamicValue, bean, field, false);
        } else if (member instanceof Method method) {
            // 方法
            value = new TransformableValue(dynamicValue, bean, method, parameter, false);
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


    /**
     * 设置为最低的优先级在其余后置处理器之后执行
     * @return
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
