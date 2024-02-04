package com.zycm.zybao.service.impl;

import com.zycm.zybao.dao.RolesFunctionDao;
import com.zycm.zybao.model.entity.RolesFunctionModel;
import com.zycm.zybao.service.interfaces.RolesFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("rolesFunctionService")
public class RolesFunctionServiceImpl implements RolesFunctionService {

	@Autowired(required = false)
	private RolesFunctionDao rolesFunctionDao;

	@Override
	public List<Map<String, Object>> selectByRole(Integer roleId) {
		return rolesFunctionDao.selectByRole(roleId);
	}

	@Override
	public void savePrivilege(Integer roleId, Integer[] functionIds) throws Exception {
		if(functionIds.length > 0){
			//清楚角色之前权限
			rolesFunctionDao.deleteByPrimaryKey(roleId);
			//增加新权限
			List<RolesFunctionModel> list = new ArrayList<RolesFunctionModel>();
			for (int i = 0; i < functionIds.length; i++) {
				RolesFunctionModel rolesFunctionModel = new RolesFunctionModel();
				rolesFunctionModel.setRoleId(roleId);
				rolesFunctionModel.setFunctionId(functionIds[i]);
				list.add(rolesFunctionModel);
			}
			rolesFunctionDao.batchInsert(list);
		}else{
			throw new Exception("角色的权限不能为空！");
		}
	}


}
