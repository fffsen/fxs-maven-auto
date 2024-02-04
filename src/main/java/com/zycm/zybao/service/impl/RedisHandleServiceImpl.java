package com.zycm.zybao.service.impl;

import com.zycm.zybao.common.redis.RedisHandle;
import com.zycm.zybao.service.interfaces.RedisHandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("redisHandleService")
public class RedisHandleServiceImpl implements RedisHandleService {

	@Autowired(required = false)
	private RedisHandle redisHandle;

	@Override
	public Object get(String key) {
		return redisHandle.get(key);
	}

	@Override
	public void remove(String key) {
		redisHandle.remove(key);
	}

	@Override
	public void set(String key, Object value) {
		redisHandle.set(key, value);
	}

	@Override
	public void set(String key, Object value, Long expireTime) {
		redisHandle.set(key, value, expireTime);
	}

	@Override
	public void addMap(String key, String field, Object value) {
		redisHandle.addMap(key, field, value);
	}
}
