package com.hk.transformation.core.value;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : DynamicValueBean
 * @date : 2023/8/21 21:24
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@ToString
@Accessors(chain = true)
public class DynamicValueBean {


    /**
     * 等同于key: 如果为空默认，如果是属性字段=全类名.属性名，如果是方法=全类名#方法名
     */
    private String value;

    /**
     * 动态值的Key, 用于后期获取和修改进行定位
     */
    private String key;


    /**
     * 默认值,初始值
     */
    private String defaultValue;


    /**
     * 动态值的Class类型
     */
    private Class<?> valueClass = void.class;


    /**
     * 动态表达式：简单的规则引擎，spEL，OGNL等
     */
    private String expression;


    /**
     * 动态表达式类型
     */
    private String elType;

}
