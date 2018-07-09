package com.mybatis.controller;


import java.net.UnknownHostException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mybatis.dao.TestDao;
import com.mybatis.entity.Dept;
import com.mybatis.entity.Business;
import com.mybatis.entity.Logoperate;
import com.mybatis.entity.RoleTable;
import com.mybatis.entity.User;
import com.mybatis.mapper.DeptDao;
import com.mybatis.mapper.BusinessDao;
import com.mybatis.mapper.LogDao;
import com.mybatis.mapper.RoleTableDao;
import com.mybatis.mapper.UserDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 
 *  Author:zouyang
 * 	Data:2018-5-14
 *  业务控制dispatch跳转
 * 
 */


@Controller
public class RoleController {
	
	TestDao testDao = new TestDao();
	
	//用户sql
	@Autowired
	UserDao userDao;
	
    //部门sql
	@Autowired
	DeptDao deptDao;
	
	//日志sql
	@Autowired
	LogDao logDao;
	
	//流程状态管理sql
	@Autowired
	BusinessDao businessDao;
	
	//权限sql
	RoleTableDao roleTableDao;
	
	/**
	 * 部门管理
	 */
	
	//部门管理管理页面
	@RequestMapping("deptHtml")
	public String deptHtml(){
		System.out.println("进入部门管理页面~~");		
		return "roleManagement/deptTableList";
	}
	
	//查询所有部门
	@RequestMapping("queryDept")
	@ResponseBody
	public synchronized JSONArray queryDept(ModelMap model,HttpServletRequest request, HttpServletResponse response){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		List<Dept> deptList = deptDao.getDeptList();
		jsonObject.put("data", deptList);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
	
	//根据id获取部门信息
	@RequestMapping("queryDeptById")
	@ResponseBody
	public synchronized JSONArray queryDeptById(String id,HttpServletRequest request, HttpServletResponse response){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		List<Dept> deptList = deptDao.getDeptById(Integer.parseInt(id));
		jsonObject.put("data", deptList);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
	
	//进入新增或修改部门信息页面
	@RequestMapping("manageDept")
	public synchronized String manageDept(String deptId,HttpServletRequest request, HttpServletResponse response,ModelMap modelMap){
		modelMap.addAttribute("deptId", deptId);
		return "roleManagement/manageDept";
	}
	
	//修改部门信息
	@RequestMapping("updateDept")
	@ResponseBody
	public int updateDept(String name,String id){
		List<Dept> deptList = deptDao.getDeptById(Integer.parseInt(id));//获取原纪录
		Dept dept = new Dept();
		dept.setId(Integer.parseInt(id));
		dept.setName(name);
		int result = deptDao.updateDept(dept);
		//日志记录		
		if(result>0){
			Logoperate logoperate = new Logoperate();
			logoperate.setUserid("admin");
			logoperate.setContent("修改部门'"+deptList.get(0).getName()+"'为'"+name+"'");
			logDao.insertLog(logoperate);
		}		
		return result;
	}
	
	//新增部门
	@RequestMapping("insertDept")
	@ResponseBody
	public int insertDept(String name){
		Dept dept = new Dept();
		dept.setName(name);
		int result = deptDao.insertDept(dept);
		//日志记录
		if(result>0){
			Logoperate logoperate = new Logoperate();
			logoperate.setUserid("admin");
			logoperate.setContent("新增部门:'"+name+"'");
			logDao.insertLog(logoperate);
		}
		return result;
	}
	
	/**
	 * 用户权限管理
	 */
	
	//进入权限管理页面
	@RequestMapping("roleManagementHtml")
	public String knowledgeHtml(){
		System.out.println("进入权限管理页面~~");
		return "roleManagement/roleTableList";
	}
		
	//分页+条件查询所有用户
	@RequestMapping("userList")
	@ResponseBody
	public synchronized JSONArray userList(String userId,String dept,int currentPage){
		int pageSize = 15;//初始化值，每页显示几行数据，当前页为第一页
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		User user = new User();
		user.setUserId(userId);
		user.setDept(dept);
		//计算出查询总数
		int totalRows = userDao.getCountSqlwhere(user);
		int countPage = (totalRows + pageSize-1)/pageSize;
		int rowStart = (currentPage - 1) * pageSize;
		user.setRowStart(rowStart);
		user.setPageSize(pageSize);
		List<User> userList = userDao.getUserPagerContent(user);
		jsonObject.put("data", userList);
		jsonObject.put("usercountPage", countPage);
		jsonObject.put("usercurrentPage", currentPage);
		jsonObject.put("userNum",totalRows);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
	
	//根据id查询用户
	@RequestMapping("getUserById")
	@ResponseBody
	public synchronized JSONArray getUserById(int id,ModelMap model){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();		
		List<User> userList = userDao.getUser(id);
		jsonObject.put("data", userList);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
		
	//根据部门名称查询用户信息
	@RequestMapping("queryUserByName")
	@ResponseBody
	public synchronized JSONArray queryUserByName(String dept,HttpServletRequest request, HttpServletResponse response){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		User user = new User();
		user.setDept(dept);
		List<User> deptList = userDao.getDeptByName(user);
		jsonObject.put("data", deptList);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
	
	//根据用户编号查询用户
	@RequestMapping("getUserByUserId")
	@ResponseBody
	public synchronized JSONArray getUserByUserId(String userId){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		List<User> userList = userDao.getUserByUserId(userId);
		jsonObject.put("data", userList);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
		
	//进入编辑权限页面
	@RequestMapping("manageRoleHtml")
	public synchronized String manageRoleHtml(int userid,ModelMap model){
		model.addAttribute("userid",userid);
		return "roleManagement/manageRole";
	}
	
	//修改用户权限
	@RequestMapping("updateUser")
	@ResponseBody
	public int updateUser(int id,String dept){
		List<User> userList =  userDao.getUser(id);
		User user = new User();
		user.setId(id);
		user.setDept(dept);
		int result = userDao.update(user);
		if(result>0){
			Logoperate logoperate = new Logoperate();
			logoperate.setUserid("admin");
			logoperate.setContent("修改"+userList.get(0).getUserId()+"的权限'"+userList.get(0).getDept()+"'为'"+dept+"'");
			logDao.insertLog(logoperate);
		}
		return result;
	}
	
	//头部ajax加载
	@RequestMapping("headHtml")
	@ResponseBody
	public synchronized String headHtml(HttpServletRequest request,ModelMap model) throws UnknownHostException{
		Principal username = request.getUserPrincipal();
		List<User> userList = userDao.getUserByUserId(username.getName());
		return userList.get(0).getDept();
	}
	
	
	//头部ajax加载
	@RequestMapping("headUserId")
	@ResponseBody
	public synchronized String headUserId(HttpServletRequest request,ModelMap model) throws UnknownHostException{
		Principal username = request.getUserPrincipal();
		return username.getName();
	}
	
	/**
	 * 流程状态管理
	 */
	
	//进入流程状态页面
	@RequestMapping("businessHtml")
	public String businessHtml(HttpServletRequest request){
		return "roleManagement/businessStatusTableList";
	}
	
	//查询流程状态表
	@RequestMapping("queryBusiness")
	@ResponseBody
	public synchronized JSONArray queryBusiness(){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		List<Business> businessList = businessDao.getBusinessList();
		jsonObject.put("data", businessList);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
	
	//进入流程状态编辑页面
	@RequestMapping("manageBusiness")
	public synchronized String manageBusiness(String businessId,HttpServletRequest request, HttpServletResponse response,ModelMap model){
		model.addAttribute("businessId",businessId);
		return "roleManagement/manageBusiness";
	}
	
	//根据id获取部门信息
	@RequestMapping("queryBusinessById")
	@ResponseBody
	public synchronized JSONArray queryBusinessById(String id,HttpServletRequest request, HttpServletResponse response){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		List<Business> businessList = businessDao.getBusinessById(Integer.parseInt(id));
		jsonObject.put("data", businessList);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
	
	//修改业务流程状态信息
	@RequestMapping("updateBusiness")
	@ResponseBody
	public synchronized int updateBusiness(String name,int id){
		List<Business> businessList = businessDao.getBusinessById(id);//获取原纪录
		Business business = new Business();
		business.setId(id);
		business.setBusinessstatus(name);
		int result = businessDao.updateBusiness(business);
		//日志记录		
		if(!businessList.get(0).getBusinessstatus().equals(name)){
			Logoperate logoperate = new Logoperate();
			logoperate.setUserid("admin");
			logoperate.setContent("修改部门'"+businessList.get(0).getBusinessstatus()+"'为'"+name+"'");
			logDao.insertLog(logoperate);
		}		
		return result;
	}
	
	//新增业务流程状态
	@RequestMapping("insertBusiness")
	@ResponseBody
	public int insertBusiness(String name){
		Business business = new Business();
		business.setBusinessstatus(name);
		int result = businessDao.insertBusiness(business);
		//日志记录
		Logoperate logoperate = new Logoperate();
		logoperate.setUserid("admin");
		logoperate.setContent("新增流程状态:'"+name+"'");
		logDao.insertLog(logoperate);
		return result;
	}
	
	//检查表是否已经存在
	@RequestMapping("QueryRoleTableName")
	@ResponseBody
	public synchronized int QueryRoleTableName(String tablenamecn){
		List<RoleTable> roleTableList = testDao.QueryRoleTableNameCN(tablenamecn);
		return roleTableList.size();
	}

}
