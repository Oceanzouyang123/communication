package com.mybatis.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mybatis.dao.DemandDao;
import com.mybatis.dao.FlowDao;
import com.mybatis.dao.InfoDao;
import com.mybatis.entity.RoleTable;
import com.mybatis.entity.TableWorkbook;
import com.mybatis.entity.User;
import com.mybatis.mapper.DeptDao;
import com.mybatis.mapper.RoleTableDao;
import com.mybatis.mapper.UserDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author zouyang
 * 现有模版管理
 *
 */

@Controller
public class ModelController {

	@Autowired
	RoleTableDao roleTableDao;
	
	//用户sql
	@Autowired
	UserDao userDao;
	
	//需求
	DemandDao demandDao = new DemandDao();
	//任务
	InfoDao infoDao = new InfoDao();
	//流程
	FlowDao flowDao = new FlowDao();
	
	
	//进入任务模板页
	@RequestMapping("modelHtml")
	public String modelHtml(){
		return "model/model";
	}
	
	//根据任务中文表名和表单类型查询
	@RequestMapping("queryModel")
	@ResponseBody
	public synchronized JSONArray queryModel(String type,String tablecn,HttpServletRequest request){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		String userName = request.getUserPrincipal().getName();
		List<User> userList = userDao.getUserByUserId(userName);
		RoleTable roleTable = new RoleTable();
		roleTable.setType(type);
		roleTable.setTablecn(tablecn);
		roleTable.setRole(userList.get(0).getDept());
		List<RoleTable> roleTableList = roleTableDao.getRoleTableByType(roleTable);
		jsonObject.put("data", roleTableList);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
	
	//进入现有任务模板添加页面
	@RequestMapping("addModel")
	public synchronized String addModel(String tablecn,String type,ModelMap modelMap){
		modelMap.addAttribute("tablecn", tablecn);
		modelMap.addAttribute("type", type);
		return "model/addModel";
	}
	
	//进入现有流程模板添加页面
	@RequestMapping("addModelFlow")
	public synchronized String addModelFlow(String tablecn,String type,ModelMap modelMap){
		modelMap.addAttribute("tablecn", tablecn);
		modelMap.addAttribute("type", type);
		return "flowModel/addModel";
	}
	
	//进入现有需求模板添加页面
	@RequestMapping("addModelDemand")
	public synchronized String addModelDemand(String tablecn,String type,ModelMap modelMap){
		modelMap.addAttribute("tablecn", tablecn);
		modelMap.addAttribute("type", type);
		return "demandModel/addModel";
	}
	
	//进入任务现有模板修改页面
	@RequestMapping("updateModel")
	public synchronized String updateModel(String tablecn,String type,ModelMap modelMap){
		modelMap.addAttribute("tablecn", tablecn);
		modelMap.addAttribute("type", type);
		return "model/updateModel";
	}
	
	//进入任务现有模板修改页面
	@RequestMapping("updateModelFlow")
	public synchronized String updateModelFlow(String tablecn,String type,ModelMap modelMap){
		modelMap.addAttribute("tablecn", tablecn);
		modelMap.addAttribute("type", type);
		return "flowModel/updateModel";
	}
	
	//进入任务现有需求修改页面
	@RequestMapping("updateModelDemand")
	public synchronized String updateModelDemand(String tablecn,String type,ModelMap modelMap){
		modelMap.addAttribute("tablecn", tablecn);
		modelMap.addAttribute("type", type);
		return "demandModel/updateModel";
	}
	
	//根据类型添加任务模板
	@RequestMapping("queryModelByTablecn")
	@ResponseBody
	public synchronized JSONArray queryModelByTablecn(String tablecn,String type){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		System.out.println("type=="+type);
		if(type.equals("需求")){
			List<TableWorkbook> tableWorkbookList = demandDao.QueryTableWorkbook(" where tablenamecn='"+tablecn+"'");
			jsonObject.put("data", tableWorkbookList);
		}else if(type.equals("流程")){
			List<TableWorkbook> flowTableWorkbookList = flowDao.QueryTableWorkbook(" where tablenamecn='"+tablecn+"'");
			jsonObject.put("data", flowTableWorkbookList);
		}else{
			List<TableWorkbook> infoTableWorkbookList = infoDao.QueryTableWorkbook(" where tablenamecn='"+tablecn+"'");
			jsonObject.put("data", infoTableWorkbookList);
		}		
		jsonArray.add(jsonObject);
		return jsonArray;
	}
	
	//插入数据到现有任务模板
	@RequestMapping("insertModel")
	@ResponseBody
	public int insertModel(String sql){
		int result = infoDao.insertModel(sql);
		return result;
	}
	
	//进入流程模板页
	@RequestMapping("flowModelHtml")
	public synchronized String flowModelHtml(){
		System.out.println("流程模板");
		return "flowModel/model";
	}
	
	//进入需求模板页
	@RequestMapping("demandModelHtml")
	public synchronized String demandDataTableHtml(){
		System.out.println("需求模板");
		return "demandModel/model";
	}
	
}
