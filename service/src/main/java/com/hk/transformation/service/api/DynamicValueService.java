package com.hk.transformation.service.api;

import com.hk.transformation.domain.response.ResponseResult;

/**
 * @author : HK意境
 * @ClassName : DynamicValueService
 * @date : 2023/8/28 22:21
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface DynamicValueService {

    /**
     * 获取所有的动态值列表
     * @return
     */
    public ResponseResult getAllDynamicValues();


    /**
     * 获取指定key 的动态值列表
     * @param key
     * @return
     */
    public ResponseResult getDynamicValuesByKey(String key);


    /**
     * 更新动态值
     * @param key
     * @param data
     * @return 返回受到影响的值对象
     */
    public ResponseResult updateTransformableValue(String key, Object data);


    /**
     * 重置动态值对象的值为默认值
     * @param key
     * @return 返回受到影响的值对象
     */
    public ResponseResult reset(String key);


    /**
     * 移除某个Key 对应的值对象
     * @param key
     * @return 返回受到影响的值对象
     */
    public ResponseResult remove(String key);



}
