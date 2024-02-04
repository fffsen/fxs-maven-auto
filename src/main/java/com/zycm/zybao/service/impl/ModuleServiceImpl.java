package com.zycm.zybao.service.impl;

import com.zycm.zybao.dao.ModuleDao;
import com.zycm.zybao.dao.ModuleFunctionDao;
import com.zycm.zybao.model.entity.ModuleFunctionModel;
import com.zycm.zybao.model.entity.ModuleModel;
import com.zycm.zybao.service.interfaces.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service("moduleService")
public class ModuleServiceImpl implements ModuleService {

	@Autowired(required = false)
	private ModuleDao moduleDao;
	@Autowired(required = false)
	private ModuleFunctionDao moduleFunctionDao;
	@Override
	public List<ModuleModel> selectPublishModule() {
		return moduleDao.selectPublishModule();
	}
	@Override
	public List<ModuleFunctionModel> selectByRoleIds(Integer[] roleId) {
		return moduleFunctionDao.selectByRoleIds(roleId);
	}






}
