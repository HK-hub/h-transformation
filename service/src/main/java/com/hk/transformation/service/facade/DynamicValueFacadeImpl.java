package com.hk.transformation.service.facade;

import com.hk.transformation.domain.value.DynamicDataDomain;
import com.hk.transformation.facade.DynamicValueFacade;
import org.springframework.stereotype.Component;

import java.util.List;

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





    @Override
    public List<DynamicDataDomain> getAllDynamicValues() {




        return null;
    }

    @Override
    public List<DynamicDataDomain> getDynamicValuesByKey(String key) {
        return null;
    }

    @Override
    public List<DynamicDataDomain> updateTransformableValue(String key, Object data) {
        return null;
    }

    @Override
    public List<DynamicDataDomain> reset(String key) {
        return null;
    }

    @Override
    public List<DynamicDataDomain> remove(String key) {
        return null;
    }
}
