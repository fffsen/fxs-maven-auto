 package com.zycm.zybao.web.websocket;

 import com.zycm.zybao.common.config.RedisConfig;
 import com.zycm.zybao.common.config.TaskConfig;
 import com.zycm.zybao.common.redis.RedisHandle;
 import com.zycm.zybao.service.interfaces.MediaAttributeService;
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
 import java.util.*;




/**
* @ClassName: WarnNoticeTask
* @Description: 实时扫描redis 存在异常行为日志时（插入usb、插拔HDMI） 自动推送告警到前端
* @author sy
* @date 2021年9月18日
*
*/
@Slf4j
@Service("warnNoticeActionTaskFacade")
public class WarnNoticeActionTaskFacade {

	@Autowired(required = false)
	private RedisHandle redisHandle;

	@Autowired(required = false)
	private MediaAttributeService mediaAttributeService;

	@Autowired(required = false)
	private UserService userService;

	@Autowired(required = false)
	private WxPushService wxPushService;


	private final static String REDIS_NOTICE_ACTION_PREFIX = RedisConfig.redisPrefix +"warn_notice_action_list_";
	private final static String TASKNOTICESTARTTIME = TaskConfig.taskNoticeStarttime;
	private final static String TASKNOTICEENDTIME = TaskConfig.taskNoticeEtime;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	private int sign=0;

	@Scheduled(fixedRate = 5000)
	public void runTask(){
		//判断是否在推送的时间范围内
		Date date = new Date();
		long workstart;
		long workend;
		int ad = 0;
		try {
			workstart = sdf.parse(sdf2.format(date)+" "+TASKNOTICESTARTTIME).getTime();
			workend = sdf.parse(sdf2.format(date)+" "+TASKNOTICEENDTIME).getTime();
			if(date.getTime() < workstart
					|| date.getTime() > workend){
				log.debug("当前免打扰时段异常行为预警推送暂停");
				sign=1;
				return;
			}
		} catch (ParseException e1) {
			log.error("处理预警推送时转换时间失败",e1);
		}


		Map<String,String> mediaInfoMap = new HashMap<String, String>();
		Map<String,String> toUserInfoMap = new HashMap<String, String>();
		Map<String,Object> params = new HashMap<String, Object>();

		//获取所有存在异常行为的缓存数据的key
		Set<String> warnNoticeActionKeys = redisHandle.getAllKeysPrefix(REDIS_NOTICE_ACTION_PREFIX);
		log.debug("开始读取redis中的"+(warnNoticeActionKeys!=null?warnNoticeActionKeys.size():0)+"条实时设备异常行为通知信息>>>>>>>>>>");
		if(warnNoticeActionKeys != null && warnNoticeActionKeys.size() > 0){

			for (String warnNoticeActionKey : warnNoticeActionKeys) {//循环获取每个存在异常行为的设备
				List<Object> mediaActionList = redisHandle.getList(warnNoticeActionKey);
				if(mediaActionList.size() > 0){
					String machineCode = warnNoticeActionKey.replace(REDIS_NOTICE_ACTION_PREFIX, "");
					String msg = "";
					for (int i = 0; i < mediaActionList.size(); i++) {
						String val = mediaActionList.get(i).toString();
						//处理合并连续的预警信息相同的信息 作为一次告警记录推送
						if(mediaActionList.size() > 1){//判断与下一条记录是否相同 如果相同本次不处理跳出循环执行下次循环 以此保证相同的连续消息只发送一次
							String info1 = val.split("#")[1];
							if(i+1 <= mediaActionList.size()-1){
								String info2 = mediaActionList.get(i+1).toString().split("#")[1];
								if(info1.equals(info2)){
									continue;
								}
							}
							//增加预警信息推送
							msg += ","+info1;
						}else if(mediaActionList.size() == 1){
							msg = val.split("#")[1];
						}
					}
					if(StringUtils.isNotBlank(msg)){
						mediaInfoMap.put(machineCode, msg);
					}
				}
			}
			String[] machineCodes = new String[mediaInfoMap.keySet().size()];
			mediaInfoMap.keySet().toArray(machineCodes);
			//根据多个机器码查询非超级用户需预警通知的
			params.put("machineCodes", machineCodes);
			params.put("warnNotice", 1);//需要预警通知的
			params.put("isAdmin", 0);//非超级管理员
			List<Map<String,Object>> user_media_list = mediaAttributeService.selectNoticeUserByMachineCodes(params);
			for (Map<String,Object> user_media : user_media_list) {
				String uid = user_media.get("uid").toString();
				String mCode = user_media.get("machineCode").toString();
				String mediaGroupName = user_media.get("clientName")!=null?user_media.get("clientName").toString():"";
				//构建所有终端信息

				//构建用户消息  过滤掉高频次离线数据
				if(toUserInfoMap.get(uid) != null){
					String old_v = toUserInfoMap.get(uid);
					toUserInfoMap.put(uid, old_v+","+mediaInfoMap.get(mCode).toString());
				}else{
					toUserInfoMap.put(uid, mediaInfoMap.get(mCode).toString());
				}

			}

			//开始点对点发送通知
			if(toUserInfoMap.keySet().size() > 0 ){
				for (String usid : toUserInfoMap.keySet()) {
					try {
						MyWebSocketHandler.sendMessageToUser(usid, new TextMessage(toUserInfoMap.get(usid)));
					} catch (Exception e) {
						log.error("对["+usid+"]的消息发送失败", e);
					}
					//公众号推送
					try {
						wxPushService.sendWarnMsg(usid.split("_")[0], toUserInfoMap.get(usid));
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
			if(superUserList.size() > 0){
				//组装消息内容 每个设备异常日志做汇总
				String alloffclientname = "";
				List<Map<String,Object>> all_media_list = mediaAttributeService.selectGroupIdByMachineCodes(machineCodes);
				for (Map<String, Object> all_media : all_media_list) {
					String clientName = all_media.get("clientName").toString();
					String maCode = all_media.get("machineCode").toString();

					alloffclientname +=","+clientName+"发现"+mediaInfoMap.get(maCode).toString();
				}
				if(StringUtils.isBlank(alloffclientname)){
					log.debug("超管消息为空不用推送！");
					//清理redis
					for (int i = 0; i < machineCodes.length; i++) {
						redisHandle.remove(REDIS_NOTICE_ACTION_PREFIX+machineCodes[i]);
					}
					return;
				}
				TextMessage mmsg = new TextMessage(alloffclientname.substring(1));
				for (Map<String, Object> superUser : superUserList) {
					ad++;
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

			//清理redis
			for (int i = 0; i < machineCodes.length; i++) {
				redisHandle.remove(REDIS_NOTICE_ACTION_PREFIX+machineCodes[i]);
			}
		}else{
			log.debug("未发现行为异常的终端不用推送预警通知！");
		}

		log.info("【异常行为预警推送】 共对"+(toUserInfoMap!=null?toUserInfoMap.keySet().size():0)+"个用户,"+ad+"个超管发送预警信息！");
	}

}
