package com.hk.transformation.core.listen.listener;

import com.hk.transformation.core.listen.singal.ValueChangeData;

/**
 * @author : HK意境
 * @ClassName : ValueChangeListener
 * @date : 2023/8/19 13:06
 * @description : 值变化监听器
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@FunctionalInterface
public interface ValueChangeListener {

    /**
     * 值发生改变事件
     * @param data {@link ValueChangeData}
     * @return object 返回旧值
     */
    public Object onChange(ValueChangeData data);

}
