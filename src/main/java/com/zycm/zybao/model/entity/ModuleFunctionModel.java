package com.zycm.zybao.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
* @ClassName: ModuleFunctionModel
* @Description: 模块功能点
* @author sy
* @date 2017年11月6日 下午4:18:28
*
*/
public class ModuleFunctionModel implements Serializable{
    private Integer functionId;

    private Integer moduleId;

    private String functionName;

    private String functionIntroduce;

    private String functionCode;

    private String functionUrl;

    private Date createTime;

    private Integer isDelete;

    private Integer isModule;

    public Integer getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Integer functionId) {
        this.functionId = functionId;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName == null ? null : functionName.trim();
    }

    public String getFunctionIntroduce() {
		return functionIntroduce;
	}

	public void setFunctionIntroduce(String functionIntroduce) {
		this.functionIntroduce = functionIntroduce;
	}

	public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode == null ? null : functionCode.trim();
    }

    public String getFunctionUrl() {
        return functionUrl;
    }

    public void setFunctionUrl(String functionUrl) {
        this.functionUrl = functionUrl == null ? null : functionUrl.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

	public Integer getIsModule() {
		return isModule;
	}

	public void setIsModule(Integer isModule) {
		this.isModule = isModule;
	}

}
