package com.hk.transformation.core.bean;

import com.hk.transformation.core.annotation.dynamic.DynamicValue;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author : HK意境
 * @ClassName : TestBean
 * @date : 2023/7/30 23:16
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Component
@DynamicValue(defaultValue = "{1,2,4,5}", key = "test")
public class TestBean {

    // 基本数据类型、包装类型、常见数据类型和时间相关数据类型
    private byte byteValue;
    private short shortValue;
    private int intValue;
    private long longValue;
    private float floatValue;
    private double doubleValue;
    private char charValue;
    private boolean booleanValue;

    private Byte byteObject;
    private Short shortObject;
    private Integer intObject;
    private Long longObject;
    private Float floatObject;
    private Double doubleObject;
    private Character charObject;
    private Boolean booleanObject;

    private String stringValue;

    // 日期类型
    private Date dateValue;
    private java.sql.Date sqlDateValue;
    private BigDecimal bigDecimalValue;
    private BigInteger bigIntegerValue;
    private LocalDate localDateValue;
    private LocalDateTime localDateTimeValue;

    // 集合类型
    private List<String> stringList;
    private Set<Integer> integerSet;
    private Map<String, Double> stringToDoubleMap;

    // 数组类型
    // 数组类型
    private String[] stringArray;
    private int[] intArray;
    private Integer[] integerArray;
    private Date[] dateArray;
    private LocalDate[] localDateArray;



}
