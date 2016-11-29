package com.sjsu.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;



public class RedisDBServiceImpl implements IDBService {
	
	Jedis redis;
	static JedisPool pool;
	
	public synchronized Jedis getJedisConnection() throws JedisConnectionException {
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig(); 
		 	config.setMaxTotal(12); 
		 	config.setMaxIdle(4);
		 	config.setMinIdle(1);
		 	config.setTestOnBorrow(true);
		 	pool = new JedisPool(config, "localhost", 6379); 
		}
		return pool.getResource();
	}


	public RedisDBServiceImpl() {
		redis = getJedisConnection();
	}


	public byte[] serialize(Object obj) throws IOException {
		try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
			try (ObjectOutputStream o = new ObjectOutputStream(b)) {
				o.writeObject(obj);
			}
			return b.toByteArray();
		}
	}

	public Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
			try (ObjectInputStream o = new ObjectInputStream(b)) {
				return o.readObject();
			}
		}
	}

	
	@Override
	public boolean containsKey(String key) {
		
		try {
			return redis.exists(serialize(key))? true:false;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
	}

	
	public boolean containsKeyAndSequence(String key, int seqID) {
		
		boolean keySeqExists = false;
		try {
			if (containsKey(key)) {
				byte[] parsedKey = serialize(key);
				byte[] parseId = serialize(seqID);
				if (redis.hexists(parsedKey, parseId)) {
					keySeqExists = true;
					return keySeqExists;
				}
				
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
	}

	/**
	 * Retrieves data stored at the key
	 * 
	 * @param key String value given for a string
	 * @return map HashMap stored corresponding to the key with Ids and values
	 */
	@Override
	public Map<Integer, byte[]> get(String key) {
		Map<Integer, byte[]> DataMap = new HashMap<Integer, byte[]>();
		try {
			if (containsKey(key)) {
				byte[] parsedKey = serialize(key);
				Set<byte[]> byteIds = redis.hkeys(parsedKey);
				for (byte[] bci : byteIds) {
					DataMap.put((Integer) deserialize(bci), redis.hget(parsedKey, bci));
				}
				
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DataMap;
	}
	
	/**
	 * To get the no. of s to discard excess
	 * @param fileName
	 * @return
	 */
	@Override
	public int noExisting(String fileName) {
		int Count = 0;
		IDBService redisClient = new RedisDBServiceImpl() ;
		if(redisClient.containsKey(fileName)){
			
			try {
				byte[] parsedKey;
				parsedKey = serialize(fileName);
				Set<byte[]> byteIds = redis.hkeys(parsedKey);
				Count = byteIds.size();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		
		
		return Count;
	}
	
	
	/**
	 * Deletes the records after no. of records
	 * @param key
	 * @param count
	 * @return 
	 */
	@Override
	public boolean deleteExcess(String key, int count) {
		
		boolean isDeleted = false;
		try {
			if (containsKey(key)) {
				byte[] serializedKey = serialize(key);
				Set<byte[]> byteSequencIds = redis.hkeys(serializedKey);
				for (byte[] bs : byteSequencIds) {
					Integer byteSeq = -1;
					byteSeq = (Integer) deserialize(bs);
					if(byteSeq > count){
						redis.hdel(serializedKey, bs);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isDeleted;
	}


	/**
	 * Get all the Ids corresponding to a Key
	 * @param key String key for which data is needed
	 * @return List of all Ids
	 */
	@Override
	public List<Integer> getIDs(String key) {
		List<Integer> IDs = new ArrayList<Integer>();
		try {
			if (containsKey(key)) {
				byte[] parsedKey = serialize(key);
				Set<byte[]> byteIds = redis.hkeys(parsedKey);
				for (byte[] bci : byteIds) {
					IDs.add((Integer) deserialize(bci));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return IDs;
	}

	/** 
	 * For updating a file
	 * 
	 */
	@Override
	public boolean put(String key, int ID, byte[] value) {
		//TODO handle is file is smaller.
		boolean done = false;
		try {
			if (containsKey(key)) {
				byte[] parsedKey = serialize(key);
				byte[] parseId = serialize(ID);
				if (redis.hexists(parsedKey, parseId)) {
					//Does on hash level
					redis.hset(parsedKey, parseId, value);
					done = true;
				}
			
				
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return done;
	}


	@Override
	protected void finalize() throws Throwable {

		if (redis != null) {
			redis.close();
		}
		super.finalize();
	}

	
	

	@Override
	public String post(String key, int ID, byte[] value)  {
		if (key == null || value == null) {
			return null;
		}
		try {
			if(containsKeyAndSequence(key, ID)){
				System.out.println("Key already present will not update existing one. Please use PUT operation yo modify.");
				//logger.info("Key already present will not update existing one. Please use PUT operation yo modify.");
			}else{
				redis.hset(serialize(key), serialize(ID), value);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return key;
	}


	public String post(byte[] value) {
		if (value == null) {
			return null;
		}
		String uuid = UUID.randomUUID().toString();
		String key = post(uuid, 1, value);
		return key;
	}


	@Override
	public boolean delete(String key) {
		
		boolean isDeleted = false;
		Map<Integer, byte[]> removedMap = new HashMap<Integer, byte[]>();
		try {
			if (containsKey(key)) {
				byte[] serializedKey = serialize(key);
				Set<byte[]> byteSequencIds = redis.hkeys(serializedKey);
				for (byte[] bs : byteSequencIds) {
					removedMap.put((Integer) deserialize(bs), redis.hget(serializedKey, bs));
					redis.hdel(serializedKey, bs);
					isDeleted = true;
					
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return isDeleted;
	}

	

	@Override
	public String getDatabaseType() {
		// TODO Auto-generated method stub
		return "Redis Database";

	}

}
