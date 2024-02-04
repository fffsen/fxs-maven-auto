package com.zycm.zybao.service.impl;

import com.zycm.zybao.dao.AttributeDictionaryDao;
import com.zycm.zybao.service.interfaces.AttributeDictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Slf4j
@Service("attributeDictionaryService")
public class AttributeDictionaryServiceImpl implements AttributeDictionaryService {

	@Autowired(required = false)
	private AttributeDictionaryDao attributeDictionaryDao;

	@Override
	public List<Map<String, Object>> selectDepartment(Map<String, Object> param) {
		return attributeDictionaryDao.selectDepartment(param);
	}

}
