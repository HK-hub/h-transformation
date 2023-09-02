package com.hk.transformation.core.value;

/**
 * @author : HK意境
 * @ClassName : Transformable
 * @date : 2023/8/19 13:30
 * @description : 可变更类
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface Transformable {

    /**
     * 更新
     * @param newValue
     * @return 返回旧值
     */
    public Object update(Object newValue);


    /**
     * 初始化或恢复默认值
     * @return 返回初始之后的值
     */
    public Object init();


    /**
     * 重置
     * @return 返回重置之前的值
     */
    public Object reset();

}
