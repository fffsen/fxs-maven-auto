package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.ModuleModel;

import java.util.List;

public interface ModuleDao {

	List<ModuleModel> selectPublishModule();
}
