package com.zycm.zybao.model.vo;

import com.zycm.zybao.model.mqmodel.request.publish.ProgramDateSetMsg;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;


/** 节目发布的vo
* @ClassName:
* @Description: TODO
* @author sy
* @date 2017年9月12日 下午6:07:00
*
*/
public class ProgramPublishVo implements Serializable{

	private Integer timeMode;//时间播放策略（1永久 2按时间段 3按星期）

	private Integer downloadMode;//下载策略（1立即下载 2按终端闲时下载）

	private String playStartDate;

	private String playStartTime;

	private String playEndDate;

	private String playEndTime;

	private String weekSet;//星期设置

	private Integer playLevel;//播放级别

	private Integer switchMode;//轮播模式

	private Integer frequency;//频率

	private Integer[] programIds;

	private Integer[] mediaGroupIds;

	private String[] timesetdata;

	private Integer isClean;//是否清理原有节目

	private Integer publisherId;

	public String[] getTimesetdata() {
		return timesetdata;
	}

	public void setTimesetdata(String[] timesetdata) {
		this.timesetdata = timesetdata;
	}

	public void setTimesetdata(String timesetdata_s) {
		String[] a = null;
		if(StringUtils.isNotBlank(timesetdata_s)){
			a = timesetdata_s.split(",");
		}
		this.timesetdata = a;
	}

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

	public Integer getSwitchMode() {
		return switchMode;
	}

	public void setSwitchMode(Integer switchMode) {
		this.switchMode = switchMode;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public Integer[] getProgramIds() {
		return programIds;
	}

	public void setProgramIds(Integer[] programIds) {
		this.programIds = programIds;
	}
	public void setProgramIds(String programIds) {
		Integer[] a = null;
		if(StringUtils.isNotBlank(programIds)){
			String[] s = programIds.split(",");
			a = new Integer[s.length];
			for (int i = 0; i < s.length; i++) {
				a[i] = Integer.parseInt(s[i]);
			}
		}
		this.programIds = a;
	}

	public Integer[] getMediaGroupIds() {
		return mediaGroupIds;
	}

	public void setMediaGroupIds(Integer[] mediaGroupIds) {
		this.mediaGroupIds = mediaGroupIds;
	}
	public void setMediaGroupIds(String mediaGroupIds) {
		Integer[] a = null;
		if(StringUtils.isNotBlank(mediaGroupIds)){
			String[] s = mediaGroupIds.split(",");
			a = new Integer[s.length];
			for (int i = 0; i < s.length; i++) {
				a[i] = Integer.parseInt(s[i]);
			}
		}
		this.mediaGroupIds = a;
	}

	public Integer getIsClean() {
		return isClean;
	}

	public void setIsClean(Integer isClean) {
		this.isClean = isClean;
	}

	public Integer getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(Integer publisherId) {
		this.publisherId = publisherId;
	}

	public ProgramDateSetMsg toProgramDateSetMsg(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");

		ProgramDateSetMsg programDateSetMsg = new ProgramDateSetMsg();
		programDateSetMsg.setTimeMode(timeMode);
		programDateSetMsg.setDownloadMode(downloadMode);
		try {
			/*programDateSetMsg.setPlayStartDate(sdf.parse(playStartDate));
			programDateSetMsg.setPlayStartTime(sdf1.parse(playStartTime));
			programDateSetMsg.setPlayEndDate(sdf.parse(playEndDate));
			programDateSetMsg.setPlayEndTime(sdf1.parse(playEndTime));*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		programDateSetMsg.setPlayStartDate(playStartDate);
		programDateSetMsg.setPlayStartTime(playStartTime);
		programDateSetMsg.setPlayEndDate(playEndDate);
		programDateSetMsg.setPlayEndTime(playEndTime);
		programDateSetMsg.setPlayLevel(playLevel);
		programDateSetMsg.setWeekSet(weekSet);
		return programDateSetMsg;
	}

	public String isNotNull(){
		if(null == this.programIds || this.programIds.length == 0){
			return "数组节目id【programIds】不能为空";
		}else if(null == this.mediaGroupIds || this.mediaGroupIds.length == 0){
			return "数组终端组id【mediaGroupIds】不能为空";
		}else if(null == this.timesetdata || this.timesetdata.length == 0){
			return "时段设置【timesetdata】不能为空";
		}else if(null != this.timeMode && this.timeMode > 0){
			if(this.timeMode == 1){//永久

			}else if(this.timeMode == 2){//按时间段
				if(StringUtils.isAnyBlank(this.playStartDate,this.playEndDate)){
					return "timeMode=2时,playStartDate、playEndDate不能有空值";
				}
				if(this.timesetdata == null || this.timesetdata.length == 0){
					return "timeMode=2时,timesetdata不能有空值";
				}
			}else if(this.timeMode == 3){//按星期
				if(StringUtils.isBlank(this.weekSet)){
					return "timeMode=3时,weekSet不能有空值";
				}
			}else if(this.timeMode == 4){//定时连播
				if(StringUtils.isAnyBlank(this.playStartDate,this.playEndDate)){
					return "timeMode=4时,playStartDate、playEndDate不能有空值";
				}
				if(this.timesetdata == null || this.timesetdata.length == 0){
					return "timeMode=4时,timesetdata不能有空值";
				}
			}else{
				return "时间播放策略【timeMode】只能是1、2、3、4，不能是别的值";
			}

			if(null == this.downloadMode || this.downloadMode <= 0 || this.downloadMode > 2){
				return "下载策略【downloadMode】只能是1、2，不能是别的值";
			}

			if(null == this.switchMode || this.switchMode <= 0 ){
				return "节目轮播策略【switchMode】必须大于0";
			}else{
				return "";
			}
		}else{
			return "时间播放策略【timeMode】不能为空或必须大于0";
		}
	}


	/**
	* @Title: toOrderTimeSet
	* @Description: 排序时间 便于获取起始时段
	* @return    参数
	* @author sy
	* @throws
	* @return List<Date>    返回类型
	*
	*/
	public List<Date> toOrderTimeSet() throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		List<Date> list = null;
		if(timesetdata != null && timesetdata.length != 0){
			list = new ArrayList<Date>();
			for (int i = 0; i < timesetdata.length; i++) {
				list.add(sdf.parse(timesetdata[i].split("#")[0]));
				list.add(sdf.parse(timesetdata[i].split("#")[1]));
			}
			//排序
			Collections.sort(list,new Comparator<Date>() {

				@Override
				public int compare(Date date1, Date date2) {
					//升序
					if(date1.getTime() > date2.getTime()){
						return 1;
					}
					if(date1.getTime() > date2.getTime()){
						return 0;
					}
					return -1;
				}

			});
		}
		return list;
	}

	/*public static void main(String[] args) {
		String[] s = {"15:22:22#16:11:11#1","11:32:22#16:00:00#3","10:45:22#20:00:01#3"};
		ProgramPublishVo programPublishVo = new ProgramPublishVo();
		programPublishVo.setTimesetdata(s);
		try {
			List<Date> l = programPublishVo.toOrderTimeSet();
			System.out.println(l);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}*/
}
