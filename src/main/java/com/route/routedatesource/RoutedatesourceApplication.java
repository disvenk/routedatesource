package com.route.routedatesource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.route.routedatesource.mapper")
public class RoutedatesourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoutedatesourceApplication.class, args);
	}
}
