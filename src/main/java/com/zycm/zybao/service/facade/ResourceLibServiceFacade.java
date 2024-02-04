package com.zycm.zybao.service.facade;

import com.aliyun.oss.model.PutObjectRequest;
import com.zycm.zybao.common.config.SysConfig;
import com.zycm.zybao.common.listener.FileUploadListener;
import com.zycm.zybao.common.listener.OssUploadProgressListener;
import com.zycm.zybao.common.storage.EdtftpUtil;
import com.zycm.zybao.common.storage.OssUtil;
import com.zycm.zybao.model.upload.ProgressModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;

/**
 * 素材库管理工具类
 */
@Slf4j
@Component("resourceLibAdapter")
public class ResourceLibServiceFacade {

	@Autowired(required = false)
	public EdtftpUtil edtftpUtil;

	@Autowired(required = false)
	public OssUtil ossUtil;

	public final static String FTP_MODEL = SysConfig.sysFtpModel;

	/**
	* @Title: connect
	* @Description: 连接服务的方法
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	public void connect() throws Exception {
		if("oss".equals(FTP_MODEL)){
			ossUtil.connect();
		}else{
			edtftpUtil.connect();
		}
	}

	/**
	* @Title: resumeUpload
	* @Description: 单个文件上传
	* @param localFile
	* @param remoteFilePath
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	public void resumeUpload(String localFile, String remoteFilePath) throws Exception{
		if("oss".equals(FTP_MODEL)){
			ossUtil.resumeUpload(localFile, remoteFilePath);
		}else{
			edtftpUtil.resumeUpload(localFile, remoteFilePath);
		}
	}

	/**
	* @Title: deleteFile
	* @Description: 删除素材库文件
	* @param remoteFileName
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	public void deleteFile(String remoteFileName) throws Exception {
		if("oss".equals(FTP_MODEL)){
			ossUtil.deleteFile(remoteFileName);
		}else{
			edtftpUtil.deleteFile(remoteFileName);
		}
	}

	/**
	* @Title: doesObjectExist
	* @Description: 判断素材、目录是否存在
	* @param remoteFileName
	* @return
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return boolean    返回类型
	*
	*/
	public boolean doesObjectExist(String remoteFileName) throws Exception {
		if("oss".equals(FTP_MODEL)){
			return ossUtil.doesObjectExist(remoteFileName);
		}else{
			return edtftpUtil.ftp.exists(remoteFileName);
		}
	}

	/**
	* @Title: createDir
	* @Description: 创建文件夹目录
	* @param dir
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	public void createDir(String dir) throws Exception {
		if("oss".equals(FTP_MODEL)){
			ossUtil.createDir(dir);
		}else{
			edtftpUtil.createDir(dir);
		}
	}

	/**
	* @Title: deleteDir
	* @Description: 删除文件夹、目录
	* @param directoryName
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	public void deleteDir(String directoryName) throws Exception {
		if("oss".equals(FTP_MODEL)){
			ossUtil.deleteDir(directoryName);
		}else{
			edtftpUtil.deleteDir(directoryName);
		}
	}

	/**
	* @Title: uploadResourceFile
	* @Description: 上传 文本为指定的文件
	* @param json
	* @param path    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	public void uploadResourceFile(String json,String path) throws Exception{
		if("oss".equals(FTP_MODEL)){
			ossUtil.uploadResourceFile(json, path);
		}else{
			edtftpUtil.uploadResourceFile(json, path);
		}
	}

	/**
	* @Title: close
	* @Description: 关闭上传服务
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	public void close() throws Exception {
		if("oss".equals(FTP_MODEL)){
			ossUtil.close();
		}else{
			if(edtftpUtil.isConnected()){
				edtftpUtil.close();
			}
		}
	}

	/**
	* @Title: getMd5
	* @Description: 获取素材库指定文件的md5值
	* @param remoteFileName
	* @return
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return String    返回类型
	*
	*/
	public String getMd5(String remoteFileName) throws Exception {
		if("oss".equals(FTP_MODEL)){
			return ossUtil.getMd5(remoteFileName).toLowerCase();
		}else{
			return edtftpUtil.getMd5(remoteFileName);
		}
	}

	/**
	* @Title: moveFile
	* @Description: 移动文件到指定的目录
	* @param remoteFileName
	* @param moveToDir
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	public void moveFile(String remoteFileName,String moveToDir) throws Exception {
		if("oss".equals(FTP_MODEL)){
			ossUtil.moveObject(remoteFileName, moveToDir);
		}else{
			edtftpUtil.ftp.rename(remoteFileName, moveToDir);
		}
	}

	/**
	* @Title: moveDir
	* @Description: 修改文件夹名称
	* @param remoteFileName
	* @param moveToDir
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	public void moveDir(String remoteFileName,String moveToDir) throws Exception {
		if("oss".equals(FTP_MODEL)){
			ossUtil.moveDir(remoteFileName, moveToDir);
		}else{
			edtftpUtil.ftp.rename(remoteFileName, moveToDir);
		}
	}

	/**
	* @Title: dirExistFile
	* @Description: 判断指定目录是否存在文件及文件夹
	* @param dir
	* @return
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return boolean    返回类型  true表示存在  false表示不存在
	*
	*/
	public boolean dirExistFile(String dir) throws Exception {
		if("oss".equals(FTP_MODEL)){
			return ossUtil.dirExistFile(dir);
		}else{
			if(edtftpUtil.ftp.directoryList(dir).length == 0){
				return false;
			}else{
				return true;
			}
		}
	}

	/**
	* @Title: downloadStream
	* @Description: TODO(下载文件)
	* @param remoteFile
	* @return
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return InputStream    返回类型
	*
	*/
	public InputStream downloadStream (String remoteFile) throws Exception {
		if("oss".equals(FTP_MODEL)){
			return ossUtil.downloadStream(remoteFile);
		}else{
			return edtftpUtil.ftp.downloadStream(remoteFile);
		}
	}

	/**
	* @Title: resumeUploadProgress
	* @Description: TODO(单个文件上传并更新上传进度信息)
	* @param localFile
	* @param remoteFilePath
	* @param progressModel 上传进度信息对象
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	public void resumeUploadProgress(String localFile, String remoteFilePath, ProgressModel progressModel) throws Exception {
		if("oss".equals(FTP_MODEL)){
			remoteFilePath = ossUtil.filePathHandle(remoteFilePath);
			if(remoteFilePath.lastIndexOf(".") >= 0 && ".txt".equalsIgnoreCase(remoteFilePath.substring(remoteFilePath.lastIndexOf(".")))){
				//oss的txt文本上传需指定内容类型  否则终端有时无法下载
				ossUtil.metadata.setContentType("application/octet-stream");
				ossUtil.ossClient.putObject(new PutObjectRequest(ossUtil.bucketName, remoteFilePath, new File(localFile),ossUtil.metadata).<PutObjectRequest>withProgressListener(new OssUploadProgressListener(progressModel)));
			}else{
				ossUtil.ossClient.putObject(new PutObjectRequest(ossUtil.bucketName, remoteFilePath, new File(localFile)).<PutObjectRequest>withProgressListener(new OssUploadProgressListener(progressModel)));
			}
		}else{
			edtftpUtil.ftp.setEventListener(new FileUploadListener(progressModel));//注册监听器
			edtftpUtil.resumeUpload(localFile, remoteFilePath);//开始上传
		}
	}
}
