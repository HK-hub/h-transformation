package com.hk.transformation.core.helper;

import com.hk.transformation.core.annotation.DynamicValue;

import java.lang.reflect.Field;

/**
 * @author : HK意境
 * @ClassName : DynamicValueHelper
 * @date : 2023/7/31 17:20
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class DynamicValueHelper {

    /**
     * 计算Field字段的@DynamicValue 注解出来
     * @param bean
     * @param field
     * @return
     */
    public static DynamicValue computeDynamicFieldAnnotation(Object bean, Field field) {

        return null;
    }


    /**
     * 给字段赋值
     * @param field 字段
     * @param value 值
     * @param clazz 值类型
     */
    public static void assignField(Field field, String value, Class<?> clazz) {

        Class<?> type = field.getType();
    }


    /**
     * 计算出能够赋值的适配的类，如果不能转换为适配的类，将抛出异常
     * @param bean
     * @param field
     * @param defaultValue
     * @param valueClass
     * @return
     */
    public static Object computeAdaptiveDynamicValue(Object bean, Field field, Object defaultValue, Class<?> valueClass) {

        return null;
    }
}
