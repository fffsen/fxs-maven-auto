package com.zycm.zybao.amq.handler;

import com.zycm.zybao.common.config.RedisConfig;

import javax.jms.Message;
import java.util.concurrent.ConcurrentHashMap;
public interface BaseHandler {

	/**
	 * @Fields clientIdKey : 存放 key（通信管道连接id) => val(机器码)
	 */
	public static ConcurrentHashMap<String, String> clientIdKey = new ConcurrentHashMap<String, String>();

	public void processor(Object message, Message msg);
}
