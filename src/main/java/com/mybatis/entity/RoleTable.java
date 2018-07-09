package com.mybatis.entity;


/**
 * @author zouyang
 * Date 2018-5-18
 * 查表权限(部门和个人)
 */
public class RoleTable {
	
	private int id;
	private String tablecn;
	private String tablepy;
	private String dept;
	private String userid;
	private String type;//表示表的类型
	private String role;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTablecn() {
		return tablecn;
	}
	public void setTablecn(String tablecn) {
		this.tablecn = tablecn;
	}
	public String getTablepy() {
		return tablepy;
	}
	public void setTablepy(String tablepy) {
		this.tablepy = tablepy;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
}
