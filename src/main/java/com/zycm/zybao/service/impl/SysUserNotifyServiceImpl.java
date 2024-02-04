package com.zycm.zybao.service.impl;

import com.zycm.zybao.dao.SysUserNotifyDao;
import com.zycm.zybao.model.entity.SysUserNotifyModel;
import com.zycm.zybao.service.interfaces.SysUserNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service("sysUserNotifyService")
public class SysUserNotifyServiceImpl implements SysUserNotifyService {

	@Autowired(required = false)
	private SysUserNotifyDao sysUserNotifyDao;

	@Override
	public Map<String, Object> selectPage(Map<String, Object> param, Integer page, Integer pageSize) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = sysUserNotifyDao.selectPageCount(param);
		if(null != totalCount && totalCount > 0){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        param.put("startRow", startRow);
	        param.put("pageSize", pageSize);
	        List<Map<String,Object>> list = sysUserNotifyDao.selectPage(param);

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
	public void updateReadStatusByKeys(Integer receiverId, Integer[] notifyIds) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("receiverId", receiverId);
		param.put("notifyIds", notifyIds);
		param.put("readStatus", 1);
		sysUserNotifyDao.updateReadStatusByKeys(param);
	}

	@Override
	public void deleteByKeys(Integer receiverId, Integer[] notifyIds) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("receiverId", receiverId);
		param.put("notifyIds", notifyIds);
		sysUserNotifyDao.deleteByKeys(param);
	}

	@Override
	public Map<String, Object> selectCount(Integer receiverId) {
		Map<String, Object> remap = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("receiverId", receiverId);
		param.put("readStatus", 0);
		Integer notCount = sysUserNotifyDao.selectPageCount(param);
		param.put("readStatus", 1);
		Integer count = sysUserNotifyDao.selectPageCount(param);
		remap.put("notRead", notCount);
		remap.put("read", count);
		return remap;
	}

	@Override
	public void batchInsert(List<SysUserNotifyModel> list) {
		sysUserNotifyDao.batchInsert(list);
	}



}
