package com.hk.transformation.core.helper;

import com.hk.transformation.core.annotation.DynamicSwitch;
import com.hk.transformation.core.annotation.DynamicValue;
import com.hk.transformation.core.constants.TransformationConstants;
import com.hk.transformation.core.utils.ReflectUtil;
import com.hk.transformation.core.value.DynamicValueBean;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.Objects;

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
    public static DynamicValueBean computeDynamicFieldAnnotation(Object bean, Field field) {

        // 判断是否直接标注@DynamicValue 注解，还是@DynamicSwitch 注解，还是通过在Class类上标注的@DynamicValue

        DynamicValueBean dynamicValueBean = new DynamicValueBean();
        // 获取Field字段上的@DynamicValue注解
        DynamicValue dynamicValue = AnnotationUtils.getAnnotation(field, DynamicValue.class);

        if (Objects.nonNull(dynamicValue)) {
            // 获取Key: 因为使用了@AliasFor 注解和 key() 属性是相通的
            String key = (String) AnnotationUtils.getValue(dynamicValue);
            if (StringUtils.isBlank(key)) {
                // key 是默认值或者空的, 采用字段的全限定名称
                String clazzName = field.getDeclaringClass().getName();
                String fieldName = field.getName();
                key = clazzName + TransformationConstants.FIELD_LOCATION_SEPARATOR + fieldName;
            }

            // 获取默认值: 如果注解没有配置默认值，那么设置
            String value = dynamicValue.defaultValue();
            if (Objects.isNull(value) || value.length() == 0) {
                // 如果是默认值"", 则需要进行判断

                try {
                    // 获取field字段声明的时候的初始值
                    field.setAccessible(true);
                    Object initValue = field.get(bean);
                    if (Objects.nonNull(initValue)) {
                        // 非空，进行赋值
                        value = ReflectUtil.valueToAdaptiveString(initValue);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            // 获取值类型的Class
            Class<?> valueClass = dynamicValue.valueClass();
            if (Objects.isNull(valueClass) || ReflectUtil.isVoidType(valueClass)) {
                // 如果没有指定类型或者类型为void -> 获取Field字段类型进行赋值
                valueClass = field.getType();
            }

            // 获取规则表达式
            String expression = dynamicValue.expression();
            if (StringUtils.isNotBlank(expression)) {
                String elType = dynamicValue.elType();
                dynamicValueBean.setExpression(expression).setElType(elType);
            }

            // 构造DynamicValueBean 对象
            dynamicValueBean.setKey(key).setValue(value).setValueClass(valueClass);
            return dynamicValueBean;
        }

        // @DynamicValue 注解为空，判断是否为添加了@DynamicSwitch 注解
        DynamicSwitch dynamicSwitch = AnnotationUtils.getAnnotation(field, DynamicSwitch.class);
        if (Objects.nonNull(dynamicSwitch)) {
            // 获取Key: 因为使用了@AliasFor 注解和 key() 属性是相通的
            String key = (String) AnnotationUtils.getValue(dynamicSwitch);
            if (StringUtils.isBlank(key)) {
                // key 是默认值或者空的, 采用字段的全限定名称
                String clazzName = field.getDeclaringClass().getName();
                String fieldName = field.getName();
                key = clazzName + TransformationConstants.FIELD_LOCATION_SEPARATOR + fieldName;
            }

            // 获取默认值: 如果注解没有配置默认值，那么设置
            boolean value = dynamicSwitch.defaultValue();
            if (BooleanUtils.isFalse(value)) {
                // 如果是默认值false, 则需要进行判断
                try {
                    // 获取field字段声明的时候的初始值
                    field.setAccessible(true);
                    boolean initValue = field.getBoolean(bean);
                    if (Objects.nonNull(initValue)) {
                        // 非空，进行赋值
                        value = initValue;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            // 获取值类型的Class
            Class<?> valueClass = dynamicSwitch.valueClass();
            if (Objects.isNull(valueClass) || ReflectUtil.isVoidType(valueClass)) {
                // 如果没有指定类型或者类型为void -> 获取Field字段类型进行赋值
                valueClass = field.getType();
            }

            // 获取规则表达式
            String expression = dynamicSwitch.expression();
            if (StringUtils.isNotBlank(expression)) {
                String elType = dynamicSwitch.elType();
                dynamicValueBean.setExpression(expression).setElType(elType);
            }

            // 构造DynamicValueBean 对象
            dynamicValueBean.setKey(key).setValue(Boolean.valueOf(value).toString())
                    .setValueClass(valueClass);

            return dynamicValueBean;
        }

        // 如果这些注解都没有，需要判断类上是否添加了@DynamicValue 注解来进行设置
        dynamicValue = AnnotationUtils.findAnnotation(bean.getClass(), DynamicValue.class);
        if (Objects.nonNull(dynamicValue)) {
            // 此处是添加在类上的动态注解
            // TODO 后期考虑添加缓存存储这些注解信息，与对象，field，method的映射信息

            // key 为field 的全限定名称，或者如果类上的DynamicValue 注解指定了key 则采用key.fieldName
            String key = dynamicValue.key();
            if (StringUtils.isEmpty(key) || key.length() == 0) {
                key = bean.getClass().getName();
            }
            key = key.concat(TransformationConstants.FIELD_LOCATION_SEPARATOR).concat(field.getName());

            // 默认值：field 字段的显示初始值
            Object initValue = null;
            Class<?> initValueClass = null;
            try {
                initValue = field.get(bean);
                initValueClass = initValue.getClass();

                // 设置
                dynamicValueBean.setDefaultValue(initValue.toString()).setValueClass(initValueClass);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            // TODO 考虑String 类型更改为Object类型
            // TODO 以后考虑字段上的@Value注解内部的 spEl 表达式
            dynamicValueBean.setKey(key);
            return dynamicValueBean;
        }

        // 能够寻找的地方都进行了查找，还是不能构建出来，返回null, 外面进行判断
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

        // 判断是否可以分配




        return null;
    }
}
