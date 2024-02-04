package com.zycm.zybao.amq.handler.receivehandler;

import com.zycm.zybao.amq.handler.BaseHandler;
import com.zycm.zybao.dao.MediaAttributeDao;
import com.zycm.zybao.model.entity.MediaAttributeModel;
import com.zycm.zybao.model.mqmodel.respose.downloadprogress.DownloadProgressRespose;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;

/**
* @ClassName: ClientFileResposeHandler
* @Description: 接收素材下载进度  处理类
* @author sy
* @date 2017年11月10日 上午11:53:44
*
*/
@Slf4j
@Component("downloadProgressReceiveHandler")
public class DownloadProgressReceiveHandler implements BaseHandler{

	@Autowired(required = false)
	private MediaAttributeDao mediaAttributeDao;

	@Override
	public void processor(Object message, Message msg) {
		DownloadProgressRespose downloadProgressRespose = null;
		try {
			downloadProgressRespose = (DownloadProgressRespose) JSONObject.toBean(JSONObject.fromObject(message), DownloadProgressRespose.class);
		} catch (Exception e) {
			log.error("【"+message+"】转换成DownloadProgressRespose json对象异常！");
			return;
		}
		if(null != downloadProgressRespose && StringUtils.isNotBlank(downloadProgressRespose.getMachineCode())){
			MediaAttributeModel mediaAttributeModel = new MediaAttributeModel();
			mediaAttributeModel.setMachineCode(downloadProgressRespose.getMachineCode());
			mediaAttributeModel.setDownloadProgress(downloadProgressRespose.getDownloadProgress());
			mediaAttributeDao.updateDownloadProgress(mediaAttributeModel);
		}else{
			log.error("接收素材下载进度消息为空或机器码为空！");
		}
	}

}
