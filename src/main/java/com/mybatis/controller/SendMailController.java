package com.mybatis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SendMailController {

	@RequestMapping("sendMail")
	public String sendMail(){
		System.out.println("发送邮件");
		return "log/logList";
	}
	
	
}
