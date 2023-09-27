package com.hk.transformation.core.config;

import com.hk.transformation.core.annotation.dynamic.EnableDynamicValue;
import com.hk.transformation.core.context.ObservationContext;
import com.hk.transformation.core.context.TransformContext;
import com.hk.transformation.core.convert.StringToCollectionConverter;
import com.hk.transformation.core.helper.DynamicValueHelper;
import com.hk.transformation.core.processor.DynamicValueObserverProcessor;
import com.hk.transformation.core.processor.DynamicValueProcessor;
import com.hk.transformation.core.registry.TransformValueRegistry;
import com.hk.transformation.core.resolver.TransformPlaceholderResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

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
     * 动态值工具类负责注解bean构建，value值转换，占位符解析
     * @return
     */
    @Bean
    public DynamicValueHelper dynamicValueHelper(TransformPlaceholderResolver transformPlaceholderResolver) {

        return new DynamicValueHelper(transformPlaceholderResolver);
    }


    /**
     * 动态值注解处理器
     * @return
     */
    @Bean
    public DynamicValueProcessor dynamicValueProcessor(TransformValueRegistry transformValueRegistry,
                                                       DynamicValueHelper dynamicValueHelper) {

        return new DynamicValueProcessor(transformValueRegistry, dynamicValueHelper);
    }


    /**
     * 动态值观察者处理器
     * @return
     */
    @Bean
    public DynamicValueObserverProcessor dynamicValueObserverProcessor(ObservationContext observationContext) {

        return new DynamicValueObserverProcessor(observationContext);
    }


    /**
     * 自定义转换服务
     * @return
     */
    @Bean
    public ConversionService conversionService() {
        DefaultConversionService service = new DefaultConversionService();
        service.addConverter(new StringToCollectionConverter(service));
        // 添加其他自定义转换器，如果有的话
        return service;
    }
}
