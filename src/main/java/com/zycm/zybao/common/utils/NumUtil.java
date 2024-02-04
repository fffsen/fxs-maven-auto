package com.zycm.zybao.common.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
 
public class NumUtil {
	
	private static BigDecimal _byte = new BigDecimal("1");
	private static BigDecimal mb = new BigDecimal("1024");
	private static BigDecimal gb = mb.multiply(mb);
	private static BigDecimal tb = gb.multiply(mb);
	
    /**
     * 转换为BigDecimal
     * 
     * @param o
     * @return BigDecimal
     * @author 
     * @date 
     */
    public static BigDecimal toBig(Object o) {
        if (o == null || o.toString().equals("") || o.toString().equals("NaN")) {
            return new BigDecimal(0);
        }
        return new BigDecimal(o.toString());
    }
 
    /**
     * 计算百分比
     * 
     * @param divisor
     * @param dividend
     * @return String
     * @author 
     * @date 
     */
    public static String getPercent(Object divisor, Object dividend) {
        if (divisor == null || dividend == null) {
            return "";
        }
        NumberFormat percent = NumberFormat.getPercentInstance();
        // 建立百分比格式化引用
        percent.setMaximumFractionDigits(2);
        BigDecimal a = toBig(divisor);
        BigDecimal b = toBig(dividend);
        if (a.equals(toBig(0)) || b.equals(toBig(0)) || a.equals(toBig(0.0))
                || b.equals(toBig(0.0))) {
            return "0.00%";
        }
        BigDecimal c = a.divide(b, 4, BigDecimal.ROUND_DOWN);
        return percent.format(c);
    }
 
    /**
     * 计算比例
     * 
     * @param divisor
     * @param dividend
     * @return String
     * @author 
     * @date 
     */
    public static String divideNumber(Object divisor, Object dividend) {
        if (divisor == null || dividend == null) {
            return "";
        }
        BigDecimal a = toBig(divisor);
        BigDecimal b = toBig(dividend);
        if (a.equals(toBig(0)) || b.equals(toBig(0))) {
            return "0";
        }
        BigDecimal c = a.divide(b, 2, BigDecimal.ROUND_DOWN);
        return c.toString();
    }
 
    /**
     * 去两个数的平均值，四舍五入
     * 
     * @param divisor
     * @param dividend
     * @return int
     * @author 
     * @date 
     */
    public static int averageNumber(Object divisor, Object dividend) {
        if (divisor == null || dividend == null) {
            return 0;
        }
        BigDecimal a = toBig(divisor);
        BigDecimal b = toBig(dividend);
        if (a.equals(toBig(0)) || b.equals(toBig(0))) {
            return 0;
        }
        BigDecimal c = a.divide(b, 0, BigDecimal.ROUND_HALF_UP);
        return c.intValue();
    }
    
    /** 
    * @Title: sizeToStr 
    * @Description: kb转其他适合单位
    * @author sy 
    * @param @param size
    * @param @return    
    * @return String   
    * @throws 
    */
    public static String sizeToStr(BigDecimal size){
    	BigDecimal returnbig = size;
    	if(_byte.compareTo(size) < 0){//大于1时开始转换
	    	if(mb.compareTo(size) <= 0){//大于等于1024时开始转换
	    		if(gb.compareTo(size) <= 0){
	    			if(tb.compareTo(size) <= 0){
	    				returnbig = size.divide(tb, 2, BigDecimal.ROUND_HALF_DOWN);
	            		return returnbig.toString()+"TB";
	    			}else{
	    				returnbig = size.divide(gb, 2, BigDecimal.ROUND_HALF_DOWN);
	            		return returnbig.toString()+"GB";
	    			}
	    		}else{
	    			returnbig = size.divide(mb, 2, BigDecimal.ROUND_HALF_DOWN);
	        		return returnbig.toString()+"MB";
	    		}
	    	}else{
	    		return returnbig.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString()+"KB";
	    	}
    	}else if(_byte.compareTo(size) == 0){
    		return "1KB";
    	}else{
    		return returnbig.multiply(mb).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString()+"B";
    	}
    }
    
    public static void main(String[] args) {
    	//BigDecimal b= new BigDecimal("1024.11");
		//System.out.println(NumUtil.sizeToStr(new BigDecimal("0.5")));
		//System.out.println(NumUtil.sizeToStr(new BigDecimal(("a".getBytes().length/1024.00)).setScale(4, BigDecimal.ROUND_HALF_DOWN)));
    	//System.out.println(new BigDecimal(("a".getBytes().length/1024.00)).setScale(4, BigDecimal.ROUND_HALF_DOWN));
	}
}
