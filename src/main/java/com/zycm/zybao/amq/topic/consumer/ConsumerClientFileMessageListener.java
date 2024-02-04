package com.zycm.zybao.amq.topic.consumer;

import com.zycm.zybao.amq.handler.BaseHandler;
import com.zycm.zybao.common.enums.MessageEvent;
import com.zycm.zybao.model.mqmodel.ProtocolModel;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component("consumerClientFileMessageListener")
public class ConsumerClientFileMessageListener implements MessageListener{

	@Autowired
	private Map<String,BaseHandler> baseHandlerMap;

	private Map<String,String> messageEventMap = Arrays.stream(MessageEvent.values()).collect(Collectors.toMap(i -> i.event, i -> i.handlerName+"::"+i.name));

	@Override
	public void onMessage(Message message) {
		String msg = "";
		try {
			if(message instanceof ActiveMQBytesMessage){
				ActiveMQBytesMessage am = (ActiveMQBytesMessage) message;
				msg = new String(am.getContent().data);
			}else if(message instanceof ActiveMQTextMessage){
				ActiveMQTextMessage am = (ActiveMQTextMessage) message;
				msg = am.getText();
			}
		} catch (Exception e) {
			log.error("Message的类型不是ActiveMQBytesMessage、ActiveMQTextMessage中的一种! "+message, e);
			return;
		}

		ProtocolModel protocolModel = null;
		if(StringUtils.isNotBlank(msg)){
			//转换成协议model
			try {
				protocolModel = (ProtocolModel) JSONObject.toBean(JSONObject.fromObject(msg), ProtocolModel.class);
			} catch (Exception e) {
				log.error("json字符串【"+msg+"】转换成ProtocolModel对象异常！");
				return;
			}

			try {
				//根据消息体类型处理  只处理respose类型消息  respose消息是终端响应给服务端的消息
				if(protocolModel != null && StringUtils.isNotBlank(protocolModel.getEvent()) && "response".equals(protocolModel.getType())){
					String val = messageEventMap.get(protocolModel.getEvent());
					if(com.zycm.zybao.common.utils.StringUtils.isNotBlank(val)){
						String[] val_arr = val.split("::");
						BaseHandler baseHandler = baseHandlerMap.get(val_arr[0]);
						if(baseHandler == null){
							log.warn("key:{},val:{} 未匹配到处理类: {}",protocolModel.getEvent(),val,msg);
							return;
						}
						log.debug("【{}】接收消息格式：{}",val_arr[1],msg);
						baseHandler.processor(protocolModel.getData(), message);
					} else {
						log.warn("消息类型 {} 没有合适的解析处理类！",protocolModel.getEvent());
					}
				} else {
					log.warn("消息体存在为空或json格式缺少属性字段！");
				}

			} catch (Exception e){
				log.error("指令处理器处理异常！", e);
			}
	     }else{
	    	 log.error("接收的消息【"+message+"】为空数据！");
	     }

	}

}
