package com.hk.transformation.core.helper;


import com.google.gson.Gson;
import com.hk.transformation.core.annotation.dynamic.DynamicSwitch;
import com.hk.transformation.core.annotation.dynamic.DynamicValue;
import com.hk.transformation.core.constants.TransformationConstants;
import com.hk.transformation.core.reflect.util.ReflectUtil;
import com.hk.transformation.core.value.DynamicValueBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.*;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.*;

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
@Slf4j
public class DynamicValueHelper {

    /**
     * 转换服务
     */
    private static final ConversionService conversionService = new DefaultConversionService();

    /**
     * JSON转换
     */
    private static final Gson gson = new Gson();


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
        DynamicValue dynamicValue = AnnotationUtils.findAnnotation(field, DynamicValue.class);

        if (Objects.nonNull(dynamicValue)) {
            // 获取Key: 因为使用了@AliasFor 注解和 key() 属性是相通的
            String key = dynamicValue.key();
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
                        // TODO 修改为JSON赋值，转换方式
                        value = ReflectUtil.valueToAdaptiveString(initValue);
                    } else {
                        // 此处需要特殊注意处理后续的 null 空指针异常
                        value = null;
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
            dynamicValueBean.setKey(key)
                    .setValue(value).setDefaultValue(value).setValueClass(valueClass)
                    .setComment(dynamicValue.comment());
            return dynamicValueBean;
        }

        // @DynamicValue 注解为空，判断是否为添加了@DynamicSwitch 注解
        DynamicSwitch dynamicSwitch = AnnotationUtils.findAnnotation(field, DynamicSwitch.class);
        if (Objects.nonNull(dynamicSwitch)) {
            // 获取Key: 因为使用了@AliasFor 注解和 key() 属性是相通的
            String key = dynamicSwitch.key();
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
            String initValue = Boolean.valueOf(value).toString();
            dynamicValueBean.setKey(key)
                    .setComment(dynamicSwitch.comment())
                    .setValue(initValue).setDefaultValue(initValue)
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
                initValueClass = field.getType();

                // 设置
                dynamicValueBean.setValue(initValue.toString())
                        .setDefaultValue(initValue.toString())
                        .setValueClass(initValueClass);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            // TODO 考虑String 类型更改为Object类型
            // TODO 以后考虑字段上的@Value注解内部的 spEl 表达式
            dynamicValueBean.setKey(key).setComment(dynamicValue.comment());
            return dynamicValueBean;
        }

        // 能够寻找的地方都进行了查找，还是不能构建出来，返回null, 外面进行判断
        return null;
    }



    /**
     * 计算出能够赋值的适配的类，如果不能转换为适配的类，将抛出异常
     * @param bean field 所属的Object
     * @param field 字段属性声明类型
     * @param value 值
     * @param valueClass 值类型
     * @return
     */
    public static Object computeAdaptiveDynamicValue(Object bean, Field field, Object value, Class<?> valueClass) {

        // 判断是否可以直接分配
        Class<?> fieldType = field.getType();
        boolean assignable = ClassUtils.isAssignable(valueClass, fieldType);

        // 能够直接赋值
        if (BooleanUtils.isTrue(assignable)) {
            // 转换为目标类型
            Object adaptiveValue = ConvertUtils.convert(value, fieldType);
            log.info("convert source value:class={},value={} to target value:class={},value={}, by direct", value.getClass(), value, adaptiveValue.getClass(), adaptiveValue);
            return adaptiveValue;
        }

        // 不能赋值
        log.warn("field:{}-class:{} can not assigned by value:{}-{}, use direct", field, field.getClass(), value, valueClass);

        // 直接转换方式失败，采用Gson 进行JSON转换
        Object tmpValue = value;
        if (!ReflectUtil.isStringType(value.getClass())) {
            // 如果不是字符串，需要转换为json 字符串
            tmpValue = gson.toJson(value);
        }

        // 尝试进行转换
        try {
            Object adaptiveObject = gson.fromJson((String) tmpValue, fieldType);
            log.info("convert value:{} to target value:{}, by json", value, adaptiveObject);
            return adaptiveObject;
        } catch (Exception e) {
            // 抛出异常
            log.warn("field:{}-class:{} can not convert by value:{}-class{}, use json", field, field.getClass(), value, valueClass);
        }

        // JSON 方式转化你失败，尝试Spring Converter方式转换
        try {
            // 进行转换
            Object adaptiveObject = conversionService.convert(value, fieldType);
            log.info("convert value:{} to target value:{}, by spring converter", value, adaptiveObject);
            // 返回转换后的对象
            return adaptiveObject;
        } catch (Exception e) {
            // 转换失败会抛出异常
            log.warn("field:{}-class:{} can not convert by value:{}-class{}, use spring convert", field, field.getClass(), value, valueClass);
        }

        // 战时没有匹配的类型
        throw new ClassCastException("value:" + value + ", class is " + valueClass + ", can not convert to field class:" + fieldType);
    }


}
