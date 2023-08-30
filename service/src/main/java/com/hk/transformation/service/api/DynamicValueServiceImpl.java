package com.hk.transformation.service.api;

import com.google.common.collect.Lists;
import com.hk.transformation.domain.request.UpdateTransformableValueDomain;
import com.hk.transformation.domain.response.ResponseResult;
import com.hk.transformation.domain.value.DynamicDataDomain;
import com.hk.transformation.facade.DynamicValueFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    private DynamicValueFacade dynamicValueFacade;


    /**
     * 获取全部动态值列表
     * @return
     */
    @Override
    public ResponseResult getAllDynamicValues() {

        List<DynamicDataDomain> allDynamicValues = null;
        try{
            allDynamicValues = this.dynamicValueFacade.getAllDynamicValues();
            // 响应List
            if (Objects.isNull(allDynamicValues)) {
                allDynamicValues = Lists.newArrayList();
            }
        } catch(Exception e){
            log.warn("get all dynamic values failed:", e);
            return ResponseResult.FAIL().setMessage("get all dynamic values failed")
                    .setData(e.getMessage());
        }

        return ResponseResult.SUCCESS(allDynamicValues);
    }


    /**
     * 根据key 获取其对应的值对象
     * @param key
     * @return
     */
    @Override
    public ResponseResult getDynamicValuesByKey(String key) {

        List<DynamicDataDomain> dynamicDataDomains = null;
        try {
            dynamicDataDomains = this.dynamicValueFacade.getDynamicValuesByKey(key);
            if (Objects.isNull(dynamicDataDomains)) {
                dynamicDataDomains = Lists.newArrayList();
            }
        } catch (Exception e) {
            log.warn("get dynamic values by key failed:", e);
            return ResponseResult.FAIL().setMessage("get dynamic values by key failed")
                    .setData(e.getMessage());
        }

        return ResponseResult.SUCCESS(dynamicDataDomains);
    }


    /**
     * 更新动态值
     * @param request
     * @return
     */
    @Override
    public ResponseResult updateTransformableValue(UpdateTransformableValueDomain request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getKey()) || Objects.isNull(request.getData());
        if (BooleanUtils.isTrue(preCheck)) {
            return ResponseResult.FAIL().setMessage("更新动态值对象事件参数校验失败!");
        }
        // 受影响的值对象集合
        List<DynamicDataDomain> updateTransformableValueList = null;
        try {
            // 返回受到影响的动态值对象
            updateTransformableValueList = this.dynamicValueFacade.updateTransformableValue(request.getKey(), request.getData());
            if (Objects.isNull(updateTransformableValueList)) {
                updateTransformableValueList = Lists.newArrayList();
            }
        } catch (Exception e) {
            log.warn("update transformable value has exception:", e);
            return ResponseResult.SUCCESS(Collections.emptyList())
                    .setMessage("update transformable value has exception:" + e.getMessage());
        }

        return ResponseResult.SUCCESS(updateTransformableValueList);
    }


    /**
     * 重置key 下的动态值对象数据为初始值/默认值
     * @param key
     * @return
     */
    @Override
    public ResponseResult reset(String key) {

        // 参数校验
        if (StringUtils.isEmpty(key)) {
            return ResponseResult.FAIL().setMessage("Key不能为空!");
        }

        // 返回受到影响的值对象
        List<DynamicDataDomain> dynamicDataDomainList = null;
        try {
            dynamicDataDomainList = this.dynamicValueFacade.reset(key);

        } catch (Exception e) {
            log.warn("reset transformable value failed:", e);
        }

        // 如果重置失败，那么其实受到影响的对象为0
        if (Objects.isNull(dynamicDataDomainList)) {
            dynamicDataDomainList = Lists.newArrayList();
        }
        return ResponseResult.SUCCESS(dynamicDataDomainList);
    }


    /**
     * 移除动态值对象，以后不在进行更新获取等
     * @param key
     * @return
     */
    @Override
    public ResponseResult remove(String key) {

        // 受到影响被移除的值对象集合
        List<DynamicDataDomain> dynamicDataDomainList = null;
        try{
            dynamicDataDomainList = this.dynamicValueFacade.remove(key);
        }catch(Exception e){
            log.warn("remove transformable value failed:", e);
        }

        // 如果为空，没有值对象受到影响
        if (Objects.isNull(dynamicDataDomainList)) {
            dynamicDataDomainList = Lists.newArrayList();
        }

        return ResponseResult.SUCCESS(dynamicDataDomainList);
    }
}
