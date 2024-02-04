package com.zycm.zybao.common.utils;

import com.zycm.zybao.common.config.IotConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;
/*
 * 利用HttpClient进行请求的工具类
 */
@Slf4j
public class HttpClientUtil {

    /**
    * @Title: doPost
    * @Description: post 请求
    * @param url
    * @param map  参数键值对
    * @param charset 字符编码
    * @return
    * @author sy
    * @throws
    * @return String    返回类型
    *
    */
    public static String doPost(String url,Map<String,String> map,String charset,Map<String, String> headers) throws Exception{
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try{
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            //设置head
            if (headers != null && headers.keySet().size() > 0) {
    			Set<String> keys = headers.keySet();
    			for (Iterator<String> i = keys.iterator(); i.hasNext();) {
    				String key = (String) i.next();
    				httpPost.addHeader(key, headers.get(key));
    			}
    		}
            //设置参数
            if(map != null){
            	 List<NameValuePair> list = new ArrayList<NameValuePair>();
                 Iterator iterator = map.entrySet().iterator();
                 while(iterator.hasNext()){
                     Entry<String,String> elem = (Entry<String, String>) iterator.next();
                     list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
                 }
                 if(list.size() > 0){
                     UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
                     httpPost.setEntity(entity);
                 }
            }

            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                }
            }
        }catch(Exception ex){
            log.error("POST请求异常",ex);
        }
        return result;
    }

    /**
    * @Title: doGet
    * @Description: get请求
    * @param url
    * @param param
    * @param charset
    * @param headers
    * @return
    * @throws Exception    参数
    * @author sy
    * @throws
    * @return String    返回类型
    *
    */
    public static String doGet(String url,String param,String charset,Map<String, String> headers) throws Exception{
        HttpClient httpClient = null;
        HttpGet httpGet = null;
        String result = null;
        try{
            httpClient = new SSLClient();
            httpGet = new HttpGet(url+((StringUtils.isNotBlank(param))?"?"+param:""));
            if (headers != null && headers.keySet().size() > 0) {
    			Set<String> keys = headers.keySet();
    			for (Iterator<String> i = keys.iterator(); i.hasNext();) {
    				String key = (String) i.next();
    				httpGet.addHeader(key, headers.get(key));
    			}
    		}
            HttpResponse response = httpClient.execute(httpGet);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                }
            }
        }catch(Exception ex){
        	log.error("GET请求异常",ex);
        }
        return result;
    }

    /**
    * @Title: unicomIotDoPost
    * @Description: 联通物联卡post请求
    * @param url
    * @param map
    * @return
    * @throws Exception    参数
    * @author sy
    * @throws
    * @return String    返回类型
    *
    */
    public static String unicomIotDoPost(String url,Map<String,String> map) throws Exception{
    	Map<String,String> headers = new HashMap<String,String>();
    	headers.put("Accept", "application/json");
    	String str = IotConfig.unicomUsername +":"+IotConfig.unicomSecretkey;
    	headers.put("Authorization","Basic "+Base64.getMimeEncoder().encodeToString(str.getBytes()));

    	return doPost(url, map, "utf-8", headers);
    }

    /**
    * @Title: unicomIotDoGet
    * @Description: 联通物联卡get请求
    * @param url
    * @param map
    * @return
    * @throws Exception    参数
    * @author sy
    * @throws
    * @return String    返回类型
    *
    */
    public static String unicomIotDoGet(String url,Map<String,Object> map,String authstr) throws Exception{
    	String result = "";
    	Map<String,String> headers = new HashMap<String,String>();
    	headers.put("Accept", "application/json");
    	String str="";
    	if(StringUtils.isNotBlank(authstr)){
    		str = authstr;
    	}else{
    		str = IotConfig.unicomUsername +":"+IotConfig.unicomSecretkey;
    	}
    	headers.put("Authorization","Basic "+Base64.getMimeEncoder().encodeToString(str.getBytes()));

    	String param = "";
    	if(map != null){
    		for (Entry<String, Object> entry: map.entrySet()) {
        		if(entry.getValue() instanceof Date){//时间需要转换
        			param += "&"+entry.getKey()+"="+encode(DateFormatUtils.format((Date)entry.getValue(), "yyyy-MM-dd'T'HH:mm:ssZZ"));
        		}
        		param += "&"+entry.getKey()+"="+entry.getValue().toString();
    		}
    		result = doGet(url, param.substring(1), "utf-8", headers);
    	}else{
        	result = doGet(url, param, "utf-8", headers);//{"errorMessage":"Invalid credentials","errorCode":"10000001"}
    	}
		if(result.contains("\"errorCode\"")){
			throw new Exception("接口调用异常！"+result);
		}
    	return result;
    }

    /**
     * @Title: mobileIotDoPost
     * @Description: 移动物联卡
     * @param url
     * @param map
     * @return
     * @throws Exception    参数
     * @author sy
     * @throws
     * @return String    返回类型
     *
     */
     public static String mobileIotDoPost(String url,Map<String,String> map) throws Exception{
     	map.remove("signature");//移除之前的数据 开始重新加密

     	map.put("username", IotConfig.mobileUsername);
 		map.put("key", IotConfig.mobileKey);
 		String tt = (int)(new Date().getTime()/1000)+"";
 		map.put("timestamp", tt);
 		//序列化参数
 		String order_seri = orderParam(map);
 		String md5_order_seri = MD5.MD5(order_seri);
 		map.put("signature", md5_order_seri);
 		//logger.debug("参数序列化："+order_seri);
 		//logger.debug("参数序列化后MD5："+md5_order_seri);

 		String apiResult = doPost(url, map, "utf-8", null);
 		if(apiResult.contains(",\"Message\":\"Success\",")){
 			return apiResult;
 		}else{
 			throw new Exception("接口调用异常！"+apiResult);
 		}

     }

     /**
     * @Title: orderParam
     * @Description: 按a-z排序参数并序列化  作为移动接口签名
     * @param params
     * @return    参数
     * @author sy
     * @throws
     * @return String    返回类型
     *
     */
     public static String orderParam(Map<String, String> params) {
     	List<String> keys = new ArrayList<String>(params.keySet());
     	Collections.sort(keys);
     	String prestr = "";
     	for (int i = 0; i < keys.size(); i++) {
 	    	String key = keys.get(i);
 	    	String value = params.get(key);
 	    	if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
 	    		prestr = prestr + key + "=" + value;
 	    	} else {
 	    		prestr = prestr + key + "=" + value + "&";
 	    	}
     	}
     	return prestr;
     }

	public static String encode(String value) {
		if (value == null || value.length() == 0) {
			return "";
		}
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
