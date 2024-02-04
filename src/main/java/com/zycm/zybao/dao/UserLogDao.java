package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.UserLogModel;

import java.util.List;
import java.util.Map;

public interface UserLogDao {

	List<Map<String,Object>> selectPage(Map<String,Object> param);

	Integer selectPageCount(Map<String,Object> param);

	void insert(UserLogModel userLogModel);

	List<Map<String,Object>> selectLoginFailByUid(Map<String,Object> param);
}
