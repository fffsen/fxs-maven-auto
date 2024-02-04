package com.zycm.zybao.service.impl;

import com.zycm.zybao.dao.UserLogDao;
import com.zycm.zybao.model.entity.UserLogModel;
import com.zycm.zybao.service.interfaces.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Service("userLogService")
public class UserLogServiceImpl implements UserLogService {

	@Autowired(required = false)
	private UserLogDao userLogDao;

	@Override
	public Map<String, Object> selectPage(Map<String, Object> param, Integer page, Integer pageSize) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = userLogDao.selectPageCount(param);
		if(null != totalCount && totalCount > 0){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        param.put("startRow", startRow);
	        param.put("pageSize", pageSize);
	        List<Map<String,Object>> list = userLogDao.selectPage(param);

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
	public void insert(Integer uid,Integer logLevel,String info) {
		UserLogModel userLogModel = new UserLogModel();
		userLogModel.setUid(uid);
		userLogModel.setInfo(info);
		userLogModel.setLogLevel(logLevel);
		userLogModel.setCreateTime(new Date());
		userLogDao.insert(userLogModel);
	}

	@Override
	public int selectLoginFailByUid(Integer uid) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String ymd = sdf.format(new Date());
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uid", uid);
		param.put("startTime", ymd+" 00:00:00");
		param.put("endTime", ymd+" 23:59:59");
		List<Map<String,Object>> flist = userLogDao.selectLoginFailByUid(param);
		return (flist != null && flist.size() !=0)?flist.size():0;
	}


}
