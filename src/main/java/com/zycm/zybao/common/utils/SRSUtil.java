package com.zycm.zybao.common.utils;

import com.zycm.zybao.common.config.SrsConfig;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
* @ClassName: SRSUtil
* @Description: 访问srs接口
* @author sy
* @date
*
*/
public class SRSUtil {
	private static final Logger logger = LoggerFactory.getLogger(SRSUtil.class);

	private static Long lastGetTime;
	private static JSONArray lastStreamData;

	private static final String SRSSTREAMPATH = SrsConfig.apiGetStream;
	private static final String SRSCLIENTPATH = SrsConfig.apiGetClient;
	/**
	* @Title: requestData
	* @Description:请求接口
	* @param path 接口地址
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	public static String requestData(String path,String param) throws Exception {
		String data = "";
        URL url = new URL(path);
        //打开和url之间的连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //PrintWriter out = null;
        //请求方式
        conn.setRequestMethod("POST");
        //设置通用的请求属性
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
        conn.setRequestProperty("contentType", "utf-8"); // 设置url中文参数编码
        conn.setConnectTimeout(5*1000);

        conn.setDoOutput(true);
        conn.setDoInput(true);
        //获取URLConnection对象对应的输出流
        //out = new PrintWriter(conn.getOutputStream());
        DataOutputStream out = new DataOutputStream (conn.getOutputStream());
        //发送请求参数即数据
        //out.print(param);
        if(StringUtils.isNotBlank(param))
        out.write(param.getBytes("utf-8"));
        //缓冲数据
        out.flush();
        //获取URLConnection对象对应的输入流
        InputStream is = conn.getInputStream();
        //构造一个字符流缓存
        BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
        String str = "";
        while ((str = br.readLine()) != null) {
        	data += str;
        }
        //关闭流
        is.close();

        conn.disconnect();
        return data;
    }

	public static JSONArray getStreams() throws Exception{
		//避免频繁访问请求数据 设置时差3秒内的请求 直接使用上一次的返回数据
		if(lastGetTime != null && lastStreamData != null && (System.currentTimeMillis() - lastGetTime < 3000)){
			logger.debug("获取srs直播源列表上传返回的数据");
			return lastStreamData;
		}

		//String srsStreamPath = "http://srs.zycm.cc:1985/api/v1/streams";
		//开启前先验证终端是否已被开启了录屏推流
		logger.debug("开始调用srs直播源列表接口");
		//String jsonstr = SRSUtil.requestData(SRSSTREAMPATH, null);
		String jsonstr = SRSUtil.requestData(SRSSTREAMPATH, null);
		if(StringUtils.isNotBlank(jsonstr) && !jsonstr.contains("\"code\":0")){
			logger.error("调用srs直播源列表接口异常"+jsonstr);
			throw new Exception("调用srs直播源列表接口异常");
		}else{
			lastGetTime = System.currentTimeMillis();
			lastStreamData = JSONObject.fromObject(jsonstr).getJSONArray("streams");
			return lastStreamData;
		}
	}

	public static JSONArray getClients() throws Exception{
		//String srsClientsPath = "http://192.168.2.169:1985/api/v1/clients?start=0&count=1000";//start为起始位置，count为条数最小为10
		//开启前先验证终端是否已被开启了录屏推流
		//logger.debug("开始调用srs获取所有客户端信息接口");
		//String jsonstr = SRSUtil.requestData(SRSCLIENTPATH, null);
		String jsonstr = SRSUtil.requestData(SRSCLIENTPATH, null);
		if(StringUtils.isNotBlank(jsonstr) && !jsonstr.contains("\"code\":0")){
			logger.error("调用srs获取所有客户端信息接口异常"+jsonstr);
			throw new Exception("调用srs获取所有客户端信息接口异常");
		}else{
			//System.out.println("调用srs直播源列表接口异常"+jsonstr);
			return JSONObject.fromObject(jsonstr).getJSONArray("clients");
		}
	}

	public static JSONObject getStream(String name) throws Exception{
		JSONArray streamArray = SRSUtil.getStreams();
		JSONObject returnjo = null;
		for (int i = 0; i < streamArray.size(); i++) {
			String streamName = streamArray.getJSONObject(i).getString("name");
			if(streamName.equals(name)){
				returnjo = streamArray.getJSONObject(i);
				break;
			}
		}
		return returnjo;
	}
	public static JSONObject getStream(String name,String app) throws Exception{
		JSONArray streamArray = SRSUtil.getStreams();
		JSONObject returnjo = null;
		for (int i = 0; i < streamArray.size(); i++) {
			String streamName = streamArray.getJSONObject(i).getString("name");
			String streamApp = streamArray.getJSONObject(i).getString("app");
			if(streamName.equals(name) && streamApp.equals(app)){
				returnjo = streamArray.getJSONObject(i);
				break;
			}
		}
		return returnjo;
	}

	public static boolean streamStatus(String streamName) throws Exception{
		JSONObject jobj = SRSUtil.getStream(streamName);
		if(jobj != null){
			return jobj.getJSONObject("publish").getBoolean("active");
		}else{
			return false;
		}
	}

	public static boolean streamStatus(String streamName,String streamApp) throws Exception{
		JSONObject jobj = SRSUtil.getStream(streamName,streamApp);
		if(jobj != null){
			return jobj.getJSONObject("publish").getBoolean("active");
		}else{
			return false;
		}
	}

	public static void main(String[] args) {
		try {
			//System.out.println("结果："+SRSUtil.streamStatus("livestream3"));

			System.out.println("结果："+SRSUtil.getClients());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
