package com.zycm.zybao.iot;


import com.zycm.zybao.iot.telecom.DesUtils;

public class demo{
	/**
	 * 接口调用示例代码(Java)
	 * @Title: demo.java
	 * @date 2017年1月23日 下午13:19:22
	 * @version V1.0
	 * @Copyright 中国电信股份有限公司物联网分公司
	 * @description API开发手册登录物联网自管理门户->在线文档查看
	*/
    public static void main(String[] args) {
    	//具体接口参数需参照自管理门户在线文档
    	String access_number="14914000000";  //物联网卡号(149或10649号段)
    	String user_id = "test";     //用户名
    	String password = "test";    //密码
    	String method = "queryPakage";  //接口名-套餐使用量查询

    	String[] arr = {access_number,user_id,password,method}; //加密数组，数组所需参数根据对应的接口文档

      	//key值指密钥，由电信提供，每个客户都有对应的key值，key值平均分为三段如下：
    	String key1 = "abc";
    	String key2 = "def";
    	String key3 = "ghi";

    	DesUtils des = new DesUtils(); //加密工具类实例化
		String passwordEnc = des.strEnc(password,key1,key2,key3);  //密码加密
		System.out.println("密码加密结果:"+passwordEnc);
		//密码加密结果：441894168BD86A2CC

		String sign = des.strEnc(DesUtils.naturalOrdering(arr),key1,key2,key3); //生成sign加密值
		System.out.println("sign加密结果:"+sign);
		//sign加密结果：45E8B9924DE397A8F7E5764767810CF774CC7E1685BA702C9C4C367EFDAE5D932B37C0C8F0F8EB0CAD6372289F407CA941894168BD86A2CC32E5804EA05BAA5099649468B9418E52

		String passwordDec = des.strDec(passwordEnc,key1,key2,key3);//密码解密
		System.out.println("密码解密结果:"+passwordDec);
        //密码解密结果 :test

		String signDec = des.strDec(sign,key1,key2,key3); //sign解密
		System.out.println("sign解密结果:"+signDec);
		//sign解密结果：14914000000,queryPakage,test,test
	}
}
