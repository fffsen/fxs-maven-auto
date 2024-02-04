package com.zycm.zybao.common.storage;

import com.enterprisedt.net.ftp.*;
import com.zycm.zybao.common.config.FtpConfig;
import com.zycm.zybao.common.utils.UrlRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
@Component
public class EdtftpUtil {


	    /**
	     * edtptpj的上传工具
	     */
	    public FileTransferClient ftp;

		@Autowired(required = false)
		private FtpConfig ftpConfig;
	    /**
	     * FTP IP
	     */
	    private String ip;

	    /**
	     * FTP端口号
	     */
	    private int port;

	    /**
	     * FTP用户名
	     */
	    private String username;

	    /**
	     * FTP密码
	     */
	    private String password;

	    private String encode;

	    private String connectMode;
	    /**
	     *
	     * 构造方法，初始化FTP IP、FTP端口、FTP用户名、FTP密码
	     *
	     */
	    public EdtftpUtil()
	    {
	        this.ip = ftpConfig.ftpIp;
	        this.port = ftpConfig.ftpPort;
	        this.username = ftpConfig.ftpUsername;
	        this.password = ftpConfig.ftpPassword;
	        this.encode = ftpConfig.ftpEncode;
	        this.connectMode = ftpConfig.ftpConnectMode;
	    }

		public EdtftpUtil(String ip, int port, String username, String password,String encode,String connectMode)
		{
			this.ip = ip;
			this.port = port;
			this.username = username;
			this.password = password;
			this.encode = encode;
			this.connectMode = connectMode;
		}
	/**
	     *
	     * 连接FTP
	     *
	     * @throws FTPException
	     *             FTPException
	     * @throws IOException
	     *             IOException
	     * @author XXG
	     */
	    public void connect() throws FTPException, IOException
	    {
	        ftp = new FileTransferClient();
	        ftp.setRemoteHost(ip);
	        ftp.setRemotePort(port);
	        ftp.setUserName(username);
	        ftp.setPassword(password);

	        //设置二进制方式上传
	        ftp.setContentType(FTPTransferType.BINARY);
	        //ftp.getAdvancedSettings().setControlEncoding("GBK");encode
	        ftp.getAdvancedSettings().setControlEncoding(encode);
	        if("PASV".equals(connectMode)){
	        	ftp.getAdvancedFTPSettings().setConnectMode(FTPConnectMode.PASV);
	        }else if("ACTIVE".equals(connectMode)){
	        	ftp.getAdvancedFTPSettings().setConnectMode(FTPConnectMode.ACTIVE);
	        }else{
	        	ftp.getAdvancedFTPSettings().setConnectMode(FTPConnectMode.PASV);
	        }
	        ftp.connect();
	    }

	    /**
	     *
	     * 上传本地文件到FTP服务器上，文件名与原文件名相同
	     *
	     * @param localFile
	     *            本地文件路径
	     * @param remoteFilePath
	     *            上传到FTP服务器所在目录（该目录必须已经存在） 如“/cc/img.jpg”
	     * @throws IOException
	     *             IOException
	     * @throws FTPException
	     *             FTPException
	     * @author XXG
	     *
	     */
	    public void resumeUpload(String localFile, String remoteFilePath)
	            throws FTPException, IOException
	    {
	        //上传：WriteMode.RESUME表示断点续传
	        ftp.uploadFile(localFile, remoteFilePath, WriteMode.RESUME);
	    }

	    public void createDir(String dir) throws Exception
	    {
	    	ftp.changeDirectory("/");
	        boolean existfile = ftp.exists(dir);
	        if(!existfile){
	        	ftp.createDirectory(dir);
	        }
	    }
	    /** 删除ftp上的文件
	    * @Title: deleteFile
	    * @Description: TODO
	    * @author sy
	    * @param @param remoteFileName  ftp文件路径 如“/taskpackage/img.jpg”
	    * @param @throws Exception
	    * @return void
	    * @throws
	    */
	    public void deleteFile(String remoteFileName) throws Exception
	    {
	    	ftp.deleteFile(remoteFileName);
	    }

	    /** 删除ftp上的文件夹
	    * @Title: deleteDir
	    * @Description: TODO
	    * @author sy
	    * @param @param directoryName ftp文件路径 如“/taskpackage/cc/”就会删除cc文件夹 并且文件夹中不能有文件
	    * @param @throws Exception
	    * @return void
	    * @throws
	    */
	    public void deleteDir(String directoryName) throws Exception
	    {
	    	ftp.deleteDirectory(directoryName);
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
	    public void uploadResourceFile(String json,String path){
	    	ByteArrayInputStream tInputStringStream =null;
	    	OutputStream os = null;
			try {
				os = ftp.uploadStream(path);
	            tInputStringStream = new ByteArrayInputStream(json.getBytes());
	            byte[] bytes = new byte[1024];
	            int c;
	            while ((c = tInputStringStream.read(bytes)) != -1) {
	                os.write(bytes, 0, c);
	            }
			} catch (FTPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					tInputStringStream.close();
					os.close();
		            tInputStringStream = null;
		            os = null;
				} catch (IOException e) {
					System.out.println("上传播放脚本文件时关闭流异常"+e.getMessage());
				}

			}


	    }
	    /**
	     *
	     * 关闭FTP连接
	     *
	     * @throws IOException
	     *             IOException
	     * @throws FTPException
	     *             FTPException
	     * @author XXG
	     */
	    public void close() throws FTPException, IOException
	    {
	        ftp.disconnect();
	    }

	    public String getMd5(String remoteFileName) throws Exception {
	    	return UrlRequestUtil.getMasterMaterialMd5(remoteFileName);
	    }

	    public boolean isConnected() throws Exception {
	    	return ftp.isConnected();
	    }

		private void testDownload(String remotePath,String localpath) throws Exception{
			FileTransferInputStream in = ftp.downloadStream(remotePath);
			//response.setContentType("text/html;charset=utf-8");
			BufferedInputStream bis = new BufferedInputStream(in);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(localpath));

			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bis.close();
			bos.close();

		}

}
