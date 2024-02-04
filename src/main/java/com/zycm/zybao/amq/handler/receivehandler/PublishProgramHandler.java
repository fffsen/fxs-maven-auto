package com.zycm.zybao.amq.handler.receivehandler;

import com.zycm.zybao.amq.handler.BaseHandler;
import com.zycm.zybao.amq.topic.producer.FBTopicSender;
import com.zycm.zybao.common.enums.MessageEvent;
import com.zycm.zybao.common.redis.RedisHandle;
import com.zycm.zybao.dao.ProgramDao;
import com.zycm.zybao.model.mqmodel.ProtocolModel;
import com.zycm.zybao.model.mqmodel.request.publish.ProgramMsg;
import com.zycm.zybao.model.mqmodel.respose.publishprogram.PublishProgramRespose;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: PublishProgramHandler
* @Description: 校验节目单处理器
* @author sy
* @date 2017年11月21日 下午2:13:29
*
*/
@Slf4j
@Component("publishProgramHandler")
public class PublishProgramHandler implements BaseHandler{

	@Autowired(required = false)
	private ProgramDao programDao;
	@Autowired(required = false)
	private FBTopicSender fBTopicSender;
	@Autowired(required = false)
	private RedisHandle redisHandle;

	@Override
	public void processor(Object msg,Message message) {
		PublishProgramRespose publishProgramRespose = null;
		try {
			publishProgramRespose = (PublishProgramRespose) JSONObject.toBean(JSONObject.fromObject(msg), PublishProgramRespose.class);
		} catch (Exception e) {
			log.error("【"+msg+"】转换成PublishProgramRespose json对象异常！");
			return;
		}

		if( null != publishProgramRespose && StringUtils.isNotBlank(publishProgramRespose.getMachineCode())){
			//根据机器码查询所在分组的节目单  包括继承的父分组的节目
			List<ProgramMsg> programList = new ArrayList<ProgramMsg>();
			/*Map<String,Object> masterMedia = syncGroupDetailsDao.selectMasterByMachineCode(publishProgramRespose.getMachineCode());
			if(masterMedia != null){//发布节目时同步组第一次是不发布节目单指令的  只标记变动行为等定时任务来触发 因为就算这里发送节目单也是老的节目单
				Integer masterGroupId = Integer.parseInt(masterMedia.get("mediaGroupId").toString());
				Object programMsgObj = redisHandle.get(Constants.REDIS_SYNC_MASTER_PROGRAM_PREFIX+masterGroupId);
				if(programMsgObj != null){
					ProgramMsg programMsg= (ProgramMsg)JSONObject.toBean(JSONObject.fromObject(programMsgObj), ProgramMsg.class);
					programList.add(programMsg);
				}else{//第一次不发送节目单指令 等定时任务触发
					log.warn("校对同步组节目【"+masterGroupId+"】节目单还未生成");
				}
			}else{
				programList = programDao.selectProgByMachineCode(publishProgramRespose.getMachineCode());
			}*/
			programList = programDao.selectProgByMachineCode(publishProgramRespose.getMachineCode());

			//FBTopicSender sender= new FBTopicSender();
			ProtocolModel protocolModel = new ProtocolModel();
			protocolModel.ServerToClient(MessageEvent.PUBLISH.event, programList);
			protocolModel.setToClient(publishProgramRespose.getMachineCode());
			JSONObject json = JSONObject.fromObject(protocolModel);
			log.debug("校验【"+publishProgramRespose.getMachineCode()+"】的节目单发送给终端内容："+json);
			fBTopicSender.sendSingleMsg(json.toString(), publishProgramRespose.getMachineCode());

		}else{
			log.error("校验节目单数据异常，向终端发出的数据为："+msg);
		}

	}

}
