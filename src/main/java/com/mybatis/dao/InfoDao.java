package com.mybatis.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mybatis.entity.TableWorkbook;
import com.mybatis.mapper.RoleTableDao;
import com.mybatis.util.MySqlHelper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 *  author:zouyang
 *  date:2018-05-11
 *  无对象sql操作，使用StringBuffer来获取数据，拼接成json格式
 *  临时需求流程
 */

public class InfoDao {

	//表权限sql
	@Autowired
	RoleTableDao roleTableDao;
	
	MySqlHelper sqlHelper = new MySqlHelper();
	
	//查询数据库中的表名称,返回表名json集合
	//SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'mybatis' and TABLE_NAME='monthbill';
	//SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'mybatis' and TABLE_NAME like 'monthbill%';--模糊查找表
	public JSONArray QueryTableName(String tablename){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		String sql = "select TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'mybatis' and TABLE_NAME = '"+tablename+"'";
		//String sql = "select TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'mybatis' and TABLE_NAME = '"+tablename+"'";
		ResultSet rs = sqlHelper.executeQuery(sql);
		try {
			while(rs.next()){
				System.out.println(rs.getString(1));
				jsonObject.put("tableName", rs.getString(1));
				jsonArray.add(jsonObject);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonArray;
	}
	
	//查询字典表
	public List<TableWorkbook> QueryTableWorkbook(String sqlwhere){
		String sql = "select tablenamecn,tablenamepy,columnnamecn,columnnamepy from infotablewordbook "+sqlwhere;
		ResultSet rs = sqlHelper.executeQuery(sql);
		List<TableWorkbook> list = new ArrayList<TableWorkbook>();
		try {
			while(rs.next()){
				TableWorkbook tableWorkbook = new TableWorkbook();
				tableWorkbook.setTablenamecn(rs.getString(1));
				tableWorkbook.setTablenamepy(rs.getString(2));
				tableWorkbook.setColumnnamecn(rs.getString(3));
				tableWorkbook.setColumnnamepy(rs.getString(4));
				list.add(tableWorkbook);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}finally {
			sqlHelper.closeResource();
		}
		return list;
	}
	
	//查询字典表DISTINCT
	public List<TableWorkbook> QueryDistictnWorkbook(String sqlwhere){
		String sql = "select DISTINCT tablenamecn,tablenamepy from infotablewordbook "+sqlwhere;
		System.out.println("sql=="+sql);
		ResultSet rs = sqlHelper.executeQuery(sql);
		List<TableWorkbook> list = new ArrayList<TableWorkbook>();
		try {
			while(rs.next()){
				TableWorkbook tableWorkbook = new TableWorkbook();
				tableWorkbook.setTablenamecn(rs.getString(1));
				tableWorkbook.setTablenamepy(rs.getString(2));
				list.add(tableWorkbook);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}finally {
			sqlHelper.closeResource();
		}
		return list;
	}
	
	//查询明细表
	public String QueryInfowhere(String sqlwhere) throws SQLException{
		StringBuffer sb = new StringBuffer();
		String sql = "select * from "+sqlwhere;
		ResultSet rs = sqlHelper.executeQuery(sql);
		ResultSetMetaData ramd = rs.getMetaData();
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
	
	//根据id查询明细表
	public String QueryInfoById(String sqlwhere) throws SQLException{
		StringBuffer sb = new StringBuffer();
		String sql = "select * from "+sqlwhere;
		ResultSet rs = sqlHelper.executeQuery(sql);
		ResultSetMetaData ramd = rs.getMetaData();
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
	
	//修改表结构
	public void alterInfoTable(String sqlwhere){
		System.out.println("修改表结构=="+sqlwhere);
		sqlHelper.executeTableUpdate(sqlwhere);
	}
	
	//查询版本号
	public int queryVersion(String tablename,int id,String version){
		int result = 0;
		String sql = "select id from "+tablename+" where id="+id+" and versionControl='"+version+"'";
		ResultSet rs = sqlHelper.executeQuery(sql);
		try {
			while(rs.next()){
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			sqlHelper.closeResource();
		}
		return result;
	}
	
	//根据id更新临时表单数据
	public int updateInfoById(String sqlwhere){
		System.out.println("根据id修改临时表单=="+sqlwhere);
		return sqlHelper.executeUpdate(sqlwhere);
	}
	
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
	
	//修改表结构
	public int InsertWorkbook(String sql){
		sql = "insert into infotablewordbook (tablenamecn,tablenamepy,columnnamecn,columnnamepy) values"+sql;
		//System.out.println("sql=="+sql);
		return sqlHelper.executeUpdate(sql);
	}
	
	//插入已有模板数据
	public int insertModel(String sql){
		return sqlHelper.executeUpdate(sql);
	}
	
	
	//批量操作 
	//批量事务插入数据
	public boolean AddSqlBatch(String[] sqls){
		//批量插入
		return sqlHelper.executeBatch(sqls);
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
}
