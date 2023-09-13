package com.hk.transformation.core.convert;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author : HK意境
 * @ClassName : StringToCollectionConverter
 * @date : 2023/9/11 18:44
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class StringToCollectionConverter implements ConditionalGenericConverter {

    private static final String[] EMPTY_STRING_ARRAY = {};
    private final ConversionService conversionService;

    public StringToCollectionConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Collection.class));
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return targetType.getElementTypeDescriptor() == null || this.conversionService.canConvert(sourceType, targetType.getElementTypeDescriptor());
    }


    /**
     * 进行转换
     * @param source
     * @param sourceType
     * @param targetType
     * @return
     */
    @Override
    @Nullable
    public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

        log.info("using custom converter: convert {} from {} to {}", source, sourceType, targetType);

        if (source == null) {
            return null;
        }
        String string = (String) source;

        String[] fields = commaDelimitedListToStringArray(string);
        TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
        Collection<Object> target = CollectionFactory.createCollection(targetType.getType(),
                (elementDesc != null ? elementDesc.getType() : null), fields.length);

        if (elementDesc == null) {
            for (String field : fields) {
                target.add(field.trim());
            }
        }
        else {
            for (String field : fields) {
                Object targetElement = this.conversionService.convert(field.trim(), sourceType, elementDesc);
                target.add(targetElement);
            }
        }
        return target;
    }


    /**
     * 自定义转换逻辑
     * @param string
     * @return
     */
    public static String[] commaDelimitedListToStringArray(String string) {

        if (string == null) {
            return EMPTY_STRING_ARRAY;
        }
        // 匹配正则表达式，去除[], {}, 这种符号开头结尾
        if (string.startsWith("[") && string.endsWith("]")) {
            // 去除首尾的[]
            string = string.substring(1);
            string = string.substring(0, string.length() - 1);
        } else if (string.startsWith("{") && string.endsWith("}")) {
            // 去除首尾的{}
            string = string.substring(1);
            string = string.substring(0, string.length() - 1);
        }

        return StringUtils.commaDelimitedListToStringArray(string);
    }

}
