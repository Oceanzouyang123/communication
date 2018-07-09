package com.mybatis.mapper;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.mybatis.entity.User;

/**
 * 
 * @author zouyang
 * Data:2018-5-11
 * 用户表管理
 *
 */


@Transactional
public interface UserDao {

	//根据id查询用户
	List<User> getUser(int id);
	
	//根据用户编号查询用户
	List<User> getUserByUserId(String userId);
	
	//根据部门名称查询用户
	List<User> getDeptByName(User user);
	
	//查询所有用户
	List<User> getUserList();
	
	//分页+条件查询
	List<User> getUserPagerContent(User user);
	
	//根据条件查询获取记录数
	int getCountSqlwhere(User user);
	
	//新增用户
	int insertUser(User user);
	
	//修改用户权限
	int update(User user);
	
	//删除用户
	
}
