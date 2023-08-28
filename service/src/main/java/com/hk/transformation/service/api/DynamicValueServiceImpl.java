package com.hk.transformation.service.api;

import com.hk.transformation.domain.response.ResponseResult;
import com.hk.transformation.facade.DynamicValueFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : DynamicValueServiceImpl
 * @date : 2023/8/28 22:23
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Service
public class DynamicValueServiceImpl implements DynamicValueService{

    @Resource
    DynamicValueFacade dynamicValueFacade;

    @Override
    public ResponseResult getAllDynamicValues() {
        return null;
    }

    @Override
    public ResponseResult getDynamicValuesByKey(String key) {
        return null;
    }

    @Override
    public ResponseResult updateTransformableValue(String key, Object data) {
        return null;
    }

    @Override
    public ResponseResult reset(String key) {
        return null;
    }

    @Override
    public ResponseResult remove(String key) {
        return null;
    }
}
