package com.hk.transformation.core.value;

import com.hk.transformation.core.annotation.DynamicValue;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : HK意境
 * @ClassName : TransformableValue
 * @date : 2023/8/17 21:29
 * @description : 包含动态值注解，属性，bean, field的复合对象
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Data
@ToString
public class TransformableValue implements InitializingBean {

    /**
     * 值key
     */
    protected String key;


    /**
     * 注解
     */
    protected DynamicValue annotation;

    /**
     * bean 对象
     */
    protected Object bean;

    /**
     * bean 名称
     */
    protected String beanName;

    /**
     * bean工厂
     */
    protected BeanFactory beanFactory;


    /**
     * Field 字段或Method方法
     */
    protected Member member;


    /**
     * 是否初始化
     */
    protected AtomicBoolean initialized = new AtomicBoolean(false);


    public TransformableValue(DynamicValue annotation, Object bean, Field field, boolean initialized) {
        this.annotation = annotation;
        this.bean = bean;
        this.member = field;
        this.initialized.set(initialized);
    }

    public TransformableValue(DynamicValue annotation, Object bean, Method method, boolean initialized) {
        this.annotation = annotation;
        this.bean = bean;
        this.member = method;
        this.initialized.set(initialized);
    }


    /**
     * 执行初始化逻辑
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        // 获取注解属性值
        String defaultValue = this.annotation.defaultValue();
        Class<?> valueClass = this.annotation.valueClass();

        if (this.member instanceof Field field) {

            // 字段初始化: 赋初值，计算表达式
            this.initialize(field, defaultValue, valueClass);
        } else if (this.member instanceof Method) {
            // 方法初始化
            log.warn("method currently not supported...");
        }

    }


    /**
     * 初始化
     * @param field
     * @param defaultValue
     * @param valueClass
     */
    private void initialize(Field field, String defaultValue, Class<?> valueClass) {

        // 是否已经初始化了
        if (BooleanUtils.isTrue(this.initialized.get())) {
            return;
        }

        // 判断是否可以赋值的类型
        boolean enableAssign = this.ensureTheValueAssignable(defaultValue, valueClass);

        try{
            field.setAccessible(true);
            // TODO 转换成为需要的值类型
            field.set(this.bean, defaultValue);
        }catch(Exception e){
            // 初始化异常
            log.warn("try to assign:{} value to field:{} of Object:{}, but failed on:{}", defaultValue, field.getName(), bean.getClass(), e.getMessage());
        }
    }


    /**
     * 判断该值是否可以赋值
     * @param defaultValue
     * @param valueClass
     * @return
     */
    private boolean ensureTheValueAssignable(String defaultValue, Class<?> valueClass) {
        return false;
    }


}
