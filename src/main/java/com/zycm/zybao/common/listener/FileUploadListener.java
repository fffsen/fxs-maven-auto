package com.zycm.zybao.common.listener;

import com.enterprisedt.net.ftp.EventListener;
import com.zycm.zybao.common.utils.NumUtil;
import com.zycm.zybao.model.upload.ProgressModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


/**
* @ClassName: FileUploadListener
* @Description: ftp开始上传文件时 该类启用
* @author sy
* @date 2018年2月28日 上午9:45:37
*
*/
@Slf4j
public class FileUploadListener implements EventListener {

    public ProgressModel progressModel_listener;

    public FileUploadListener() {

    }

    public FileUploadListener(ProgressModel progressModel) {
      this.progressModel_listener = progressModel;
    }

    /* 在传输开始后  该方法被不停的调用
     *
     */
    @Override
	public void bytesTransferred(String remoteFilename, String connId, long count) {
    	//logger.debug("bytesTransferred:"+remoteFilename+","+connId+","+count+","+progressModel_listener.getContentLength());
		String filename = StringUtils.isBlank(remoteFilename.substring(remoteFilename.lastIndexOf("/") + 1))
				? "getFileError" : remoteFilename.substring(remoteFilename.lastIndexOf("/") + 1);
    	progressModel_listener.setBytesRead(count/1024);
    	progressModel_listener.setRemoteFilename(filename);
    	progressModel_listener.setPercent(NumUtil.getPercent(progressModel_listener.getBytesRead(), progressModel_listener.getContentLength()));

    }

	@Override
	public void commandSent(String connId, String cmd) {
		//logger.debug("commandSent:"+connId+","+cmd);
	}

	@Override
	public void downloadCompleted(String arg0, String arg1) {
		//logger.debug("downloadCompleted:"+arg0+","+arg1);
	}

	@Override
	public void downloadStarted(String arg0, String arg1) {
		//logger.debug("downloadStarted:"+arg0+","+arg1);
	}

	@Override
	public void replyReceived(String connId, String reply) {
		//logger.debug("replyReceived:"+connId+","+reply);
	}

	@Override
	public void uploadCompleted(String arg0, String arg1) {
		log.info("uploadCompleted:"+arg0+","+arg1);
	}

	@Override
	public void uploadStarted(String connId, String remoteFilename ) {
		//上传开始出发事件
		log.debug("uploadStarted:"+connId+","+remoteFilename );
	}

}
