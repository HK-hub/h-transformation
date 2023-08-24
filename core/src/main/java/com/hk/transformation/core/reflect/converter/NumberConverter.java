package com.hk.transformation.core.reflect.converter;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * @ClassName : NumberConverter
 * @author : HK意境
 * @date : 2023/8/23 17:09
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class NumberConverter {
    public static boolean canConvertToInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean canConvertToFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean canConvertToDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean canConvertToBigInteger(String str) {
        try {
            new BigInteger(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean canConvertToBigDecimal(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean canConvertToShort(String str) {
        try {
            Short.parseShort(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean canConvertToLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static BigDecimal convertToBigDecimal(String str) {
        return new BigDecimal(str);
    }



    public static void main(String[] args) {
        String str = "123.45";

        System.out.println("Can convert to Integer: " + canConvertToInteger(str));
        System.out.println("Can convert to Float: " + canConvertToFloat(str));
        System.out.println("Can convert to Double: " + canConvertToDouble(str));
        System.out.println("Can convert to BigInteger: " + canConvertToBigInteger(str));
        System.out.println("Can convert to BigDecimal: " + canConvertToBigDecimal(str));
        System.out.println("Can convert to Short: " + canConvertToShort(str));
        System.out.println("Can convert to Long: " + canConvertToLong(str));
    }
}
