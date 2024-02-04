package com.zycm.zybao.common.utils;

import org.apache.commons.lang3.StringUtils;

/**  
* @ClassName: MobileIotUtils  
* @Description: 移动物联卡工具类   第三方返回状态的说明 1:待激活 2:已激活 3:停机 4:注销 5:库存 6:可测试 7:失效
* @author sy  
* @date 2020年6月16日  
*    
*/
public class MobileIotUtils {

	/**  
	* @Title: changeCardStatusMobile  
	* @Description: 把第三方返回的状态转换成信发数据库的对应状态 
	* @param status
	* @return    参数  
	* @author sy
	* @throws 
	* @return Integer    返回类型  
	*  
	*/  
	public static Integer changeCardStatusMobile(String status){
		
		if(StringUtils.isBlank(status)){
			return 0;
		}
		Integer type = null;
		switch (status) {
		case "1"://待激活
			type = 1;
			break;
		case "2"://已激活
			type = 3;
			break;
		case "3"://停机
			type = 4;
			break;
		case "4"://注销
			type = 5;
			break;
		case "5"://库存
			type = 6;
			break;
		case "6"://可测试
			type = 2;
			break;
		case "7"://失效
			type = 5;
			break;
		default:
			type = 0;//未知
			break;
		}
		return type;
	}
	
	public static void main(String[] args) {
		System.out.println(MobileIotUtils.changeCardStatusMobile("2"));
	}
}
