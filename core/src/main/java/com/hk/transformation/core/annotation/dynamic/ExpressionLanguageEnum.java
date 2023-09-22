package com.hk.transformation.core.annotation.dynamic;

import lombok.Getter;

/**
 * @author : HK意境
 * @ClassName : ExpressionLanguageEnum
 * @date : 2023/7/30 23:28
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public enum ExpressionLanguageEnum {

    /**
     * 规则引擎
     */
    RULE("rule"),

    /**
     * spring EL 表达式
     */
    SpEL("SpEL"),

    /**
     * ognl 对象图导航语言
     */
    OGNL("ognl"),

    ;
    @Getter
    private String type;

    ExpressionLanguageEnum(String type) {
        this.type = type;
    }
}
