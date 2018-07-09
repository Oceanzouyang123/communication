package com.mybatis.mapper;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.mybatis.entity.Logoperate;

/**
 * 
 * @author zouyang
 * Data:2018-5-16
 * 日志查询
 *
 */

@Transactional
public interface LogDao {
	
	//分页+条件查询日志
	List<Logoperate> getLogPagerContent(Logoperate log);
	
	//新增日志
	int insertLog(Logoperate log);

	//日志记录总数
	int getCountSqlwhere(Logoperate log);
	
}
