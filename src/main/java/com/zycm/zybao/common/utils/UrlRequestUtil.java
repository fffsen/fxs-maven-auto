package com.zycm.zybao.common.utils;

import com.zycm.zybao.common.config.FtpConfig;
import com.zycm.zybao.common.config.IotConfig;
import com.zycm.zybao.model.mqmodel.jolokia.ActivemqTopicSubscriptions;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jolokia.client.J4pAuthenticator;
import org.jolokia.client.J4pClient;
import org.jolokia.client.J4pClientBuilder;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pReadResponse;
import org.json.simple.JSONArray;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author sy
 * @ClassName: UrlRequestUtil
 * @Description: 访问url接口
 * @date 2019年2月26日
 */
@Slf4j
public class UrlRequestUtil {

    /**
     * @param path 接口地址
     * @param
     * @return void    返回类型
     * @throws
     * @Title: requestData
     * @Description:请求接口
     * @author sy
     */
    public static String requestData(String path, String param) throws Exception {
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
        conn.setConnectTimeout(5 * 1000);

        conn.setDoOutput(true);
        conn.setDoInput(true);
        //获取URLConnection对象对应的输出流
        //out = new PrintWriter(conn.getOutputStream());
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        //发送请求参数即数据
        //out.print(param);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(param))
            out.write(param.getBytes("utf-8"));
        //缓冲数据
        out.flush();
        //获取URLConnection对象对应的输入流
        InputStream is = conn.getInputStream();
        //构造一个字符流缓存
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String str = "";
        while ((str = br.readLine()) != null) {
            data += str;
        }
        //关闭流
        is.close();

        conn.disconnect();
        return data;
    }

    public static String requestData(String path, String param, String authstr) throws Exception {
        String data = "";
        URL url = new URL(path);
        //打开和url之间的连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //PrintWriter out = null;
        //请求方式
        conn.setRequestMethod("GET");
        //设置通用的请求属性
        //conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("ContentType", "utf-8"); // 设置url中文参数编码
        conn.setRequestProperty("Authorization", authstr);

        conn.setRequestProperty("Accept-Encoding", "gzip,deflate,br");
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,und;q=0.8");
        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setRequestProperty("Pragma", "no-cache");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");

        conn.setConnectTimeout(5 * 1000);

        conn.setDoOutput(true);
        conn.setDoInput(true);
        //获取URLConnection对象对应的输出流
        //out = new PrintWriter(conn.getOutputStream());
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        //发送请求参数即数据
        //out.print(param);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(param))
            out.write(param.getBytes("utf-8"));
        //缓冲数据
        out.flush();
        //获取URLConnection对象对应的输入流
        InputStream is = conn.getInputStream();
        //构造一个字符流缓存
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String str = "";
        while ((str = br.readLine()) != null) {
            data += str;
        }
        //关闭流
        is.close();

        conn.disconnect();
        return data;
    }

    public static String getMasterMaterialMd5(String relativepath) {
        try {
            String mastermd5 = UrlRequestUtil.requestData(FtpConfig.ftpMasterGetmaterialmd5, "relativePath=" + relativepath);
            if (StringUtils.isNotBlank(mastermd5)) {
                JSONObject jsono = JSONObject.fromObject(mastermd5);
                String val = jsono.get("data") != null ? jsono.get("data").toString() : "";
                return val;
            }
        } catch (Exception e) {
            log.error("获取主ftp的素材md5值失败！", e);
        }

        return "";
    }

    /**
     * @param method
     * @param param
     * @return
     * @return String    返回类型
     * @throws Exception 参数
     * @throws
     * @Title: requestDataForUnicom
     * @Description: 联通物联卡接口访问
     * @author sy
     */
    public static String requestDataForUnicom(String method, String authstr, String param, String type) throws Exception {
        String data = "";
        URL url = null;
        if ("GET".equalsIgnoreCase(type)) {
            //url = new URL(PropertyHandler.getUnicomIotHttpsPrefix()+method+"?"+param);"https://api.10646.cn/rws/api/v1/devices/"
            url = new URL(IotConfig.unicomPrefix + method + "?" + param);
        } else {
            //url = new URL(PropertyHandler.getUnicomIotHttpsPrefix()+method);"https://api.10646.cn/rws/api/v1/"
            url = new URL(IotConfig.unicomPrefix + method);
        }


		/*//创建SSLContext对象，并使用我们指定的信任管理器初始化
		TrustManager[] tm = {new MyX509TrustManager()};
		SSLContext sslContext = SSLContext.getInstance("SSL","SunJSSE");
		sslContext.init(null, tm, new java.security.SecureRandom());

		//从上述SSLContext对象中得到SSLSocketFactory对象
		SSLSocketFactory ssf = sslContext.getSocketFactory();*/


        //打开和url之间的连接
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        PrintWriter out = null;
        //conn.setSSLSocketFactory(ssf);
        //请求方式
        conn.setRequestMethod(type);
        //设置通用的请求属性
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", authstr);

        conn.setRequestProperty("Accept-Encoding", "gzip,deflate,br");
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,und;q=0.8");
        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setRequestProperty("Host", "api.10646.cn");
        conn.setRequestProperty("Pragma", "no-cache");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");


        conn.setRequestProperty("Connection", "Keep-Alive");
        //conn.setRequestProperty("Charset","UTF-8");
        //conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
        //conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
        conn.setRequestProperty("Referer", "https://api.10646.cn/rws/rws_embedded.html?assets=get-device-details.json&usr=&pwd=");
        //conn.setRequestProperty("assets", "get-device-details.json");
        //conn.setRequestProperty("usr", "");
        //conn.setRequestProperty("pwd", "");

        //conn.setConnectTimeout(5*1000);
        //conn.setRequestProperty("Content-Length",String.valueOf(param.length()));
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setDoInput(true);

        //获取URLConnection对象对应的输出流
        out = new PrintWriter(conn.getOutputStream());
        //发送请求参数即数据
        //if("POST".equalsIgnoreCase(type)){
        // out.print(param);
        //}
        //缓冲数据
        out.flush();
        //获取URLConnection对象对应的输入流
        InputStream is = conn.getInputStream();
        //构造一个字符流缓存
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String str = "";
        while ((str = br.readLine()) != null) {
            data += str;
        }
        //关闭流
        is.close();
        //断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
        //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
        conn.disconnect();
        return data;
    }

    /**
     * @param param
     * @param type
     * @return
     * @return String    返回类型
     * @throws Exception 参数
     * @throws
     * @Title: requestDataFortelecom
     * @Description: 电信物联卡流量数据查询
     * @author sy
     */
    public static String requestDataFortelecom(String param, String type) throws Exception {
        String data = "";
        URL url = null;
        if ("GET".equalsIgnoreCase(type)) {
            url = new URL(IotConfig.telecomPrefix+"?"+param);
            //url = new URL("http://api.ct10649.com:9001/m2m_ec/query.do?" + param);
        } else {
            url = new URL(IotConfig.telecomPrefix);
            //url = new URL("http://api.ct10649.com:9001/m2m_ec/query.do");
        }
        log.debug("同步电信物联卡请求：" + IotConfig.unicomPrefix + "?" + param);
        //打开和url之间的连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        PrintWriter out = null;
        //conn.setSSLSocketFactory(ssf);
        //请求方式
        conn.setRequestMethod(type);
        //设置通用的请求属性
        conn.setRequestProperty("Accept", "application/json");
        //conn.setRequestProperty("Authorization", authstr);

        conn.setRequestProperty("Accept-Encoding", "gzip,deflate,br");
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,und;q=0.8");
        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setRequestProperty("Host", "api.10646.cn");
        conn.setRequestProperty("Pragma", "no-cache");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");


        conn.setRequestProperty("Connection", "Keep-Alive");

        //conn.setConnectTimeout(5*1000);
        //conn.setRequestProperty("Content-Length",String.valueOf(param.length()));
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setDoInput(true);

        //获取URLConnection对象对应的输出流
        out = new PrintWriter(conn.getOutputStream());
        //发送请求参数即数据
        if ("POST".equalsIgnoreCase(type)) {
            out.print(param);
        }
        //缓冲数据
        out.flush();
        //获取URLConnection对象对应的输入流
        InputStream is = conn.getInputStream();
        //构造一个字符流缓存
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String str = "";
        while ((str = br.readLine()) != null) {
            data += str;
        }
        //关闭流
        is.close();
        //断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
        //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
        conn.disconnect();
        return data;
    }

}
