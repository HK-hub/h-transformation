package com.hk.transformation.web.controller;

import com.hk.transformation.domain.request.UpdateTransformableValueDomain;
import com.hk.transformation.domain.response.ResponseResult;
import com.hk.transformation.service.api.DynamicValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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


    /**
     * 获取全部动态值对象数据
     * @return
     */
    @GetMapping("/all")
    public ResponseResult getAllDynamicValues() {
        return dynamicValueService.getAllDynamicValues();
    }


    /**
     * 获取指定Key 下动态值数据
     * @param key
     * @return
     */
    @GetMapping("/key")
    public ResponseResult getDynamicValuesByKey(@RequestParam String key) {
        return this.dynamicValueService.getDynamicValuesByKey(key);
    }


    /**
     * 更新动态值
     * @param request
     * @return
     */
    @PostMapping("/update")
    public ResponseResult updateTransformableValue(@RequestBody UpdateTransformableValueDomain request) {
        return this.dynamicValueService.updateTransformableValue(request);
    }


    /**
     * 重置key 下对应的动态值
     * @param key
     * @return
     */
    @PostMapping("/reset")
    public ResponseResult reset(String key) {
        return this.dynamicValueService.reset(key);
    }


    /**
     * 移除动态值对象
     * @param key
     * @return
     */
    @PostMapping("/remove")
    public ResponseResult remove(String key) {
        return this.dynamicValueService.remove(key);
    }

}
