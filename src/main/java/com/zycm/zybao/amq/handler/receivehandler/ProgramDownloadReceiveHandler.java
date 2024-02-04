package com.zycm.zybao.amq.handler.receivehandler;

import com.zycm.zybao.amq.handler.BaseHandler;
import com.zycm.zybao.common.constant.Constants;
import com.zycm.zybao.common.redis.RedisHandle;
import com.zycm.zybao.model.mqmodel.respose.publishprogram.ProgramDownloadRespose;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;

/**
* @ClassName: ClientFileResposeHandler
* @Description: 接收终端反馈的节目下载完成情况
* @author sy
* @date 2021年01月10日 上午11:53:44
*
*/
@Slf4j
@Component("programDownloadReceiveHandler")
public class ProgramDownloadReceiveHandler implements BaseHandler{

	@Autowired(required = false)
	private RedisHandle redisHandle;

	@Override
	public void processor(Object message, Message msg) {
		ProgramDownloadRespose programDownloadRespose = null;
		try {
			programDownloadRespose = (ProgramDownloadRespose) JSONObject.toBean(JSONObject.fromObject(message), ProgramDownloadRespose.class);
		} catch (Exception e) {
			log.error("【"+message+"】转换成ProgramDownloadRespose json对象异常！");
			return;
		}
		if(null != programDownloadRespose && StringUtils.isNotBlank(programDownloadRespose.getMachineCode())){
			if(StringUtils.isBlank(programDownloadRespose.getNotFinishProgramIds())){//没有未完成的节目才能到定时任务中更新状态
				//记录数据到redis中
				redisHandle.addList(Constants.REDIS_MEDIA_PROGRAM_DOWNLOAD_STATUS_KEY, programDownloadRespose.toString2());
			}
			log.debug("终端反馈节目下载完成："+message);
		}else{
			log.error("终端反馈节目下载完成时接收到的消息为空！");
		}
	}

}
