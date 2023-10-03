package com.hk.transformation.core.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

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
@Slf4j
@Component
public class TransformPlaceholderResolver implements EnvironmentAware {

    private Environment environment;


    /**
     * 将形如 ${xxx} 的占位符解析出来并且获取其对应的配置值
     * @param placeholder
     * @return
     */
    public String resolvePlaceholder(String placeholder) {

        return this.environment.resolveRequiredPlaceholders(placeholder);
    }


    /**
     * 根据key 获取配置文件
     * @param key
     * @return
     */
    public String getConfigValue(String key) {
        return this.environment.getProperty(key);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    /**
     * 解析SpEL 表达式
     * @param expression
     * @return
     */
    public String resolveExpression(String expression) {

        SpelExpressionParser parser = new SpelExpressionParser();
        try{
            Expression parseExpression = parser.parseExpression(expression);
            String value = parseExpression.getValue(String.class);
            return value;
        }catch(Exception e){
            log.warn("Failed to parse expression:{}, on:{}", expression, e.getMessage());
        }

        return null;
    }

}
