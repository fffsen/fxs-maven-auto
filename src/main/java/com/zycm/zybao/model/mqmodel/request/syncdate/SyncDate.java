package com.zycm.zybao.model.mqmodel.request.syncdate;

import java.util.Date;

/** 同步时间消息体  服务器到终端的通信
* @ClassName: SyncDate
* @Description: TODO
* @author sy
* @date 2017年9月13日 下午2:52:24
*
*/
public class SyncDate {

	private String serverDate;

	public SyncDate(String serverDate) {
		super();
		this.serverDate = serverDate;
	}

	public String getServerDate() {
		return serverDate;
	}

	public void setServerDate(String serverDate) {
		this.serverDate = serverDate;
	}


}
