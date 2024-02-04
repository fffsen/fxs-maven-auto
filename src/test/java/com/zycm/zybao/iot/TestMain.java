package com.zycm.zybao.iot;


import com.zycm.zybao.common.utils.HttpClientUtil;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
//对接口进行测试
public class TestMain {
	//89860618050011728623
    //private String url = "https://api.10646.cn/rws/api/v1/devices/89860618050011728623";
    //private String url = "https://api.10646.cn/rws/api/v1/devices/89860618050011728623/locationHistory";
    //private String url = "https://api.10646.cn/rws/api/v1/devices?IccId=89860618050011728623&modifiedSince="+URL.encode("2019-02-18T00:21:33+00");
    //private String url = "https://api.10646.cn/rws/api/v1/devices/usageInZone?cycleStartDate=2018-11-26Z";
    private String url = "https://api.10646.cn/rws/api/v1/devices/89860619150027011169/ctdUsages";

    private String charset = "utf-8";
    private HttpClientUtil httpClientUtil = null;

    public TestMain(){
        httpClientUtil = new HttpClientUtil();
    }

    public void test(){


        String httpOrgCreateTest = url ;
        Map<String,String> createMap = new HashMap<String,String>();

        //createMap.put("authuser","*****");
        //createMap.put("authpass","*****");
        //createMap.put("orgkey","****");
        //createMap.put("orgname","****");
        //String httpOrgCreateTestRtn = httpClientUtil.doPost(httpOrgCreateTest,createMap,charset);
        //String httpOrgCreateTestRtn = httpClientUtil.doGet(httpOrgCreateTest,createMap,charset,null);

        try {
			//System.out.println("result:"+HttpClientUtil.unicomIotDoGet(url, null));
			//String str = "";
			String str = "";
	    	String ss = "Basic "+Base64.getMimeEncoder().encodeToString(str.getBytes());
	    	System.out.println(ss);
	    	System.out.println(new String(Base64.getDecoder().decode(ss), "UTF-8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void main(String[] args){
        TestMain main = new TestMain();
        main.test();
    }
}
