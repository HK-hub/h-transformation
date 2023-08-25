package com.hk.transformation.core.processor;


import com.hk.transformation.core.annotation.observation.DynamicObserver;
import com.hk.transformation.core.context.TransformContext;
import com.hk.transformation.core.listen.observation.observer.ValueObserver;
import com.hk.transformation.core.registry.TransformValueRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.Resource;
import java.util.LinkedList;
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
public class DynamicValueObserverProcessor implements BeanPostProcessor {

    private TransformContext context = TransformContext.getInstance();


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

        // 注解不为空，注册到观察者列表中
        LinkedMultiValueMap<String, ValueObserver> observerMap = context.getObserverMap();
        String[] keys = annotation.key();
        for (String key : keys) {
            if (!observerMap.containsKey(key)) {
                observerMap.put(key, new LinkedList<>());
            }
            observerMap.put
        }




    }
}
