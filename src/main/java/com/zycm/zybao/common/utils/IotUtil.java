package com.zycm.zybao.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
@Slf4j
public class IotUtil {
	/**
	* @Title: getIotType
	* @Description: 根据iccid来判断是属于哪个运营商的物联卡
	* @param iccid
	* @return
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return Integer    返回类型
	*
	*/
	public static Integer getIotType(String iccid) throws Exception{
		if(StringUtils.isNotBlank(iccid)){
			String prefixnum = iccid.substring(0, 6);
			//0未知 1电信 2联通 3移动
			if(prefixnum.equals("898600") || prefixnum.equals("898602") || prefixnum.equals("898604") || prefixnum.equals("898607")){
				return 3;
			}
			if(prefixnum.equals("898601") || prefixnum.equals("898606") || prefixnum.equals("898609")){
				return 2;
			}
			if(prefixnum.equals("898603") || prefixnum.equals("898611")){
				return 1;
			}
			return 0;
		}else{
			throw new Exception("判断物联卡类型时iccid为空！");
		}
	}
	//网络类型  1物联网 2兴旺网 3私有网 4公网
	public static Integer getNetType(String ip){
		try {
			if(StringUtils.isNotBlank(ip)){
				String ip_prefix1 = ip.substring(0, ip.indexOf("."));
				String ip_prefix2 = ip.substring(0, ip.indexOf(".", ip.indexOf(".")+1));
				if(ip_prefix2.equals("172.16")){
					return 2;
				}else if(ip_prefix2.equals("192.168")){
					return 3;
				}else if(ip_prefix1.equals("10")){
					return 1;
				}else{
					return 4;
				}
			}
		} catch (Exception e) {
			log.error("根据ip:"+ip+"判断网络类型异常！", e);
		}
		return 0;
	}

}
