package com.zycm.zybao.common.exception;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: sy
 * @create: 2023-04-27 16:28
 */
@Configuration
public class SimpleMappingException extends SimpleMappingExceptionResolver {

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,
                                              Object handler, Exception ex) {
        String requestType = request.getHeader("X-Requested-With");
        //指定自定义的认证  授权状态码  便于页面处理
        if(ex instanceof org.apache.shiro.authz.UnauthenticatedException){
            response.setStatus(800);
			/* if(!"XMLHttpRequest".equals(requestType)){//费ajax请求
				return new ModelAndView(new RedirectView("http://192.168.2.234:8080/"));
			 }*/
            return new ModelAndView();
        }
        if(ex instanceof org.apache.shiro.authz.UnauthorizedException){
            response.setStatus(801);
            return new ModelAndView();
			 /*if(!"XMLHttpRequest".equals(requestType)){//费ajax请求
			     return new ModelAndView("error/noPerms.jsp");
			 }*/
        }
        return super.doResolveException(request, response, handler, ex);
    }


}
