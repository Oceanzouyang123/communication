package com.mybatis.mapper;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.mybatis.entity.MonthBill;

@Transactional
public interface MonthBillDao {
	
	//根据id查询monthbill对象
	MonthBill getMonthbill(int id);
	
	//查询所有
	List<MonthBill> getMonthbillList();
	
	//批量插入
	@Transactional
	void insertBatch(List<MonthBill> list);
	
	//分页
	List<MonthBill> getMonthbillPager(int rowStart,int pageSize);
	
	//多条件匹配查询
	List<MonthBill> getMonthbillSqlWhere(MonthBill monthBill);
	//List<MonthBill> getMonthbillSqlWhere(String sqlwhere);
	
	//分页+多条件匹配查询
	List<MonthBill> getMonthbillPagerContent(MonthBill monthBill);
	
	//根据条件查询获取记录数
	int getCountSqlwhere(MonthBill monthBill);
	
	//获取目的地组别
	List<MonthBill> getDestination();
	
	//调用存储过程
	List<MonthBill> getMonthBillProcedure(MonthBill monthBill);
}
