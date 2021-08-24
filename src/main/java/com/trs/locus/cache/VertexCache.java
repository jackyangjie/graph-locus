package com.trs.locus.cache;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;

public class VertexCache {

	// 创建logback的logger
	private static Logger logger = LoggerFactory.getLogger(VertexCache.class);
//    声明一个静态的内存块,guava里面的本地缓存
	private static LoadingCache<String, Vertex> localcache =
			// 构建本地缓存，调用链的方式
			// ,1000是设置缓存的初始化容量，maximumSize是设置缓存最大容量，当超过了最大容量，guava将使用LRU算法（最少使用算法），来移除缓存项
			// expireAfterAccess(12,TimeUnit.HOURS)设置缓存有效期为12个小时
			CacheBuilder.newBuilder().initialCapacity(10000000).maximumSize(10000000).expireAfterAccess(5, TimeUnit.MINUTES)
					// build里面要实现一个匿名抽象类
					.build(new CacheLoader<String, Vertex>() {
//                   这个方法是默认的数据加载实现,get的时候，如果key没有对应的值，就调用这个方法进行加载

						@Override
						public Vertex load(String s) throws Exception {
//                        为什么要把return的null值写成字符串，因为到时候用null去.equal的时候，会报空指针异常
							return null;
						}
					});

	/*
	 * 添加本地缓存
	 */
	public static void put(String key, Vertex value) {
		localcache.put(key, value);
	}

	/*
	 * 得到本地缓存
	 */
	public static Vertex get(String key) {
		Vertex value = null;
		try {
			  value = localcache.get(key);
		} catch (Exception e) {
		}
		return value;
	}
	
	public static void remove(String key) {
		localcache.invalidate(key);
	}

}
