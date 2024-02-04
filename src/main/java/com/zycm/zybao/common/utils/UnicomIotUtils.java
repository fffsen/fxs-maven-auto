package com.zycm.zybao.common.utils;

import org.apache.commons.lang3.StringUtils;

/**  
* @ClassName: UnicomIotUtils  
* @Description: 联通物联卡接口 工具类  
* @author sy  
* @date 2019年4月1日  
*    
*/
public class UnicomIotUtils {
	
	public static Integer changeCardStatus(String status){
		
		if(StringUtils.isBlank(status)){
			return 0;
		}
		Integer type = null;
		switch (status) {
		case "ACTIVATED"://激活
			type = 3;
			break;
		case "ACTIVATION_READY"://可激活状态
			type = 1;
			break;
		case "DEACTIVATED"://停用状态
			type = 4;
			break;
		case "INVENTORY"://库存状态  就是一个没使用卡的状态
			type = 6;
			break;
		case "RETIRED"://失效
			type = 5;
			break;
		case "TEST_READY"://可测试
			type = 2;
			break;
		default:
			type = 0;//未知
			break;
		}
		return type;
	}
	
	public static Integer changeCardStatusTelecom(String status){
		
		if(StringUtils.isBlank(status)){
			return 0;
		}
		Integer type = null;
		switch (status) {
		case "4"://激活
			type = 3;
			break;
		case "1"://可激活状态
			type = 1;
			break;
		case "5"://停用状态
			type = 4;
			break;
		case "6"://库存状态  就是一个没使用卡的状态
			type = 6;
			break;
		case "RETIRED"://失效
			type = 5;
			break;
		case "2"://可测试
			type = 2;
			break;
		default:
			type = 0;//未知
			break;
		}
		return type;
	}

	public static void main(String[] args) {
		System.out.println(UnicomIotUtils.changeCardStatus(null));
	}
}
