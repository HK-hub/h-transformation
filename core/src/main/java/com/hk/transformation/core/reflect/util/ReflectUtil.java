package com.hk.transformation.core.reflect.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * @author : HK意境
 * @ClassName : ReflectUtil
 * @date : 2023/8/19 10:27
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class ReflectUtil {


    /**
     * 判断是否可以赋值
     * @param field 字段
     * @param value
     * @param fieldType 字段属性声明类型
     * @param valueType
     * @return
     */
    public static boolean isAssignable(Field field, Class<?> fieldType, Object value, Class<?> valueType) {

        // 如果是 void.class 不能赋值
        if (isVoidType(fieldType) || isVoidType(valueType)) {
            return false;
        }

        // 如果设置的类型都是相同的那么即可直接赋值
        if (fieldType.isAssignableFrom(valueType)) {
            // 可赋值的
            return true;
        }

        // 判断field 是否基本类型
        if (isBaseType(fieldType)) {

            if (isStringType(fieldType)) {
                if (isStringType(valueType)) {
                    return true;
                }
                // 不是String类型但是如果是基本类型，那么是可以转换为String的，但如果是其他对象类型则不行
                return isBaseType(valueType);
            } else if (isNumberType(fieldType)) {
                // 判断是否数值类型
                if (isNumberType(valueType)) {
                    return true;
                } else if (String.class.equals(valueType)) {
                    // 是字符串，判断是否可以通过转换成为数值
                    return NumberUtils.isCreatable(String.valueOf(value));
                }
            } else if (isCharType(fieldType)) {
                // 是否Char类型
                if (isCharType(valueType)) {
                    return true;
                } else if (String.class.equals(valueType)) {
                    return isCharValue((String) value);
                }
            } else if (isBooleanType(fieldType)) {
                // 是否Boolean类型
                if (isCharType(valueType)) {
                    return true;
                } else if (String.class.equals(valueType)) {
                    return isBooleanValue((String) value);
                }
            }
        } else if (fieldType.isArray()) {
            // 是否数组类型

            // 获取数组成员类型
            Class<?> fieldComponentType = fieldType.getComponentType();
            // 需要判断valueType是否也为数组
            if (valueType.isArray()) {
                // 如果也是数组需要判断成员是否能够赋值
                Class<?> valueComponentType = valueType.getComponentType();
                // 如果值类型也是数组对象，其实可以直接判断两个成员类型是否可以赋值
                return fieldComponentType.isAssignableFrom(valueComponentType);
            } else {

                // valueType 值类型不是数组，但是可能为单个元素值，也可能为String
                // 判断valueType 和 fieldComponentType 的关系。
                if (fieldComponentType.isAssignableFrom(valueType)) {
                    // 单个元素可以赋值
                    return true;
                }

                // 不是能够直接赋值的，需要判断是否可以转换
                if (isBaseType(fieldComponentType) && isBaseType(valueType)) {
                    return true;
                }

                return false;
            }
        } else if (isCollectionType(fieldType)) {

            // 判断valueType 类型
            Class<?> genericType = getGenericType(field);
            if (isCollectionType(valueType)) {
                // 是否可以赋值或进行转换
                return true;
            } else if (isStringType(valueType)) {
                // 字符串类型
                return true;
            } else if (isArrayType(valueType)){
                // 非集合，非字符串，尝试是否数组类型

                // 获取集合元素类型
                Class<?> componentType = valueType.getComponentType();
                if (genericType.isAssignableFrom(componentType)) {

                    // 能够直接赋值
                    return true;
                } else if (isBaseType(genericType) && isBaseType(componentType)){

                    // 不能直接赋值，是否可以通过转换
                    return true;
                } else if (isStringType(componentType)) {
                    return true;
                }
            }

            return false;
        } else {
            // 是否对象类型
            return isStringType(valueType);
        }

        return false;
    }

    /**
     * 是否基本类型：原始类型 + 包装类 + 字符串类型
     * @param targetClazz
     * @return
     */
    public static boolean isBaseType(Class<?> targetClazz) {

        return isNumberType(targetClazz) || isCharType(targetClazz) || isBooleanType(targetClazz) || isStringType(targetClazz);
    }

    /**
     * 是否基本类型及其包装类，不包括String
     * @param targetClazz
     * @return
     */
    public static boolean isPrimitiveOrWrapperType(Class<?> targetClazz) {

        return isNumberType(targetClazz) || isCharType(targetClazz) || isBooleanType(targetClazz);
    }


    /**
     * 是否数组类型
     * @param targetClazz
     * @return
     */
    public static boolean isArrayType(Class<?> targetClazz) {

        return targetClazz.isArray();
    }


    /**
     * 是否集合类型
     * @param targetClazz
     * @return
     */
    public static boolean isCollectionType(Class<?> targetClazz) {
        return Collection.class.isAssignableFrom(targetClazz);
    }


    /**
     * 是否是Map 类型
     * @param targetClazz
     * @return
     */
    public static boolean isMapType(Class<?> targetClazz) {
        return Map.class.isAssignableFrom(targetClazz);
    }

    /**
     * 判断是否数值类型
     * @param targetClazz java.lang.reflect.Field 的数据类型 clazz。 getType 方法获取
     * @return boolean 是否是Number类型
     */
    public static boolean isNumberType(Class<?> targetClazz) {

        // 判断包装类
        if (Number.class.isAssignableFrom(targetClazz)) {
            // 基本类型包装类，BigDecimal, BigInteger, 原子类
            return true;
        }

        // 判断原始类,过滤掉特殊的基本类型
        if (targetClazz == boolean.class || targetClazz == char.class || targetClazz == void.class) {
            return false;
        }

        // 基本类型 + boolean + char + void
        return targetClazz.isPrimitive();
    }


    /**
     * 判断是否Boolean类型
     * @param targetClazz 目标类
     * @return
     */
    public static boolean isBooleanType(Class<?> targetClazz) {

        // Boolean 类型就两种状态基本类和包装类
        return Boolean.class.isAssignableFrom(targetClazz);
    }


    /**
     * 判断是否Boolean类型
     * @param targetClazz 目标类
     * @return
     */
    public static boolean isCharType(Class<?> targetClazz) {

        // Boolean 类型就两种状态基本类和包装类
        return Character.class.isAssignableFrom(targetClazz);
    }

    /**
     * 判断是否String类型
     * @param targetClazz
     * @return
     */
    public static boolean isStringType(Class<?> targetClazz) {

        return String.class.equals(targetClazz);
    }


    /**
     * 判断是否void类型
     * @param targetClazz
     * @return
     */
    public static boolean isVoidType(Class<?> targetClazz) {

        return Void.class.equals(targetClazz) || void.class.equals(targetClazz);
    }

    /**
     * 判断一个String是否可以转换为Boolean值
     * @param value
     * @return
     */
    public static boolean isBooleanValue(String value) {
        return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
    }


    /**
     * 判断一个String是否可以转换为Char
     * @param str
     * @return
     */
    public static boolean isCharValue(String str) {
        return StringUtils.isEmpty(str) || str.length() == 1;
    }


    /**
     * 获取泛型的类型
     * @param field
     * @return
     */
    public static Class<?> getGenericType(Field field) {

        Type listFieldType = field.getGenericType();
        if (listFieldType instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) listFieldType).getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                // 显示指定了泛型类型
                Type genericType = actualTypeArguments[0];
                return getTypeClass(genericType);
            } else {
                // 没有指定泛型类型，返回 Object
                return Object.class;
            }
        } else {
            // 没有指定泛型
            return field.getDeclaringClass();
        }
    }


    /**
     * 获取一个类声明的泛型参数
     * @param clazz
     * @return
     */
    public static Type getGenericType(Class<?> clazz) {
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (typeArguments.length > 0) {
                return typeArguments[0];
            }
        }
        return Object.class;
    }


    public static Class<?> getTypeClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        } else {
            throw new IllegalArgumentException("Unsupported Type: " + type);
        }
    }


    /**
     * 将Value转换为适合的String：基本类型直接of(), 数组类型转换为[e1,e2,e3,e4], 集合类型转换为{1,2,3,4},对象类型转换为JSON对象，Map 类型转换为{key:value,key:value,key:value}
     * @param value
     * @return
     */
    public static String valueToAdaptiveString(Object value) {


        return "";
    }
}
