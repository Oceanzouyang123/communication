package com.mybatis.mapper;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.mybatis.entity.Dept;

/**
 * 
 * @author zouyang
 * Data:2018-5-16
 * 部门表管理
 *
 */

@Transactional
public interface DeptDao {

	//根据id查询部门信息
	List<Dept> getDeptById(int id);
	
	//根据部门名称查询部门信息
	List<Dept> getDeptByName(String name);
	
	//查询所有部门信息
	List<Dept> getDeptList();
	
	//新增部门
	int insertDept(Dept dept);
	
	//修改部门信息
	int updateDept(Dept dept);
		
	//删除部门
	int delDept(int id);
	
}
