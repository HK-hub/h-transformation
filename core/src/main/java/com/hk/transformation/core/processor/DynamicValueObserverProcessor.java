package com.hk.transformation.core.processor;


import com.hk.transformation.core.annotation.observation.DynamicObserver;
import com.hk.transformation.core.context.ObservationContext;
import com.hk.transformation.core.listen.observation.observer.ValueObserver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : DynamicValueObserverProcessor
 * @date : 2023/8/25 21:38
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class DynamicValueObserverProcessor implements BeanPostProcessor {


    /**
     * 观察者上下文
     */
    private ObservationContext context;

    /**
     * 构造器注入
     * @param observationContext
     */
    public DynamicValueObserverProcessor(ObservationContext observationContext) {
        this.context = observationContext;
    }


    /**
     * 后置处理每个Bean看是否是
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        // 看是否存在 @DynamicObserver
        DynamicObserver annotation = AnnotationUtils.getAnnotation(bean.getClass(), DynamicObserver.class);
        if (Objects.isNull(annotation)) {
            return bean;
        }

        // 需要确定Bean对象是否实现了 ValueObserver 接口
        if (bean instanceof ValueObserver observerBean) {
            String[] keys = annotation.key();

            // 注解不为空，注册到观察者列表中
            if (ArrayUtils.isNotEmpty(keys)) {
                for (String key : keys) {
                    context.add(key, observerBean);
                }
            }
        }

        return bean;
    }
}
