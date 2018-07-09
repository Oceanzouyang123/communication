package com.mybatis.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mybatis.entity.Knowcenter;
import com.mybatis.entity.Logoperate;
import com.mybatis.mapper.KnowledgeDao;
import com.mybatis.mapper.LogDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 *  Author:zouyang
 * 	Data:2018-5-14
 *  知识库
 * 
 */

@Controller
public class KnowledgeController {
	
	//知识库sql
	@Autowired
	KnowledgeDao knowledgeDao;
	
	//日志sql
	@Autowired
	LogDao logDao;
	
	//进入知识库页面
	@RequestMapping("knowledgeHtml")
	public synchronized String knowledgeHtml(ModelMap modelMap,HttpServletRequest request,HttpServletResponse response){
		System.out.println("进入知识库~~");
		modelMap.addAttribute("userid", request.getUserPrincipal().getName());
		return "knowledge/knowledgeTableList";
	}
	
	//获取知识库信息分页
	@RequestMapping("getknowledgePager")
	@ResponseBody
	public synchronized JSONArray getknowledgePager(String userid,String keyword,int currentPage,String title){
		int pageSize = 15;//初始化值，每页显示几行数据，当前页为第一页
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Knowcenter knowcenter = new Knowcenter();
		knowcenter.setKeyword(keyword);
		knowcenter.setTitle(title);
		//计算出查询总数
		int totalRows = knowledgeDao.getCountSqlwhere(knowcenter);
		int countPage = (totalRows + pageSize-1)/pageSize;
		int rowStart = (currentPage - 1) * pageSize;
		knowcenter.setRowStart(rowStart);
		knowcenter.setPageSize(pageSize);
		List<Knowcenter> knowcenterList = knowledgeDao.getKnowledgeList(knowcenter);
		jsonObject.put("data", knowcenterList);
		jsonObject.put("knowledgecountPage", countPage);
		jsonObject.put("knowledgecurrentPage", currentPage);
		jsonObject.put("knowledgeNum",totalRows);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
	
	//进入知识库管理页面
	@RequestMapping("manageKnowledge")
	public synchronized String manageKnowledge(ModelMap modelMap,HttpServletRequest request,HttpServletResponse response,String knowledgeId){
		System.out.println("进入知识库编辑页面");
		modelMap.addAttribute("userid", request.getUserPrincipal().getName());
		modelMap.addAttribute("knowledgeId", knowledgeId);
		return "knowledge/manageKonwledge";
	}
	
	//进入知识库查看页面
	@RequestMapping("queryKnowledge")
	public synchronized String queryKnowledge(ModelMap modelMap,HttpServletRequest request,HttpServletResponse response,String knowledgeId){
		modelMap.addAttribute("userid", request.getUserPrincipal().getName());
		modelMap.addAttribute("knowledgeId", knowledgeId);
		return "knowledge/knowledgeDetial";
	}
	
	//插入知识库
	@RequestMapping("insertKnowledge")
	@ResponseBody
	public int insertKnowledge(String title,String keyword,String content,ModelMap modelMap,HttpServletRequest request,HttpServletResponse response){
		Knowcenter knowcenter = new Knowcenter();
		knowcenter.setTitle(title);
		knowcenter.setKeyword(keyword);
		knowcenter.setContent(content);
		if(request.getSession().getAttribute("filenamegroup")==null){
			knowcenter.setFilepath("");
		}else{
			knowcenter.setFilepath(request.getSession().getAttribute("filenamegroup").toString());
		}		
		knowcenter.setUserid(request.getUserPrincipal().getName());
		int result = knowledgeDao.insertKnowledge(knowcenter);
		if(result>0){
			Logoperate logoperate = new Logoperate();
			logoperate.setUserid(request.getUserPrincipal().getName());
			logoperate.setContent("新增知识库,标题:'"+title+"'");
			logDao.insertLog(logoperate);
		}
		return result;
	}
	
	//修改知识库信息
	@RequestMapping("updateKnowledge")
	@ResponseBody
	public int updateKnowledge(String title,String keyword,String content,int id,HttpServletRequest request,HttpServletResponse response){
		Knowcenter knowcenter = new Knowcenter();
		knowcenter.setId(id);
		knowcenter.setTitle(title);
		knowcenter.setKeyword(keyword);
		knowcenter.setContent(content);
		int result = knowledgeDao.updateKnowledge(knowcenter);	
		return result;
	}
	
	//批量上传文件
	@RequestMapping("/batchUpload")
	@ResponseBody
	public synchronized int batchUpload(Model model,HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile[] files){
		String filenamegroup = "";
		int result = 0;
		MultipartFile file;
		String pathdir = "D:/uploadFile";
		File filepath = new File(pathdir);
		//判断上传文件的保存目录是否存在
        if (!filepath.exists() && !filepath.isDirectory()) {
            System.out.println("D:/uploadFile目录不存在，需要创建");
            //创建目录
            filepath.mkdir();
        }
		for (int i = 0; i < files.length; i++) {
			System.out.println(files.length+"==多文件名"+files[i].getOriginalFilename());
			String path = "D:/uploadFile/"+files[i].getOriginalFilename();
			filenamegroup = filenamegroup+files[i].getOriginalFilename()+"ψ";
			System.out.println("path=="+path);
			file = files[i];
			try {
				upload(path,file);
			} catch (IOException e) {
				e.printStackTrace();
				result = 1; 
			}
		}
		request.getSession().setAttribute("filenamegroup", filenamegroup);
		return result;
	}
	
	//下载文件
	@RequestMapping("/downloadFile")
	@ResponseBody
	public synchronized int testDownload(HttpServletResponse res,String filename) throws UnsupportedEncodingException {
	    int result = 0;
		String fileName = filename;
	    res.setHeader("content-type", "application/octet-stream");
	    res.setContentType("application/octet-stream");
	    res.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes(),"iso-8859-1"));
	    byte[] buff = new byte[1024];
	    BufferedInputStream bis = null;
	    OutputStream os = null;
	    try {
	      os = res.getOutputStream();
	      bis = new BufferedInputStream(new FileInputStream(new File("d://uploadFile//"+ fileName)));
	      int i = bis.read(buff);
	      while (i != -1) {
	        os.write(buff, 0, buff.length);
	        os.flush();
	        i = bis.read(buff);
	      }
	    } catch (IOException e) {
	    	result = 1;
	        e.printStackTrace();
	    } finally {
	      if (bis != null) {
	        try {
	          bis.close();
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	      }
	    }
	    return result;
	  }

	
	//根据id获取知识库信息(进入编辑页面)
	@RequestMapping("getknowledgeById")
	@ResponseBody
	public synchronized JSONArray getknowledgeById(int id){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		List<Knowcenter> knowcenterList = knowledgeDao.getKnowledgeById(id);
		jsonObject.put("data", knowcenterList);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
	
	//根据id获取知识库信息(查看或评论)
	@RequestMapping("queryKnowledgeById")
	@ResponseBody
	public synchronized JSONArray queryKnowledgeById(int id){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		List<Knowcenter> knowcenterList = knowledgeDao.getKnowledgeById(id);
		jsonObject.put("data", knowcenterList);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
	
	//查询最新的10条知识库记录
	@RequestMapping("getKnowledge")
	@ResponseBody
	public synchronized JSONArray queryKnowledgeById(){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		List<Knowcenter> knowcenterList = knowledgeDao.getKnowledge();
		jsonObject.put("data", knowcenterList);
		jsonArray.add(jsonObject);
		return jsonArray;
	}
	
	//bootstrap模板manageModel
	@RequestMapping("manageModel")
	public synchronized String manageModel(ModelMap modelMap,HttpServletRequest request,HttpServletResponse response,String knowledgeId){
		System.out.println("进入知识库模板编辑页面");
		modelMap.addAttribute("userid", request.getUserPrincipal().getName());
		modelMap.addAttribute("knowledgeId", knowledgeId);
		return "knowledge/manageModel";
	}
	
	//上传文件,先将excel文件写入到服务器端，然后从服务器端再获取
    public synchronized void upload(String filename,MultipartFile file) throws IOException{
    	File outFile = new File(filename);
    	BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(outFile)); // 创建文件输出流
    	//根据上传的文件，创建缓冲区，生成字符流，存入缓冲区
        byte[] buffer = file.getBytes();//创建文件流       
        //把缓冲区的字符流 写入 字符流目的地(把输入流写入输出流)
        stream.write(buffer);
        //第6步：刷新流，关闭流
        stream.close();
    }
    
}
