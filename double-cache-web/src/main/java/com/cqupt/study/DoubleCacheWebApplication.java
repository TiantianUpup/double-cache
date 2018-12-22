package com.cqupt.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.cqupt.study.dao")
public class DoubleCacheWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoubleCacheWebApplication.class, args);
	}

}

