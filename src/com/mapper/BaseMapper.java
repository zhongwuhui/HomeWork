package com.mapper;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface BaseMapper<T> {
	
		// 删除记录
		public void delete(int id) throws Exception;

		// 更新记录
		public int update(T t) throws Exception;

		// 添加记录
		public int add(T t) throws Exception;

		// 得到查询数量
		public Long getTotal(Map map) throws Exception;

		// 条件分页查询
		public List<T> find(Map map) throws Exception;
}

