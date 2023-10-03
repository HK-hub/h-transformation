package com.hk.transformation.facade;

import com.hk.transformation.domain.value.DynamicDataDomain;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : DynamicValueFacade
 * @date : 2023/8/28 22:10
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface DynamicValueFacade {


    /**
     * 获取所有的动态值列表
     * @return
     */
    public List<DynamicDataDomain> getAllDynamicValues();


    /**
     * 获取指定key 的动态值列表
     * @param key
     * @return
     */
    public List<DynamicDataDomain> getDynamicValuesByKey(String key);


    /**
     * 更新动态值
     * @param key
     * @param data
     * @return 返回受到影响的值对象
     */
    public List<DynamicDataDomain> updateTransformableValue(String key, Object data);

    /**
     * 采用表达式更新指定key下的值
     * @param key
     * @param expression
     * @param type
     * @return
     */
    public List<DynamicDataDomain> updateWithExpression(String key, String expression, String type);


    /**
     * 重置动态值对象的值为默认值
     * @param key
     * @return 返回受到影响的值对象
     */
    public List<DynamicDataDomain> reset(String key);


    /**
     * 移除某个Key 对应的值对象
     * @param key
     * @return 返回受到影响的值对象
     */
    public List<DynamicDataDomain> remove(String key);

}
