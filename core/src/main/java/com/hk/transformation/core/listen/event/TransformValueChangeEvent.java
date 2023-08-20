package com.hk.transformation.core.listen.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author : HK意境
 * @ClassName : TransformValueChangeEvent
 * @date : 2023/8/19 13:27
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class TransformValueChangeEvent extends ApplicationEvent {
    public TransformValueChangeEvent(Object source) {
        super(source);
    }
}
