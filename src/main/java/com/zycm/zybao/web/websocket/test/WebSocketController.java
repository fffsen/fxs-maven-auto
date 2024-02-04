package com.zycm.zybao.web.websocket.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/websocket")
public class WebSocketController {
 
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