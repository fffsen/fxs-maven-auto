package com.zycm.zybao.common.listener;

import com.zycm.zybao.common.config.RedisConfig;
import com.zycm.zybao.common.redis.RedisHandle;
import com.zycm.zybao.dao.MediaAttributeDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @ClassName: SpringAfterLoadRedisListener
* @Description: spring加载完成后 加载数据到redis中
* @author sy
* @date 2019年8月10日
*
*/
@Slf4j
@Service
public class SpringAfterLoadRedisListener implements ApplicationListener<ContextRefreshedEvent>{

	@Autowired
	private MediaAttributeDao mediaAttributeDao;

	@Autowired
	private RedisHandle redisHandle;

	private String redis_prefix = RedisConfig.redisPrefix;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() == null){
            //查询所有未激活的终端
			log.debug("\n#####开始加载未激活的终端数据到redis中############################");
			Map<String,Object> condition = new HashMap<String,Object>();
			condition.put("workStatus", 0);
			List<Map<String,Object>> distrustMedia = mediaAttributeDao.selectByCondition(condition);
			int a = 0;
			if(distrustMedia.size() > 0){
				redisHandle.remove(redis_prefix+"distrust_media");
				for (Map<String, Object> map : distrustMedia) {
					String machineCode = map.get("machineCode").toString();
					if(StringUtils.isNotBlank(machineCode)){
						redisHandle.addMap(redis_prefix+"distrust_media", machineCode, 1);
						a++;
					}
				}
			}
			log.info("\n#####扫描到"+distrustMedia.size()+"个未激活的终端，加载了"+a+"条数据到redis####");
        }
	}

}
