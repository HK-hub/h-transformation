package com.hk.transformation.core.processor;

import com.hk.transformation.core.annotation.dynamic.DynamicValue;
import com.hk.transformation.core.annotation.dynamic.IgnoreValue;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : AbstractTransformationProcessor
 * @date : 2023/8/16 16:34
 * @description :  动态处理器，定义模板接口
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public abstract class AbstractTransformationProcessor implements BeanPostProcessor, PriorityOrdered {


    /**
     * 对Bean对象内部的Field和Method进行处理
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        // 反射获取Bean对象内的所有Field进行处理
        List<Field> fieldList = this.findAllField(bean.getClass());
        for (Field field : fieldList) {
            this.processField(bean, beanName, field);
        }

        // 反射获取Bean对象的所有Method进行处理
        List<Method> methodList = this.findAllMethod(bean.getClass());
        for (Method method : methodList) {
            this.processMethod(bean, beanName, method);
        }

        return bean;
    }


    /**
     * 处理method 方法
     * @param bean
     * @param beanName
     * @param method
     */
    protected abstract void processMethod(Object bean, String beanName, Method method);


    /**
     * 处理field 字段
     * @param bean
     * @param beanName
     * @param field
     */
    protected abstract void processField(Object bean, String beanName, Field field);


    /**
     * 判断在类上是否标注了 @DynamicValue 注解
     * @param bean
     * @return List<Field> 如果标注了注解，返回应该被作为动态值的 Field属性
     */
    private List<Field> ensureTypeWaitProcessFields(Object bean) {

        // 待处理字段列表
        List<Field> waitProcessFields = new ArrayList<>();

        Class<?> beanClass = bean.getClass();
        DynamicValue dynamicValue = beanClass.getAnnotation(DynamicValue.class);

        // 获取每个字段Field
        Field[] beanClassFields = beanClass.getFields();
        // 类上标注了动态值注解
        if (Objects.nonNull(dynamicValue)) {

            // 对每个Field进行判断是否需要被动态化
            for (Field field : beanClassFields) {
                // 是否标注了 @IgnoreValue 注解, 是否 final修饰的字段
                IgnoreValue ignoreValue = field.getAnnotation(IgnoreValue.class);
                if (Objects.nonNull(ignoreValue)) {
                    continue;
                }

                // 判断修饰符
                int modifiers = field.getModifiers();
                if (Modifier.isFinal(modifiers)) {
                    // TODO final 修饰字段，不可更改: 后续想办法可以对对象类型的 final 字段也进行修改
                    continue;
                }

                // 将字段加入待处理中
                waitProcessFields.add(field);
            }
        } else {
            // 对每个Field进行判断是否需要被动态化
            for (Field field : beanClassFields) {
                // 是否标注了 @IgnoreValue 注解, 是否 final修饰的字段
                DynamicValue annotation = field.getAnnotation(DynamicValue.class);
                if (Objects.isNull(annotation)) {
                    continue;
                }

                // 判断修饰符
                int modifiers = field.getModifiers();
                if (Modifier.isFinal(modifiers)) {
                    // TODO final 修饰字段，不可更改: 后续想办法可以对对象类型的 final 字段也进行修改
                    continue;
                }

                // 将字段加入待处理中
                waitProcessFields.add(field);
            }
        }

        return waitProcessFields;
    }

    /**
     * 获取需要处理的方法
     * @param bean
     * @return
     */
    private List<Method> ensureTypeWaitProcessMethods(Object bean) {

        Class<?> beanClass = bean.getClass();

        // 待处理集合
        List<Method> waitProcessMethods = new ArrayList<>();

        // 获取所有的方法
        Method[] methods = beanClass.getMethods();
        for (Method method : methods) {
            // 判断是否标注了 @DynamicValue
            DynamicValue annotation = method.getAnnotation(DynamicValue.class);
            if (Objects.nonNull(annotation)) {
                // 标注了注解，添加到待处理集合
                waitProcessMethods.add(method);
            }
        }

        return waitProcessMethods;
    }

    /**
     * 获取类下所有字段，及其父类字段
     * @param clazz
     * @return
     */
    private List<Field> findAllField(Class<?> clazz) {

        final List<Field> res = new LinkedList<>();
        // 忽略字段
        ReflectionUtils.FieldFilter fieldFilter = field -> {
            IgnoreValue annotation = field.getAnnotation(IgnoreValue.class);
            return annotation == null;
        };
        ReflectionUtils.doWithFields(clazz, res::add, fieldFilter);
        return res;
    }

    /**
     * 获取类下所有方法，及其父类方法
     * @param clazz
     * @return
     */
    private List<Method> findAllMethod(Class<?> clazz) {
        final List<Method> res = new LinkedList<>();

        // 静态方法，抽象，非public方法过滤
        ReflectionUtils.MethodFilter methodFilter = method -> {
            int modifiers = method.getModifiers();

            // 是否非public方法
            if (BooleanUtils.isFalse(Modifier.isPublic(modifiers))) {
                // 不添加
                return false;
            }

            // 是否非静态方法
            if (BooleanUtils.isTrue(Modifier.isStatic(modifiers))) {
                return false;
            }

            // 是否非抽象方法
            if (BooleanUtils.isTrue(Modifier.isAbstract(modifiers))) {
                return false;
            }

            return true;
        };
        ReflectionUtils.doWithMethods(clazz, res::add, methodFilter);
        return res;
    }


    /**
     * 初始化后处理
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
