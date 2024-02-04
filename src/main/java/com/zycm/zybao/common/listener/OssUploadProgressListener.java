package com.zycm.zybao.common.listener;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.zycm.zybao.model.upload.ProgressModel;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class OssUploadProgressListener implements ProgressListener{

	private long bytesWritten = 0;
    private long totalBytes = -1;
    private boolean succeed = false;

    public ProgressModel progressModel_listener;

    public OssUploadProgressListener() {

    }

    public OssUploadProgressListener(ProgressModel progressModel) {
      this.progressModel_listener = progressModel;
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        switch (eventType) {
        case TRANSFER_STARTED_EVENT:
            log.debug("开始oss上传......");
            break;
        case REQUEST_CONTENT_LENGTH_EVENT:
            this.totalBytes = bytes;
            log.debug("文件大小 "+this.totalBytes + " bytes 将上传oss");
            break;
        case REQUEST_BYTE_TRANSFER_EVENT:
            this.bytesWritten += bytes;
            if (this.totalBytes != -1) {
                int percent = (int)(this.bytesWritten * 100.0 / this.totalBytes);
                progressModel_listener.setBytesRead(bytes);
            	//progressModel_listener.setRemoteFilename(filename);
            	progressModel_listener.setPercent(percent+"%");
                //System.out.println(bytes + " bytes have been written at this time, upload progress: " + percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");
                log.debug(bytes + " bytes 已写入, 上传进度: " + percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");
            } else {
                //System.out.println(bytes + " bytes have been written at this time, upload ratio: unknown" + "(" + this.bytesWritten + "/...)");
                log.debug(bytes + " bytes 已写入, 上传比率: unknown" + "(" + this.bytesWritten + "/...)");
            }
            break;
        case TRANSFER_COMPLETED_EVENT:
            this.succeed = true;
            progressModel_listener.setPercent("100%");
            //System.out.println("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
            log.debug("上传成功,上传大小 "+ this.bytesWritten + " bytes");
            break;
        case TRANSFER_FAILED_EVENT:
            //System.out.println("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
            log.error("上传失败,上传大小 "+ this.bytesWritten + " bytes");
            break;
        default:
            break;
        }
    }

    public boolean isSucceed() {
        return succeed;
    }
}
