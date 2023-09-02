package com.hk.transformation.core.helper;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.hk.transformation.core.annotation.dynamic.DynamicSwitch;
import com.hk.transformation.core.annotation.dynamic.DynamicValue;
import com.hk.transformation.core.constants.TransformationConstants;
import com.hk.transformation.core.reflect.converter.CollectionConverter;
import com.hk.transformation.core.reflect.converter.StringConverter;
import com.hk.transformation.core.reflect.util.ReflectUtil;
import com.hk.transformation.core.value.DynamicValueBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.annotation.AnnotationUtils;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
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
            dynamicValueBean.setKey(key).setValue(value).setDefaultValue(value).setValueClass(valueClass);
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
            dynamicValueBean.setKey(key).setValue(initValue)
                    .setDefaultValue(initValue)
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
                dynamicValueBean.setValue(initValue.toString())
                        .setDefaultValue(initValue.toString())
                        .setValueClass(initValueClass);
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
     * @param field 字段属性声明类型
     * @param value 原始数值
     * @param valueClass
     * @return
     */
    public static Object computeAdaptiveDynamicValue(Object bean, Field field, Object value, Class<?> valueClass) {

        // 判断是否可以分配
        boolean assignable = ReflectUtil.isAssignable(field, field.getType(), value, valueClass);
        if (BooleanUtils.isFalse(assignable)) {
             // 不能赋值
            log.error("field:{}-class:{} can not assigned by value:{}-class{}", field, field.getClass(), value, valueClass);
            throw new ClassCastException("field:" + field + "-class:"+ field.getClass() + ", can not assigned by value:"+ value + "-class:" + valueClass);
        }

        Class<?> fieldType = field.getType();
        // // 判断 valueClass 是否可以直接分配给 fieldClass
        if (ClassUtils.isAssignable(valueClass, fieldType)) {
            // 可以直接分配，返回值
            return value;
        }

        // 是否基本类型
        if (ReflectUtil.isBaseType(fieldType)) {
            // 基本类型：number，void, boolean, char, string
            if (ReflectUtil.isNumberType(fieldType)) {
                // 如果valueClass 也是number类型那么在前面 ClassUtils.isAssignable() 则会判断成功已经返回了
                // 此时value 可能为Number中非基本类型及其包装类，或者为String类
                if (valueClass.isPrimitive()) {
                    // 如果是基本类型需要转换为包装类型
                    valueClass = ClassUtils.wrapperToPrimitive(valueClass);
                }

                // 判断是否是数值类型
                if (ReflectUtil.isNumberType(valueClass)) {
                    return NumberUtils.createNumber(value.toString());
                } else if (ReflectUtil.isStringType(valueClass)) {
                    // 如果是String 直接尝试转换为Number
                    return NumberUtils.createNumber(value.toString());
                }

                // 如果既不是基本数值类型也不是其包装类型，也不是字符串那么无法转换，抛出异常
                throw new ClassCastException("value:" + value + ", class is " + valueClass + ", can not convert to field class:" + fieldType);
            } else if (ReflectUtil.isBooleanType(fieldType)) {
                // boolean 类型: 直接判断是否可以转为Boolean
                return BooleanUtils.toBooleanObject(value.toString());
            } else if (ReflectUtil.isCharType(fieldType)) {
                // char 类型，尝试直接转换为一个char
                if (ReflectUtil.isCharType(valueClass)) {
                    // 无需转换直接返回
                    return value;
                }
                // 转换成为char
                return CharUtils.toCharacterObject(value.toString());
            } else if (ReflectUtil.isStringType(valueClass)) {
                return String.valueOf(value);
            }
        }
        // 是否集合类型
        else if (ReflectUtil.isCollectionType(fieldType)) {
            Class<?> genericType = ReflectUtil.getGenericType(field);
            // 如果是集合类型可以判断value 是否集合，还是componentClass ,还是String 类型来进行单独值转换
            if (ReflectUtil.isCollectionType(valueClass)) {
                return CollectionConverter.convertCollection(fieldType, (Collection) value, genericType);
            } else if (genericType.isAssignableFrom(valueClass)) {
                // 如果不是集合，判断泛型类型是否可以被value类型赋值
                return value;
            } else if (ReflectUtil.isPrimitiveOrWrapperType(valueClass)) {
                // 如果是基本数据类型
                return genericType.cast(value);
            } else if (ReflectUtil.isStringType(valueClass)) {
                // String 类型的需要将其转换为[], 或者{} 格式
                String[] valueArray = StringConverter.convertStringToArray(String.valueOf(value));
                // 还需要判断元素能不能转换为genericType
                Gson gson = new Gson();
                ArrayList<Object> res = Lists.newArrayList();
                for (String v : valueArray) {
                    res.add(gson.fromJson(v, genericType));
                }
                return res;
            } else {
                throw new ClassCastException("value:" + value + ", class is " + valueClass + ", can not convert to field class:" + fieldType);
            }
        }
        // 是数组类型
        else if (ReflectUtil.isArrayType(fieldType)) {

            // 获取componentType
            Class<?> componentType = fieldType.getComponentType();
            // 如果value 也是Array类型
            if (ReflectUtil.isArrayType(valueClass)) {
                Class<?> valueComponentType = valueClass.getComponentType();
                if (ClassUtils.isAssignable(valueComponentType, componentType)) {
                    // 可以直接赋值
                    return value;
                } else {
                    // 类型不能直接赋值，是否可以通过转换
                    Gson gson = new Gson();
                    gson.fromJson(String.valueOf(value), fieldType);
                }
            } else if (ReflectUtil.isPrimitiveOrWrapperType(valueClass)) {
                if (ClassUtils.isAssignable(valueClass, componentType)) {
                    return value;
                }
                throw new ClassCastException("value:" + value + ", class is " + valueClass + ", can not convert to field class:" + fieldType);
            } else if (ReflectUtil.isStringType(valueClass)) {

                // 转换为数组分割
                String[] vArray = StringConverter.convertStringToArray((String) value);

                // 创建ComponentType 类型数组
                Gson gson = new Gson();
                Object componentArray = Array.newInstance(componentType, vArray.length);

                // 添加转换后的元素
                for (int i = 0; i < vArray.length; i++) {

                    // 转换元素为指定类型
                    Object o = gson.fromJson(vArray[i], componentType);
                    // 如果可以成功转换，设置，没有抛出异常则说明能够构造出适应的数据结构
                    Array.set(componentArray, i, o);
                }

                // 没有抛出异常
                return componentArray;
            } else if (ReflectUtil.isCollectionType(valueClass)) {
                // 集合类型: 直接判断是否可以转换
                Type valueGenericType = ReflectUtil.getGenericType(valueClass);
                if (ClassUtils.isAssignable((Class<?>) valueGenericType, fieldType)) {
                    return ((Collection) value).toArray();
                }
            }
        }
        // 是否Map 类型
        else if (ReflectUtil.isMapType(fieldType)) {

        }
        // 是否对象类型
        else {
            Gson gson = new Gson();
            return gson.fromJson((String) value, fieldType);
        }


        // 战时没有匹配的类型
        throw new ClassCastException("value:" + value + ", class is " + valueClass + ", can not convert to field class:" + fieldType);
    }
}
