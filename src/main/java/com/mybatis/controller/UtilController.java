package com.mybatis.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mybatis.entity.MonthBill;
import com.mybatis.util.ExcelUtil;

@Controller
public class UtilController {

	//批量导入数据
	@RequestMapping("/batchInsert")
	public synchronized String batchInsert(Model model,HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile[] files) throws IOException{
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
        //monthBillDao.insertBatch(list);
        long end = System.currentTimeMillis();
        System.out.println("耗时~~"+(start-end));
		return "model/model";
	}
	
	//导入花名册
	@RequestMapping("userBatchInsert")
	public synchronized String userBatchInsert(Model model,HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile[] files) throws IOException{
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
        //monthBillDao.insertBatch(list);
        long end = System.currentTimeMillis();
        System.out.println("耗时~~"+(start-end));
		return "roleManagement/roleTableList";
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
