package com.zycm.zybao.common.utils;

import org.apache.commons.lang3.StringUtils;

public class FileTypeUtil {
	//支持的视频类型
	private static final String[] mediaType = { "mp4", "avi", "mov", "wmv", "asf", "3gp", "mkv","rm","rmvb"};
	//支持的图片类型
	private static final String[] img = {"bmp","jpg", "jpeg", "png","gif","psd"};
	//支持的文本类型
	private static final String[] document = { "txt", "doc", "docx"};
	//支持的安装包类型
	private static final String[] installPackage = { "apk"};
	
	/** 
	* @Title: getFileType 
	* @Description: TODO 
	* @author sy 
	* @param @param fileName
	* @param @return    
	* @return Integer   类型： 1图片 2视频 3文本  6安装包 99其他
	* @throws 
	*/
	public static Integer getFileType(String fileName){
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
		if(StringUtils.isBlank(fileType)){
			return 99;
		}
		for (int i = 0; i < mediaType.length; i++) {
			if(mediaType[i].equals(fileType)){
				return 2;
			}
		}
		for (int i = 0; i < img.length; i++) {
			if(img[i].equals(fileType)){
				return 1;
			}
		}
		for (int i = 0; i < document.length; i++) {
			if(document[i].equals(fileType)){
				return 3;
			}
		}
		for (int i = 0; i < installPackage.length; i++) {
			if(installPackage[i].equals(fileType)){
				return 6;
			}
		}
		return 99;
	}
}
