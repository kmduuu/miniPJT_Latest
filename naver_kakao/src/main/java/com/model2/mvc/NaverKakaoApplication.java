package com.model2.mvc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.model2.mvc")
public class NaverKakaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(NaverKakaoApplication.class, args);
	}

}
