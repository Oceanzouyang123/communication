package com.mybatis.mapper;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.mybatis.entity.RoleTable;

/**
 * 
 * @author zouyang
 * Data:2018-5-18
 * 查表权限管理
 *
 */

@Transactional
public interface RoleTableDao {
	
	//根据id查询表权限信息
	List<RoleTable> getRoleTableId(int id);
	
	//根据部门查询表权限信息
	List<RoleTable> getRoleTableByDept(RoleTable roleTalbe);
	
	//根据拼音表名查询表权限信息
	List<RoleTable> getRoleTableByTablePY(RoleTable roleTalbe);
	
	//根据中文表名查询表权限信息
	List<RoleTable> getRoleTableByTableCN(RoleTable roleTalbe);
	
	//查询所有表权限信息
	List<RoleTable> getRoleTableList();
	
	//新增表权限
	int insertRoleTable(RoleTable roletable);
	
	//修改表权限信息
	int updateRoleTable(RoleTable roletable);
	
	//删除表权限
	int delRoleTable(int id);
	
	//根据中文表名和表单类型查询
	List<RoleTable> getRoleTableByType(RoleTable roletable);
	
}
