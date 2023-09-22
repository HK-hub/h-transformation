package com.hk.transformation.core.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

/**
 * @ClassName : TransformPlaceholderResolver
 * @author : HK意境
 * @date : 2023/9/22 18:08
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Component
public class TransformPlaceholderResolver {

    @Autowired
    private Environment env;

    @Autowired
    private StringValueResolver stringValueResolver;

    /**
     * 将形如 ${xxx} 的占位符解析出来并且获取其对应的配置值
     * @param valueWithPlaceholder
     * @return
     */
    public String resolvePlaceholder(String valueWithPlaceholder) {
        return stringValueResolver.resolveStringValue(valueWithPlaceholder);
    }

    public String getConfigValue(String key) {
        return env.getProperty(key);
    }
}
