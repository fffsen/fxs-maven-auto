package com.zycm.zybao.dao;

import com.zycm.zybao.model.entity.ProgramLayoutModel;

import java.util.List;
import java.util.Map;


public interface ProgramLayoutDao {

	void insertOfBatch(List<ProgramLayoutModel> plList);

	void deleteByProgramId(Integer programId);

	List<Map<String,Object>> selectByProgramId(Integer programId);
}
