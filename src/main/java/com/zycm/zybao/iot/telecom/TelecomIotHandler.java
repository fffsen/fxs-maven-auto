package com.zycm.zybao.iot.telecom;

import com.zycm.zybao.common.config.IotConfig;
import com.zycm.zybao.common.utils.DateUtil;
import com.zycm.zybao.common.utils.UrlRequestUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
* @ClassName: TelecomIotHandler
* @Description: 电信物联卡处理器O
* @author sy
* @date 2019年8月2日
*
*/
@Slf4j
public class TelecomIotHandler {

	private String url_prefix;
	private String user_id;

	private String password;

	private String passWordEnc;

	private String key1;
	private String key2;
	private String key3;
	private DesUtils des = new DesUtils(); //DES加密工具类实例化

	public TelecomIotHandler() {
		//初始化处理器时 处理准备工作
		url_prefix = IotConfig.telecomPrefix;
		user_id = IotConfig.telecomUserId;     //用户名
		password = IotConfig.telecomPassWord;    //密码
		String key = IotConfig.telecomKey;
		key1 = key.substring(0,3);
		key2 = key.substring(3,6);
		key3 = key.substring(6,9);
		passWordEnc = des.strEnc(password,key1,key2,key3);  //密码加密

	}

	/**
	* @Title: getSIMList
	* @Description: 获取列表数据
	* @return    参数
	* @author sy
	* @throws
	* @return JSONObject    返回的是json数据串
	*
	*/
	public JSONObject getSIMList(String iccid){
		try {
			String[] arr1 = {user_id,password,"getSIMList"};
			String sign = des.strEnc(DesUtils.naturalOrdering(arr1),key1,key2,key3); //生成sign加密值

			String param = "method=getSIMList&user_id="+user_id+"&iccid="+iccid
							+"&passWord="+passWordEnc+"&sign="+sign+"&pageIndex=1";
			String re = UrlRequestUtil.requestDataFortelecom(param, "POST");
			if(StringUtils.isNotBlank(re) && !re.contains("\"resultCode\":\"0\"")){
				log.error("调用电信的sim卡列表接口异常"+re);
				return null;
			}else{
				return JSONObject.fromObject(re);
			}
		} catch (Exception e) {
			log.error("调用电信的sim卡列表接口异常", e);
		}
		return null;
	}

	/**
	* @Title: getSIMList
	* @Description: 获取列表数据
	* @return    参数
	* @author sy
	* @throws
	* @return JSONObject    返回的是json数据串
	*
	*/
	public JSONObject getSIMList(Integer pageIndex){
		try {
			String[] arr1 = {user_id,password,"getSIMList"};
			String sign = des.strEnc(DesUtils.naturalOrdering(arr1),key1,key2,key3); //生成sign加密值

			String param = "method=getSIMList&user_id="+user_id
							+"&passWord="+passWordEnc+"&sign="+sign+"&pageIndex="+pageIndex;
			String re = UrlRequestUtil.requestDataFortelecom(param, "POST");
			if(StringUtils.isNotBlank(re) && !re.contains("\"resultCode\":\"0\"")){
				log.error("调用电信的sim卡列表接口异常"+re);
				return null;
			}else{
				return JSONObject.fromObject(re);
			}
		} catch (Exception e) {
			log.error("调用电信的sim卡列表接口异常", e);
		}
		return null;
	}
	/**
	* @Title: queryTrafficByDate
	* @Description: 获取流量信息
	* @return    参数
	* @author sy
	* @throws
	* @return JSONObject    返回的是xml
	*
	*/
	public long queryTrafficByDate(String iccid){
		try {
			String[] arr1 = {iccid,user_id,password,"queryTrafficByDate"};
			String sign = des.strEnc(DesUtils.naturalOrdering(arr1),key1,key2,key3); //生成sign加密值
			String[] se = DateUtil.getCurrentMonthStartAndEnd().split(",");

			String param = "method=queryTrafficByDate&user_id="+user_id+"&iccid="+iccid
					+"&startDate="+se[0]+"&endDate="+se[1]+"&passWord="+passWordEnc+"&sign="+sign;
			String re = UrlRequestUtil.requestDataFortelecom(param, "POST");
			if(StringUtils.isNotBlank(re) && !re.contains("<root>")){
				log.error("调用电信的流量查询接口异常"+re);
				return 0;
			}else{
				String currbytes = re.substring(re.indexOf("<TOTAL_BYTES_CNT>")+17, re.indexOf("<", re.indexOf("<TOTAL_BYTES_CNT>")+1));
				log.debug("结果："+currbytes);
				if(StringUtils.isNotBlank(currbytes) && (currbytes.contains("MB") || currbytes.contains("mb"))){
					//转化mb
					return (long)(Double.parseDouble(currbytes.substring(0,currbytes.length()-2))*1024*1024);
				}else if(StringUtils.isNotBlank(currbytes) && (currbytes.contains("KB") || currbytes.contains("kb"))){
					return (long)(Double.parseDouble(currbytes.substring(0,currbytes.length()-2))*1024);
				}else if(StringUtils.isNotBlank(currbytes) && (currbytes.contains("B") || currbytes.contains("b"))){
					return (long)Double.parseDouble(currbytes.substring(0,currbytes.length()-1));
				}else if(StringUtils.isNotBlank(currbytes) && (currbytes.contains("GB") || currbytes.contains("gb"))){
					return (long)(Double.parseDouble(currbytes.substring(0,currbytes.length()-2))*1024*1024*1024);
				}else{
					return 0;
				}
			}
		} catch (Exception e) {
			log.error("调用电信的流量查询接口异常", e);
		}
		return 0;
	}

	/*public static void main(String[] args) {
		try {
			String iccid="89860316462016112841";
			String user_id = "0pU0G63qAIMKP9so18p10S543S87m67J";
			String passWordEnc = new DesUtils().strEnc("8P4wTTdm7gStEk81","0Jc","Hbz","2aR");

			String[] arr1 = {user_id,"8P4wTTdm7gStEk81","getSIMList"};
			String sign = new DesUtils().strEnc(DesUtils.naturalOrdering(arr1),"0Jc","Hbz","2aR"); //生成sign加密值

			//+"&iccid="+iccid.substring(0,iccid.length()-1)
			String param = "method=getSIMList&user_id="+user_id
							+"&passWord="+passWordEnc+"&sign="+sign+"&pageIndex=4";
			String re = UrlRequestUtil.requestDataFortelecom(param, "POST");
			if(StringUtils.isNotBlank(re) && !re.contains("\"resultCode\":\"0\"")){
				log.error("调用电信的sim卡列表接口异常"+re);

			}else{
				System.out.println(JSONObject.fromObject(re));
			}
		} catch (Exception e) {
			log.error("调用电信的sim卡列表接口异常", e);
		}

	}*/

}
