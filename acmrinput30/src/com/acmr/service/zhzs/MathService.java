package com.acmr.service.zhzs;


import acmr.math.CalculateFunction;

import java.math.BigDecimal;

public class MathService extends CalculateFunction {

    /**
     * max函数
     * @param a
     * @param b
     * @return
     */
    public String function_max(String a,String b){
        BigDecimal a1 = new BigDecimal(a);
        BigDecimal b1 = new BigDecimal(b);
        a1 = a1.max(b1);
        return a1.toPlainString();
    }

    /**
     * 取最小
     * @param a
     * @param b
     * @return
     */
    public String function_min(String a,String b){
        BigDecimal a1 = new BigDecimal(a);
        BigDecimal b1 = new BigDecimal(b);
        a1 = a1.min(b1);
        return a1.toPlainString();
    }

    /**
     * pow
     * @param x
     * @param y
     * @return
     */
    public String function_pow(String x,String y) {
        BigDecimal a1 = new BigDecimal(x);
        BigDecimal b1 = new BigDecimal(y);
        a1 = BigDecimal.valueOf(Math.pow(a1.doubleValue(),b1.doubleValue()));
        return a1.toPlainString();
    }

    /**
     * exp
     * @param a
     * @return
     */
    public String function_exp(String a){
        BigDecimal a1 = new BigDecimal(a);
        a1 = BigDecimal.valueOf(Math.exp(a1.doubleValue()));
        return a1.toPlainString();
    }

    /**
     * log
     * @param a
     * @return
     */
    public String function_log(String a){
        BigDecimal a1 = new BigDecimal(a);
        a1 = BigDecimal.valueOf(Math.log(a1.doubleValue()));
        return a1.toPlainString();
    }

    /**
     * log10
     * @param a
     * @return
     */
    public String function_log10(String a){
        BigDecimal a1 = new BigDecimal(a);
        a1 = BigDecimal.valueOf(Math.log10(a1.doubleValue()));
        return a1.toPlainString();
    }

    /**
     * random
     * @return
     */
    public String function_chance(){
        BigDecimal a1 = BigDecimal.valueOf(Math.random());
        return a1.toPlainString();
    }
}
