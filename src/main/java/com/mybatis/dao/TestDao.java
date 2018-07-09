package com.mybatis.dao;


/**
 * 
 *  author:zouyang
 *  date:2018-05-10
 *  无对象sql操作，使用StringBuffer来获取数据，拼接成json格式
 *  
 */

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mybatis.entity.RoleTable;
import com.mybatis.util.MySqlHelper;


public class TestDao {

	MySqlHelper sqlHelper = new MySqlHelper();
	
	//无对象查询省份信息
	public String QueryNosqlwhere(String sqlwhere) throws SQLException
	{
		StringBuffer sb = new StringBuffer();
		String sql = "select * from province "+sqlwhere;
		ResultSet rs = sqlHelper.executeQuery(sql);
		ResultSetMetaData ramd = rs.getMetaData();
		//System.out.println(ramd.getColumnCount()+"--"+ramd.getColumnName(2)+"--"+ramd.getColumnType(2));		
		try {
			while(rs.next()){
				sb.append("{");
				String content = "";
				for(int i=1;i<=ramd.getColumnCount();i++){
					content = content + "\""+ramd.getColumnName(i)+"\""+":"+"\""+rs.getString(i)+"\",";
					//sb.append(content);
				}
				content = content.substring(0, content.length()-1);
				sb.append(content+"},");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			sqlHelper.closeResource();
		}		
		if(sb.length()>1){
			String result = "["+sb.substring(0,sb.length()-1)+"]";
			return result;
		}else{
			return "";
		}			
	}
	
	//动态建表
	public int CreateTable(String createSql){
		//动态建表
		int result = sqlHelper.executeUpdate(createSql);
		//动态插入数据
/*		result = sqlHelper.executeUpdate(insertSqls);
		//插入字典表
		boolean bool = false;
		if(result>0){
			bool =InsertTableWorkbook(sqls);
		}	*/	
		return result;
	}
	
	//插入字典表
	public boolean InsertTableWorkbook(String[] sqls){
		
		return sqlHelper.executeBatch(sqls);
	}
	
	
	//查询表名称
	//SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'mybatis' and TABLE_NAME='monthbill';
	//SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'mybatis' and TABLE_NAME like 'monthbill%';--模糊查找表
	public String QueryTableName(){
		StringBuffer sb = new StringBuffer();
		//String sql = "select TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'mybatis' and TABLE_NAME like 'monthbill%'";
		String sql = "select TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'mybatis' and TABLE_NAME = 'monthbill'";
		ResultSet rs = sqlHelper.executeQuery(sql);
		try {
			while(rs.next()){
				System.out.println(rs.getString(1));
				sb.append(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	//根据中文表名查询权限表
	public List<RoleTable> QueryRoleTableNameCN(String tablenamecn){
		String sql = "select id,tablecn,tablepy,dept,userid from roletable where tablecn='"+tablenamecn+"'";
		ResultSet rs = sqlHelper.executeQuery(sql);
		List<RoleTable> list = new ArrayList<RoleTable>();
		try {
			while(rs.next()){
				RoleTable roleTable = new RoleTable();
				roleTable.setId(rs.getInt(1));
				roleTable.setTablecn(rs.getString(2));
				roleTable.setTablepy(rs.getString(3));
				roleTable.setDept(rs.getString(4));
				roleTable.setUserid(rs.getString(5));
				list.add(roleTable);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
