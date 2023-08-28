package com.hk.transformation.web.controller;

import com.hk.transformation.domain.response.ResponseResult;
import com.hk.transformation.service.api.DynamicValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : TransformationController
 * @date : 2023/8/28 22:26
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@RestController
@RequestMapping("/transformable/v1")
public class TransformationController {

    @Resource
    private DynamicValueService dynamicValueService;



    public ResponseResult getAllDynamicValues() {
        return dynamicValueService.getAllDynamicValues();
    }

    public ResponseResult getDynamicValuesByKey(@RequestParam String key) {
        return this.dynamicValueService.getDynamicValuesByKey(key);
    }

    public ResponseResult updateTransformableValue(String key, Object data) {
        return this.dynamicValueService.updateTransformableValue(key, data);
    }

    public ResponseResult reset(@RequestParam String key) {
        return this.dynamicValueService.reset(key);
    }

    public ResponseResult remove(@RequestParam String key) {
        return this.dynamicValueService.remove(key);
    }

}
