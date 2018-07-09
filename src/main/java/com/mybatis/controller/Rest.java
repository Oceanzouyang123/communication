package com.mybatis.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Rest {

	@RequestMapping("abc")
	public String TestController(){
		return "123";
	}
}
