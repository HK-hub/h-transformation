package com.hk.transformation.core.listen.event;


import lombok.Getter;
import lombok.Setter;

/**
 * @author : HK意境
 * @ClassName : ValueChangeEvent
 * @date : 2023/8/19 13:08
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Getter
@Setter
public class ValueChangeEvent extends TransformValueChangeEvent {

    /**
     * 变更的key
     */
    private String key;

    /**
     * 变更后的值
     */
    private Object value;


    public ValueChangeEvent(Object source) {
        super(source);
    }
}
