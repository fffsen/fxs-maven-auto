package com.zycm.zybao.service.task;

import com.zycm.zybao.amq.handler.BaseHandler;
import com.zycm.zybao.common.config.MqConfig;
import com.zycm.zybao.common.constant.Constants;
import com.zycm.zybao.common.redis.RedisHandle;
import com.zycm.zybao.common.utils.IotUtil;
import com.zycm.zybao.dao.MediaAttributeDao;
import com.zycm.zybao.model.entity.MediaAttributeModel;
import com.zycm.zybao.model.mqmodel.jolokia.ActivemqTopicSubscriptions;
import com.zycm.zybao.model.mqmodel.task.heartbeat.HeartbeatTask;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jolokia.client.J4pClient;
import org.jolokia.client.J4pClientBuilder;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pReadResponse;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
/**
* @ClassName: FixOnlineMediaTask
* @Description: 此任务用于同步mq、mysql、redis中的终端在线状态
* 相对于FixOnlineMediaTask2去掉了新上线下线的终端筛选过程  这种方式无法避免mysql与mq的在线终端一致性
* 如果手动修改了mysql的状态  那么mysql与mq的终端在线数据就不一致了
* 所以此任务就是解决此问题  以mq的数据为主导 实现同步数据  而不是mysql与mq分开来维护状态
* @author sy
* @date 2020年04月20日
*
*/
@Slf4j
@Component
public class FixOnlineMediaTask3{

	@Autowired(required = false)
	private MediaAttributeDao mediaAttributeDao;
	@Autowired(required = false)
	private RedisHandle redisHandle;

	private SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//private int isFirstNoOnline = 0;

	private String MQ_WEB_USERNAME = MqConfig.mqWebUserName;
	private String MQ_WEB_PASSWORD = MqConfig.mqWebPassword;
	private String MQ_API_URL = MqConfig.mqApiUrl;
	private String MQ_API_J4PREADREQUEST = MqConfig.mqApiRequest;
	/**
	* @Title: syncMediaInfo
	* @Description:
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	@Scheduled(fixedRate = 15000)
	public void syncMediaInfo(){
		int aa = 0;
		Date d1 = new Date();
		List<Map<String,Object>> pList = new ArrayList<Map<String,Object>>();
		List<String> one_MQList = new ArrayList<String>();
		//List<String> one_redisList = new ArrayList<String>();
		//List<String> new_online_MQList = new ArrayList<String>();
		//List<String> new_offline_MQList = new ArrayList<String>();
		//List<String> exception_online_MQList = new ArrayList<String>();
		Map<String,Object> param = new HashMap<String,Object>();
//		if(isFirstNoOnline == 0){//服务停止后 销毁不及时的处理
//			redisHandle.remove(MYSQL_ONLINE_MEDIA_REDIS_KEY);
//		}
		try {
			//获取mq中的真实
			//J4pClientBuilder cb = new J4pClientBuilder().password("zycm!%%&%**^)%^").user("zycm").url("http://m2.zycmad.com:8161/api/jolokia");
			J4pClientBuilder cb = new J4pClientBuilder().password(MQ_WEB_PASSWORD).user(MQ_WEB_USERNAME).url(MQ_API_URL);
			J4pClient j44 = cb.build();
	        J4pReadRequest req = new J4pReadRequest(MQ_API_J4PREADREQUEST);
	        J4pReadResponse resp = j44.execute(req);
		    JSONArray oo = resp.getValue("Subscriptions");

		    String methodinfo="MQ在线"+oo.size()+"个,";
		    if(oo.size() > 0 ){
		    	//获得mq的在线列表数据
		    	ActivemqTopicSubscriptions[] s2 = (ActivemqTopicSubscriptions[]) net.sf.json.JSONArray.toArray(net.sf.json.JSONArray.fromObject(oo.toJSONString()), ActivemqTopicSubscriptions.class);
		    	for (int i = 0; i < s2.length; i++) {
		    		one_MQList.add(s2[i].getMachineCode());
		    	}
		    	/*if(allOnlineMedia.size() > 0){
					for (String aom : allOnlineMedia) {
						one_redisList.add(aom.replace(ONLINE_REDIS_PREFIX, ""));
					}
		    	}*/
		    	//同步mysql数据
	    		//全部修改成离线 排除拆机、通信异常的终端
				param.put("adStatus", 0);
				param.put("machineCode", null);
				param.put("useStatus", "offline");//拆机的排除
				mediaAttributeDao.updateAdStatusByConf(param);
				//批量修改在线状态
				param.put("adStatus", 1);
				param.put("machineCode", one_MQList.toArray());
				param.put("useStatus", null);
				mediaAttributeDao.updateAdStatusByConf(param);

		    }else{
		    	//全部修改成离线
				param.put("adStatus", 0);
				param.put("machineCode", null);
				param.put("useStatus", "offline");//拆机的排除
				mediaAttributeDao.updateAdStatusByConf(param);

				//redisHandle.remove(MYSQL_ONLINE_MEDIA_REDIS_KEY);
				methodinfo += "直接更新所有终端离线";
		    }

		    //查出redis中在线的终端
			Set<String> allOnlineMedia = redisHandle.getAllKeysPrefix(Constants.ONLINE_REDIS_PREFIX);
			int forCount = 0;
			if(allOnlineMedia.size() > 0){
				//处理ip、终端名称、节目及最后通信时间
				for (String OnlineMedia : allOnlineMedia) {
					try {
						Object val = redisHandle.get(OnlineMedia);
						if(val != null && !Constants.ESTABLISH_ONLINE.equals(val.toString())){
							HeartbeatTask heartbeatTask = (HeartbeatTask) JSONObject.toBean(JSONObject.fromObject(val), HeartbeatTask.class);
							if(heartbeatTask.getIsUpdateToDB() == 1){
								Integer playProgramNum = StringUtils.isNotBlank(heartbeatTask.getPlayProgramIds())?heartbeatTask.getPlayProgramIds().split(",").length:0;

								MediaAttributeModel mediaAttributeModel = new MediaAttributeModel();
								mediaAttributeModel.setMachineCode(heartbeatTask.getMachineCode());
								mediaAttributeModel.setClientNumber(heartbeatTask.getClientNumber());
								mediaAttributeModel.setMediaIp(heartbeatTask.getIp());
								mediaAttributeModel.setAdStatus(1);
								mediaAttributeModel.setAdDelete(1);
								mediaAttributeModel.setPlayProgramNum(playProgramNum);
								mediaAttributeModel.setLastReceiveTime(heartbeatTask.getReceiveTime());
								mediaAttributeModel.setNetType(IotUtil.getNetType(heartbeatTask.getIp()));
								mediaAttributeDao.updateByCode(mediaAttributeModel);
								aa++;

								heartbeatTask.setIsUpdateToDB(0);
								Long expireTime = redisHandle.getExpireTime(Constants.ONLINE_REDIS_PREFIX+heartbeatTask.getMachineCode());
								//logger.info("key【"+ONLINE_REDIS_PREFIX+heartbeatTask.getMachineCode()+"】过期时间:"+expireTime+"秒");
								redisHandle.set(Constants.ONLINE_REDIS_PREFIX+heartbeatTask.getMachineCode(),JSONObject.fromObject(heartbeatTask).toString(), expireTime);
							}else{
								//更新最后通信时间
								Map<String,Object> pMap = new HashMap<String,Object>();
								pMap.put("machineCode", heartbeatTask.getMachineCode());
								pMap.put("lastReceiveTime", heartbeatTask.getReceiveTime());
								pList.add(pMap);

							}
						}
					} catch (Exception e) {
						log.error("定时处理【"+OnlineMedia+"】转换并更新媒体机信息异常！",e);
					}
				}

				if(pList.size() > 0){
					if(pList.size() > 100){
						//计算分几次批量修改
						forCount = (int) Math.ceil(pList.size()/100.00);
						List<Map<String,Object>> pList2 = null;
						for (int i = 0; i < forCount; i++) {
							//如果数量较大  分段进行批量修改  每100修改一次
							if(i == forCount-1){//最后一次
								pList2 = pList.subList(i*100, pList.size());
							}else{
								pList2 = pList.subList(i*100, i*100+100);
							}
							mediaAttributeDao.batchUpdateLastTime(pList2);
						}
					}else{
						//如果数量较大  分段进行批量修改  每100修改一次
						mediaAttributeDao.batchUpdateLastTime(pList);
					}
				}
			}
			methodinfo += "更新"+aa+"个终端ip、编号信息,分"+forCount+"批同步"+pList.size()+"个最后通信时间";

		    //isFirstNoOnline = 1;
			Date d2 = new Date();
			log.info("【在线状态同步任务】 定时同步终端状态,"+methodinfo+",开始时间"+sdf3.format(d1)+",结束时间"+sdf3.format(d2)+",耗时"+(d2.getTime()-d1.getTime())/1000+"秒");
		} catch (Exception e1) {
			//isFirstNoOnline = 0;
			log.error("同步终端状态数据异常！",e1);
		}
		one_MQList = null;
		//new_online_MQList = null;
		//new_offline_MQList = null;
		//exception_online_MQList = null;
		//one_redisList = null;
		param = null;
		pList = null;
	}


}
