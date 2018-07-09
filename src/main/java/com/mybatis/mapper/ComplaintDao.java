package com.mybatis.mapper;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.mybatis.entity.Complaint;

/**
 * 
 * @author zouyang
 * Data:2018-5-24
 * 投诉sql
 *
 */


@Transactional
public interface ComplaintDao {
	
	//分页+条件查询投诉
	List<Complaint> getComplaintPagerContent(Complaint complaint);
	
	//新增投诉
	int insertComplaint(Complaint complaint);

	//日志记录总数
	int getCountSqlwhere(Complaint complaint);
	
	//根据id获取投诉信息
	List<Complaint> getComplaintById(Complaint complaint);
	
	//修改投诉信息
	int updateComplaintById(Complaint complaint);
	
	//获取前10条投诉记录
	List<Complaint> getComplaintByTop(String userId);
}
