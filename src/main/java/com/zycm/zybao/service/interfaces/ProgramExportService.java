package com.zycm.zybao.service.interfaces;

import com.zycm.zybao.model.mqmodel.request.publish.ProgramMsg;

import java.util.List;
import java.util.Map;


/**
* @ClassName: ProgramExportService
* @Description: 节目导出成节目包
* @author sy
* @date 2018年7月11日
*
*/
public interface ProgramExportService {

	List<Map<String,Object>> selectByProgramIds(Integer[] programIds);

	List<ProgramMsg> selectProgByGroupId(Integer mediaGroupId);

	List<ProgramMsg> selectProgByProgramIds(Integer[] programIds);

	Map<String,Object> selectByPrimaryKey(Integer mediaGroupId);

	List<Map<String,Object>> selectProgramByGroupId(Map<String,Object> param);

}
