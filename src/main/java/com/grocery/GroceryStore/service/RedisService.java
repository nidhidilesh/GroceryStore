package com.grocery.GroceryStore.service;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RedisService {

	@Autowired
	RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private ObjectMapper objMapper;
	
	private Logger log = Logger.getLogger("RedisService");
	
	public <T> T get(String key, Class<T> class1) {
		try {
			Object o = redisTemplate.opsForValue().get(key);
			
			if(o!=null) {
				System.out.println("from getter = "+o);
				return objMapper.readValue(o.toString(), class1);
			}
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("Exception "+e);
			return null;
		} 
	}
	
	public void set(String key, Object o, int ttl) {
		try {
			System.out.println(objMapper.writeValueAsString(o));
			redisTemplate.opsForValue().set(key, objMapper.writeValueAsString(o), ttl, TimeUnit.SECONDS);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}