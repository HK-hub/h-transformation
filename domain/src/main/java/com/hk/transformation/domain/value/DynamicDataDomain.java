package com.hk.transformation.domain.value;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : DynamicDataDomain
 * @date : 2023/8/28 22:14
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class DynamicDataDomain {

    /**
     * 动态值的Key, 用于后期获取和修改进行定位
     */
    private String key;


    /**
     * 当前值
     */
    private Object value;


    /**
     * 默认值,初始值
     */
    private String defaultValue;


    /**
     * 动态值的Class类型
     */
    private Class<?> valueClass;


    /**
     * 动态表达式：简单的规则引擎，spEL，OGNL等
     */
    private String expression;


    /**
     * 动态表达式类型
     */
    private String elType;


}
