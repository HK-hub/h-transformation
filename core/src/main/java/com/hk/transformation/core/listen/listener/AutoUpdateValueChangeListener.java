package com.hk.transformation.core.listen.listener;

import com.hk.transformation.core.listen.event.ValueChangeEvent;
import com.hk.transformation.core.registry.TransformValueRegistry;
import com.hk.transformation.core.value.TransformableValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

import java.util.List;

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
@Slf4j
public class AutoUpdateValueChangeListener implements ValueChangeListener, ApplicationListener<ValueChangeEvent>, ApplicationContextAware {

    private TransformValueRegistry transformValueRegistry;

    private ConfigurableBeanFactory beanFactory;

    private ApplicationContext applicationContext;


    public AutoUpdateValueChangeListener() {

    }


    @Override
    public Object onChange(ValueChangeEvent event) {

        // 解析事件内容
        String key = event.getKey();
        Object newValue = event.getValue();
        // 获取需要变更的对象
        List<TransformableValue> transformableValues = this.transformValueRegistry.get(beanFactory, key);

        // 更新值，获取旧值，用作观察者的通知
        for (TransformableValue transformableValue : transformableValues) {
            try {
                Object oldValue = transformableValue.update(newValue);
            } catch (Exception e) {
                log.warn("Error updating transformValue:{} with key:{}, value:{} failed", transformableValue, key, newValue);
            }
        }
        // 观察者模式通知观察者值变更

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
