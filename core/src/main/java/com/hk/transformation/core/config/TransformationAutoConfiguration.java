package com.hk.transformation.core.config;

import com.hk.transformation.core.annotation.dynamic.EnableDynamicValue;
import com.hk.transformation.core.context.ObservationContext;
import com.hk.transformation.core.context.TransformContext;
import com.hk.transformation.core.processor.DynamicValueObserverProcessor;
import com.hk.transformation.core.processor.DynamicValueProcessor;
import com.hk.transformation.core.registry.TransformValueRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author : HK意境
 * @ClassName : TransformationAutoConfiguration
 * @date : 2023/9/5 22:00
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Configuration
@ComponentScan("com.hk.transformation")
@ConditionalOnBean(annotation = EnableDynamicValue.class)
public class TransformationAutoConfiguration {


    /**
     * 注入上下文对象
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(TransformContext.class)
    public TransformContext transformContext() {

        return TransformContext.getInstance();
    }


    /**
     * 注入注册中心
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(TransformValueRegistry.class)
    public TransformValueRegistry transformValueRegistry(TransformContext transformContext) {

        return new TransformValueRegistry(transformContext);
    }


    /**
     * 观察者上下文Bean
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ObservationContext.class)
    public ObservationContext observationContext() {

        return ObservationContext.getInstance();
    }


    /**
     * 动态值注解处理器
     * @return
     */
    @Bean
    public DynamicValueProcessor dynamicValueProcessor(TransformValueRegistry transformValueRegistry) {

        return new DynamicValueProcessor(transformValueRegistry);
    }


    /**
     * 动态值观察者处理器
     * @return
     */
    @Bean
    public DynamicValueObserverProcessor dynamicValueObserverProcessor(ObservationContext observationContext) {

        return new DynamicValueObserverProcessor(observationContext);
    }

}
