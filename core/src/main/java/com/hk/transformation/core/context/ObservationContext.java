package com.hk.transformation.core.context;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.hk.transformation.core.listen.observation.observer.ValueObserver;
import com.hk.transformation.core.value.TransformableValue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;

/**
 * @author : HK意境
 * @ClassName : ObservationContext
 * @date : 2023/8/27 20:40
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class ObservationContext implements Observable{

    /**
     * 观察者列表
     */
    @Getter
    private final Multimap<String, ValueObserver> observerMap = LinkedListMultimap.create();

    /**
     * Context实例
     */
    private static final ObservationContext INSTANCE = new ObservationContext();


    /**
     * 通知数据变更
     * @param key
     * @param oldValue
     * @param newValue
     * @return
     */
    @Override
    public boolean notify(String key, Object oldValue, Object newValue) {

        Collection<ValueObserver> valueObservers = observerMap.get(key);
            if (CollectionUtils.isNotEmpty(valueObservers)) {
                for (ValueObserver observer : valueObservers) {
                    try{
                        observer.update(oldValue, newValue);
                    }catch(Exception e){
                        log.warn("notify key={}, transformable observer update failed:", key, e);
                    }
                }
            }
        return true;
    }

    @Override
    public void add(String key, ValueObserver observer) {

       observerMap.put(key, observer);
    }


    /**
     * 移除Key 下对应的全部观察者
     * @param key
     * @return 返回被移除的观察者列表
     */
    @Override
    public Collection<ValueObserver> remove(String key) {
        return observerMap.removeAll(key);
    }


    /**
     * 移除指定Key 下指定的观察者对象
     * @param key
     * @param observer
     * @return
     */
    @Override
    public boolean remove(String key, ValueObserver observer) {
        return observerMap.remove(key, observer);
    }


    /**
     * 单例
     * @return
     */
    public static ObservationContext getInstance() {
        return INSTANCE;
    }

}
