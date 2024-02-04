package com.zycm.zybao.service.task;

import com.zycm.zybao.amq.handler.BaseHandler;
import com.zycm.zybao.common.config.IotConfig;
import com.zycm.zybao.common.redis.RedisHandle;
import com.zycm.zybao.common.utils.HttpClientUtil;
import com.zycm.zybao.common.utils.MobileIotUtils;
import com.zycm.zybao.common.utils.UnicomIotUtils;
import com.zycm.zybao.dao.IotMediaAttributeDao;
import com.zycm.zybao.iot.telecom.TelecomIotHandler;
import com.zycm.zybao.model.entity.IotMediaAttributeModel;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @ClassName: RefreshMediaIotTask
* @Description: 刷新物联卡流量信息任务
* @author sy
* @date 2019年3月28日
*
*/
@Slf4j
@Component
public class RefreshMediaIotTask {
	@Autowired(required = false)
	private IotMediaAttributeDao iotMediaAttributeDao;
	@Autowired(required = false)
	private RedisHandle redisHandle;

	private TelecomIotHandler telecomIotHandler = new TelecomIotHandler();
	/**
	* @Title: syncIotInfo
	* @Description: 同步redis与数据库的媒体机的状态、接收时间、节目数、终端编号、ip信息
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	@Scheduled(fixedRate = 60000)
	public void syncIotInfo(){
		//只有触发广告发布的行为时  隔半小时后才会去更新流量数据
		Date d1 = new Date();
		Object time = redisHandle.get("refresh_iot");
		if(time == null || (d1.getTime() - (long)time) < 30*60*1000){//每分钟
			return;
		}

		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//刷新联通物联卡流量数据
		//查询联通的物联卡数据
		List<IotMediaAttributeModel> unicomIotList = iotMediaAttributeDao.selectByIotType(2);
		log.debug("开始更新"+unicomIotList.size()+"个联通物联卡的流量数据>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		String unicom_error = "";
		if(unicomIotList.size() > 0){
			IotMediaAttributeModel iotMediaAttribute = new IotMediaAttributeModel();
			String authstr="";
			for (IotMediaAttributeModel iotMediaAttributeModel : unicomIotList) {
				if(StringUtils.isBlank(iotMediaAttributeModel.getIccid())){
					continue;
				}
				String unicomIotUrlCtdUsages = IotConfig.unicomCtdUsages.replace("{iccid}", iotMediaAttributeModel.getIccid());
				try {
					String unicomJsonStr = HttpClientUtil.unicomIotDoGet(unicomIotUrlCtdUsages, null,null);
					JSONObject unicomJson = JSONObject.fromObject(unicomJsonStr);
					iotMediaAttribute.setIccid(iotMediaAttributeModel.getIccid());
					iotMediaAttribute.setImsi(unicomJson.getString("imsi"));
					iotMediaAttribute.setAccessNumber(unicomJson.getString("msisdn"));
					iotMediaAttribute.setCurDataUsage(unicomJson.getLong("ctdDataUsage"));
					iotMediaAttribute.setCardStatus(UnicomIotUtils.changeCardStatus(unicomJson.getString("status")));
					iotMediaAttributeDao.updateData(iotMediaAttribute);
					//redisHandle.remove("refresh_iot");
				} catch (Exception e) {
					try {
						//如果账户1查询数据异常就去第二个账户找
						if(StringUtils.isBlank(authstr)){
							authstr = IotConfig.unicomUsername2 +":"+ IotConfig.unicomSecretkey2;
						}
						String unicomJsonStr = HttpClientUtil.unicomIotDoGet(unicomIotUrlCtdUsages, null,authstr);
						JSONObject unicomJson = JSONObject.fromObject(unicomJsonStr);
						iotMediaAttribute.setIccid(iotMediaAttributeModel.getIccid());
						iotMediaAttribute.setImsi(unicomJson.getString("imsi"));
						iotMediaAttribute.setAccessNumber(unicomJson.getString("msisdn"));
						iotMediaAttribute.setCurDataUsage(unicomJson.getLong("ctdDataUsage"));
						iotMediaAttribute.setCardStatus(UnicomIotUtils.changeCardStatus(unicomJson.getString("status")));
						iotMediaAttributeDao.updateData(iotMediaAttribute);
					} catch (Exception e2) {
						unicom_error += ","+iotMediaAttributeModel.getIccid();
						log.error("更新联通物联卡【"+iotMediaAttributeModel.getIccid()+"】失败！", e);
					}

				}
			}
		}
		log.debug("结束更新"+unicomIotList.size()+"个联通物联卡的流量数据,失败【"+(StringUtils.isNotBlank(unicom_error)?unicom_error.substring(1):0)+"】<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		//电信物联卡流量数据更新
		List<IotMediaAttributeModel> telecomIotList = iotMediaAttributeDao.selectByIotType(1);
		log.debug("开始更新"+telecomIotList.size()+"个电信物联卡的流量数据>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		String telecom_error = "";
		if(telecomIotList.size() > 0){
			IotMediaAttributeModel iotMediaAttribute = new IotMediaAttributeModel();
			//加载电信物联卡列表数据
			Map<String,JSONObject> simMap = new HashMap<String,JSONObject>();

			for (int pi = 1; pi < 100; pi++) {
				JSONObject siminfo_json = telecomIotHandler.getSIMList(pi);//电信接口有次数限制 电信接口每分钟10次
				JSONArray siminfo_arr = siminfo_json.getJSONObject("description").getJSONArray("simList");
				if(siminfo_arr.size() > 0){
					for (int td = 0; td < siminfo_arr.size(); td++) {
						simMap.put(siminfo_arr.getJSONObject(td).getString("iccid"), siminfo_arr.getJSONObject(td));
					}
				}else{
					log.warn("同步电信物联卡接口查到第"+pi+"页时列表数据为空结束接口调用！");
					break;
				}
			}
			log.debug("同步电信物联卡接口查到"+simMap.size()+"条数据！");
			for (IotMediaAttributeModel iotMediaAttributeModel2 : telecomIotList) {
				if(StringUtils.isBlank(iotMediaAttributeModel2.getIccid())){
					continue;
				}
				/*if(aa%10 == 0 && aa != 0){//控制接口访问频率 电信接口每分钟10次
					try {
						Thread.sleep(61000);
					} catch (InterruptedException e) {
						logger.error("同步电信物联卡接口暂停60s数据异常", e);
					}
					logger.error("同步电信物联卡接口暂停60s!!");
				}*/
				try {
					//电信的物联卡号为真实的物联卡去掉最后一位的字符串
					String tele_iccid = iotMediaAttributeModel2.getIccid().substring(0, iotMediaAttributeModel2.getIccid().length()-1);
					//JSONObject siminfo_json = telecomIotHandler.getSIMList(tele_iccid);
					long curDataUsage = telecomIotHandler.queryTrafficByDate(tele_iccid);
					JSONObject siminfo = simMap.get(tele_iccid);
					iotMediaAttribute.setIccid(iotMediaAttributeModel2.getIccid());
					//iotMediaAttribute.setImsi(siminfo.getString("imsi"));description
					iotMediaAttribute.setAccessNumber(siminfo.getString("accNumber"));
					iotMediaAttribute.setCurDataUsage(curDataUsage);

					iotMediaAttribute.setCardStatus(UnicomIotUtils.changeCardStatusTelecom(siminfo.getJSONArray("simStatus").getString(0)));
					iotMediaAttributeDao.updateData(iotMediaAttribute);

				} catch (Exception e) {
					telecom_error += ","+iotMediaAttributeModel2.getIccid();
					log.error("更新电信物联卡【"+iotMediaAttributeModel2.getIccid()+"】失败！", e);
				}

			}
			simMap = null;
		}
		log.debug("结束更新"+telecomIotList.size()+"个电信物联卡的流量数据,失败【"+(StringUtils.isNotBlank(telecom_error)?telecom_error.substring(1):0)+"】<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

		//移动物联卡流量数据更新
		List<IotMediaAttributeModel> mobileIotList = iotMediaAttributeDao.selectByIotType(3);
		log.debug("开始更新"+mobileIotList.size()+"个移动物联卡的流量数据>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		String mobile_error = "";
		if(mobileIotList.size() > 0){
			IotMediaAttributeModel iotMediaAttribute = new IotMediaAttributeModel();
			Map<String,String> paramm = new HashMap<String,String>();
			for (IotMediaAttributeModel iotMediaAttributeModel3 : mobileIotList) {
				if(StringUtils.isBlank(iotMediaAttributeModel3.getIccid())){
					continue;
				}
				iotMediaAttribute.setIccid(iotMediaAttributeModel3.getIccid());
				//iotMediaAttribute.setImsi(unicomJson.getString("imsi"));
				//iotMediaAttribute.setAccessNumber(unicomJson.getString("msisdn"));
				//获取流量数据
				paramm.put("card", iotMediaAttributeModel3.getIccid());
				try {
					String mobileCardGprs = HttpClientUtil.mobileIotDoPost(IotConfig.mobilePrefix +"QueryGprs", paramm);
					JSONArray gpsObj = JSONObject.fromObject(mobileCardGprs).getJSONObject("Data").getJSONArray("GPS");
					long mCurDataUsage = (long)(gpsObj.getJSONObject(0).getDouble("Used")*1024*1024);
					long mTotalDataUsage = (long)(gpsObj.getJSONObject(0).getDouble("Total")*1024*1024);
					iotMediaAttribute.setCurDataUsage(mCurDataUsage);
					iotMediaAttribute.setTotalDataUsage(mTotalDataUsage);
				} catch (Exception e) {
					mobile_error += ","+iotMediaAttributeModel3.getIccid();
					log.error("更新移动物联卡【"+iotMediaAttributeModel3.getIccid()+"】流量数据失败！", e);
					continue;
				}
				try {
					//获取卡状态
					//paramm.put("card", iotMediaAttributeModel3.getIccid());
					String mobileCardStatus = HttpClientUtil.mobileIotDoPost(IotConfig.mobilePrefix+"QueryCardStatus", paramm);
					JSONArray cardStatusObj = JSONObject.fromObject(mobileCardStatus).getJSONArray("Data");
					iotMediaAttribute.setCardStatus(MobileIotUtils.changeCardStatusMobile(cardStatusObj.getString(0)) );
				} catch (Exception e) {
					mobile_error += ","+iotMediaAttributeModel3.getIccid();
					log.error("更新移动物联卡【"+iotMediaAttributeModel3.getIccid()+"】卡状态失败！", e);
					continue;
				}

				iotMediaAttributeDao.updateData(iotMediaAttribute);
			}


		}
		log.debug("结束更新"+mobileIotList.size()+"个移动物联卡的流量数据,失败【"+(StringUtils.isNotBlank(mobile_error)?mobile_error.substring(1):0)+"】<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

		redisHandle.remove("refresh_iot");
		Date d2 = new Date();
		log.info("【物联卡更新任务】定时更新"+unicomIotList.size()+"个联通物联卡,"+telecomIotList.size()+"个电信物联卡信息,"+mobileIotList.size()+"个移动物联卡信息,开始时间"+sdf3.format(d1)+",结束时间"+sdf3.format(d2)+",耗时"+(d2.getTime()-d1.getTime())/1000+"秒");
	}

	/*public static void main(String[] args) {
		JSONObject obj = JSONObject.fromObject("{\"description\":{\"pageIndex\":\"4\",\"simList\":[{\"accNumber\":\"1064971305533\",\"iccid\":\"8986031646201611296\",\"activationTime\":\"20180322\",\"simStatus\":[\"4\"],\"custId\":\"980000036874\"},{\"accNumber\":\"1064971305633\",\"iccid\":\"8986031646201611299\",\"activationTime\":\"\",\"simStatus\":[\"1\"],\"custId\":\"980000036874\"}]},\"resultCode\":\"0\",\"resultMsg\":\"处理成功！\",\"groupTransactionId\":\"1000000190201909256834932898\"}");
		Map<String, Class> classMap = new HashMap<String, Class>();
		classMap.put("simStatus", ArrayList.class);
		JSONArray ja = obj.getJSONObject("description").getJSONArray("simList");
		//TeleSim[] tts = (TeleSim[]) JSONArray.toArray(ja, TeleSim.class);
		System.out.println(ja.size());
	}*/
}
