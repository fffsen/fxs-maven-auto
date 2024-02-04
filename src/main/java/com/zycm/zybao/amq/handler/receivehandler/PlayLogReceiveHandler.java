package com.zycm.zybao.amq.handler.receivehandler;

import com.zycm.zybao.amq.handler.BaseHandler;
import com.zycm.zybao.dao.MediaPlayLogDao;
import com.zycm.zybao.model.entity.MediaPlayLogModel;
import com.zycm.zybao.model.mqmodel.task.playlog.PlayLogList;
import com.zycm.zybao.model.mqmodel.task.playlog.PlayLogTask;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* @ClassName: ClientFileResposeHandler
* @Description: 接收终端文件信息  处理类
* @author sy
* @date 2017年11月10日 上午11:53:44
*
*/
@Slf4j
@Component("playLogReceiveHandler")
public class PlayLogReceiveHandler implements BaseHandler{

	@Autowired(required = false)
	private MediaPlayLogDao mediaPlayLogDao;

	@Override
	public void processor(Object message, Message msg) {
		PlayLogTask playLogTask = null;
		Map<String, Class> classMap = new HashMap<String, Class>();
		try {
	 		classMap.put("playLogList", PlayLogList.class);
	 		playLogTask = (PlayLogTask) JSONObject.toBean(JSONObject.fromObject(message), PlayLogTask.class,classMap);
		} catch (Exception e) {
			log.error("【"+message+"】转换成playLogTask json对象异常！");
			return;
		}

		if(null != playLogTask){
			if(StringUtils.isNotBlank(playLogTask.getMachineCode()) && playLogTask.getPlayLogList().size() > 0){
				Integer[] programIds = new Integer[playLogTask.getPlayLogList().size()];
				List<MediaPlayLogModel> list = new ArrayList<MediaPlayLogModel>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				//SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
				Date date = new Date();
				String currentDay = sdf.format(date);
				String materialIds = "";
				for (int i = 0; i < programIds.length; i++) {
					//只处理当天的播放日志
					if(currentDay.equals(playLogTask.getPlayLogList().get(i).getLogDate())){
						programIds[i] = playLogTask.getPlayLogList().get(i).getProgramId();
						MediaPlayLogModel mediaPlayLogModel = new MediaPlayLogModel();
						String playLogId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
						mediaPlayLogModel.setPlayLogId(playLogId);
						mediaPlayLogModel.setMachineCode(playLogTask.getMachineCode());
						mediaPlayLogModel.setProgramId(playLogTask.getPlayLogList().get(i).getProgramId());
						mediaPlayLogModel.setProgramName(playLogTask.getPlayLogList().get(i).getProgramName());
						mediaPlayLogModel.setPlayTime(playLogTask.getPlayLogList().get(i).getPlaytime());
						mediaPlayLogModel.setUpdateTime(date);
						mediaPlayLogModel.setLogDate(date);
						//去重
						if(!materialIds.contains(","+playLogTask.getPlayLogList().get(i).getProgramId())){
							list.add(mediaPlayLogModel);
							materialIds += ","+playLogTask.getPlayLogList().get(i).getProgramId();
						}
					}
				}

				if(list.size() == 0){
					return;
				}
				//删除当天中的指定节目的记录
				mediaPlayLogDao.deleteByMachineCodeForDay(playLogTask.getMachineCode(), programIds,sdf.format(date));
				//批量增加播放记录
				mediaPlayLogDao.batchInsert(list);

				log.debug("###########"+Thread.currentThread().getName()+"接收终端播放日志消息：机器码【"+playLogTask.getMachineCode()
					+"】 总日志数据 "+playLogTask.getPlayLogList().size()+";当日数据 "+list.size());
			}else{
				log.error("【"+(StringUtils.isNotBlank(playLogTask.getMachineCode())?playLogTask.getMachineCode():"")+"】播放日志中机器码为空或播放日志记录为空！");
			}
		}else{
			log.error("接收播放日志信息为空！");
		}
	}


}
