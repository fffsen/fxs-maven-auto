package com.zycm.zybao.iot;

import com.zycm.zybao.common.utils.UrlRequestUtil;
import com.zycm.zybao.iot.telecom.DesUtils;

public class TTest {

	public static void main(String[] args) {
		String access_number="1064971309531";    //物联网卡号(149或10649号段) 1410337471874
		String iccid="8986111827903780118";
		String monthDate = "20190801";
		String user_id = "0pU0G63qAIMKP9so18p10S";     //用户名
		String password = "8P4wTTdm7";    //密码
		//String user_id = "HNZYWHC";     //用户名
		//String password = "1ypCI9";    //密码
		//String method = "queryPakage";    //接口名-套餐使用量查询接口
		//String method = "queryTrafficByDate";
		//String method = "queryCustomAttr";
		String method = "getSIMList";

		String[] arr = {iccid,user_id,password,method }; //加密数组，数组所需参数根据对应的接口文档，根据iccid查询时传入iccid
		//key1,key2,key2为电信提供的9位长接口密钥平均分为三段所形成
		//key1为密钥前三位，key2为密钥中间三位，key3位密钥最后三位
		String key1 = "0Jc";
		String key2 = "Hbz";
		String key3 = "2aR";
		DesUtils des = new DesUtils(); //DES加密工具类实例化
		String passWord = des.strEnc(password,key1,key2,key3);  //密码加密
		String[] arr1 = {user_id,password,method };
		String sign = des.strEnc(DesUtils.naturalOrdering(arr1),key1,key2,key3); //生成sign加密值
		System.out.println(passWord+"  "+sign);

		String passwordDec = des.strDec(passWord,key1,key2,key3);//密码解密
		System.out.println("密码解密结果:"+passwordDec);
        //密码解密结果 :test

		String signDec = des.strDec(sign,key1,key2,key3); //sign解密
		System.out.println("sign解密结果:"+signDec);
		try {

			/*Map<String,String> param1 = new HashMap<String,String>();
			param1.put("", value);
			param1.put("", value);
			param1.put("", value);
			param1.put("", value);
			param1.put("", value);
			param1.put("", value);*/
			//String param = "method="+method+"&user_id="+user_id+"&access_number="+access_number+"&monthDate="+monthDate+"&passWord="+passWord+"&sign="+sign;
			//String param = "method="+method+"&user_id="+user_id+"&iccid="+iccid+"&startDate=20190801&endDate=20190831&passWord="+passWord+"&sign="+sign;
			//String param = "method="+method+"&user_id="+user_id+"&access_number="+access_number+"&passWord="+passWord+"&sign="+sign;
			String param = "method="+method+"&user_id="+user_id+"&access_number="+access_number+"&passWord="+passWord+"&sign="+sign+"&pageIndex=1";

			String re = UrlRequestUtil.requestDataFortelecom(param, "POST");
  			System.out.println(re);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
