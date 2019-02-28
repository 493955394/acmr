package com.acmr.service.zhzs;


import acmr.math.CalculateFunction;

import java.math.BigDecimal;

public class MathService extends CalculateFunction {

    /**
     * max函数
     * @param
     * @param
     * @return
     */
    public String function_max(String orgStr){
        String [] result = orgStr.split(",");

        BigDecimal max = new BigDecimal(result[0]);
        for (int i = 0; i <result.length ; i++) {
            BigDecimal a = new BigDecimal(result[i]);
            if(a.compareTo(max)>0){
                max=a;
            }
        }
        return max.toPlainString();
    }

    /**
     * 取最小
     * @param
     * @param
     * @return
     */
    public String function_min(String orgStr){
        String [] result = orgStr.split(",");

        BigDecimal min = new BigDecimal(result[0]);
        for (int i = 0; i <result.length ; i++) {
            BigDecimal a = new BigDecimal(result[i]);
            if(a.compareTo(min)<0){
                min=a;
            }
        }
        return min.toPlainString();
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

    /**
     * 求数组的平均值
     * @param orgStr
     * @return
     */
    public String function_avg(String orgStr){
        String [] result = orgStr.split(",");
        BigDecimal add = new BigDecimal(result[0]);
        for (int i = 1; i <result.length ; i++) {
            add = add.add(new BigDecimal(result[i]));
        }
        BigDecimal data = add.divide(new BigDecimal(result.length),16,4);
        return data.toPlainString();
    }
}
