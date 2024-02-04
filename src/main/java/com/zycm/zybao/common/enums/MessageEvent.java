package com.zycm.zybao.common.enums;

public enum MessageEvent {

	/** 发布事件
	* @Fields PUBLISH : TODO
	*/
	PUBLISH("Publish","发布节目事件","publishProgramHandler"),

	/**  选择节目播放 点播
	* @Fields CHOICEPLAY
	*/
	CHOICEPLAY("ChoicePlay","节目点播事件",""),
	/**
	* @Fields   视频行为 包括静音 取消静音  暂停 播放 等
	*/
	VIDEOACTION("VideoAction","视频操作行为事件",""),
	/**
	* @Fields led发布
	*/
	LEDPUBLISH("LedPublish","LED发布事件","ledPublishTxtHandler"),
	/** 节目下刊
	* @Fields PROGRAMDOWN : TODO
	*/
	PROGRAMDOWN("ProgramDown","节目下刊事件",""),
	/** 远程截屏
	* @Fields SCREENSHOT : TODO
	*/
	SCREENSHOT("Screenshot","远程截屏事件",""),
	/** 插播字幕
	* @Fields INSERTCAPTION : TODO
	*/
	INSERTCAPTION("InsertCaption","插播字幕事件",""),
	/** 插播节目
	* @Fields INSERTPROGRAM : TODO
	*/
	INSERTPROGRAM("InsertProgram","插播节目事件",""),
	/** 取消插播
	* @Fields CANCELINSERT : TODO
	*/
	CANCELINSERT("CancelInsert","取消插播",""),
	/** 获取终端文件
	* @Fields CLIENTFILE : TODO
	*/
	CLIENTFILE("ClientFile","获取终端文件","clientFileReceiveHandler"),
	/** 删除终端过期文件
	* @Fields DELETEFILE : TODO
	*/
	DELETEFILE("DeleteFile","删除终端过期文件",""),

	/**  删除全部过期文件
	* @Fields DELETEEXPIREFILE
	*/
	DELETEEXPIREFILE("DeleteExpireFile","删除全部过期文件",""),
	/** 终端重启
	* @Fields RESTART : TODO
	*/
	RESTART("Restart","终端重启",""),
	/**
	* @Fields 切换mq
	*/
	CHANGEMQ("ChangeMq","切换mq",""),
	/** 终端升级
	* @Fields UPGRADE : TODO
	*/
	UPGRADE("Upgrade","终端升级","upgradeReceiveHandler"),
	/** 终端注册
	* @Fields REGISTERCODE : TODO
	*/
	REGISTERCODE("RegisterCode","终端注册",""),
	/** 同步服务器时间
	* @Fields SYNCDATE : TODO
	*/
	SYNCDATE("SyncDate","同步服务器时间","syncDateReceiveHandler"),
	/**  同步时间后的同步结果
	* @Fields SYNCDATERESULT
	*/
	SYNCDATERESULT("SyncDateResult","同步时间后的同步结果","syncDateResultReceiveHandler"),
	/** 获取终端基本配置信息（音量、亮度、ftp）
	* @Fields CLIENTCONF : TODO
	*/
	BASECLIENTCONF("BaseClientConf","获取终端基本配置信息","baseClientConfReceiveHandler"),
	/** 获取终端所有配置信息
	* @Fields CLIENTCONF : TODO
	*/
	ALLCLIENTCONF("AllClientConf","获取终端所有配置信息","allClientConfReceiveHandler"),
	/** 终端心跳
	* @Fields HEARTBEAT : TODO
	*/
	HEARTBEAT("Heartbeat","终端心跳","heartbeatReceiveHandler"),
	/** 运行日志
	* @Fields RUNLOG : TODO
	*/
	RUNLOG("RunLog","运行日志","runLogReceiveHandler"),
	/** 播放日志
	* @Fields PLAYLOG : TODO
	*/
	PLAYLOG("PlayLog","播放日志","playLogReceiveHandler"),
	/**
	* @Fields  开关屏
	*/
	OUTPUTMODE("OutputMode","开关屏",""),
	/**
	* @Fields  测速
	*/
	TESTSPEED("TestSpeed","测速","tesNetSpeedReceiveHandler"),
	/** 素材下载进度
	* @Fields DOWNLOADPROGRESS : TODO
	*/
	DOWNLOADPROGRESS("DownloadProgress","素材下载进度","downloadProgressReceiveHandler"),
	/**
	* @Fields 远程实时预览播放画面
	*/
	REMOTEREALTIMEPREVIEW("RemoteRealtimePreview","远程实时预览播放画面","openRemotePreviewReceiveHandler"),
	/**
	* @Fields 远程实时预览播放画面
	*/
	REMOTEREALTIMEPREVIEWCLOSE("RemoteRealtimePreviewClose","远程实时预览播放画面关闭","closeRemotePreviewReceiveHandler"),
	/**
	* @Fields 终端定时反馈的信息
	*/
	CLIENTINTERVALTASK("ClientIntervalTask","终端定时反馈的信息","clientConfIntervalTaskReceiveHandler"),
	/**
	* @Fields 同步播放进度
	*/
	SYNCPROGRESS("SyncProgress","同步播放进度","syncPlayReceiveHandler"),
	/**
	* @Fields 同步播放进度
	*/
	PROGRAMDOWNLOAD("ProgramDownload","节目下载进度","programDownloadReceiveHandler"),

	/**
	* @Fields 亮度设置
	*/
	LIGHTSET("LightSet","亮度设置","");


	public String name;

	public String handlerName;

	public String event;

	private MessageEvent(String event,String name,String handlerName){
		this.event = event;
		this.name = name;
		this.handlerName = handlerName;
	}

}
