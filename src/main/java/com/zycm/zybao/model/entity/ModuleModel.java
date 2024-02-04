package com.zycm.zybao.model.entity;

import java.io.Serializable;
import java.util.List;

/**
* @ClassName: ModuleModel
* @Description: 系统模块
* @author sy
* @date 2017年11月6日 下午4:19:14
*
*/
public class ModuleModel implements Serializable{
    private Integer moduleId;

    private String moduleName;

    private String moduleToProject;

    private Integer isDelete;

    private List<ModuleFunctionModel> mfmodel;

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName == null ? null : moduleName.trim();
    }

    public String getModuleToProject() {
        return moduleToProject;
    }

    public void setModuleToProject(String moduleToProject) {
        this.moduleToProject = moduleToProject == null ? null : moduleToProject.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

	public List<ModuleFunctionModel> getMfmodel() {
		return mfmodel;
	}

	public void setMfmodel(List<ModuleFunctionModel> mfmodel) {
		this.mfmodel = mfmodel;
	}


}
