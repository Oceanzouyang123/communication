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

import com.mybatis.dao.FlowDao;
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
 * @author zouyang
 * Data:2018-5-10
 * 创建临时流程表单
 * 
 */

@Controller
public class FlowController {

	FlowDao flowDao = new FlowDao();
	
	//日志记录
	@Autowired
	LogDao logDao;
		
	//用户sql
	@Autowired
	UserDao userDao;
		
	//表权限sql
	@Autowired
	RoleTableDao roleTableDao;
		
	//进入创建临时流程表单
	@RequestMapping("flow")
	public String test(){
		System.out.println();
		return "flow/addTempFlow";
	}
	
	//查询字典表(Distict)
	@RequestMapping("flowQueryTableWorkbook")
	@ResponseBody
	public synchronized JSONArray flowQueryTableWorkbook(HttpServletRequest request,HttpServletResponse response,String tablename,String getFlowTable,String dept){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		//判断是否新增，getFlowTable不为空表示新增
		if(getFlowTable == null || getFlowTable.length() <= 0){
			//tablename为空则表示查询部门
			if(tablename == null || tablename.length() <= 0){
				System.out.println("流程权限查询==");
				//权限查询部门
				RoleTable roleTable = new RoleTable();
				roleTable.setDept(dept);
				roleTable.setType("流程");				
				List<RoleTable> roleTableList = roleTableDao.getRoleTableByDept(roleTable);
				jsonObject.put("data", roleTableList);
				jsonObject.put("dept", 1);
				jsonArray.add(jsonObject);
			}else{
				System.out.println("输入表名查询=="+tablename);
				//查询该表拼音 //修改成先查询权限表，获取权限表的表名list然后根据权限表的listtablename再查询字典表
				RoleTable roleTable = new RoleTable();
				roleTable.setDept(dept);
				roleTable.setType("流程");
				roleTable.setTablecn(tablename);
				List<RoleTable> roleTableList = roleTableDao.getRoleTableByDept(roleTable);
				if(roleTableList.size()>0){
					List<TableWorkbook> list = flowDao.QueryDistictnWorkbook(" where tablenamecn = '"+roleTableList.get(0).getTablecn()+"'");
					jsonObject.put("data", list);
				}
				jsonObject.put("dept", 0);
				jsonArray.add(jsonObject);
			}
		}else{
			System.out.println("新增流程查询=="+getFlowTable);
			//查询该表拼音
			List<TableWorkbook> list = flowDao.QueryDistictnWorkbook(" where tablenamepy ='"+getFlowTable+"'");
			jsonObject.put("data", list);
			jsonObject.put("dept", 0);
			jsonArray.add(jsonObject);
		}		
		return jsonArray;
	}
	
	//查询字典表明细
	@RequestMapping("flowQueryAllTableWorkbook")
	@ResponseBody
	public synchronized JSONArray flowQueryAllTableWorkbook(HttpServletRequest request,HttpServletResponse response,String tablename){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		List<TableWorkbook> list = flowDao.QueryTableWorkbook(" where tablenamepy ='"+tablename+"'");
		jsonObject.put("data", list);
		jsonArray.add(jsonObject);			
		return jsonArray;
	}
	
	//查询表细表内容queryFlowTable
	@RequestMapping("queryFlowTable")
	@ResponseBody
	public synchronized String queryFlowTable(HttpServletRequest request,HttpServletResponse response,String tablename) throws SQLException{
		System.out.println("开始查询流程表细表"+tablename);
		String result  = flowDao.QueryFlowwhere(tablename);
		return result;
	}
	
	//进入明细表
	@RequestMapping("flowTableDetial")
	public synchronized String flowTableDetial(HttpServletRequest request,HttpServletResponse response,String tablename,ModelMap modelMap){	
		modelMap.addAttribute("flowTable", tablename);
		return "flow/flowTableDetial";
	}
	
	//查询
	@RequestMapping("flowDataTableHtml")
	public synchronized String DataTableHtml(HttpServletRequest request,HttpServletResponse response,String newtable,ModelMap model){	
		String userid = request.getUserPrincipal().getName();
		List<User> userList = userDao.getUserByUserId(userid);
		String dept = userList.get(0).getDept();
		model.addAttribute("dept", dept);
		model.addAttribute("flowTable", newtable);
		return "flow/flowTableList";
	}
	
	//动态建表
	@RequestMapping("flowCreateTable")
	@ResponseBody
	public synchronized int flowCreateTable(HttpServletRequest request,HttpServletResponse response,String tablePY,String columnId){	
		String createTableSql = "CREATE TABLE "+tablePY+" ("
				+"id int(11) NOT NULL AUTO_INCREMENT,"
				+"versionControl varchar(50),"
				+"status varchar(20),"
				+columnId
				+"PRIMARY KEY (id)"
		        +") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		//System.out.println("createTableSql=="+createTableSql);		
		int result = flowDao.CreateTable(createTableSql);
		return result;
	}
	
	//插入动态表
	@RequestMapping("insertFlowTable")
	@ResponseBody
	public synchronized boolean insertFlowTable(HttpServletRequest request,String tablenamecn,String tablenamepy,String insertSql,String insertWordbook,String dept,String chooseValue){
		String[] sqls = new String[5];
		sqls[0] = "insert into flowtablewordbook(tablenamecn,tablenamepy,columnnamecn,columnnamepy) values ('"+tablenamecn+"','"+tablenamepy+"','版本','versonControl')";
		sqls[1] = "insert into flowtablewordbook(tablenamecn,tablenamepy,columnnamecn,columnnamepy) values ('"+tablenamecn+"','"+tablenamepy+"','状态','status')";
		sqls[2] = "insert into flowtablewordbook(tablenamecn,tablenamepy,columnnamecn,columnnamepy) values "+insertWordbook; 
		sqls[3] = "insert into "+tablenamepy+" "+insertSql; 
		sqls[4] = "insert into roletable(tablecn,tablepy,dept,userid,type,role) values ('"+tablenamecn+"','"+tablenamepy+"','"+dept+"','"+request.getUserPrincipal().getName()+"','流程','"+chooseValue+"')";
		boolean bool  = flowDao.AddSqlBatch(sqls);
		if(bool){
			//插入日志
			Logoperate logoperate = new Logoperate();
			logoperate.setUserid(request.getUserPrincipal().getName());
			logoperate.setContent("新增流程上报:"+tablenamecn);
			logDao.insertLog(logoperate);
		}		
		return bool;
	}
	
	//页面跳转到新增修改页
	@RequestMapping("flowByIdHtml")
	public synchronized String flowByIdHtml(HttpServletRequest request,HttpServletResponse response,int id,String flowTableName,ModelMap modelMap){		
		System.out.println("进入id=="+id);
		modelMap.addAttribute("flowId", id);
		modelMap.addAttribute("flowTable", flowTableName);
		return "flow/addTempFlow";
	}
		
	//根据id获取table
	@RequestMapping("flowById")
	@ResponseBody
	public synchronized String flowById(HttpServletRequest request,HttpServletResponse response,String tablename,String id) throws SQLException{
		String result = "";
		//表名为空表示未传递id过来,不是修改
		if(tablename == null || tablename.length() <= 0){
				
		}else{
			String sqlwhere = tablename+" where id="+id;
			result = flowDao.QueryFlowwhere(sqlwhere);
		}		
		return result;
	}
	
	//修改表结构
	//alter table tablename add transactor1 varchar(10),add transactor2 varchar(10);
	@RequestMapping("alterFlowByTable")
	@ResponseBody
	public synchronized int alterFlowByTable(String tablename,int start,int end){
		String column = "";
		for(int i=start;i<end;i++){
			column = column+" add "+tablename+i+" varchar(200),";
		}
		column = column.substring(0,column.length()-1);
		String sqlwhere = "alter table "+tablename+column+";";
		flowDao.alterFlowTable(sqlwhere);
		return 0;
	}
	
	//修改临时表 动态添加字段alter table tablename add transactor1 varchar(10),add transactor2 varchar(10);
	@RequestMapping("updateFlowById")
	@ResponseBody
	public synchronized int updateFlowById(HttpServletRequest request,HttpServletResponse response,String tablename,String canshu,int id,String version,String dept,String chooseValue){
		System.out.println("进入修改临时任务表单数据");
		int result = 0;
		//先判断版本号，然后再更新表
		result = flowDao.queryVersion(tablename, id, version);
		if(result==0){
			return 0;
		}else{
			String[] sqls = new String[2];
			sqls[0] = "update "+tablename +" set "+canshu+" where id="+id;
			sqls[1] = "update roletable set dept='"+dept+"',role='"+chooseValue+"' where tablepy='"+tablename+"'";		
			boolean bool = flowDao.AddSqlBatch(sqls);
			if (bool) {
				result = 1;
			}
			if(result>0){
				//日志记录
				Logoperate logoperate = new Logoperate();
				logoperate.setUserid(request.getUserPrincipal().getName());
				RoleTable roleTable = new RoleTable();
				roleTable.setTablepy(tablename);
				roleTable.setType("流程");
				List<RoleTable> roleTableList = roleTableDao.getRoleTableByTablePY(roleTable);
				logoperate.setContent("修改了 "+roleTableList.get(0).getTablecn());
				logDao.insertLog(logoperate);
			}
			return result;
		}			
	}
	
	//修改临时表 动态添加字段alter table tablename add transactor1 varchar(10),add transactor2 varchar(10);
	@RequestMapping("updateModelFlowById")
	@ResponseBody
	public synchronized int updateModelFlowById(HttpServletRequest request,HttpServletResponse response,String tablename,String canshu,int id,String version,String updateLog){
		System.out.println("进入修改模板临时流程表单数据");
		int result = 0;
		//先判断版本号，然后再更新表
		result = flowDao.queryVersion(tablename, id, version);
		if(result==0){
			return 0;
		}else{
			String sql = "update "+tablename +" set "+canshu+" where id="+id;		
			result = flowDao.updateFlowById(sql);
			if(result>0){
				//日志记录
				Logoperate logoperate = new Logoperate();
				logoperate.setUserid(request.getUserPrincipal().getName());
				RoleTable roleTable = new RoleTable();
				roleTable.setTablepy(tablename);
				roleTable.setType("流程");
				List<RoleTable> roleTableList = roleTableDao.getRoleTableByTablePY(roleTable);
				logoperate.setContent("修改流程表:"+roleTableList.get(0).getTablecn() +updateLog);
				logDao.insertLog(logoperate);
			}
			return result;
		}			
	}
		
	//插入到字典表
	@RequestMapping("insertFlowWorkbookById")
	@ResponseBody
	public int insertFlowWorkbookById(String canshu,String tablename,int id){
		int result = flowDao.InsertWorkbook(canshu);
		System.out.println("resultFlowTableDetialt=="+result);
		return result;
	}
		
	//根据权限表查询对应用户的任务
	@RequestMapping("getFlow")
	@ResponseBody
	public synchronized JSONArray getFlow(HttpServletRequest request){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		RoleTable roleTable = new RoleTable();
		List<User> userList = userDao.getUserByUserId(request.getUserPrincipal().getName());
		roleTable.setDept(userList.get(0).getDept());
		roleTable.setType("流程");
		List<RoleTable> roleTableList = roleTableDao.getRoleTableByDept(roleTable);
		jsonObject.put("data", roleTableList);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
	
}
