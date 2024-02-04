package com.zycm.zybao.model.mqmodel.request.publish;

import java.io.Serializable;
import java.util.Date;

public class PlayTimeSetMsg implements Serializable{

	private Integer timeSetId;

    //private String startDate;

    private String startTime;

    //private String endDate;

    private String endTime;

    private Integer frequency;


    public Integer getTimeSetId() {
		return timeSetId;
	}

	public void setTimeSetId(Integer timeSetId) {
		this.timeSetId = timeSetId;
	}

	/*public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
*/
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/*public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}*/

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }
}
