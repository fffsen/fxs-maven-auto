package com.zycm.zybao.model.vo;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class FtpLoginVo implements Serializable{

	public String ip;
	public String port;
	public String user;
	public String pwd;

	public long time;

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public static boolean isEmpty(FtpLoginVo ftpLoginVo){
		if(StringUtils.isBlank(ftpLoginVo.getIp()) || StringUtils.isBlank(ftpLoginVo.getPort())
				|| StringUtils.isBlank(ftpLoginVo.getUser()) || StringUtils.isBlank(ftpLoginVo.getPwd())){
			return false;
		}else{
			return true;
		}
	}

	public static FtpLoginVo toObject(HttpServletRequest request){
		String ip = request.getParameter("ip");
		String port = request.getParameter("port");
		String user = request.getParameter("user");
		String pwd = request.getParameter("pwd");
		FtpLoginVo ftpLoginVo = new FtpLoginVo();
		ftpLoginVo.setIp(ip);
		ftpLoginVo.setPort(port);
		ftpLoginVo.setUser(user);
		ftpLoginVo.setPwd(pwd);
		return ftpLoginVo;

	}

}
