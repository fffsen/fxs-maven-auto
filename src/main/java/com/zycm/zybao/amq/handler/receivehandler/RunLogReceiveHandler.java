package com.zycm.zybao.amq.handler.receivehandler;

import com.zycm.zybao.amq.handler.BaseHandler;
import com.zycm.zybao.common.config.RedisConfig;
import com.zycm.zybao.common.constant.Constants;
import com.zycm.zybao.common.redis.RedisHandle;
import com.zycm.zybao.dao.MediaRunLogDao;
import com.zycm.zybao.model.entity.MediaRunLogModel;
import com.zycm.zybao.model.mqmodel.task.runlog.RunLogList;
import com.zycm.zybao.model.mqmodel.task.runlog.RunLogTask;
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
@Component("runLogReceiveHandler")
public class RunLogReceiveHandler implements BaseHandler{

	@Autowired(required = false)
	private MediaRunLogDao mediaRunLogDao;
	@Autowired(required = false)
	private RedisHandle redisHandle;

	private String redis_prefix = RedisConfig.redisPrefix;

	private String LOG_REDIS_PREFIX = "log_" + redis_prefix;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void processor(Object message, Message msg) {
		RunLogTask runLogTask = null;
		Map<String, Class> classMap = new HashMap<String, Class>();
		try {
	 		classMap.put("runLogList", RunLogList.class);
	 		runLogTask = (RunLogTask) JSONObject.toBean(JSONObject.fromObject(message), RunLogTask.class,classMap);
		} catch (Exception e) {
			log.error("【"+message+"】转换成RunLogTask json对象异常！");
			return;
		}

		if(null != runLogTask){
			if(StringUtils.isNotBlank(runLogTask.getMachineCode()) && !runLogTask.getMachineCode().endsWith("test") && runLogTask.getRunLogList().size() > 0){
				//增加运行日志
				List<MediaRunLogModel> list = new ArrayList<MediaRunLogModel>();
				Date date = new Date();
				for (int i = 0; i < runLogTask.getRunLogList().size(); i++) {
					MediaRunLogModel mediaRunLogModel = new MediaRunLogModel();
					mediaRunLogModel.setMachineCode(runLogTask.getMachineCode());
					mediaRunLogModel.setLogInfo(runLogTask.getRunLogList().get(i).getLogInfo());
					mediaRunLogModel.setLogLevel(runLogTask.getRunLogList().get(i).getLogLevel());

					/*try {
						mediaRunLogModel.setLogTime(sdf.parse(runLogTask.getRunLogList().get(i).getLogTime()));
					} catch (ParseException e) {
						logger.error("运行日志时间转换异常", e);
					}*/
					mediaRunLogModel.setLogTime(date);
					mediaRunLogModel.setCreateTime(date);

					//连续日志做合并
					String logk = LOG_REDIS_PREFIX+runLogTask.getMachineCode();
					Set<Object> logkey = redisHandle.getMapFieldKey(logk);
					if(logkey != null && logkey.size() > 0){
						String rinfo = redisHandle.getMapField(logk, "info")==null?"":redisHandle.getMapField(logk, "info").toString();
						Integer rnum = redisHandle.getMapField(logk, "num")==null?0:Integer.parseInt(redisHandle.getMapField(logk, "num").toString());
						Long rlasttime = redisHandle.getMapField(logk, "lasttime")==null?0:Long.parseLong(redisHandle.getMapField(logk, "lasttime").toString());
						Long rfirsttime = redisHandle.getMapField(logk, "firsttime")==null?0:Long.parseLong(redisHandle.getMapField(logk, "firsttime").toString());
						Integer rlogId = redisHandle.getMapField(logk, "logId")==null?0:Integer.parseInt(redisHandle.getMapField(logk, "logId").toString());

						if(!runLogTask.getRunLogList().get(i).getLogInfo().contains("终端上线")
								&& !runLogTask.getRunLogList().get(i).getLogInfo().contains("终端离线")
								&&runLogTask.getRunLogList().get(i).getLogInfo().equals(rinfo)
								&& (date.getTime() - rlasttime.longValue() < 60*60*1000)){
							rinfo = rinfo + "";
							rnum += 1;
							mediaRunLogModel.setLogInfo(runLogTask.getRunLogList().get(i).getLogInfo()+" 合并"+rnum+"条 开始时间:"+sdf.format(new Date(rfirsttime)));
							mediaRunLogModel.setRunLogId(rlogId);
							mediaRunLogDao.updateInfoByPrimaryKey(mediaRunLogModel);
							//redisHandle.addMap(logk, "info", rinfo);
							redisHandle.addMap(logk, "num", rnum);
							redisHandle.addMap(logk, "lasttime", date.getTime());
							continue;
						}
					}
					redisHandle.addMap(logk, "info", runLogTask.getRunLogList().get(i).getLogInfo());
					redisHandle.addMap(logk, "num", 1);
					redisHandle.addMap(logk, "lasttime", date.getTime());
					redisHandle.addMap(logk, "firsttime", date.getTime());
					list.add(mediaRunLogModel);

				}
				if(list.size() > 0){
					mediaRunLogDao.batchInsert(list);
					for (MediaRunLogModel mediaRunLogModel : list) {
						redisHandle.addMap(LOG_REDIS_PREFIX+mediaRunLogModel.getMachineCode(), "logId", mediaRunLogModel.getRunLogId());

						//标记播放盒插拔行为日志 预警用
						if(mediaRunLogModel.getLogInfo().contains("外置连接设备")
								|| mediaRunLogModel.getLogInfo().contains("插入U盘")
								|| mediaRunLogModel.getLogInfo().contains("HDMI设备")){
							redisHandle.addList(Constants.REDIS_ONLINE_MEDIA_PREFIX+"warn_notice_action_list_"+mediaRunLogModel.getMachineCode(),date.getTime()+"#"+mediaRunLogModel.getLogInfo());
						}
					}
				}

			}else{
				if(runLogTask.getMachineCode().endsWith("test")){
					log.error("运行日志中机器码为测试切换连接的机器码不用记录！");
				}else{
					log.error("运行日志中机器码为空或运行日志记录为空！");
				}
			}
		}else{
			log.error("接收运行日志信息为空！");
		}
	}

}
