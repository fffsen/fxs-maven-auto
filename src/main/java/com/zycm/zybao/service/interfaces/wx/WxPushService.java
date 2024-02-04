package com.zycm.zybao.service.interfaces.wx;

public interface WxPushService {

	/**
	* @Title: sendWxMsg
	* @Description: 发送预警信息
	* @param openId
	* @return    参数
	* @author sy
	* @throws
	* @return boolean    返回类型
	*
	*/
	boolean sendWarnMsg(String openId,String msg);

	/**
	* @Title: chkSignature
	* @Description:验证签名 验证消息的确来自微信服务器.
	* @param timestamp
	* @param nonce
	* @param signature
	* @return    参数
	* @author sy
	* @throws
	* @return boolean    返回类型
	*
	*/
	boolean chkSignature(String timestamp, String nonce, String signature);
}
