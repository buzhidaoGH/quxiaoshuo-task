package com.localhost.quxiaoshuotask;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//扫描Mybatis的Mapper,注解配置
@MapperScan(basePackages = "com.localhost.quxiaoshuotask.dao")
//开启定时任务
@EnableScheduling
public class QuxiaoshuoTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuxiaoshuoTaskApplication.class, args);
	}

}
