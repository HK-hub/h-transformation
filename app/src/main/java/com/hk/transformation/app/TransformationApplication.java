package com.hk.transformation.app;

import com.hk.transformation.core.annotation.dynamic.EnableDynamicValue;
import com.hk.transformation.core.context.ObservationContext;
import com.hk.transformation.core.context.TransformContext;
import com.hk.transformation.core.processor.DynamicValueObserverProcessor;
import com.hk.transformation.core.processor.DynamicValueProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author : HK意境
 * @ClassName : TransformationApplication
 * @date : 2023/9/5 20:14
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@EnableDynamicValue
@SpringBootApplication
public class TransformationApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TransformationApplication.class, args);

        TransformContext transformContext = applicationContext.getBean(TransformContext.class);
        ObservationContext observationContext = applicationContext.getBean(ObservationContext.class);
        DynamicValueProcessor dynamicValueProcessor = applicationContext.getBean(DynamicValueProcessor.class);
        DynamicValueObserverProcessor dynamicValueObserverProcessor = applicationContext.getBean(DynamicValueObserverProcessor.class);

        System.out.println(transformContext);
        System.out.println(observationContext);
        System.out.println(dynamicValueProcessor);
        System.out.println(dynamicValueObserverProcessor);
    }

}
