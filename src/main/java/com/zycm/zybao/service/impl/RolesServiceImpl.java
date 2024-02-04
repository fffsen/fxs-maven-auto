package com.zycm.zybao.service.impl;

import com.zycm.zybao.dao.RolesDao;
import com.zycm.zybao.dao.UserRolesDao;
import com.zycm.zybao.model.entity.RolesModel;
import com.zycm.zybao.service.interfaces.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("rolesService")
public class RolesServiceImpl implements RolesService {

	@Autowired(required = false)
	private RolesDao rolesDao;

	@Autowired(required = false)
	private UserRolesDao userRolesDao;

	@Override
	public Map<String, Object> selectPage(Integer page, Integer pageSize,Map<String, Object> param) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = rolesDao.selectPageCount(param);
		if(null != totalCount && totalCount > 0){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        param.put("startRow", startRow);
	        param.put("pageSize", pageSize);
	        List<Map<String,Object>> list = rolesDao.selectPage(param);

	        returndata.put("dataList", list);
	        returndata.put("totalPage", totalPage);

		}else{
			returndata.put("dataList", new ArrayList());
	        returndata.put("totalPage", 0);
		}
		returndata.put("page", page);
		returndata.put("pageSize", pageSize);
		returndata.put("total", totalCount);

		return returndata;
	}

	@Override
	public Map<String, Object> selectByPrimaryKey(Integer roleId) {
		return rolesDao.selectByPrimaryKey(roleId);
	}


	@Override
	public void deleteByPrimaryKey(Integer roleId) throws Exception{
		//查询角色是否被使用 被使用的不能删除
		List<Map<String,Object>> userInfoList = userRolesDao.selectUserByRole(roleId);
		if(userInfoList.size() > 0){
			throw new Exception("删除的角色存在使用的用户，不能删除");
		}else{
			rolesDao.deleteByPrimaryKey(roleId);
		}
	}

	@Override
	public void insert(String roleName, String roleIntroduce) {
		RolesModel rolesModel = new RolesModel();
		rolesModel.setRoleName(roleName);
		rolesModel.setRoleIntroduce(roleIntroduce);
		rolesModel.setIsDelete(1);
		rolesModel.setRoleCreateTime(new Date());
		rolesModel.setRoleToProject("advert-publish");
		rolesDao.insert(rolesModel);
	}

	@Override
	public void updateByPrimaryKey(Integer roleId, String roleName, String roleIntroduce) {
		RolesModel rolesModel = new RolesModel();
		rolesModel.setRoleName(roleName);
		rolesModel.setRoleIntroduce(roleIntroduce);
		rolesModel.setRoleId(roleId);
		rolesDao.updateByPrimaryKey(rolesModel);
	}

	@Override
	public List<Map<String, Object>> selectByName(Map<String,Object> param) {
		return rolesDao.selectByName(param);
	}

	@Override
	public List<Map<String, Object>> selectRoleInfo(Map<String, Object> param) {
		return rolesDao.selectRoleInfo(param);
	}

	@Override
	public List<RolesModel> selectByUserName(String userName) {
		return rolesDao.selectByUserName(userName);
	}






}
