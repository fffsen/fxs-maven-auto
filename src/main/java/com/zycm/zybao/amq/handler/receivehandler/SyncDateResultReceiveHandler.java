package com.zycm.zybao.amq.handler.receivehandler;

import com.zycm.zybao.amq.handler.BaseHandler;
import com.zycm.zybao.common.constant.Constants;
import com.zycm.zybao.dao.MediaRunLogDao;
import com.zycm.zybao.model.mqmodel.respose.syncdate.SyncDateResultRespose;
import com.zycm.zybao.service.interfaces.MediaRunLogService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* @ClassName: ClientFileResposeHandler
* @Description: 接收终端同步时间执行结果  处理类
* @author sy
* @date 2017年11月10日 上午11:53:44
*
*/
@Slf4j
@Component("syncDateResultReceiveHandler")
public class SyncDateResultReceiveHandler implements BaseHandler{

	@Autowired(required = false)
	private MediaRunLogService mediaRunLogService;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HHmmss");
	//private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void processor(Object message, Message msg) {
		SyncDateResultRespose syncDateResultRespose = null;
		try {
			syncDateResultRespose = (SyncDateResultRespose) JSONObject.toBean(JSONObject.fromObject(message), SyncDateResultRespose.class);
		} catch (Exception e) {
			log.error("【"+message+"】转换成SyncDateRespose json对象异常！");
			return;
		}
		if(null != syncDateResultRespose && StringUtils.isNotBlank(syncDateResultRespose.getMachineCode())
				&& StringUtils.isNotBlank(syncDateResultRespose.getClientDatetime())){
			long clienttime;
			try {
				clienttime = sdf.parse(syncDateResultRespose.getClientDatetime()).getTime();
				Date date = new Date();
				long servertime = date.getTime();
				if(servertime - clienttime > 60*1000 || clienttime - servertime > 60*1000){
					/*String errorinfo = "时间同步异常终端时间["+syncDateResultRespose.getClientDatetime()+"]服务端时间["+sdf.format(date)+"]偏差值超过一分钟";
					List<MediaRunLogModel> mrlist = new ArrayList<MediaRunLogModel>();
					MediaRunLogModel mediaRunLogModel = new MediaRunLogModel();
					mediaRunLogModel.setMachineCode(syncDateResultRespose.getMachineCode());
					mediaRunLogModel.setLogInfo(SERVICE_PREFIX+errorinfo);
					mediaRunLogModel.setLogLevel(3);
					mediaRunLogModel.setLogTime(date);
					mediaRunLogModel.setCreateTime(date);
					mrlist.add(mediaRunLogModel);
					mediaRunLogDao.batchInsert(mrlist);*/
					String errorinfo = "时间同步异常,终端时间与服务端时间偏差值超过一分钟";
					mediaRunLogService.insert(syncDateResultRespose.getMachineCode(), Constants.SERVICE_PREFIX+errorinfo, 3, date);

					/*mrlist = null;
					mediaRunLogModel = null;*/
					date = null;
				}
			} catch (ParseException e) {
				log.error("处理同步时间后的执行结果异常"+message, e);
			}

		}else{
			log.error("终端接收同步服务器时间的执行结果时接收到的消息为空！"+message);
		}
	}

}
