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
 import java.util.Date;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;




/**
* @ClassName: WarnNoticeTask
* @Description: 实时扫描redis 存在离线通知时 自动推送离线到前端 （不推送高频率的消息，高频率的需要降频率后推送）
* @author sy
* @date 2020年6月23日
*
*/
@Slf4j
@Service("warnNoticeTaskFacade")
public class WarnNoticeTaskFacade {

	@Autowired(required = false)
	private RedisHandle redisHandle;

	@Autowired(required = false)
	private MediaAttributeService mediaAttributeService;

	@Autowired(required = false)
	private UserService userService;

	@Autowired(required = false)
	private WxPushService wxPushService;


	private final static String REDISONLINEMEDIAPREFIX = RedisConfig.redisPrefix;
	private final static String TASKNOTICESTARTTIME = TaskConfig.taskNoticeStarttime;
	private final static String TASKNOTICEENDTIME = TaskConfig.taskNoticeEtime;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	private int sign=0;

	@Scheduled(fixedRate = 5000)
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
				sign=1;
				return;
			}
		} catch (ParseException e1) {
			log.error("处理预警推送时转换时间失败",e1);
		}


		Map<String,String> toUserInfoMap = new HashMap<String, String>();

		Map<String,Object> params = new HashMap<String, Object>();

		Map<String,Object> warnNoticeMap = redisHandle.getMap(REDISONLINEMEDIAPREFIX+"warn_notice");
		log.debug("开始读取redis中的"+(warnNoticeMap!=null?warnNoticeMap.keySet().size():0)+"条实时通知信息>>>>>>>>>>");
		int ad = 0;
		if(warnNoticeMap != null && warnNoticeMap.keySet().size() > 0){
			//处理高频离线的终端
			Object[] warnNoticeMapArr = warnNoticeMap.keySet().toArray();
			if(sign == 1){//从暂停推送过渡到推送时段时清空之前的离线数据
				for (String mc : warnNoticeMap.keySet()) {
					redisHandle.removeMapField(REDISONLINEMEDIAPREFIX+"warn_notice", mc);
				}
				sign = 0;
				return;
			}
			for (int i = 0; i < warnNoticeMapArr.length; i++) {
				String mac = (String) warnNoticeMapArr[i];
				Object val = redisHandle.getMapField(REDISONLINEMEDIAPREFIX+"warn_notice_high_frequency", mac);
				if(val != null){//高频需删除 延时推送
					redisHandle.removeMapField(REDISONLINEMEDIAPREFIX+"warn_notice", mac);
					warnNoticeMap.remove(mac);
				}
			}
			if(warnNoticeMap.keySet().size() == 0){
				log.debug("剔除高频离线终端后为空不用推送！");
				return;
			}

			String[] machineCodes = new String[warnNoticeMap.keySet().size()];
			warnNoticeMap.keySet().toArray(machineCodes);

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
					toUserInfoMap.put(uid, old_v+","+(StringUtils.isNotBlank(mediaGroupName)?mediaGroupName:mCode));
				}else{
					toUserInfoMap.put(uid, (StringUtils.isNotBlank(mediaGroupName)?mediaGroupName:mCode));
				}

			}

			//开始点对点发送通知
			if(toUserInfoMap.keySet().size() > 0 ){
				for (String usid : toUserInfoMap.keySet()) {
					String msg = toUserInfoMap.get(usid);
					try {
						MyWebSocketHandler.sendMessageToUser(usid, new TextMessage(msg));
					} catch (Exception e) {
						log.error("对["+usid+"]的消息发送失败", e);
					}
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
			if(superUserList.size() > 0){
				//组装消息内容
				String alloffclientname = "";
				List<Map<String,Object>> all_media_list = mediaAttributeService.selectGroupIdByMachineCodes(machineCodes);
				for (Map<String, Object> all_media : all_media_list) {
					String mediaGroupName = all_media.get("clientName").toString();
					alloffclientname +=","+mediaGroupName;
				}
				if(StringUtils.isBlank(alloffclientname)){
					log.debug("超管消息为空不用推送！");
					//清理redis
					for (int i = 0; i < machineCodes.length; i++) {
						redisHandle.removeMapField(REDISONLINEMEDIAPREFIX+"warn_notice", machineCodes[i]);
					}
					return;
				}
				String msg1 = alloffclientname.substring(1);
				TextMessage mmsg = new TextMessage(msg1);
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
				redisHandle.removeMapField(REDISONLINEMEDIAPREFIX+"warn_notice", machineCodes[i]);
			}

		}

		log.info("【低频实时推送】 共对"+(toUserInfoMap!=null?toUserInfoMap.keySet().size():0)+"个用户,"+ad+"个超管发送预警信息！");
	}

}
