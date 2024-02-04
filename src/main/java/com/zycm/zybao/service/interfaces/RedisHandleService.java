package com.zycm.zybao.service.interfaces;

public interface RedisHandleService {

	 Object get(String key);
	 void remove(String key);
	 void set(String key, Object value);
	 void set(String key, Object value, Long expireTime);
	 void addMap(String key, String field, Object value);
}
