package com.hk.transformation.core.event.listener;

import com.hk.transformation.core.event.signal.ValueChangeEvent;
import com.hk.transformation.core.registry.TransformValueRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

/**
 * @author : HK意境
 * @ClassName : AutoUpdateValueChangeListener
 * @date : 2023/8/19 13:10
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class AutoUpdateValueChangeListener implements ValueChangeListener, ApplicationListener<ValueChangeEvent>, ApplicationContextAware {

    private TransformValueRegistry transformValueRegistry;

    private ConfigurableBeanFactory beanFactory;

    private ApplicationContext applicationContext;


    public AutoUpdateValueChangeListener() {

    }


    @Override
    public Object onChange(ValueChangeEvent event) {





        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
        this.transformValueRegistry = this.applicationContext.getBean(TransformValueRegistry.class);
    }

    @Override
    public void onApplicationEvent(ValueChangeEvent event) {

        // 处理变更事件
        this.onChange(event);
    }
}
