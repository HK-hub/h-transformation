package com.hk.transformation.core.listen.observation.observer;


/**
 * @author : HK意境
 * @ClassName : TransformableObserver
 * @date : 2023/8/23 8:34
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface TransformableObserver<T extends Object> {

    /**
     * 观察者更新方法
     * @param oldValue
     * @param newValue
     */
    public void update(T oldValue, T newValue);

}
