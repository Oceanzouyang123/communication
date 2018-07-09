package com.mybatis.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mybatis.entity.Logoperate;
import com.mybatis.mapper.LogDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author zouyang
 * Data:2018-5-16
 * 日志记录
 */

@Controller
public class LogController {
	
	@Autowired
	LogDao logDao;
	
	//进入日志页面
	@RequestMapping("logOperateHtml")
	public String logHtml(){
		return "log/logList";
	}
	
	@RequestMapping("/loginout")
	public String logout(HttpServletRequest request){
		
		System.out.println("退出");
		return "";
	}
	
	//分页+条件查询日志
	@RequestMapping("logList")
	@ResponseBody
	public JSONArray logList(String userid,String content,int currentPage){
		int pageSize = 15;//初始化值，每页显示几行数据，当前页为第一页
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Logoperate log = new Logoperate();
		log.setUserid(userid);
		log.setContent(content);
		//计算出查询总数
		int totalRows = logDao.getCountSqlwhere(log);
		int countPage = (totalRows + pageSize-1)/pageSize;
		int rowStart = (currentPage - 1) * pageSize;
		log.setRowStart(rowStart);
		log.setPageSize(pageSize);
		List<Logoperate> logList = logDao.getLogPagerContent(log);
		jsonObject.put("data", logList);
		jsonObject.put("logcountPage", countPage);
		jsonObject.put("logcurrentPage", currentPage);
		jsonObject.put("logNum",totalRows);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
	
	
	//新增日志
	@RequestMapping("insertLog")
	public void insertLog(String userid,String content,HttpServletRequest request){
		Logoperate log = new Logoperate();
		log.setUserid(userid);
		log.setContent(content);
		logDao.insertLog(log);
		//System.out.println("获取sessionUserId=="+request.getSession().getAttribute("sessionUserId"));
	}
}
