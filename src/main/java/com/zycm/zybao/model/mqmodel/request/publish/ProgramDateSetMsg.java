package com.zycm.zybao.model.mqmodel.request.publish;

import java.io.Serializable;
import java.util.List;

import com.zycm.zybao.model.entity.PlayTimeSetModel;

/** 节目播放时间设置消息体
* @ClassName: ProgramDateSetMsg
* @Description: TODO
* @author sy
* @date 2017年9月12日 下午6:07:00
*
*/
public class ProgramDateSetMsg implements Serializable{

	private Integer timeMode;//时间播放策略（1永久 2按时间段 3按星期）

	private Integer downloadMode;//下载策略（1立即下载 2按终端闲时下载）

	private String playStartDate;

	private String playStartTime;

	private String playEndDate;

	private String playEndTime;

	private String weekSet;

	private Integer playLevel;

	private List<PlayTimeSetModel> playTimeSetList;

	public Integer getTimeMode() {
		return timeMode;
	}

	public void setTimeMode(Integer timeMode) {
		this.timeMode = timeMode;
	}

	public Integer getDownloadMode() {
		return downloadMode;
	}

	public void setDownloadMode(Integer downloadMode) {
		this.downloadMode = downloadMode;
	}

	public String getPlayStartDate() {
		return playStartDate;
	}

	public void setPlayStartDate(String playStartDate) {
		this.playStartDate = playStartDate;
	}

	public String getPlayStartTime() {
		return playStartTime;
	}

	public void setPlayStartTime(String playStartTime) {
		this.playStartTime = playStartTime;
	}

	public String getPlayEndDate() {
		return playEndDate;
	}

	public void setPlayEndDate(String playEndDate) {
		this.playEndDate = playEndDate;
	}

	public String getPlayEndTime() {
		return playEndTime;
	}

	public void setPlayEndTime(String playEndTime) {
		this.playEndTime = playEndTime;
	}

	public String getWeekSet() {
		return weekSet;
	}

	public void setWeekSet(String weekSet) {
		this.weekSet = weekSet;
	}

	public Integer getPlayLevel() {
		return playLevel;
	}

	public void setPlayLevel(Integer playLevel) {
		this.playLevel = playLevel;
	}

	public List<PlayTimeSetModel> getPlayTimeSetList() {
		return playTimeSetList;
	}

	public void setPlayTimeSetList(List<PlayTimeSetModel> playTimeSetList) {
		this.playTimeSetList = playTimeSetList;
	}



}
