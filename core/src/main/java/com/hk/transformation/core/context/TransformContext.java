package com.hk.transformation.core.context;

import com.google.common.collect.*;
import com.hk.transformation.core.value.TransformableValue;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.aop.framework.AopProxyUtils;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author : HK意境
 * @ClassName : TransformContext
 * @date : 2023/7/31 22:25
 * @description : 值转换上下文: 用户记录和变更动态值
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Getter
public class TransformContext extends AbstractTransformableEnvironment {

    /**
     * Context实例
     */
    private static final TransformContext INSTANCE = new TransformContext();


    @Override
    public void add(String key, Field field) {

    }

    @Override
    public void update(String key, Object value) {


    }



    @Override
    public void remove(String key) {
        Collection<Multimap<String, TransformableValue>> values = this.registry.values();
        for (Multimap<String, TransformableValue> multimap : values) {
            if (multimap.containsKey(key)) {
                multimap.removeAll(key);
            }
        }
    }



    @Override
    public void remove(String key, Class<?> clazz) {
        Collection<Multimap<String, TransformableValue>> values = this.registry.values();
        for (Multimap<String, TransformableValue> multimap : values) {

            // 获取key 下集合
            Collection<TransformableValue> valueCollection = multimap.get(key);
            if (CollectionUtils.isEmpty(valueCollection)) {
                continue;
            }

            // 删除key 下符合类型的值对象
            for (TransformableValue transformableValue : valueCollection) {

                // 代理对象
                Object bean = transformableValue.getBean();

                // 判断直接类型
                if (ClassUtils.isAssignable(clazz, bean.getClass())) {
                    valueCollection.remove(transformableValue);

                } else if (AopProxyUtils.ultimateTargetClass(bean).isAssignableFrom(clazz)) {
                    // 判断代理目标对象类型
                    valueCollection.remove(transformableValue);
                }
            }
        }
    }


    public static TransformContext getInstance() {
        return INSTANCE;
    }

}
