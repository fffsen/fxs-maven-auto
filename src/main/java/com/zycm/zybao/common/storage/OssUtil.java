package com.zycm.zybao.common.storage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import com.zycm.zybao.common.config.OssConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.SimplifiedObjectMeta;
import com.zycm.zybao.common.listener.OssUploadProgressListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OssUtil {

	@Autowired(required = false)
	private OssConfig ossConfig;

	private String endpoint;

	private String accessKeyId;

	private String accessKeySecret;

	public String bucketName;

	public OSS ossClient;

	public ObjectMetadata metadata = new ObjectMetadata();

	public OssUtil(){
		this.endpoint = ossConfig.endpoint;
		this.accessKeyId = ossConfig.accessKeyId;
		this.accessKeySecret = ossConfig.accessKeySecret;
		this.bucketName = ossConfig.bucketName;
	}

	public OssUtil(String endpoint,String accessKeyId,String accessKeySecret,String bucketName){
		this.endpoint = endpoint;
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
		this.bucketName = bucketName;
	}

	/**
	* @Title: pathHandle
	* @Description: oss的文件类型地址处理   规则是起始位置不带/ 结束位置不为/ 需要转换
	* @param filePath
	* @return
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return String    返回类型
	*
	*/
	public String filePathHandle(String filePath) throws Exception {
		if(StringUtils.isNotBlank(filePath)){
			//先验证结尾
			if("/".equals(filePath.substring(filePath.length()-1))){
				throw new Exception("oss文件路径格式不正确不能是文件夹结尾"+filePath);
			}
			if("/".equals(filePath.substring(0, 1))){
				return filePath.substring(1);
			}else if(filePath.length() > 2 && "\\".equals(filePath.substring(0, 2))){
				return filePath.substring(2);
			}else{
				return filePath;
			}
		}else{
			throw new Exception("oss路径不能为空");
		}
	}

	/**
	* @Title: pathHandle
	* @Description: oss的文件类型地址处理   规则是起始位置不带/ 结束位置为/ 需要转换
	* @param
	* @return
	* @throws Exception    参数
	* @author sy
	* @throws
	* @return String    返回类型
	*
	*/
	private String dirPathHandle(String dirPath) throws Exception {
		if(StringUtils.isNotBlank(dirPath)){
			//先验证结尾
			if(!"/".equals(dirPath.substring(dirPath.length()-1))){
				throw new Exception("oss文件夹路径格式不正确"+dirPath);
			}
			//再验证起始
			if("/".equals(dirPath.substring(0, 1))){
				return dirPath.substring(1);
			}else if(dirPath.length() > 2 && "\\".equals(dirPath.substring(0, 2))){
				return dirPath.substring(2);
			}else{
				return dirPath;
			}
		}else{
			throw new Exception("oss文件夹路径不能为空");
		}
	}

	private String pathHandle(String path) throws Exception {
		if(StringUtils.isNotBlank(path)){
			//再验证起始
			if("/".equals(path.substring(0, 1))){
				return path.substring(1);
			}else if(path.length() > 2 && "\\".equals(path.substring(0, 2))){
				return path.substring(2);
			}else{
				return path;
			}
		}else{
			throw new Exception("oss资源路径不能为空");
		}
	}

	public void connect() throws Exception{
		// 创建ClientConfiguration实例，按照您的需要修改默认参数。
		ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
		// 关闭CNAME选项。
		conf.setSupportCname(true);
		// 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
		//conf.setMaxConnections(200);
		// 设置Socket层传输数据的超时时间，默认为50000毫秒。
		conf.setSocketTimeout(15000);
		// 设置建立连接的超时时间，默认为50000毫秒。
		conf.setConnectionTimeout(15000);
		// 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
		//conf.setConnectionRequestTimeout(1000);
		// 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
		//conf.setIdleConnectionTime(10000);
		// 设置失败请求重试次数，默认为3次。
		//conf.setMaxErrorRetry(5);
		// 设置是否支持将自定义域名作为Endpoint，默认支持。
		//conf.setSupportCname(true);
		// 设置是否开启二级域名的访问方式，默认不开启。
		//conf.setSLDEnabled(true);
		// 设置连接OSS所使用的协议（HTTP或HTTPS），默认为HTTP。
		//conf.setProtocol(Protocol.HTTP);
		// 设置用户代理，指HTTP的User-Agent头，默认为aliyun-sdk-java。
		//conf.setUserAgent("aliyun-sdk-java");
		// 设置代理服务器端口。
		//conf.setProxyHost("<yourProxyHost>");
		// 设置代理服务器验证的用户名。
		//conf.setProxyUsername("<yourProxyUserName>");
		// 设置代理服务器验证的密码。
		//conf.setProxyPassword("<yourProxyPassword>");
		// 设置是否开启HTTP重定向，默认开启。
		//conf.setRedirectEnable(true);
		// 设置是否开启SSL证书校验，默认开启。
		//conf.setVerifySSLEnable(true);

		// 创建OSSClient实例。
		ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, conf);
	}

	public void resumeUpload(String localFile, String remoteFilePath) throws Exception{
		remoteFilePath = filePathHandle(remoteFilePath);
		if(remoteFilePath.lastIndexOf(".") >= 0 && ".txt".equalsIgnoreCase(remoteFilePath.substring(remoteFilePath.lastIndexOf(".")))){
			//oss的txt文本上传需指定内容类型  否则终端有时无法下载
			metadata.setContentType("application/octet-stream");
			ossClient.putObject(bucketName, remoteFilePath, new File(localFile),metadata);
		}else{
			ossClient.putObject(bucketName, remoteFilePath, new File(localFile));
		}
	}

	public void deleteFile(String remoteFileName) throws Exception {
		remoteFileName = filePathHandle(remoteFileName);
		ossClient.deleteObject(bucketName, remoteFileName);
	}
	public boolean doesObjectExist(String dir) throws Exception{
		dir = pathHandle(dir);
		return ossClient.doesObjectExist(bucketName, dir);
	}

	public void createDir(String dir) throws Exception{
		dir = dirPathHandle(dir);
		/*if(StringUtils.isNotBlank(dir) && dir.substring(dir.length()).equals("/")){

		}
*/
		 boolean found = ossClient.doesObjectExist(bucketName, dir);
		 if(!found){
			 ossClient.putObject(bucketName, dir, new ByteArrayInputStream(new byte[0]));
		 }
	}

	public void deleteDir(String directoryName) throws Exception {
		directoryName = dirPathHandle(directoryName);
		ossClient.deleteObject(bucketName, directoryName);
	}

	/** 上传播放脚本文件
	    * @Title: uploadResourceFile
	    * @Description: TODO
	    * @author sy
	    * @param @param json 脚本播放json串
	    * @param @param path 资源脚本存放的路径
	    * @return void
	    * @throws
	    */
    public void uploadResourceFile(String json,String path) throws Exception{
    	path = filePathHandle(path);
    	ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(json.getBytes());
		try {
			ossClient.putObject(bucketName, path, tInputStringStream);
		} catch (Exception e) {
			log.error("上传播放脚本文件时传输流异常", e);
		} finally {
			try {
				tInputStringStream.close();
	            tInputStringStream = null;
			} catch (IOException e) {
				log.error("上传播放脚本文件时关闭流异常", e);
			}
		}
    }

    public void close() throws Exception {
    	ossClient.shutdown();
    }

    public String getMd5(String remoteFileName) throws Exception {
    	remoteFileName = filePathHandle(remoteFileName);
    	SimplifiedObjectMeta objectMeta = ossClient.getSimplifiedObjectMeta(bucketName, remoteFileName);
    	return objectMeta.getETag();
    }

    public void moveObject(String remoteFileName,String moveToDir) throws Exception {
    	remoteFileName = filePathHandle(remoteFileName);
    	moveToDir = filePathHandle(moveToDir);
    	//复制文件
    	ossClient.copyObject(bucketName, remoteFileName, bucketName, moveToDir);
    	//判断是否成功复制
    	boolean isok = ossClient.doesObjectExist(bucketName, moveToDir);
    	if(isok){
    		//删除原来位置的素材
    		ossClient.deleteObject(bucketName, remoteFileName);
    	}
    }

    public void moveDir(String remoteFileName,String moveToDir) throws Exception {
    	if(StringUtils.isNotBlank(remoteFileName) && StringUtils.isNotBlank(moveToDir)){
			//先验证结尾
			if(!"/".equals(remoteFileName.substring(remoteFileName.length()-1))){
				remoteFileName = remoteFileName+"/";
			}
			if(!"/".equals(moveToDir.substring(moveToDir.length()-1))){
				moveToDir = moveToDir+"/";
			}
			//再验证起始
			if("/".equals(remoteFileName.substring(0, 1))){
				remoteFileName = remoteFileName.substring(1);
			}else if(remoteFileName.length() > 2 && "\\".equals(remoteFileName.substring(0, 2))){
				remoteFileName = remoteFileName.substring(2);
			}
			if("/".equals(moveToDir.substring(0, 1))){
				moveToDir = moveToDir.substring(1);
			}else if(moveToDir.length() > 2 && "\\".equals(moveToDir.substring(0, 2))){
				moveToDir = moveToDir.substring(2);
			}

			//复制文件
	    	ossClient.copyObject(bucketName, remoteFileName, bucketName, moveToDir);
	    	//判断是否成功复制
	    	boolean isok = ossClient.doesObjectExist(bucketName, moveToDir);
	    	if(isok){
	    		//删除原来位置的素材
	    		ossClient.deleteObject(bucketName, remoteFileName);
	    	}
		}else{
			throw new Exception("moveDir:oss文件夹路径不能为空");
		}

    }

    public boolean dirExistFile(String dir) throws Exception {
    	dir = dirPathHandle(dir);
    	//List<Object> dirFile = new ArrayList<Object>();
    	// 构造ListObjectsRequest请求。
    	ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
    	// 设置正斜线（/）为文件夹的分隔符。
    	listObjectsRequest.setDelimiter("/");
    	// 列出fun目录下的所有文件和文件夹,必须以 / 结尾。
    	listObjectsRequest.setPrefix(dir);
    	ObjectListing listing = ossClient.listObjects(listObjectsRequest);
    	//获取目录下的所有文件
    	List<OSSObjectSummary> fileList = listing.getObjectSummaries();
    	if(fileList.size() > 0 ){
    		//去掉本身数据
    		for (OSSObjectSummary s : fileList) {
    			if(dir.equals(s.getKey())){
    				fileList.remove(s);
    				break;
    			}
    		    //System.out.println("\t" + s.getKey());
    		}
    		if(fileList.size() > 0){
    			return true;
    		}
    	}
    	//获取目录下的所有子文件夹
    	List<String> dirFileList = listing.getCommonPrefixes();
    	if(dirFileList.size() > 0){
    		return true;
    	}
    	return false;
    }

    public InputStream downloadStream (String remoteFile) throws Exception {
    	remoteFile = filePathHandle(remoteFile);
    	OSSObject ossObject = ossClient.getObject(bucketName, remoteFile);
    	return ossObject.getObjectContent();
    }

	public static void main(String[] args) {
	}
}
