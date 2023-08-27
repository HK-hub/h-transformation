package com.hk.transformation.core.context;

import com.hk.transformation.core.listen.observation.observer.ValueObserver;
import com.hk.transformation.core.value.TransformableValue;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author : HK意境
 * @ClassName : Observable
 * @date : 2023/8/27 20:37
 * @description : 可观察类
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface Observable {

    /**
     * 通知数据变更
     * @param key
     * @param oldValue
     * @param newValue
     * @return
     */
    public boolean notify(String key, Object oldValue, Object newValue);



    /**
     * 添加Key 观察者
     * @param key
     * @param observer
     */
    public void add(String key, ValueObserver observer);


    /**
     * 移除key 下全部 观察者
     * @param key
     */
    public Collection<ValueObserver> remove(String key);


    /**
     * 移除key 下指定观察者对象
     * @param key
     * @param observer
     */
    public boolean remove(String key, ValueObserver observer);


}
