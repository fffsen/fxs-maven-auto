package com.zycm.zybao.service.impl;

import com.zycm.zybao.dao.MediaGroupDao;
import com.zycm.zybao.dao.ProgramDao;
import com.zycm.zybao.dao.ProgramMaterialRelationDao;
import com.zycm.zybao.dao.ProgramPublishRecordDao;
import com.zycm.zybao.model.mqmodel.request.publish.ProgramMsg;
import com.zycm.zybao.service.interfaces.ProgramExportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Slf4j
@Service("programExportService")
public class ProgramExportServiceImpl implements ProgramExportService {

	@Autowired(required = false)
   	private ProgramMaterialRelationDao programMaterialRelationDao;
	@Autowired(required = false)
   	private ProgramDao programDao;
	@Autowired(required = false)
   	private ProgramPublishRecordDao programPublishRecordDao;
	@Autowired(required = false)
	private MediaGroupDao mediaGroupDao;

	@Override
	public List<Map<String,Object>> selectByProgramIds(Integer[] programIds){
		return programMaterialRelationDao.selectByProgramIds(programIds);
	}

	@Override
	public List<ProgramMsg> selectProgByGroupId(Integer mediaGroupId){
		return programDao.selectProgByGroupId(mediaGroupId);
	}

	@Override
	public List<ProgramMsg> selectProgByProgramIds(Integer[] programIds){
		return programDao.selectProgByProgramIds(programIds);
	}

	@Override
	public Map<String,Object> selectByPrimaryKey(Integer mediaGroupId){
		return mediaGroupDao.selectByPrimaryKey(mediaGroupId);
	}

	@Override
	public List<Map<String,Object>> selectProgramByGroupId(Map<String,Object> param){
		return programPublishRecordDao.selectProgramByGroupId(param);
	}
}
