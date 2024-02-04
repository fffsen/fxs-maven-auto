package com.zycm.zybao.service.impl;

import com.zycm.zybao.common.constant.Constants;
import com.zycm.zybao.common.redis.RedisHandle;
import com.zycm.zybao.common.utils.DateUtil;
import com.zycm.zybao.common.utils.StringUtils;
import com.zycm.zybao.dao.MediaRunLogDao;
import com.zycm.zybao.model.entity.MediaRunLogModel;
import com.zycm.zybao.service.interfaces.MediaRunLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@Service("mediaRunLogService")
public class MediaRunLogServiceImpl implements MediaRunLogService {

	@Autowired(required = false)
	private MediaRunLogDao mediaRunLogDao;
	@Autowired
	private RedisHandle redisHandle;

	@Override
	public Map<String, Object> selectPage(Map<String, Object> param, Integer page, Integer pageSize) {
		Map<String, Object> returndata = new HashMap<String, Object>();
		Integer totalCount = mediaRunLogDao.selectPageCount(param);
		if(null != totalCount && totalCount > 0){
			//总页数
	        Double totalPage = Math.ceil(totalCount/Double.parseDouble(pageSize+""));
	        int startRow = (page-1)*pageSize;
	        param.put("startRow", startRow);
	        param.put("pageSize", pageSize);
	        List<Map<String,Object>> list = mediaRunLogDao.selectPage(param);

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

	@Override
	public List<Map<String, Object>> getOffCountByCondition(Map<String, Object> param) {
		return mediaRunLogDao.getOffCountByCondition(param);
	}

	@Override
	public MediaRunLogModel insert(String machineCode, String logInfo, Integer logLevel, Date date) {
		if(StringUtils.isBlank(machineCode)){
			log.error("新增运行日志异常，机器码为空");
			return null;
		}

		MediaRunLogModel mediaRunLogModel = new MediaRunLogModel();
		mediaRunLogModel.setMachineCode(machineCode);
		mediaRunLogModel.setLogInfo(logInfo);
		mediaRunLogModel.setLogLevel(logLevel);
		mediaRunLogModel.setLogTime(date);
		mediaRunLogModel.setCreateTime(date);

		//连续日志做合并
		String logk = Constants.LOG_REDIS_PREFIX+machineCode;
		Set<Object> logkey = redisHandle.getMapFieldKey(logk);
		if(logkey != null && logkey.size() > 0){
			String rinfo = redisHandle.getMapField(logk, "info")==null?"":redisHandle.getMapField(logk, "info").toString();
			Integer rnum = redisHandle.getMapField(logk, "num")==null?0:Integer.parseInt(redisHandle.getMapField(logk, "num").toString());
			Long rlasttime = redisHandle.getMapField(logk, "lasttime")==null?0:Long.parseLong(redisHandle.getMapField(logk, "lasttime").toString());
			Long rfirsttime = redisHandle.getMapField(logk, "firsttime")==null?0:Long.parseLong(redisHandle.getMapField(logk, "firsttime").toString());
			Integer rlogId = redisHandle.getMapField(logk, "logId")==null?0:Integer.parseInt(redisHandle.getMapField(logk, "logId").toString());
			//排除上下线日志 合并相同及一小时内的
			if(StringUtils.isNotBlank(logInfo)
					&& !logInfo.contains("终端上线")
					&& !logInfo.contains("终端离线")
					&& logInfo.equals(rinfo)
					&& (date.getTime() - rlasttime.longValue() < 60*60*1000)){
				rinfo = rinfo + "";
				rnum += 1;
				mediaRunLogModel.setLogInfo(logInfo+" 合并"+rnum+"条 开始时间:"+ DateUtil.sdf_ymdhms.format(new Date(rfirsttime)));
				mediaRunLogModel.setRunLogId(rlogId);
				mediaRunLogDao.updateInfoByPrimaryKey(mediaRunLogModel);

				redisHandle.addMap(logk, "num", rnum);
				redisHandle.addMap(logk, "lasttime", date.getTime());
				return mediaRunLogModel;
			}
		}
		//新机器 新加redis数据
		redisHandle.addMap(logk, "info", logInfo);
		redisHandle.addMap(logk, "num", 1);
		redisHandle.addMap(logk, "lasttime", date.getTime());
		redisHandle.addMap(logk, "firsttime", date.getTime());

		mediaRunLogDao.insert(mediaRunLogModel);
		redisHandle.addMap(logk, "logId", mediaRunLogModel.getRunLogId());
		return mediaRunLogModel;
	}

	@Override
	public void batchInsert(List<MediaRunLogModel> list) {
		mediaRunLogDao.batchInsert(list);
	}
}
