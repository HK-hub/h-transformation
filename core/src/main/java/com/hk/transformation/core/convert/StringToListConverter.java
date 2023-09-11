package com.hk.transformation.core.convert;

import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : StringToListConverter
 * @date : 2023/9/11 18:44
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class StringToListConverter<T> implements Converter<String, List<T>> {

    @Override
    public List<T> convert(String source) {
        return null;
    }
}
