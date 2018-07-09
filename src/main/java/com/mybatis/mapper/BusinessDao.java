package com.mybatis.mapper;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.mybatis.entity.Business;

/**
 * 
 * @author zouyang
 * Data:2018-5-18
 * 流程状态管理
 *
 */

@Transactional
public interface BusinessDao {

	//根据id查询流程信息
	List<Business> getBusinessById(int id);
	
	//查询所有流程信息
	List<Business> getBusinessList();
	
	//新增流程
	int insertBusiness(Business business);
	
	//修改流程信息
	int updateBusiness(Business business);
		
	//删除流程
	int delBusiness(int id);
	
}
