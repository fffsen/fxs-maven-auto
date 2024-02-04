package com.zycm.zybao.model.mqmodel;

/** mq 消息的最外层包装
* @ClassName: ProtocolModel
* @Description: TODO
* @author sy
* @date 2017年9月12日 下午5:25:23
*
*/
public class ProtocolModel {
	/*消息类型，有request、response、task
		request：表示是服务端到终端的消息
		response：表示终端到服务端的响应信息
		task：表示终端到服务端的定时任务反馈信息*/
	private String type;
	/*消息发送方向，有ServerToClient、ClientToServer，
		ServerToClient：服务端发送到终端，
		ClientToServer：终端发送到服务端*/
	private String direction;

	private String toClient;//"all"全接受  "z001"表示只有终端机器码为z001的接收
	/*功能事件的标示，详见枚举类*/
	private String event;

	private Object extendAttr;
	/*消息体*/
	private Object data;

	public ProtocolModel() {
		super();
	}



	public ProtocolModel(String type,String direction,String event,Object data){
		this.type = type;
		this.direction = direction;
		this.event = event;
		this.data = data;
	}



	public void ServerToClient(String event, Object data) {
		this.type = "request";
		this.direction = "ServerToClient";
		this.event = event;
		this.data = data;
	}


	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	public String getToClient() {
		return toClient;
	}

	public void setToClient(String toClient) {
		this.toClient = toClient;
	}

	public Object getExtendAttr() {
		return extendAttr;
	}

	public void setExtendAttr(Object extendAttr) {
		this.extendAttr = extendAttr;
	}

}
