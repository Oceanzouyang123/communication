package com.mybatis.util;

import com.mybatis.util.MySqlHelper;

public class BatchDao {
	MySqlHelper sqlHelper = new MySqlHelper();

	//批量事务插入数据
	public boolean AddMonthBillBatch(String[] sqls){
		//批量插入
		return sqlHelper.executeBatch(sqls);
	}
	
}
