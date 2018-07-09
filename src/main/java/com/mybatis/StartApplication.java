package com.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 *  Author:zouyang
 * 	Data:2018-4-18
 *  Create Project for mybatis
 *  
 */

@MapperScan("com.mybatis.mapper")//扫描mybatis的接口包类
@SpringBootApplication
//@ComponentScan("com.mybatis.controller")//包名
public class StartApplication{
	
	public static void main(String[] args) {		
		SpringApplication.run(StartApplication.class,args);
		System.out.println("mybatis start~~");
	}

}
