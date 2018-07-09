package com.mybatis.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MySqlHelper {
    private Connection conn = null;

	private Statement stmt = null;

	private PreparedStatement prestmt = null;

	private ResultSet rs = null;
    
	static
	{
		try
		{
			//Class.forName("org.postgresql.Driver");
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e)
		{
			System.err.println("driver error " + e.getMessage());
		}
	}
	
	public void getConnection()
	{
		try
		{
			String URL = "jdbc:mysql://localhost:3306/mybatis?useUnicode=true&characterEncoding=utf-8&useSSL=false";
			//String URL = "jdbc:mysql://10.44.8.103:3306/mybatis?useUnicode=true&characterEncoding=utf-8&useSSL=false";
			String userName = "root";
			String Password = "123456";
//			String URL = "jdbc:postgresql://localhost:5432/test";
//			String userName = "postgres";
//			String Password = "123456";
			conn = DriverManager.getConnection(URL, userName, Password);
			conn.setAutoCommit(false);
		} catch (SQLException e)
		{
			System.err.println("connection error " + e.getMessage());
		}
	}
	
	public void createStatement()
	{
		getConnection();
		try
		{
			stmt = conn.createStatement();
		} catch (SQLException e)
		{
			System.err.println("createStatement error" + e.getMessage());
		}
	}
	
	public void createPrepareStatement(String sql)
	{
		getConnection();
		try
		{
			prestmt = conn.prepareStatement(sql);
		} catch (SQLException e)
		{
			System.err.println("createPrepareStatement " + e.getMessage());
		}
	}
	
	public ResultSet executeQuery(String sql)
	{
		createStatement();
		try
		{
			rs = stmt.executeQuery(sql);
		} catch (SQLException e)
		{
			System.err.println(e.getMessage());
		}
		return rs;
	}
	
	//统计总数
	public int executeCountQuery(String sql)
	{
		int count = 0;
		createStatement();
		try
		{
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				count = rs.getInt(1);
			}			
		} catch (SQLException e)
		{
			System.err.println(e.getMessage());
		}
		return count;
	}
	
	public int executeUpdate(String sql)
	{
		int result = 0;
		createStatement();
		try
		{
			result = stmt.executeUpdate(sql);			
			commit();
		} catch (SQLException e)
		{
			rollBack();
			System.out.println(e.getMessage());
		} finally
		{
			closeResource();
		}
		return result;
	}
	
	//修改表结构
	public int executeTableUpdate(String sql)
	{
		int result = 0;
		createStatement();
		try
		{
			System.out.println("错误sql"+sql);
			result = stmt.executeUpdate(sql);			
		} catch (SQLException e)
		{
			rollBack();
			System.out.println(e.getMessage());
		} finally
		{
			closeResource();
		}
		return result;
	}
	
	public ResultSet executeQuery(String sql, String[] args)
	{
		createPrepareStatement(sql);
		try
		{
			if (args != null && args.length != 0)
			{
				for (int i = 0; i < args.length; i++)
				{
					prestmt.setString(i + 1, args[i]);
				}
			}
			rs = prestmt.executeQuery();
		} catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
		return rs;
	}
	
	public int executeUpdate(String sql, String[] args)
	{
		createPrepareStatement(sql);
		int result = 0;
		try {
		if(args != null && args.length != 0)
		{
			for(int i = 0; i < args.length; i++)
			{
			   prestmt.setString(i + 1, args[i]);	
			}
		}
		result = prestmt.executeUpdate();
		commit();
		}catch(SQLException e)
		{
			rollBack();
			System.err.println(e.getMessage());
		}finally
		{
		    closeResource();	
		}
		return result;
	}
	
	//批量插入或修改
	public boolean executeBatch(String[] sqls)
	{ 
		createStatement();
		boolean isCorrect = false;
		try {
			if(sqls != null && sqls.length > 0)
			{
				for(int i = 0; i < sqls.length; i++)
				{
				   System.out.println(sqls[i]);
				   stmt.addBatch(sqls[i]);	
				}
			}
			stmt.executeBatch();
			commit();
			isCorrect = true;
		}catch(SQLException e)
		{
			rollBack();
			System.err.println(e.getMessage());
		}finally {
			closeResource();
		}
		return isCorrect;
	}

	private void commit()
	{
		try
		{
			conn.commit();
		} catch (SQLException e)
		{
			System.err.println(e.getMessage());
		}
	}

	private void rollBack()
	{
		try
		{
			conn.rollback();
		} catch (SQLException e)
		{
			System.err.println(e.getMessage());
		}
	}
	
	public void closeResource()
	{
		try
		{
			if (rs != null)
			{
				rs.close();
				rs = null;
			}
			if (stmt != null)
			{
				stmt.close();
				stmt = null;
			}
			if (prestmt != null)
			{
				prestmt.close();
				prestmt = null;
			}
			if (conn != null)
			{
				conn.close();
				conn = null;
			}
		} catch (SQLException e)
		{
			System.err.println(e.getMessage());
		}
	}
}
