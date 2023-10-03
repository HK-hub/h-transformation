package com.hk.transformation.test.core;


import com.hk.transformation.core.helper.DynamicValueHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Field;
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
 * @ClassName : DynamicValueHelperTest
 * @date : 2023/9/3 20:54
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class  DynamicValueHelperTest {

    @Test
    public void computeDynamicFieldAnnotation() {
    }

    @Test
    public void computeAdaptiveDynamicValue() throws Exception {

        // 计算出适合的值对象

        TestValue testValue = new TestValue();

        Field field = testValue.getClass().getDeclaredField("intValue");
        Object value = "100";
        Object adaptiveValue = DynamicValueHelper.computeAdaptiveDynamicValue(null, testValue, field.getType(), value, value.getClass());
        boolean canAccess = field.canAccess(testValue);
        field.setAccessible(true);
        field.set(testValue, adaptiveValue);
        field.setAccessible(canAccess);
        log.info("转换后的值：{}, toString={}, field value:{}", adaptiveValue, adaptiveValue.toString(), field.get(testValue));
    }



    @Getter
    static class TestValue {

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


}