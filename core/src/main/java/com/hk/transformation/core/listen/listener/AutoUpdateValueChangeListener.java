package com.hk.transformation.core.listen.listener;

import com.google.common.collect.Lists;
import com.hk.transformation.core.context.ObservationContext;
import com.hk.transformation.core.listen.event.ValueChangeEvent;
import com.hk.transformation.core.listen.singal.ValueChangeData;
import com.hk.transformation.core.registry.TransformValueRegistry;
import com.hk.transformation.core.value.TransformableValue;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
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
@Component
public class AutoUpdateValueChangeListener implements ValueChangeListener, ApplicationListener<ValueChangeEvent>, ApplicationContextAware {

    private TransformValueRegistry transformValueRegistry;

    private ConfigurableBeanFactory beanFactory;

    private ApplicationContext applicationContext;

    private ObservationContext observationContext = ObservationContext.getInstance();


    public AutoUpdateValueChangeListener() {

    }


    /**
     * 监听到变更事件，进行更新值
     * @param data {@link ValueChangeData}
     * @return
     */
    @Override
    public Object onChange(ValueChangeData data) {

        // 解析事件内容
        String key = data.getKey();
        Object newValue = data.getValue();
        // 获取需要变更的对象
        List<TransformableValue> transformableValues = this.transformValueRegistry.get(beanFactory, key);

        // 更新值，获取旧值，用作观察者的通知
        ArrayList<@Nullable Object> oldValueList = Lists.newArrayList();
        for (TransformableValue transformableValue : transformableValues) {
            try {
                // 更新之后返回旧值
                Object oldValue = transformableValue.update(newValue);
                // 添加到待通知变更的旧数据列表
                oldValueList.add(oldValue);
            } catch (Exception e) {
                log.warn("Error updating transformValue:{} with key:{}, value:{} failed", transformableValue, key, newValue);
            }
        }
        // 观察者模式通知观察者值变更
        // TODO 后续使用线程池，采用异步的方式来进行通知
        for (Object oldValue : oldValueList) {
            observationContext.notify(key, oldValue, newValue);
        }

        // 返回更新之后的新值
        return newValue;
    }


    /**
     * 设置ApplicationContext 和 BeanFactory
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
        this.transformValueRegistry = this.applicationContext.getBean(TransformValueRegistry.class);

        // 注入BeanFactory
        this.beanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
    }


    @Override
    public void onApplicationEvent(ValueChangeEvent event) {

        // 处理变更事件
        this.onChange(event.getData());
    }

}
