package com.hk.transformation.core.listen.event;


import com.hk.transformation.core.listen.singal.ValueChangeData;
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

    private ValueChangeData data;


    public ValueChangeEvent(ValueChangeData data, Object source) {
        super(source);
        this.data = data;
    }




}
