package com.zycm.zybao.amq.listener;

import com.zycm.zybao.amq.handler.BaseHandler;
import com.zycm.zybao.common.constant.Constants;
import com.zycm.zybao.common.redis.RedisHandle;
import com.zycm.zybao.dao.MediaAttributeDao;
import com.zycm.zybao.dao.MediaRunLogDao;
import com.zycm.zybao.model.entity.MediaRunLogModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.advisory.ConsumerEvent;
import org.apache.activemq.advisory.ConsumerListener;
import org.apache.activemq.advisory.ConsumerStartedEvent;
import org.apache.commons.lang3.StringUtils;

import javax.jms.Message;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** 发布系统  消费端连接状态监控  大量终端时存在漏数据情况 所以此处只日志信息 在线状态已调整成直接获取AMQ的在线数据更准确
* @ClassName: ConsumerStatusListener
* @Description: TODO
* @author sy
* @date 2017年9月18日 上午11:21:22
*
*/
@Slf4j
public class ConsumerStatusListener implements ConsumerListener{


	/*private MediaAttributeDao mediaAttributeDao;*/

	private MediaRunLogDao mediaRunLogDao;
	private RedisHandle redisHandle;

	public ConsumerStatusListener(MediaAttributeDao mediaAttributeDao,MediaRunLogDao mediaRunLogDao,RedisHandle redisHandle) {
		super();
		/*this.mediaAttributeDao = mediaAttributeDao;*/
		this.mediaRunLogDao = mediaRunLogDao;
		this.redisHandle = redisHandle;
	}


	@Override
	public void onConsumerEvent(ConsumerEvent event) {//ConsumerInfo ConsumerEventSource  ConsumerStartedEvent ConsumerStoppedEvent
		String machineCode2 = "";
    	if(!event.isStarted()){//下线操作 修改状态
    		/*ConsumerStoppedEvent consumerStoppedEvent = (ConsumerStoppedEvent) event;*/
    		machineCode2 = BaseHandler.clientIdKey.get(event.getConsumerId().toString());
    		if(StringUtils.isBlank(machineCode2)){
    			log.error("消费者："+event.getConsumerId()+"离线时获取不到机器码");
    			return;
    		}
    		//记录登录日志
    		Date date = new Date();
			List<MediaRunLogModel> mrlist = new ArrayList<MediaRunLogModel>();
			MediaRunLogModel mediaRunLogModel = new MediaRunLogModel();
			mediaRunLogModel.setMachineCode(machineCode2);
			mediaRunLogModel.setLogInfo(Constants.SERVICE_PREFIX+"终端离线");
			mediaRunLogModel.setLogLevel(1);
			mediaRunLogModel.setLogTime(date);
			mediaRunLogModel.setCreateTime(date);
			mrlist.add(mediaRunLogModel);
			mediaRunLogDao.batchInsert(mrlist);

			//离线记录做离线通知用
			redisHandle.addMap(Constants.REDIS_ONLINE_MEDIA_PREFIX+"warn_notice", machineCode2, date.getTime());
			//redis清理
			//redisHandle.set(OFFLINE_REDIS_PREFIX+machineCode2,ESTABLISH_OFFLINE);//加入离线缓存kv
			redisHandle.remove(Constants.ONLINE_REDIS_PREFIX+machineCode2);//清理在线列表
			BaseHandler.clientIdKey.remove(event.getConsumerId().toString());
			log.debug("消费者："+event.getConsumerId()+",clientID:"+machineCode2+" 是否启动："+event.isStarted());
    	}else{
    		//上线的状态更正 与日志增加   在心跳中处理更准确
    		/*online_status.put(event.getConsumerId().toString(), 1);*/
    		ConsumerStartedEvent consumerStartedEvent = (ConsumerStartedEvent) event;
    		machineCode2 = consumerStartedEvent.getConsumerInfo().getClientId();
    		Date date = new Date();
    		//记录登录日志
			List<MediaRunLogModel> mrlist = new ArrayList<MediaRunLogModel>();
			MediaRunLogModel mediaRunLogModel = new MediaRunLogModel();
			mediaRunLogModel.setMachineCode(machineCode2);
			mediaRunLogModel.setLogInfo(Constants.SERVICE_PREFIX+"终端上线");
			mediaRunLogModel.setLogLevel(1);
			mediaRunLogModel.setLogTime(date);
			mediaRunLogModel.setCreateTime(date);
			mrlist.add(mediaRunLogModel);
			mediaRunLogDao.batchInsert(mrlist);
			//redisHandle.set(ONLINE_REDIS_PREFIX+machineCode2,ESTABLISH_ONLINE,ONLINE_REDIS_EXPIRETIME);


			BaseHandler.clientIdKey.put(event.getConsumerId().toString(),machineCode2);
			log.debug("消费者："+event.getConsumerId()+",clientID:"+machineCode2+" 是否启动："+event.isStarted());

			/*online_status.put(producerId, 0);*/
    	}
    	return;
	}

}
