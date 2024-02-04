package com.zycm.zybao.amq.handler.receivehandler;

import com.zycm.zybao.amq.handler.BaseHandler;
import com.zycm.zybao.amq.topic.producer.FBTopicSender;
import com.zycm.zybao.common.enums.MessageEvent;
import com.zycm.zybao.model.mqmodel.ProtocolModel;
import com.zycm.zybao.model.mqmodel.request.syncdate.SyncDate;
import com.zycm.zybao.model.mqmodel.respose.syncdate.SyncDateRespose;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* @ClassName: ClientFileResposeHandler
* @Description: 接收终端文件信息  处理类
* @author sy
* @date 2017年11月10日 上午11:53:44
*
*/
@Slf4j
@Component("syncDateReceiveHandler")
public class SyncDateReceiveHandler implements BaseHandler{

	@Autowired(required = false)
	private FBTopicSender fBTopicSender;

	@Override
	public void processor(Object message, Message msg) {
		SyncDateRespose syncDateRespose = null;
		try {
			syncDateRespose = (SyncDateRespose) JSONObject.toBean(JSONObject.fromObject(message), SyncDateRespose.class);
		} catch (Exception e) {
			log.error("【"+message+"】转换成SyncDateRespose json对象异常！");
			return;
		}
		if(null != syncDateRespose && StringUtils.isNotBlank(syncDateRespose.getMachineCode())){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HHmmss");
			SyncDate sd = new SyncDate(sdf.format(new Date()));
			//FBTopicSender sender= new FBTopicSender();
			ProtocolModel protocolModel = new ProtocolModel();
			protocolModel.ServerToClient(MessageEvent.SYNCDATE.event, sd);
			protocolModel.setToClient(syncDateRespose.getMachineCode());
			JSONObject json = JSONObject.fromObject(protocolModel);
			log.debug("向终端发送同步时间："+json);
			fBTopicSender.sendSingleMsg(json.toString(), syncDateRespose.getMachineCode());
		}else{
			log.error("终端同步服务器时间接收到的消息为空！");
		}
	}

}
