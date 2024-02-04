package com.zycm.zybao.web.websocket;

import com.zycm.zybao.service.interfaces.wx.WxPushService;
import com.zycm.zybao.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MyWebSocketHandler extends TextWebSocketHandler {


    public static final Map<String, WebSocketSession> users =  new ConcurrentHashMap<String, WebSocketSession>();  //Map来存储WebSocketSession，key用用户id+会话id 即在线用户列表

    @Autowired(required = false)
	private WxPushService wxPushService;
    /**
     * 连接成功时候，会触发页面上onopen方法
     */
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.debug("成功建立websocket连接!");
        String userId = session.getAttributes().get("userId").toString() + "_" +session.getId();
        users.put(userId,session);
    }

    /**
     * 关闭连接时触发
     */
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        //log.debug("关闭websocket连接");
//    	if(session.isOpen()){
//            session.close();
//        }
        String userId = session.getAttributes().get("userId").toString() + "_" +session.getId();
        users.remove(userId);
        log.debug("用户"+userId+"已退出！剩余在线用户"+users.size());
    }

    /**
     * js调用websocket.send时候，会调用该方法
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);

         //收到消息，自定义处理机制，实现业务
        log.debug("服务器收到消息："+message.getPayload());
        if(message.getPayload().startsWith("#ptp#")){ //单发某人
        	String userId = session.getAttributes().get("userId").toString() + "_" +session.getId();
            sendMessageToUser(userId, message) ;
        }else if(message.getPayload().startsWith("#pta#")){
            sendMessageToUsers(message);
        }else{

        }
    }

    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){
            session.close();
        }
        log.debug("传输出现异常，关闭websocket连接... ");
        String userId = session.getAttributes().get("userId").toString() + "_" +session.getId();
        users.remove(userId);
    }

    public boolean supportsPartialMessages() {

        return false;
    }


    /**
     * 给某个用户发送消息
     *
     * @param userId
     * @param message
     */
    public static void sendMessageToUser(String userId, TextMessage message) {
    	if(StringUtils.isBlank(userId)){
    		log.error("userId为空不能对某个用户发送消息！");
    		return;
    	}
    	if(users.keySet().size() == 0){
    		log.warn("没有在线用户不用对某个用户发送消息！");
    		return;
    	}

    	//处理同用户不同方式登录session不同 但都要发送通知
		for (String usId : users.keySet()) {
			if(StringUtils.isNotBlank(usId) && userId.equals(usId.split("_")[0])){
				try {
					if (users.get(usId).isOpen()) {
		                users.get(usId).sendMessage(message);
		                log.debug("发送及时预警消息to["+usId+"]："+message.getPayload());
		            }
		        } catch (IOException e) {
		         	//发送失败 清理session
		         	log.error("发送及时预警消息to["+usId+"]失败，会话已失效需删除！"+message.getPayload(),e);
		         	users.remove(usId);
		         	try {
		         		if(users.get(usId) != null)
		         		users.get(usId).close();
					} catch (Exception e2) {
						log.error("关闭"+usId+"的会话连接异常！",e2);
					}
		        }
			}
		}

    }

    /**
     * 给某个用户发送消息
     *
     * @param message
     */
    public static void sendMessageToAdmin(TextMessage message) {
    	if(users.keySet().size() == 0){
    		log.warn("没有在线超管不用发送消息！");
    		return;
    	}

    	//处理同用户不同方式登录session不同 但都要发送通知
		for (String usId : users.keySet()) {
			if(StringUtils.isNotBlank(usId) && users.get(usId).getAttributes().get("uGroupId").toString().equals(BaseController.SUPERUSERGROUPID+"")){
				try {
					if (users.get(usId).isOpen()) {
		                users.get(usId).sendMessage(message);
		                log.debug("发送及时预警消息to["+usId+"]超管："+message.getPayload());
		            }

		        } catch (IOException e) {
		         	//发送失败 清理session
		         	log.error("发送及时预警消息to["+usId+"]超管失败，会话已失效需删除！"+message.getPayload(),e);
		         	users.remove(usId);
		         	try {
		         		if(users.get(usId) != null)
		         		users.get(usId).close();
					} catch (Exception e2) {
						log.error("关闭"+usId+"超管的会话连接异常！",e2);
					}
		        }
			}
		}

    }

    /**
     * 超管的微信推送
     *
     * @param userId
     * @param message
     */
   /* public void sendWXMessageToAdmin(String message) {

    	//处理同用户不同方式登录session不同 但都要发送通知
		for (String usId : users.keySet()) {
			if(StringUtils.isNotBlank(usId) && users.get(usId).getAttributes().get("uGroupId").toString().equals(BaseController.SUPERUSERGROUPID+"")){
				try {
					//公众号推送
					wxPushService.sendWarnMsg(usId, message);
				} catch (Exception e) {
					log.error("公众号推送"+usId+"的预警信息异常！",e);
				}
			}
		}

    }*/

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public static void sendMessageToUsers(TextMessage message) {
    	if(users.keySet().size() == 0){
    		log.warn("没有在线用户不用对所有用户发送消息！");
    		return;
    	}
        for (String userId : users.keySet()) {
        	if(StringUtils.isBlank(userId)){
        		log.error("群发时发现userId为空，删除"+userId);
        		users.remove(userId);
        		try {
        			if(users.get(userId) != null)
             		users.get(userId).close();
				} catch (Exception e2) {
					log.error("关闭"+userId+"的会话连接异常！",e2);
				}
        		continue;
        	}
            try {
            	WebSocketSession wss = users.get(userId);
                if (wss.isOpen()) {
                    wss.sendMessage(message);
                    log.debug("群发送及时预警消息to["+userId+"]："+message.getPayload());
                }
            } catch (IOException e) {
            	//发送失败 清理session
             	log.error("群发送及时预警消息to["+userId+"]失败，会话已失效需删除！"+message.getPayload(),e);
             	users.remove(userId);
             	try {
             		if(users.get(userId) != null)
             		users.get(userId).close();
				} catch (Exception e2) {
					log.error("关闭"+userId+"的会话连接异常！",e2);
				}

            }
        }
    }

}
