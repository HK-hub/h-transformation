package com.hk.transformation.core.bean;

import com.hk.transformation.core.annotation.dynamic.DynamicValue;
import org.springframework.stereotype.Component;

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
}
