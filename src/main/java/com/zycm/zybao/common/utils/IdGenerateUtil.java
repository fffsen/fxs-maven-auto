package com.zycm.zybao.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class IdGenerateUtil {

	public static Long GenerateByTime(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return Long.parseLong(sdf.format(date));
	}
	public static Long GenerateByTime(Date date,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return Long.parseLong(sdf.format(date));
	}
	public static Long GenerateByTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return Long.parseLong(sdf.format(new Date()));
	}

	/*public static Long GenerateByTimeAndRandom(Date date,int length){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return Long.parseLong(sdf.format(date));
	}*/

	public static void main(String[] args) {
		System.out.println(GenerateByTime(new Date()));
		System.out.println(new Random().nextDouble());
	}
}
