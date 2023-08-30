package com.hk.transformation.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : UpdateTransformableValueDomain
 * @date : 2023/8/29 22:14
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class UpdateTransformableValueDomain {

    /**
     * 需要更新的key
     */
    private String key;

    /**
     * 更新之后的新值
     */
    private Object data;

}
