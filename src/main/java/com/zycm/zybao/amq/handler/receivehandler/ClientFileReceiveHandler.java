package com.zycm.zybao.amq.handler.receivehandler;

import com.zycm.zybao.amq.handler.BaseHandler;
import com.zycm.zybao.model.mqmodel.respose.clientfile.ClientFileList;
import com.zycm.zybao.model.mqmodel.respose.clientfile.ClientFileRespose;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
* @ClassName: ClientFileResposeHandler
* @Description: 接收终端文件信息  处理类
* @author sy
* @date 2017年11月10日 上午11:53:44
*
*/
@Slf4j
@Component
public class ClientFileReceiveHandler implements BaseHandler{

	public static ConcurrentHashMap<String, List<ClientFileList>> clientFileConcurrentHashMap = new ConcurrentHashMap<String, List<ClientFileList>>();

	@Override
	public void processor(Object message, Message msg) {
		ClientFileRespose clientFileRespose = null;
		Map<String, Class> classMap = new HashMap<String, Class>();
		try {
	 		classMap.put("clientFileList", ClientFileList.class);
	 		clientFileRespose = (ClientFileRespose) JSONObject.toBean(JSONObject.fromObject(message), ClientFileRespose.class,classMap);
		} catch (Exception e) {
			log.error("【"+message+"】转换成ClientFileRespose json对象异常！");
			return;
		}
		//保存终端的 文件信息
	 	clientFileConcurrentHashMap.put(clientFileRespose.getMachineCode(),clientFileRespose.getClientFileList());
	}

}
