package com.hk.transformation.test.util;

import java.math.BigDecimal;

/**
 * @author : HK意境
 * @ClassName : MixUtil
 * @date : 2023/8/30 16:07
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class MixUtil {


    /**
     * 左误差范围
     * @param value
     * @param precision
     * @return
     */
    public static BigDecimal precisionLeftBound(BigDecimal value, BigDecimal precision) {

        return value.subtract(precision);
    }


    /**
     * 右误差范围
     * @param decimal
     * @param precisionBound
     * @return
     */
    public static BigDecimal precisionRightBound(BigDecimal decimal, BigDecimal precisionBound) {
        return decimal.add(precisionBound);
    }
}
