package com.mybatis.entity;

/**
 * 
 * @author zouyang
 * Data:2018-5-18
 * 业务流程状态描述
 * 
 */
public class Business {
	
	private int id;
	private String businessstatus;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBusinessstatus() {
		return businessstatus;
	}
	public void setBusinessstatus(String businessstatus) {
		this.businessstatus = businessstatus;
	}
		
}
