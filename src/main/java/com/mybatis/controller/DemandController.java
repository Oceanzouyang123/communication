package com.mybatis.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mybatis.dao.DemandDao;
import com.mybatis.entity.Logoperate;
import com.mybatis.entity.RoleTable;
import com.mybatis.entity.TableWorkbook;
import com.mybatis.entity.User;
import com.mybatis.mapper.LogDao;
import com.mybatis.mapper.RoleTableDao;
import com.mybatis.mapper.UserDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 *  Author:zouyang
 * 	Data:2018-5-10
 *  创建临时需求表单
 * 
 */

@Controller
public class DemandController {
	
	DemandDao demandDao = new DemandDao();
	
	//日志记录
	@Autowired
	LogDao logDao;
	
	//用户sql
	@Autowired
	UserDao userDao;
	
	//表权限sql
	@Autowired
	RoleTableDao roleTableDao;
	
	//进入创建临时需求表单
	@RequestMapping("demand")
	public String demand(HttpServletRequest request,HttpServletResponse response){		
		System.out.println("进入需求表单功能");
		return "demand/addTempDemand";
	}
	
	//查询字典表(Distict)
	@RequestMapping("QueryTableWorkbook")
	@ResponseBody
	public synchronized JSONArray QueryTableWorkbook(HttpServletRequest request,HttpServletResponse response,String tablename,String getDemandTable,String dept){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		//判断是否新增，getDemandTable不为空表示新增
		if(getDemandTable == null || getDemandTable.length() <= 0){
			//tablename为空则表示查询部门
			if(tablename == null || tablename.length() <= 0){
				System.out.println("需求权限查询==");
				//权限查询部门
				RoleTable roleTable = new RoleTable();
				roleTable.setDept(dept);
				roleTable.setType("需求");				
				List<RoleTable> roleTableList = roleTableDao.getRoleTableByDept(roleTable);
				jsonObject.put("data", roleTableList);
				jsonObject.put("dept", 1);
				jsonArray.add(jsonObject);
			}else{
				System.out.println("输入需求表名查询=="+tablename);
				//查询该表拼音 //修改成先查询权限表，获取权限表的表名list然后根据权限表的listtablename再查询字典表
				RoleTable roleTable = new RoleTable();
				roleTable.setDept(dept);
				roleTable.setType("需求");
				roleTable.setTablecn(tablename);
				List<RoleTable> roleTableList = roleTableDao.getRoleTableByDept(roleTable);
				if(roleTableList.size()>0){
					List<TableWorkbook> list = demandDao.QueryDistictnWorkbook(" where tablenamecn = '"+roleTableList.get(0).getTablecn()+"'");
					jsonObject.put("data", list);
				}
				jsonObject.put("dept", 0);
				jsonArray.add(jsonObject);
			}
		}else{
			System.out.println("新增需求查询=="+getDemandTable);
			//查询该表拼音
			List<TableWorkbook> list = demandDao.QueryDistictnWorkbook(" where tablenamepy ='"+getDemandTable+"'");
			jsonObject.put("data", list);
			jsonObject.put("dept", 0);
			jsonArray.add(jsonObject);
		}		
		return jsonArray;
	}
	
	//查询字典表明细
	@RequestMapping("QueryAllTableWorkbook")
	@ResponseBody
	public synchronized JSONArray QueryAllTableWorkbook(HttpServletRequest request,HttpServletResponse response,String tablename){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		List<TableWorkbook> list = demandDao.QueryTableWorkbook(" where tablenamepy ='"+tablename+"'");
		jsonObject.put("data", list);
		jsonArray.add(jsonObject);			
		return jsonArray;
	}
	
	//查询明细表内容queryDemandTable
	@RequestMapping("queryDemandTable")
	@ResponseBody
	public synchronized String queryDemandTable(HttpServletRequest request,HttpServletResponse response,String tablename) throws SQLException{
		System.out.println("开始查询明细表"+tablename);
		String result  = demandDao.QueryDemandwhere(tablename);
		return result;
	}
	
	//进入明细表
	@RequestMapping("demandTableDetial")
	public synchronized String demandTableDetial(HttpServletRequest request,HttpServletResponse response,String tablename,ModelMap modelMap){	
		modelMap.addAttribute("demandTable", tablename);
		return "demand/demandTableDetial";
	}
	
	//查询
	@RequestMapping("DataTableHtml")
	public synchronized String DataTableHtml(HttpServletRequest request,HttpServletResponse response,String newtable,ModelMap model){	
		String userid = request.getUserPrincipal().getName();
		List<User> userList = userDao.getUserByUserId(userid);
		String dept = userList.get(0).getDept();
		model.addAttribute("dept", dept);
		model.addAttribute("demandTable", newtable);
		return "demand/demandTableList";
	}
	
	
	//动态建表
	@RequestMapping("CreateTable")
	@ResponseBody
	public synchronized int CreateTable(HttpServletRequest request,HttpServletResponse response,String tablePY,String columnId){	
		System.out.println("tablename=="+tablePY+"--"+"columnId=="+columnId);
		String createTableSql = "CREATE TABLE "+tablePY+" ("
				+"id int(11) NOT NULL AUTO_INCREMENT,"
				+"versionControl varchar(50),"
				+"status varchar(20),"
				+columnId
				+"PRIMARY KEY (id)"
		        +") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		//System.out.println("createTableSql=="+createTableSql);		
		int result = demandDao.CreateTable(createTableSql);
		return result;
	}
	
	//插入动态表
	@RequestMapping("insertDemandTable")
	@ResponseBody
	public synchronized boolean insertDemandTable(HttpServletRequest request,String tablenamecn,String tablenamepy,String insertSql,String insertWordbook,String dept,String chooseValue){
//		System.out.println("tablenamecn=="+tablenamecn);
//		System.out.println("tablenamepy=="+tablenamepy);
//		System.out.println("tablenamepy=="+insertSql);
//		System.out.println("tablenamepy=="+insertWordbook);
		String[] sqls = new String[5];
		sqls[0] = "insert into tablewordbook(tablenamecn,tablenamepy,columnnamecn,columnnamepy) values ('"+tablenamecn+"','"+tablenamepy+"','版本','versonControl')";
		sqls[1] = "insert into tablewordbook(tablenamecn,tablenamepy,columnnamecn,columnnamepy) values ('"+tablenamecn+"','"+tablenamepy+"','状态','status')";
		sqls[2] = "insert into tablewordbook(tablenamecn,tablenamepy,columnnamecn,columnnamepy) values "+insertWordbook; 
		sqls[3] = "insert into "+tablenamepy+" "+insertSql; 
		sqls[4] = "insert into roletable(tablecn,tablepy,dept,userid,type,role) values ('"+tablenamecn+"','"+tablenamepy+"','"+dept+"','"+request.getUserPrincipal().getName()+"','需求','"+chooseValue+"')";
		boolean bool  = demandDao.AddSqlBatch(sqls);
		if(bool){
			//插入日志
			Logoperate logoperate = new Logoperate();
			logoperate.setUserid(request.getUserPrincipal().getName());
			logoperate.setContent("新增需求上报:"+tablenamecn);
			logDao.insertLog(logoperate);
		}		
		return bool;
	}
	
	//页面跳转到新增修改页
	@RequestMapping("demandByIdHtml")
	public synchronized String demandByIdHtml(HttpServletRequest request,HttpServletResponse response,int id,String demandTableName,ModelMap modelMap){		
		System.out.println("进入id=="+id);
		modelMap.addAttribute("demandId", id);
		modelMap.addAttribute("demandTable", demandTableName);
		return "demand/addTempDemand";
	}
	
	//根据id获取table
	@RequestMapping("demandById")
	@ResponseBody
	public synchronized String demandById(HttpServletRequest request,HttpServletResponse response,String tablename,String id) throws SQLException{
		System.out.println(tablename+"--"+id);
		String result = "";
		//表名为空表示未传递id过来,不是修改
		if(tablename == null || tablename.length() <= 0){
			
		}else{
			String sqlwhere = tablename+" where id="+id;
			result = demandDao.QueryDemandwhere(sqlwhere);
		}		
		return result;
	}
	
	//修改表结构
	//alter table tablename add transactor1 varchar(10),add transactor2 varchar(10);
	@RequestMapping("alterDemandByTable")
	@ResponseBody
	public synchronized int alterDemandByTable(String tablename,int start,int end){
		System.out.println("进入到修改表结构--"+tablename+"--"+start+"--"+end);
		String column = "";
		for(int i=start;i<end;i++){
			column = column+" add "+tablename+i+" varchar(200),";
		}
		column = column.substring(0,column.length()-1);
		String sqlwhere = "alter table "+tablename+column+";";
		demandDao.alterDemandTable(sqlwhere);
		return 0;
	}
	
	//修改临时表 动态添加字段alter table tablename add transactor1 varchar(10),add transactor2 varchar(10);
	@RequestMapping("updateDemandById")
	@ResponseBody
	public synchronized int updateDemandById(HttpServletRequest request,HttpServletResponse response,String tablename,String canshu,int id,String version,String dept,String chooseValue){
		System.out.println("进入修改临时需求表单数据");
		int result = 0;
		//先判断版本号，然后再更新表
		result = demandDao.queryVersion(tablename, id, version);
		if(result==0){
			return 0;
		}else{
			String[] sqls = new String[2];
			sqls[0] = "update "+tablename +" set "+canshu+" where id="+id;
			sqls[1] = "update roletable set dept='"+dept+"',role='"+chooseValue+"' where tablepy='"+tablename+"'";		
			boolean bool = demandDao.AddSqlBatch(sqls);
			if (bool) {
				result = 1;
			}
			if(result>0){
				//日志记录
				Logoperate logoperate = new Logoperate();
				logoperate.setUserid(request.getUserPrincipal().getName());
				RoleTable roleTable = new RoleTable();
				roleTable.setTablepy(tablename);
				roleTable.setType("需求");
				List<RoleTable> roleTableList = roleTableDao.getRoleTableByTablePY(roleTable);
				logoperate.setContent("修改了 "+roleTableList.get(0).getTablecn());
				logDao.insertLog(logoperate);
			}
			return result;
		}			
	}
	
	//修改模板
	@RequestMapping("updateModelDemandById")
	@ResponseBody
	public synchronized int updateModelDemandById(HttpServletRequest request,HttpServletResponse response,String tablename,String canshu,int id,String version,String updateLog){
		System.out.println("进入修改模板需求表单数据");
		int result = 0;
		//先判断版本号，然后再更新表
		result = demandDao.queryVersion(tablename, id, version);
		if(result==0){
			return 0;
		}else{
			String sql = "update "+tablename +" set "+canshu+" where id="+id;		
			result = demandDao.updateDemandById(sql);
			if(result>0){
				//日志记录
				Logoperate logoperate = new Logoperate();
				logoperate.setUserid(request.getUserPrincipal().getName());
				RoleTable roleTable = new RoleTable();
				roleTable.setTablepy(tablename);
				roleTable.setType("需求");
				List<RoleTable> roleTableList = roleTableDao.getRoleTableByTablePY(roleTable);
				logoperate.setContent("修改需求表:"+roleTableList.get(0).getTablecn() +updateLog);
				logDao.insertLog(logoperate);
			}
			return result;
		}			
	}
	
	
	//插入到字典表
	@RequestMapping("insertWorkbookById")
	@ResponseBody
	public int insertWorkbookById(String canshu,String tablename,int id){
		int result = demandDao.InsertWorkbook(canshu);
		System.out.println("resuldemandTableDetialt=="+result);
		return result;
	}
	
	//根据权限表查询对应用户的需求
	@RequestMapping("getRemand")
	@ResponseBody
	public synchronized JSONArray getRemand(HttpServletRequest request){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		RoleTable roleTable = new RoleTable();
		List<User> userList = userDao.getUserByUserId(request.getUserPrincipal().getName());
		roleTable.setDept(userList.get(0).getDept());
		roleTable.setType("需求");
		List<RoleTable> roleTableList = roleTableDao.getRoleTableByDept(roleTable);
		jsonObject.put("data", roleTableList);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
}
