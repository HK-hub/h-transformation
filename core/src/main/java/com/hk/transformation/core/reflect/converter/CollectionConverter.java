package com.hk.transformation.core.reflect.converter;

import java.util.*;

/**
 * @ClassName : CollectionConverter
 * @author : HK意境
 * @date : 2023/8/23 22:43
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class CollectionConverter {

    public static Collection convertCollection(Class fieldClass, Collection value, Class valueClass) {
        if (!Collection.class.isAssignableFrom(fieldClass)) {
            throw new IllegalArgumentException("fieldClass must be a Collection type");
        }

        Collection convertedCollection;
        
        try {
            convertedCollection = (Collection) fieldClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create an instance of the specified collection type", e);
        }

        for (Object item : value) {
            if (valueClass.isInstance(item)) {
                convertedCollection.add(valueClass.cast(item));
            } else {
                throw new IllegalArgumentException("Item in the source collection is not of the specified valueClass");
            }
        }

        return convertedCollection;
    }

}
