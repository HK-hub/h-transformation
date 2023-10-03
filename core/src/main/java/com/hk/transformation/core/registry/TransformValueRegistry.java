package com.hk.transformation.core.registry;

import com.hk.transformation.core.context.TransformContext;
import com.hk.transformation.core.value.TransformableValue;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : HK意境
 * @ClassName : TransformValueRegistry
 * @date : 2023/8/17 21:19
 * @description : 注册进入Context中
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@NoArgsConstructor
public class TransformValueRegistry {


    /**
     * 初始化标志
     */
    private final AtomicBoolean initialized = new AtomicBoolean(false);


    /**
     * 环境
     */
    private TransformContext context;


    public TransformValueRegistry(TransformContext context) {
        this.context = context;
    }

    /**
     * 注册到Context环境，Spring 环境
     * @param beanFactory
     * @param key
     * @param value
     */
    public void registry(BeanFactory beanFactory, String key, TransformableValue value) {

        // 注册
        context.add(beanFactory, key, value);

        // 执行初始化逻辑
        this.initialize(value);
    }


    /**
     * 获取值对象
     * @param beanFactory
     * @param key
     * @return
     */
    public List<TransformableValue> get(BeanFactory beanFactory, String key) {

       return this.context.get(beanFactory, key);
    }


    /**
     * 初始化
     * @param value
     */
    private void initialize(TransformableValue value) {

        // 为字段或方法赋值
        Member member = value.getMember();

        // 执行对于的初始化逻辑
        try {
            if (member instanceof Field field) {
                // 初始化
                value.init();
            } else if (member instanceof Method) {
                // 方法类型
                // log.warn("dynamic method is not support..., please wait...");
                value.init();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
