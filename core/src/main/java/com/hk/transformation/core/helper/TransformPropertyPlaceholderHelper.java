package com.hk.transformation.core.helper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author : HK意境
 * @ClassName : TransformPropertyPlaceholderHelper
 * @date : 2023/9/22 17:27
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Getter
public class TransformPropertyPlaceholderHelper implements EnvironmentAware {

    private static final Map<String, String> wellKnownSimplePrefixes = new HashMap<>(4);

    static {
        // 根据后缀获取配对的前缀
        wellKnownSimplePrefixes.put("}", "{");
        wellKnownSimplePrefixes.put("]", "[");
        wellKnownSimplePrefixes.put(")", "(");
    }


    /** Prefix for system property placeholders: "${". */
    public static final String PLACEHOLDER_PREFIX = "${";

    /** Suffix for system property placeholders: "}". */
    public static final String PLACEHOLDER_SUFFIX = "}";

    /** Value separator for system property placeholders: ":". */
    public static final String VALUE_SEPARATOR = ":";


    private final String placeholderPrefix = PLACEHOLDER_PREFIX;

    private final String placeholderSuffix = PLACEHOLDER_SUFFIX;

    private final String simplePrefix;

    private final String valueSeparator = VALUE_SEPARATOR;

    /**
     * 启用严格解析模式，不能解析将抛出异常
     */
    private final boolean ignoreUnresolvablePlaceholders = false;

    /**
     * 占位符处理器，将占位符转换为字符串数值
     */
    private final PropertyPlaceholderHelper.PlaceholderResolver placeholderResolver;

    private Environment environment;

    private PropertySource propertySource;

    public TransformPropertyPlaceholderHelper(PropertyPlaceholderHelper.PlaceholderResolver resolver) {

        Assert.notNull(placeholderPrefix, "'placeholderPrefix' must not be null");
        Assert.notNull(placeholderSuffix, "'placeholderSuffix' must not be null");

        // simplePrefixForSuffix = '{'
        String simplePrefixForSuffix = wellKnownSimplePrefixes.get(this.placeholderSuffix);
        if (simplePrefixForSuffix != null && this.placeholderPrefix.endsWith(simplePrefixForSuffix)) {
            this.simplePrefix = simplePrefixForSuffix;
        }
        else {
            this.simplePrefix = this.placeholderPrefix;
        }

        // 设置解析器
        this.placeholderResolver = resolver;
    }




    /**
     * 将格式为{@code ${name}}的所有占位符替换为提供的属性配置中的值，
     * @param value 包含要替换占位符的值
     * @param properties 要替换占位符的{@code Properties}对象
     * @return 替换占位符后的值
     */
    public String replacePlaceholders(String value, final Properties properties) {
        Assert.notNull(properties, "'properties' must not be null");
        return replacePlaceholders(value, properties::getProperty);
    }


    /**
     * 将格式为{@code ${name}}的所有占位符替换为提供的属性配置中的值，
     * @param value 包含要替换占位符的值
     * @param placeholderResolver 要替换占位符的{@code Properties}对象
     * @return 替换占位符后的值
     */
    public String replacePlaceholders(String value, PropertyPlaceholderHelper.PlaceholderResolver placeholderResolver) {
        Assert.notNull(value, "'value' must not be null");
        return parseStringValue(value, placeholderResolver, null);
    }



    /**
     * 解析占位符为配置值
     * @param value
     * @return
     */
    public String parseStringValue(String value) {

       return parseStringValue(value,null);
    }


    /**
     * 解析占位符为配置值
     * @param value
     * @param visitedPlaceholders
     * @return
     */
    public String parseStringValue(String value, @Nullable Set<String> visitedPlaceholders) {

        return parseStringValue(value, this.placeholderResolver, visitedPlaceholders);
    }


    /**
     * 将格式为{@code ${name}}的所有占位符替换为提供的属性配置中的值，
     * @param value 包含要替换占位符的值
     * @return 替换占位符后的值
     */
    public String parseStringValue(String value, PropertyPlaceholderHelper.PlaceholderResolver placeholderResolver, @Nullable Set<String> visitedPlaceholders) {

        // 获取前缀地址
        int startIndex = value.indexOf(this.placeholderPrefix);
        if (startIndex == -1) {
            return value;
        }

        StringBuilder result = new StringBuilder(value);
        while (startIndex != -1) {
            int endIndex = findPlaceholderEndIndex(result, startIndex);
            if (endIndex != -1) {
                String placeholder = result.substring(startIndex + this.placeholderPrefix.length(), endIndex);
                String originalPlaceholder = placeholder;
                if (visitedPlaceholders == null) {
                    visitedPlaceholders = new HashSet<>(4);
                }
                if (!visitedPlaceholders.add(originalPlaceholder)) {
                    throw new IllegalArgumentException(
                            "Circular placeholder reference '" + originalPlaceholder + "' in property definitions");
                }
                // Recursive invocation, parsing placeholders contained in the placeholder key.
                placeholder = parseStringValue(placeholder, visitedPlaceholders);
                // Now obtain the value for the fully resolved key...
                String propVal = placeholderResolver.resolvePlaceholder(placeholder);
                if (propVal == null && this.valueSeparator != null) {
                    int separatorIndex = placeholder.indexOf(this.valueSeparator);
                    if (separatorIndex != -1) {
                        String actualPlaceholder = placeholder.substring(0, separatorIndex);
                        String defaultValue = placeholder.substring(separatorIndex + this.valueSeparator.length());
                        propVal = placeholderResolver.resolvePlaceholder(actualPlaceholder);
                        if (propVal == null) {
                            propVal = defaultValue;
                        }
                    }
                }
                if (propVal != null) {
                    // Recursive invocation, parsing placeholders contained in the
                    // previously resolved placeholder value.
                    propVal = parseStringValue(propVal, visitedPlaceholders);
                    result.replace(startIndex, endIndex + this.placeholderSuffix.length(), propVal);
                    if (log.isTraceEnabled()) {
                        log.trace("Resolved placeholder '" + placeholder + "'");
                    }
                    startIndex = result.indexOf(this.placeholderPrefix, startIndex + propVal.length());
                }
                else if (this.ignoreUnresolvablePlaceholders) {
                    // Proceed with unprocessed value.
                    startIndex = result.indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
                }
                else {
                    throw new IllegalArgumentException("Could not resolve placeholder '" +
                            placeholder + "'" + " in value \"" + value + "\"");
                }
                visitedPlaceholders.remove(originalPlaceholder);
            }
            else {
                startIndex = -1;
            }
        }
        return result.toString();
    }



    /**
     * 获取占位符介绍索引下标
     * @param buf
     * @param startIndex
     * @return
     */
    private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
        int index = startIndex + this.placeholderPrefix.length();
        int withinNestedPlaceholder = 0;
        while (index < buf.length()) {
            if (StringUtils.substringMatch(buf, index, this.placeholderSuffix)) {
                if (withinNestedPlaceholder > 0) {
                    withinNestedPlaceholder--;
                    index = index + this.placeholderSuffix.length();
                }
                else {
                    return index;
                }
            }
            else if (StringUtils.substringMatch(buf, index, this.simplePrefix)) {
                withinNestedPlaceholder++;
                index = index + this.simplePrefix.length();
            }
            else {
                index++;
            }
        }
        return -1;
    }


    /**
     * 表达式
     * @param el
     * @return 占位符
     */
    public static String getPlaceholder(String el) {

        // TODO 因为这里的el 表达式存在嵌套所以不好进行解析的深度控制
        return el;
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 解析占位符
     * @param placeholder
     * @return
     */
    public String resolvePlaceholder(String placeholder) {
        String result = this.environment.resolveRequiredPlaceholders(placeholder);
        log.debug("resolved placeholder:{}, result:{}", placeholder, result);
        return result;
    }
}
