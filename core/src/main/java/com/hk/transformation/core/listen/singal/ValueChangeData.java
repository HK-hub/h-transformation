package com.hk.transformation.core.listen.singal;

import lombok.Data;

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
public class ValueChangeData {

    /**
     * 变更的key
     */
    private String key;

    /**
     * 变更后的值
     */
    private Object value;


}
