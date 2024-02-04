 package com.zycm.zybao.amq.handler.receivehandler;

 import com.zycm.zybao.amq.handler.BaseHandler;
 import com.zycm.zybao.common.constant.Constants;
 import com.zycm.zybao.common.enums.MessageEvent;
 import com.zycm.zybao.common.redis.RedisHandle;
 import com.zycm.zybao.model.mqmodel.respose.clientconfintervaltask.ClientConfIntervalTaskRespose;
 import lombok.extern.slf4j.Slf4j;
 import net.sf.json.JSONObject;
 import org.apache.commons.lang3.StringUtils;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;

 import javax.jms.Message;
 import java.util.Date;

/**
* @ClassName: ClientFileResposeHandler
* @Description: 接收终端所有配置  处理类
* @author sy
* @date 2017年11月10日 上午11:53:44
*
*/
@Slf4j
@Component("clientConfIntervalTaskReceiveHandler")
public class ClientConfIntervalTaskReceiveHandler implements BaseHandler{

	@Autowired(required = false)
	private RedisHandle redisHandle;


	@Override
	public void processor(Object message, Message msg) {
		ClientConfIntervalTaskRespose clientConfIntervalTaskRespose = null;
		try {
			clientConfIntervalTaskRespose = (ClientConfIntervalTaskRespose) JSONObject.toBean(JSONObject.fromObject(message), ClientConfIntervalTaskRespose.class);
		} catch (Exception e) {
			log.error("【"+message+"】转换成ClientConfIntervalTaskRespose json对象异常！");
			return;
		}

		if(null != clientConfIntervalTaskRespose){
			if(StringUtils.isNotBlank(clientConfIntervalTaskRespose.getMachineCode())){
				Date date = new Date();
				//更新录播状态  redis中的录播状态(1开启中 2已开启 3开启失败 11关闭中 12已关闭 13关闭失败 0未知)
				Object val = redisHandle.getMapField(Constants.LIVE_STREAM_REDIS_KEY, clientConfIntervalTaskRespose.getMachineCode());
				if(val != null && val != ""){
					try {
						int oldRecordScreenStatus = Integer.parseInt(val.toString().split("_")[0]);
						long lastUpdateTime = Long.parseLong(val.toString().split("_")[1]);
						//判断  如果是1开启中和11关闭中 的2种状态 如果时间差没超过2分钟  则不能修改状态 因为有可能服务端还没收到终端的反馈信息
						if((oldRecordScreenStatus == 1 || oldRecordScreenStatus == 11) && (date.getTime() - lastUpdateTime > 2*60*1000)){
							redisHandle.addMap(Constants.LIVE_STREAM_REDIS_KEY, clientConfIntervalTaskRespose.getMachineCode(), clientConfIntervalTaskRespose.getRecordScreenStatus()+"_"+date.getTime());
						}else if(oldRecordScreenStatus == 2 || oldRecordScreenStatus == 12 || oldRecordScreenStatus == 0){
							redisHandle.addMap(Constants.LIVE_STREAM_REDIS_KEY, clientConfIntervalTaskRespose.getMachineCode(), clientConfIntervalTaskRespose.getRecordScreenStatus()+"_"+date.getTime());
						}
					} catch (Exception e) {
						log.error("更新录播状态信息异常！"+e.getMessage());
					}
				}else{
					redisHandle.addMap(Constants.LIVE_STREAM_REDIS_KEY, clientConfIntervalTaskRespose.getMachineCode(), clientConfIntervalTaskRespose.getRecordScreenStatus()+"_"+date.getTime());
				}

			}else{
				log.error("接收终端定时反馈的信息中机器码为空！");
			}
		}else{
			log.error("接收终端定时反馈的信息为空！");
		}
	}

}
