package com.hk.transformation.service.facade;

import com.google.common.collect.Lists;
import com.hk.transformation.core.context.TransformContext;
import com.hk.transformation.core.listen.event.ValueChangeEvent;
import com.hk.transformation.core.listen.singal.ValueChangeData;
import com.hk.transformation.core.value.DynamicValueBean;
import com.hk.transformation.core.value.TransformableValue;
import com.hk.transformation.domain.value.DynamicDataDomain;
import com.hk.transformation.facade.DynamicValueFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.*;

/**
 * @author : HK意境
 * @ClassName : DynamicValueFacadeImpl
 * @date : 2023/8/28 22:21
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class DynamicValueFacadeImpl implements DynamicValueFacade {

    @Resource
    private TransformContext transformContext;
    @Resource
    private ApplicationContext applicationContext;


    /**
     * 获取全部的动态值对象
     * @return
     */
    @Override
    public List<DynamicDataDomain> getAllDynamicValues() {

        // 获取全部动态值集合
        List<TransformableValue> transformableValueList = this.transformContext.all();

        // 转换为DynamicDataDomain
        List<DynamicDataDomain> dynamicDataDomains = transformableValueList.stream().map(this::convertToDynamicDataDomain).toList();
        log.info("get all dynamic values:size={},elements={}", dynamicDataDomains.size(), dynamicDataDomains);

        // 进行转换
        return dynamicDataDomains;
    }


    /**
     * 获取指定key 下的数据对象
     * @param key
     * @return
     */
    @Override
    public List<DynamicDataDomain> getDynamicValuesByKey(String key) {

        // 通过context 对象获取key 下对应动态值对象列表
        List<TransformableValue> transformableValueList = this.transformContext.get(key);

        // 进行转换
        List<DynamicDataDomain> dynamicDataDomainList = transformableValueList.stream().map(this::convertToDynamicDataDomain).toList();

        // 返回数据
        return dynamicDataDomainList;
    }


    /**
     * 更新动态值对象
     * @param key
     * @param data
     * @return 返回受到影响的值对象，返回修改之前的旧值
     */
    @Override
    public List<DynamicDataDomain> updateTransformableValue(String key, Object data) {

        // 构建更新事件
        ValueChangeEvent valueChangeEvent = new ValueChangeEvent(new ValueChangeData(key, data), this);
        // 发布更新事件
        this.applicationContext.publishEvent(valueChangeEvent);

        // TODO 后续通过多线程/异步上下文工具获取受到影响被更新的对象值
        return Lists.newArrayList();
    }


    /**
     * 重置动态值对象为初始值
     * @param key
     * @return
     */
    @Override
    public List<DynamicDataDomain> reset(String key) {

        // 获取key下集合
        List<TransformableValue> transformableValues = this.transformContext.get(key);


        // 进行重置
        List<DynamicDataDomain> dynamicDataDomainList = transformableValues.stream().map(needResetValue -> {
            needResetValue.reset();
            return this.convertToDynamicDataDomain(needResetValue);
        }).toList();

        // 返回受到影响的对象
        log.info("reset key:{}, dynamicValues:{}", key, dynamicDataDomainList);
        return dynamicDataDomainList;
    }


    /**
     * 移除Key 下对应的动态值对象
     * @param key
     * @return
     */
    @Override
    public List<DynamicDataDomain> remove(String key) {

        // 移除值对象集合
        List<TransformableValue> needRemoveValueList = this.transformContext.remove(key);

        // 转换为响应对象
        List<DynamicDataDomain> dynamicDataDomainList = needRemoveValueList.stream().map(this::convertToDynamicDataDomain).toList();
        log.info("remove key:{}, dynamicValues:{}", key, needRemoveValueList);
        return dynamicDataDomainList;
    }


    /**
     * 转换为Domain数据对象
     * @param value
     * @return
     */
    private DynamicDataDomain convertToDynamicDataDomain(TransformableValue value) {
        DynamicDataDomain domain = new DynamicDataDomain();
        DynamicValueBean bean = value.getDynamicValueBean();
        domain.setKey(value.getKey()).setValue(value.getValue())
                .setDefaultValue(bean.getDefaultValue())
                .setValueClass(value.getValue().getClass())
                .setExpression(bean.getExpression()).setElType(bean.getElType());

        return domain;
    }

}
