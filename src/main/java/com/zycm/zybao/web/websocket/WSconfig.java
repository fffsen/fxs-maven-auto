package com.zycm.zybao.web.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class WSconfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer{

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        //springwebsocket 4.1.5版本前默认支持跨域访问，之后的版本默认不支持跨域，需要设置.setAllowedOrigins("*")
        registry.addHandler(webSocketHandler(),"/websocket/connectServer")
                .addInterceptors(new MyWebSocketHandlerInterceptor());

        registry.addHandler(webSocketHandler(), "/sockjs/connectServer")
                .addInterceptors(new MyWebSocketHandlerInterceptor()).withSockJS();
    }

    @Bean
    public TextWebSocketHandler webSocketHandler(){

        return new MyWebSocketHandler();
    }
}
