package com.zycm.zybao.service.impl;

import com.zycm.zybao.dao.MediaAttributeDao;
import com.zycm.zybao.dao.MediaRunLogDao;
import com.zycm.zybao.service.interfaces.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* @ClassName: StatisticsServiceImpl
* @Description: 统计模块
* @author sy
* @date 2021年10月13日
*
*/
@Slf4j
@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {

	@Autowired(required = false)
	private MediaAttributeDao mediaAttributeDao;
	@Autowired(required = false)
	private MediaRunLogDao mediaRunLogDao;

	@Override
	public Map<String,Object> selectMediaStatistics(Map<String,Object> param){
		Map<String,Object> returndata = new HashMap<String, Object>();

		//获取在线信息
		List<Map<String, Object>> onlineStatistics = mediaAttributeDao.getOnlineStatistics(param);
		int tt = 0;
		if(onlineStatistics != null && onlineStatistics.size() > 0){
			returndata.put("onClient", "0");//在线数
			returndata.put("offClient", "0");//离线数
			returndata.put("exceptionClient", "0");//连接异常数
			returndata.put("invalidClient", "0");//拆机数
			for (Map<String, Object> map2 : onlineStatistics) {
				if(map2.get("adStatus") == null || "".equals(map2.get("adStatus").toString())){
					tt += Integer.parseInt(map2.get("num").toString());
				}else{
					int  num = map2.get("num")!=null?Integer.parseInt(map2.get("num").toString()):0;
					if("-2".equals(map2.get("adStatus").toString())){
						returndata.put("exceptionClient", num);
					}else if("-1".equals(map2.get("adStatus").toString())){
						returndata.put("invalidClient", num);
					}else if("0".equals(map2.get("adStatus").toString())){
						returndata.put("offClient", num);
					}else if("1".equals(map2.get("adStatus").toString())){
						returndata.put("onClient", num);
					}
					tt += num;
				}
			}
			returndata.put("totalClient", tt);
		}else{
			returndata.put("totalClient", 0);
			returndata.put("onClient", 0);
			returndata.put("offClient", 0);
			returndata.put("exceptionClient", 0);
			returndata.put("invalidClient", 0);
		}

		//获取未上刊数
		Integer notProgMedia = mediaAttributeDao.selectPageMediaProgNumZeroCount(param);
		returndata.put("noProgClient", notProgMedia);
		return returndata;
	}

	@Override
	public Map<String, Object> pageStatisticsRunLog(Map<String,Object> param,Integer page,Integer pageSize) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = mediaRunLogDao.pageStatisticsRunLogCount(param);
		if(null != totalCount && totalCount > 0){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        param.put("startRow", startRow);
	        param.put("pageSize", pageSize);
	        List<Map<String,Object>> list = mediaRunLogDao.pageStatisticsRunLog(param);

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

}
