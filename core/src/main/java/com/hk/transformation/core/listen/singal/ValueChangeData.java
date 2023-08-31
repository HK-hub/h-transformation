package com.hk.transformation.core.listen.singal;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : HK意境
 * @ClassName : ValueChangeData
 * @date : 2023/8/21 10:11
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@NoArgsConstructor
public class ValueChangeData {

    /**
     * 变更的key
     */
    private String key;

    /**
     * 变更后的值
     */
    private Object value;

    public ValueChangeData(String key, Object value) {
        this.key = key;
        this.value = value;
    }
}
