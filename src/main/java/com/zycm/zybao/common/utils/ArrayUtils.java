package com.zycm.zybao.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: sy
 * @create: 2023-04-23 08:26
 */
public class ArrayUtils {

    public static Integer[] toInt(String[] s){
        List<Integer> list = new ArrayList<Integer>();
        if(null != s && s.length > 0){
            for (int i = 0; i < s.length; i++) {
                if(StringUtils.isNotBlank(s[i])){
                    list.add(Integer.parseInt(s[i]));
                }
            }
            Integer[] a = new Integer[list.size()];
            for (int i = 0; i < a.length; i++) {
                a[i] = list.get(i);
            }
            return a;
        }else{
            return null;
        }
    }

    public static String arrayToString(Integer[] astr){
        String rstr = "";
        if(astr.length > 0){
            for (int i = 0; i < astr.length; i++) {
                if(astr[i] != null)
                    rstr += ","+astr[i];
            }
            if(StringUtils.isNotBlank(rstr))
                rstr = rstr.substring(1);
        }
        return rstr;
    }
}
