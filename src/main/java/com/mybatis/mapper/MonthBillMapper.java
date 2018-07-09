package com.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mybatis.entity.MonthBill;

public interface MonthBillMapper {
	@Select("SELECT billno,chargedweight,producttype,price,discount,destination FROM monthbill")
	//Result为了别名
//	@Results({
//		@Result(property = "nickName", column = "nickName")
//	})
	List<MonthBill> getAll();
	
	@Select("SELECT * FROM monthbill WHERE id = #{id}")
	MonthBill getOne(int id);

	@Insert("INSERT INTO monthbill(billno,chargedweight,producttype) VALUES(#{billno}, #{chargedweight}, #{producttype})")
	void insert(MonthBill monthBill);

	@Update("UPDATE monthbill SET billno=#{billno},chargedweight=#{chargedweight} WHERE id =#{id}")
	void update(MonthBill monthBill);
	
	@Delete("DELETE FROM monthbill WHERE id =#{id}")
	void delete(int id);
	
	//获取表总数
	@Select("SELECT count(*) FROM monthbill")
	int QueryTotal();
	
	//分页查询
	@Select("select billno,chargedweight,producttype,price,discount,destination from monthbill where id >= (select id FROM monthbill ORDER BY id LIMIT #{0}, 1) LIMIT #{1}")
	List<MonthBill> queryPager(int rowStart,int pageSize);
	
	//查询产品类型
	@Select("select producttype from monthbill GROUP BY producttype")
	List<MonthBill> queryProductType();
}
