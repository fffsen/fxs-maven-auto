 package com.zycm.zybao.web.websocket;

 import com.zycm.zybao.common.config.RedisConfig;
 import com.zycm.zybao.common.config.TaskConfig;
 import com.zycm.zybao.common.redis.RedisHandle;
 import com.zycm.zybao.service.interfaces.MediaAttributeService;
 import com.zycm.zybao.service.interfaces.MediaRunLogService;
 import com.zycm.zybao.service.interfaces.SysUserNotifyService;
 import com.zycm.zybao.service.interfaces.UserService;
 import com.zycm.zybao.service.interfaces.wx.WxPushService;
 import com.zycm.zybao.web.BaseController;
 import lombok.extern.slf4j.Slf4j;
 import org.apache.commons.lang3.StringUtils;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.scheduling.annotation.Scheduled;
 import org.springframework.stereotype.Service;
 import org.springframework.web.socket.TextMessage;

 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;




/**
* @ClassName: WarnNoticeTask
* @Description: 定时任务  定时微信公众号推送离线次数 处理延时推送的终端（也就是高频率的信息降频率后的汇总消息推送见WarnNoticeWXTaskFacade的任务处理）
* @author sy
* @date 2020年6月23日
*
*/
@Slf4j
@Service("warnNoticeWXTaskFacade")
public class WarnNoticeWXTaskFacade {

	@Autowired(required = false)
	private RedisHandle redisHandle;

	@Autowired(required = false)
	private MediaAttributeService mediaAttributeService;

	@Autowired(required = false)
	private UserService userService;

	@Autowired(required = false)
	private MediaRunLogService mediaRunLogService;

	@Autowired(required = false)
	private WxPushService wxPushService;

	@Autowired(required = false)
	private SysUserNotifyService sysUserNotifyService;

	private final static String REDISONLINEMEDIAPREFIX = RedisConfig.redisPrefix;
	private final static Integer TASKNOTICETIMEDFF = TaskConfig.taskNoticeTimedff;
	private final static Integer TASKNOTICEFREQUENCY = TaskConfig.taskNoticeFrequency;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static String TASKNOTICESTARTTIME = TaskConfig.taskNoticeStarttime;
	private final static String TASKNOTICEENDTIME = TaskConfig.taskNoticeEtime;
	private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	private int sign=0;
	//定时刷新高频次离线终端数据

	/*@Scheduled(fixedRate = 180000)*/
	public void runTask1(){
		//判断是否在推送的时间范围内
		Date date = new Date();
		long workstart;
		long workend;
		try {
			workstart = sdf.parse(sdf2.format(date)+" "+TASKNOTICESTARTTIME).getTime();
			workend = sdf.parse(sdf2.format(date)+" "+TASKNOTICEENDTIME).getTime();
			if(date.getTime() < workstart
					|| date.getTime() > workend){
				log.debug("预警推送暂停当前免打扰时段");
				if(sign==0){
					//清空高频离线数据
					Map<String,Object> warnNoticeMap = redisHandle.getMap(REDISONLINEMEDIAPREFIX+"warn_notice_high_frequency");
					for (String machineCode : warnNoticeMap.keySet()) {
						redisHandle.removeMapField(REDISONLINEMEDIAPREFIX+"warn_notice_high_frequency", machineCode);
					}
				}
				sign=1;
				return;
			}
		} catch (ParseException e1) {
			log.error("处理预警推送时转换时间失败",e1);
		}
		sign=0;
		Map<String,String> toUserInfoMap = new HashMap<String, String>();
		Map<String,Integer> highFrequencyMediaMap = new HashMap<String, Integer>();
		Date data = new Date();
		Map<String,Object> params = new HashMap<String, Object>();
		int ad=0;
		String startTM = sdf.format(data.getTime() - TASKNOTICETIMEDFF*60*1000);
		String endTM = sdf.format(data);
		//查询前一个时间范围的频率
		params.put("startTime", startTM);
		params.put("endTime", endTM);
		List<Map<String, Object>> mediaOffCountList = mediaRunLogService.getOffCountByCondition(params);

		log.debug("开始统计高频离线终端"+startTM+"到"+endTM+"共"+mediaOffCountList.size()+"条>>>>>>>>>>");
		if(mediaOffCountList.size() > 0){
			//清空高频离线数据
			Map<String,Object> warnNoticeMap = redisHandle.getMap(REDISONLINEMEDIAPREFIX+"warn_notice_high_frequency");
			for (String machineCode : warnNoticeMap.keySet()) {
				redisHandle.removeMapField(REDISONLINEMEDIAPREFIX+"warn_notice_high_frequency", machineCode);
			}

			String allClientName="";
			//更新新数据
			for (Map<String, Object> map : mediaOffCountList) {
				String macode = map.get("machineCode").toString();
				String clientName = map.get("clientName") !=null?map.get("clientName").toString():"";
				Integer logNum = Integer.parseInt(map.get("logNum").toString());

				if(logNum.intValue() > TASKNOTICEFREQUENCY.intValue()){//超过指定阀值则认为是高频离线终端需推送
					redisHandle.addMap(REDISONLINEMEDIAPREFIX+"warn_notice_high_frequency", macode, data.getTime()+"#"+TASKNOTICETIMEDFF+"#"+logNum);
					highFrequencyMediaMap.put(macode, logNum);
					allClientName += ","+(StringUtils.isNotBlank(clientName)?clientName:macode)+"("+logNum+"次)";
				}else{
					if(warnNoticeMap.get(macode) != null){//或者前一个时间区是否做过高频过滤 如果有则现在是推送汇总数据
						highFrequencyMediaMap.put(macode, logNum);
						allClientName += ","+(StringUtils.isNotBlank(clientName)?clientName:macode)+"("+logNum+"次)";
					}
				}

			}

			if(StringUtils.isBlank(allClientName)){
				log.debug("从"+startTM+"到"+endTM+"没有超过"+TASKNOTICEFREQUENCY+"次的终端");
				return;
			}

			//根据过滤的高频离线终端机器码查询所属的用户
			//根据多个机器码查询非超级用户需预警通知的
			params.put("machineCodes", highFrequencyMediaMap.keySet().toArray());
			params.put("warnNotice", 1);//需要预警通知的
			params.put("isAdmin", 0);//非超级管理员
			List<Map<String,Object>> user_media_list = mediaAttributeService.selectNoticeUserByMachineCodes(params);
			for (Map<String,Object> user_media : user_media_list) {
				String uid = user_media.get("uid").toString();
				String mCode = user_media.get("machineCode").toString();
				String mediaGroupName = user_media.get("clientName")!=null?user_media.get("clientName").toString():"";
				//构建所有终端信息

				//构建用户消息
				if(toUserInfoMap.get(uid) != null){
					String old_v = toUserInfoMap.get(uid);
					toUserInfoMap.put(uid, old_v+","+(StringUtils.isNotBlank(mediaGroupName)?mediaGroupName:mCode)+"("+highFrequencyMediaMap.get(mCode)+"次)");
				}else{
					toUserInfoMap.put(uid, (StringUtils.isNotBlank(mediaGroupName)?mediaGroupName:mCode)+"("+highFrequencyMediaMap.get(mCode)+"次)");
				}

			}

			//公众号推送
			//开始点对点发送通知
			if(toUserInfoMap.keySet().size() > 0 ){
				for (String usid : toUserInfoMap.keySet()) {
					String msg = toUserInfoMap.get(usid);
					//公众号推送
					try {
						wxPushService.sendWarnMsg(usid.split("_")[0], msg);
					} catch (Exception e) {
						log.error("对["+usid+"]的公众号消息发送失败", e);
					}

				}
			}else{
				log.debug("用户为空不用推送！");
			}

			//开始对超管发送  先查询需要预警的
			params.put("uGroupId", BaseController.SUPERUSERGROUPID);//查询超级管理员
			List<Map<String,Object>> superUserList = userService.selectByCondition(params);
			//Map<String, WebSocketSession> users = MyWebSocketHandler.users;
			if(superUserList.size() > 0){
				ad = superUserList.size();
				//组装消息内容
				String msg1 = allClientName.substring(1);
				TextMessage mmsg = new TextMessage(msg1);
				for (Map<String, Object> superUser : superUserList) {
					String uid1 = superUser.get("uid").toString();
					//web推送
					try {
						MyWebSocketHandler.sendMessageToUser(uid1, mmsg);
					} catch (Exception e) {
						log.error("对超管推送"+uid1+"的预警信息异常！",e);
					}
					//公众号推送
					try {
						wxPushService.sendWarnMsg(uid1, mmsg.getPayload());
					} catch (Exception e) {
						log.error("超管公众号推送"+uid1+"的预警信息异常！",e);
					}
				}

			}else{
				log.debug("超管为空不用推送！");
			}

		}else{//清空高频离线数据
			Map<String,Object> warnNoticeMap = redisHandle.getMap(REDISONLINEMEDIAPREFIX+"warn_notice_high_frequency");
			for (String machineCode : warnNoticeMap.keySet()) {
				redisHandle.removeMapField(REDISONLINEMEDIAPREFIX+"warn_notice_high_frequency", machineCode);
			}
		}
		mediaOffCountList = null;
		log.info("【高频延时推送】 把"+highFrequencyMediaMap.size()+"个终端分别发送"+(toUserInfoMap!=null?toUserInfoMap.keySet().size():0)+"个用户,"+ad+"个超管发送预警信息！");
	}

}
