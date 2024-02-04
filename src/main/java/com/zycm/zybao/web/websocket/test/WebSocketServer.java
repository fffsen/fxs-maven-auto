package com.zycm.zybao.web.websocket.test;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/api/pushMessage/{userId}")
public class WebSocketServer {
 
    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    public static final Map<String, WebSocketSession> users =  new ConcurrentHashMap<String, WebSocketSession>();  //Map来存储WebSocketSession，key用用户id+会话id 即在线用户列表


    /**
     *  online number user
     */
    private static int onlineCount = 0;
    /**
     *  ConcurrentHashMap
     */
    private static ConcurrentHashMap<String,WebSocketServer> concurrentHashMap = new ConcurrentHashMap<>();
    /**
     *  WebSocket Session
     */
    private Session session;
    /**
     *  Current userId;
     */
    private String userId = "";
 
 
    /**
     * made success next
     * @param session
     * @param userId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId){
        this.session = session;
        this.userId = userId;
        if(concurrentHashMap.containsKey(userId)){
            concurrentHashMap.remove(userId);
            concurrentHashMap.put(userId,this);
        }else{
            concurrentHashMap.put(userId,this);
            addOnlineCount();
        }
        log.info("userId:"+userId+"online user num:"+getOnlineCount());
        sendMessage("link WebSocket is success!");
 
    }
 
 
    @OnMessage
    public void onMessage(String message){
        log.info("userId ："+userId+",send message:"+message+"to :other");
        if(StringUtils.isNotBlank(message)){
            Map<String,String> map = (Map<String, String>) JSONObject.parse(message);
            String toUserId = map.get("toUserId");@Controller
            @RequestMapping("/websocket")
             class WebSocketController {

                @Autowired
                private WebSocketServer webSocketServer;

                @GetMapping
                public String goIndex(){

                    return "WebSocketDemo";
                }

                @GetMapping("/index")
                public String index(){
                    return "PollingDemo";
                }


                @GetMapping("/sendAllMessage")
                @ResponseBody
                public void sendAllMessage(String msg){
                    webSocketServer.sendAllMessage(msg);
                }

                @PostMapping("/sendMoneyMessage")
                @ResponseBody
                public void sendMoneyMessage(String[] userIds,@RequestParam("msg") String msg2){
                    System.out.println("==============="+userIds);
                    webSocketServer.sendMoneyMessage(userIds,msg2);
                }
            }
            String msg = map.get("contentText");
            if(StringUtils.isNotBlank(toUserId) && concurrentHashMap.containsKey(toUserId)){
                concurrentHashMap.get(toUserId).sendMessage(msg);
            }else{
                log.error("this userId is not this System");
            }
        }
 
    }
 
    @OnError
    public void onError(Throwable error){
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }
 
    @OnClose
    public void onClose(){
        if(concurrentHashMap.containsKey(userId)){
            concurrentHashMap.remove(userId);
            subOnlineCount();
        }else{
            log.info("user downed :"+userId+"online num is:"+getOnlineCount());
        }
    }
 
 
 
 
    private void sendMessage(String message) {
        try{
            this.session.getBasicRemote().sendText(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
 
 
    private static void sendText(String message,String userId){
        log.info("send message:"+message+",to userId:"+userId);
        if(StringUtils.isNotBlank(userId) && concurrentHashMap.containsKey(userId)){
            concurrentHashMap.get(userId).sendMessage(message);
        }else {
            log.error("userId:"+userId+"is down");
        }
 
    }
 
 
    //广播
    public void sendAllMessage(String message){
        Set<Map.Entry<String, WebSocketServer>> entries = concurrentHashMap.entrySet();
        for(Map.Entry<String, WebSocketServer> enty : entries){
            enty.getValue().sendMessage("【服务器】 广播消息："+message);
        }
    }
 
    //多发
    public void sendMoneyMessage(String[] userIds,String message){
        for (String userId : userIds){
            if(concurrentHashMap.containsKey(userId)){
                concurrentHashMap.get(userId).sendMessage("【服务器】VIP定向广播："+message);
            }else{
                log.error(userId+"已断开无法通信");
            }
        }
    }
 
 
 
 
    public static synchronized int getOnlineCount(){
        return onlineCount;
    }
 
    public static synchronized void addOnlineCount(){
        log.info("调用++");
        onlineCount++;
    }
 
    public static synchronized void subOnlineCount(){
        log.info("调用--");
        onlineCount--;
    }
 
}