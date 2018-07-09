package com.mybatis.controller;

/**
 * 
 *  Author:zouyang
 * 	Data:2018-4-18
 *  业务控制dispatch跳转
 * 
 */


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties.Mongo;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mybatis.util.ExcelUtil;
import com.mybatis.dao.TestDao;
import com.mybatis.entity.MonthBill;
import com.mybatis.mapper.MonthBillDao;
import com.mybatis.mapper.MonthBillMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class IndexController {
	
	@Autowired
	private MonthBillMapper monthBillMapper;
	
	@Resource
	private MonthBillDao monthBillDao;
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	TestDao testDao = new TestDao();
	
	@RequestMapping("login")
	public String loginHtml(){
		MonthBill monthBill = new MonthBill();
		monthBill.setId(1);
		monthBill.setBillno("012245562669");
		//测试多参数传入存储过程查询
		List<MonthBill> list = monthBillDao.getMonthBillProcedure(monthBill);
		System.out.println(list.size());
		return "login";
	}
	
	@RequestMapping("iii")
	public String index1(Model model,HttpServletRequest request,HttpServletResponse response){
		System.out.println("进入iii页面");
		return "index";
	}
	
	//进入from.html表格页面
	@RequestMapping("froms")
	public String forms(Model model,HttpServletRequest request,HttpServletResponse response){
		return "form";
	}
	
	//进入head.html公共头部页面
	@RequestMapping("head")
	public String head(Model model,HttpServletRequest request,HttpServletResponse response){
		return "head";
	}
	
	//进入sidebar.html公共右侧导航页面
	@RequestMapping("sidebar")
	public String sidebar(Model model,HttpServletRequest request,HttpServletResponse response){
		return "sidebar";
	}
	
	//进入tableList.html公共右侧导航页面
	@RequestMapping("tablelist")
	public String tablelist(Model model,HttpServletRequest request,HttpServletResponse response){
		return "tableList";
	}
	
	//分页显示数据
	@RequestMapping("pagerList")
	@ResponseBody
	public JSONArray pagerList(ModelMap model,int currentPage){
		int pageSize = 15;//初始化值，每页显示几行数据，当前页为第一页
		//自定义算法分页
		int totalRows = monthBillMapper.QueryTotal();
		int countPage = (totalRows + pageSize-1)/pageSize;
		int rowStart = (currentPage - 1) * pageSize;
		//System.out.println("totalRows:"+totalRows);
		//分页的mapper和xml两种方式
		//List<MonthBill> monthBillList = monthBillMapper.queryPager(rowStart,pageSize);
		List<MonthBill> monthBillList = monthBillDao.getMonthbillPager(rowStart, pageSize);
		//WHERE  id >= (SELECT id FROM monthbill ORDER BY id LIMIT 10000, 1) LIMIT 15;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonList = new JSONObject();
		jsonList.put("billcountPage", countPage);
		jsonList.put("billcurrentPage", currentPage);
		jsonList.put("billNum",totalRows);
		jsonList.put("data",monthBillList);
		jsonArray.add(jsonList);//将结果封装到JsonArray里
		//model.addAttribute("data", jsonArray);
		return jsonArray;
	}
	
	//查询产品类型
	@RequestMapping("productTypeSelect")
	@ResponseBody
	public JSONArray quertProductType(){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonList = new JSONObject();
		List<MonthBill> quertProductType = monthBillMapper.queryProductType();
		jsonList.put("productType", quertProductType);
		jsonArray.add(jsonList);
		return jsonArray;
	}
	
	//test测试xml方式sql,根据id查询monthbill对象
	@RequestMapping("/testgetbyid")
	public String test(){
		MonthBill monthBill = monthBillDao.getMonthbill(41472);
		System.out.println(monthBill.getBillno());
		return "tableList";
	}
	
	//多条件匹配查询
	@RequestMapping("/getMonthbillSqlWhere")
	public String getMonthbillSqlWhere(String billno,String producttype){
		MonthBill monthBill = new MonthBill();
		monthBill.setBillno(billno);
		if(producttype.equals("所有类型")){
			monthBill.setProducttype("");
		}else{
			monthBill.setProducttype(producttype);
		}
		//List<MonthBill> list = monthBillDao.getMonthbillSqlWhere(" where billno='"+billno+"'");
		List<MonthBill> list = monthBillDao.getMonthbillSqlWhere(monthBill);
		System.out.println(list.size());
		return "tableList";
	}
	
	//分页+多条件匹配
	@RequestMapping("/getMonthbillContentSqlWhere")
	@ResponseBody
	public JSONArray getMonthbillContentSqlWhere(String billno,String producttype,String destination,int currentPage){
		//System.out.println(billno+"--"+producttype+"--"+destination+"--"+currentPage);
		int pageSize = 15;//初始化值，每页显示几行数据，当前页为第一页		
		MonthBill monthBill = new MonthBill();
		monthBill.setBillno(billno);
		if(destination.equals("目的地")){
			monthBill.setDestination("");
		}else{
			monthBill.setDestination(destination);
		}
		if(producttype.equals("所有类型")){
			monthBill.setProducttype("");
		}else{
			monthBill.setProducttype(producttype);
		}
		int totalRows = monthBillDao.getCountSqlwhere(monthBill);
		int countPage = (totalRows + pageSize-1)/pageSize;
		int rowStart = (currentPage - 1) * pageSize;
		monthBill.setPageSize(pageSize);
		monthBill.setRowStart(rowStart);
		List<MonthBill> list = monthBillDao.getMonthbillPagerContent(monthBill);
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonList = new JSONObject();
		jsonList.put("billcountPage", countPage);
		jsonList.put("billcurrentPage", currentPage);
		jsonList.put("billNum",totalRows);
		jsonList.put("data",list);
		jsonArray.add(jsonList);//将结果封装到JsonArray里
		//model.addAttribute("data", jsonArray);
		return jsonArray;
	}
	
	//获取目的地组别
	@RequestMapping("/getDestination")
	@ResponseBody
	public JSONArray getDestination(){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonList = new JSONObject();
		List<MonthBill> listDestination = monthBillDao.getDestination();
		jsonList.put("data", listDestination);
		jsonArray.add(jsonList);
		return jsonArray;
	}
	
	//test测试xml方式sql
	@RequestMapping("/testgetall")
	public String getall(){
		List<MonthBill> monthbillList = monthBillDao.getMonthbillList();
		//System.out.println(monthbillList.size());
//		for (int i = 0; i < monthbillList.size(); i++) {
//			System.out.println(monthbillList.get(i).getPrice());
//		}
		return "tableList";
	}
	
	//test页面跳转
	@RequestMapping("/testbillhtml")
	public String testbillhtml(){
		return "billList";
	}
	
	//test批量插入
	@RequestMapping("/testbatchInsert")
	public String testbatchInsert(Model model,HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile[] files) throws IOException{
		int startCount = 0;
		int endCount = 0;
		int count = 0;
		MultipartFile file;
		for (int i = 0; i < files.length; i++) {
			System.out.println(files.length+"==多文件名"+files[i].getOriginalFilename());
		}
		
		file = files[0];
		//System.out.println("获取上传文件"+file.getName()+"=="+file.getOriginalFilename());
		String[] ss = file.getOriginalFilename().split("-");
		String titledate= ss[0];
		String[] s = titledate.split("\\.");
		titledate = s[0];
		//清空表
		//monthBillDao.delete();
		//System.out.println("结果:"+deleteResult);
		//String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
		String path = "";
		String os = System.getProperty("os.name");  
		if(os.toLowerCase().startsWith("win")){  
			path = "D:/uploadFile";
			System.out.println("当前操作系统为: "+os);  
		} else{
			path = "/home/sfadmin/uploadFile";
			System.out.println("当前操作系统为: "+os);
		}
		System.out.println(path);
		File filepath = new File(path);
        //判断上传文件的保存目录是否存在
        if (!filepath.exists() && !filepath.isDirectory()) {
            System.out.println("D:/uploadFile目录不存在，需要创建");
            //创建目录
            filepath.mkdir();
        }
		path = path+"/"+file.getOriginalFilename();
		System.out.println(path+"--上传");
		//上传文件
		upload(path,file);
		System.out.println("文件上传完毕");
		try {
			Thread.sleep(1000);
			System.out.println("waiting for upload excel 1sec...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		File getFile = new File(path);  
		//获取客户名称
		String customername = "";
		if(getFile.getName().endsWith("xlsx")){
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(getFile));  
	        XSSFSheet sheet = wb.getSheetAt(0); 
	        XSSFRow row5 = sheet.getRow(3);// 获得工作薄的第五行
	        XSSFCell cell4 = row5.getCell(4);// 获得第五行的第四个单元格
	        customername = cell4.getStringCellValue();
		}else{
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(getFile));  
	        HSSFSheet sheet = wb.getSheetAt(0);  
	        HSSFRow row5 = sheet.getRow(3);// 获得工作薄的第五行
	        HSSFCell cell4 = row5.getCell(4);// 获得第五行的第四个单元格
	        customername = cell4.getStringCellValue();
		}

		long start = System.currentTimeMillis();
		List<MonthBill> list = new ArrayList<MonthBill>();			
		ArrayList<ArrayList<Object>> result = ExcelUtil.readExcel(getFile);
		//System.out.println(result.size()+"集合的结果");
		String[] sqls = new String[result.size()];
        for(int i = 0 ;i < result.size() ;i++){ 
        	//判断获取数据之后，直到出现小计则退出循环
        	if(endCount==1&&i>=startCount){
        		break;
        	}
            for(int j = 0;j<result.get(i).size(); j++){  
                //System.out.println("第"+i+"行,第"+j+"列："+ result.get(i).get(j).toString());  
                if(result.get(i).get(j).toString().equals("序号")){
                	//System.out.println("搜索到序号，开始计数--");
                	startCount = i+1;
                }
                if(i>=startCount){
                	//j==2获取运单号码
                	if(j==2){
                		String billno = result.get(i).get(j).toString();
                		//System.out.println(result.get(i).get(0).toString()+"~~"+result.get(i).get(j).toString()+"=-=");
                		if(result.get(i).get(0).toString().contains("计")&&result.get(i).get(0).toString().contains("合")){
                			//System.out.println("找到了合计");
                			//如果到了合计，则不用再继续往下获取excel表格中的数据
                			endCount = 1;
                			break;
                		}
                		if(billno == null || billno.length() <= 0){
                			//运单号码为空，则不写入数据库
                		}else{
                			//5、6、8、9、12、29表示需要获取的数据列
                			String chargedWeight = result.get(i).get(5).toString();
                			String productType = result.get(i).get(6).toString();
                			String price = result.get(i).get(8).toString();
                			String discount = result.get(i).get(9).toString();
                			String increment = result.get(i).get(12).toString();
                			String destination = result.get(i).get(29).toString();             			
                			//System.out.println("第"+i+"行,第"+j+"列："+ billno+"--"+chargedWeight+"--"+productType+"--"+price+"--"+discount+"--"+destination+"--"+increment);               			             			
                			//封装sql语句   
                			MonthBill monthBill = new MonthBill();
                			monthBill.setBillno(billno);
                			monthBill.setChargedweight(chargedWeight);
                			monthBill.setDestination(destination);
                			monthBill.setDiscount(discount);
                			monthBill.setPrice(price);
                			monthBill.setProducttype(productType);
                			monthBill.setTitledate(titledate);
                			monthBill.setCustomername(customername);
                			monthBill.setIncrement(increment);
                			list.add(monthBill);
                			//sqls[count] = "INSERT INTO monthbill(billno,chargedweight,destination,discount,price,producttype,increment,titledate,customername) VALUES ('"+billno+"','"+chargedWeight+"', '"+destination+"','"+discount+"', '"+price+"', '"+productType+"', '"+increment+"', '"+titledate+"', '"+customername+"')";
                			//count = count + 1;
                		} 
                	} 
                }
            }
        }
        //批量执行sql，如果全部成功则commit提交
        //boolean resultBoolean = monthBillMapper.AddMonthBillBatch(sqls);  
        monthBillDao.insertBatch(list);
        long end = System.currentTimeMillis();
        System.out.println("耗时~~"+(start-end));
		return "redirect:/tablelist";
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
    
    //进入测试无对象查询页面
    @RequestMapping("/testnosqlhtml")
    public String testnosqlhtml() throws SQLException{
    	return "testnosql";
    }
    
    //测试无对象查询
    @RequestMapping("/testnosql")
    @ResponseBody
    public String testnosql() throws SQLException{
    	String result = testDao.QueryNosqlwhere("");
    	System.out.println(result);
    	return result;
    }
    
    //首页
    @RequestMapping("/index")
    public String index(){
    	//testSendSimple();
    	System.out.println("返回到index页面");    	
    	return "index";
    }
    
    public void testSendSimple(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("331391117@qq.com");
        message.setTo("yangzou@sf-express.com");
        message.setSubject("标题：测试标题");
        message.setText("测试内容部份");
        javaMailSender.send(message);
    }
    
}
