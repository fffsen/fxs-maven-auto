package com.zycm.zybao.model.upload;

import java.io.Serializable;

import com.zycm.zybao.common.utils.NumUtil;

/**
 * 负责处理进度信息
 *
 * @author sy
 *
 */
public class ProgressModel implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 已读字节 **/
    private long bytesRead = 0L;
    /** 已读MB **/
    private String mbRead = "0";
    /** 总长度 **/
    private long contentLength = 0L;
    /****/
    private int items;

    private String remoteFilename;
    /** 已读百分比 **/
    private String percent;
    /** 读取速度 **/
    private String speed;
    /** 开始读取的时间 **/
    private long startReatTime = System.currentTimeMillis();

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public String getPercent() {
//    	if(StringUtils.isBlank(percent)){
//    		percent = NumUtil.getPercent(bytesRead, contentLength);
//    	}
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getSpeed() {
        speed = NumUtil.divideNumber(
                NumUtil.divideNumber(bytesRead * 1000,
                        System.currentTimeMillis() - startReatTime), 1000);
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public long getStartReatTime() {
        return startReatTime;
    }

    public void setStartReatTime(long startReatTime) {
        this.startReatTime = startReatTime;
    }

    public String getMbRead() {
        mbRead = NumUtil.divideNumber(bytesRead, 1000000);
        return mbRead;
    }

    public void setMbRead(String mbRead) {
        this.mbRead = mbRead;
    }

    public String getRemoteFilename() {
		return remoteFilename;
	}

	public void setRemoteFilename(String remoteFilename) {
		this.remoteFilename = remoteFilename;
	}

	@Override
    public String toString() {
        return "ProgressModel [bytesRead=" + bytesRead + ", mbRead=" + mbRead
                + ", contentLength=" + contentLength + ", items=" + items
                + ", percent=" + percent + ", speed=" + speed
                + ", startReatTime=" + startReatTime + "]";
    }

}
