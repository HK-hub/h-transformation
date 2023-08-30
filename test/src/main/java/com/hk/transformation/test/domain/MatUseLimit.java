package com.hk.transformation.test.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : MatUseLimit
 * @date : 2023/8/30 15:34
 * @description : 物料使用限制
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class MatUseLimit {


    /**
     * 下限
     */
    private BigDecimal left;

    /**
     * 上限
     */
    private BigDecimal right;


    /**
     * 等值：固定多少量。相当于 left=right
     */
    private BigDecimal fixed;


    /**
     * 误差范围：例如 0.001, 0.1
     */
    private BigDecimal bound;



    public BigDecimal getLeft() {

        if (Objects.nonNull(fixed)) {
            this.left = fixed;
            this.right = fixed;
        }

        return left;
    }

    public BigDecimal getRight() {

        if (Objects.nonNull(fixed)) {
            this.left = fixed;
            this.right = fixed;
        }

        return left;
    }



}
