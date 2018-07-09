package com.mybatis.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mybatis.entity.Complaint;
import com.mybatis.entity.Logoperate;
import com.mybatis.entity.RoleTable;
import com.mybatis.mapper.ComplaintDao;
import com.mybatis.mapper.LogDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 
 *  Author:zouyang
 * 	Data:2018-5-24
 *  投诉
 * 
 */

@Controller
public class ComplaintController {

	//投诉sql
	@Autowired
	ComplaintDao complaintDao;
	
	//日志记录
	@Autowired
	LogDao logDao;
	
	//进入投诉页面
	@RequestMapping("ComplaintHtml")
	public synchronized String ComplaintHtml(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
		modelMap.addAttribute("userid", request.getUserPrincipal().getName());
		return "complaint/complaintList";
	}
	
	//进入投诉管理页面
	@RequestMapping("manageComplaint")
	public synchronized String manageComplaint(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
		modelMap.addAttribute("userid", request.getUserPrincipal().getName());
		return "complaint/manageComplaint";
	}
	
	//新增投诉信息
	@RequestMapping("insertComplaint")
	@ResponseBody
	public int insertComplaint(String userid,String content,String type,String nickname,String dept){
		Complaint complaint = new Complaint();
		complaint.setUserid(userid);
		complaint.setContent(content);
		complaint.setType(type);
		complaint.setNickname(nickname);
		complaint.setDept(dept);
		complaint.setStatus("投诉中");
		int result = complaintDao.insertComplaint(complaint);
		if(result>0){
			Logoperate logoperate = new Logoperate();
			logoperate.setUserid(userid);
			logoperate.setContent("新增了投诉: "+content);
			logDao.insertLog(logoperate);
		}
		return result;
	}
	
	//撤销投诉
	@RequestMapping("updateComplaint")
	@ResponseBody
	public int updateComplaint(String id,HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
		Complaint complaint = new Complaint();
		complaint.setId(Integer.parseInt(id));
		complaint.setStatus("已撤销");
		int result = complaintDao.updateComplaintById(complaint);
		if(result>0){
			Logoperate logoperate = new Logoperate();
			logoperate.setUserid(request.getUserPrincipal().getName());
			logoperate.setContent("撤销了投诉");
			logDao.insertLog(logoperate);
		}
		return result;
	}
	
	//分页+条件查询日志
	@RequestMapping("complaintList")
	@ResponseBody
	public synchronized JSONArray complaintList(String nickname,String content,int currentPage,HttpServletRequest request){
		int pageSize = 15;//初始化值，每页显示几行数据，当前页为第一页
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Complaint complaint = new Complaint();
		complaint.setNickname(nickname);
		complaint.setContent(content);
		complaint.setDept("财务");
		complaint.setUserid(request.getUserPrincipal().getName());
		//计算出查询总数
		int totalRows = complaintDao.getCountSqlwhere(complaint);
		int countPage = (totalRows + pageSize-1)/pageSize;
		int rowStart = (currentPage - 1) * pageSize;
		complaint.setRowStart(rowStart);
		complaint.setPageSize(pageSize);
		List<Complaint> complaintList = complaintDao.getComplaintPagerContent(complaint);
		jsonObject.put("data", complaintList);
		jsonObject.put("complaintcountPage", countPage);
		jsonObject.put("complaintcurrentPage", currentPage);
		jsonObject.put("complaintNum",totalRows);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
	
	//首页投诉
	@RequestMapping("getComplaint")
	@ResponseBody
	public synchronized JSONArray getComplaint(HttpServletRequest request){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		String userId = request.getUserPrincipal().getName();
		List<Complaint> complaintList = complaintDao.getComplaintByTop(userId);
		jsonObject.put("data", complaintList);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
}
