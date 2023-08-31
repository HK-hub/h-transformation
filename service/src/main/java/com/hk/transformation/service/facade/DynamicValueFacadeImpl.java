package com.hk.transformation.service.facade;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.hk.transformation.core.context.TransformContext;
import com.hk.transformation.core.listen.event.ValueChangeEvent;
import com.hk.transformation.core.listen.singal.ValueChangeData;
import com.hk.transformation.core.value.DynamicValueBean;
import com.hk.transformation.core.value.TransformableValue;
import com.hk.transformation.domain.value.DynamicDataDomain;
import com.hk.transformation.facade.DynamicValueFacade;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.BeanFactory;
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

        // 获取装在值对象的Map集合
        Map<BeanFactory, Multimap<String, TransformableValue>> registry = transformContext.getRegistry();

        // 获取全部值集合
        Collection<Multimap<String, TransformableValue>> transformMultimapCollection = registry.values();
        if (CollectionUtils.isEmpty(transformMultimapCollection)) {
            return Collections.emptyList();
        }

        // 将集合合并到一个List中
        List<DynamicDataDomain> dynamicDataDomainList = new ArrayList<>(transformMultimapCollection.size());

        for (Multimap<String, TransformableValue> multimap : transformMultimapCollection) {
            Collection<TransformableValue> transformableValueCollection = multimap.values();
            // 转换为DynamicDataDomain
            List<DynamicDataDomain> dynamicDataDomains = transformableValueCollection.stream()
                    .map(this::convertToDynamicDataDomain).toList();

            // 添加到集合中
            dynamicDataDomainList.addAll(dynamicDataDomains);
        }

        // 进行转换
        return dynamicDataDomainList;
    }


    /**
     * 获取指定key 下的数据对象
     * @param key
     * @return
     */
    @Override
    public List<DynamicDataDomain> getDynamicValuesByKey(String key) {

        // 获取装在值对象的Map集合
        Map<BeanFactory, Multimap<String, TransformableValue>> registry = transformContext.getRegistry();
        Collection<Multimap<String, TransformableValue>> multimapCollection = registry.values();

        // 依次取出Multimap查找指定key 下的动态值对象集合
        List<TransformableValue> transformableValueList = new ArrayList<>();
        for (Multimap<String, TransformableValue> multimap : multimapCollection) {
            Collection<TransformableValue> valueCollection = multimap.get(key);
            if (CollectionUtils.isNotEmpty(valueCollection)) {
                transformableValueList.addAll(valueCollection);
            }
        }

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

        // 获取装在值对象的Map集合
        Map<BeanFactory, Multimap<String, TransformableValue>> registry = transformContext.getRegistry();

        // 将需要被重置的对象采集出来
        List<TransformableValue> needResetValueList = new ArrayList<>();
        for (Multimap<String, TransformableValue> multimap : registry.values()) {
            Collection<TransformableValue> valueCollection = multimap.get(key);
            if (CollectionUtils.isNotEmpty(valueCollection)) {
                needResetValueList.addAll(valueCollection);
            }
        }


        // 进行重置
        List<DynamicDataDomain> dynamicDataDomainList = needResetValueList.stream().map(needResetValue -> {
            needResetValue.init();
            return this.convertToDynamicDataDomain(needResetValue);
        }).toList();

        // 返回受到影响的对象
        return dynamicDataDomainList;
    }


    /**
     * 移除Key 下对应的动态值对象
     * @param key
     * @return
     */
    @Override
    public List<DynamicDataDomain> remove(String key) {

        // 获取装在值对象的Map集合
        Map<BeanFactory, Multimap<String, TransformableValue>> registry = transformContext.getRegistry();

        // 将需要被重置的对象采集出来
        List<TransformableValue> needResetValueList = new ArrayList<>();

        for (Multimap<String, TransformableValue> multimap : registry.values()) {
            // 移除Key下对应值对象列表
            if (multimap.containsKey(key)) {
                // 移除值对象并返回
                Collection<TransformableValue> removeValueList = multimap.removeAll(key);
                needResetValueList.addAll(removeValueList);
            }
        }

        // 转换为响应对象
        List<DynamicDataDomain> dynamicDataDomainList = needResetValueList.stream().map(this::convertToDynamicDataDomain).toList();
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
