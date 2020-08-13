package com.bhyh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.bhyh")
@ServletComponentScan
@EnableSwagger2
@MapperScan("com.bhyh.dao.mapper")
public class TokenDemoApplication {

	public static void main(String[] args) {                                                                                                                                                                                                

		SpringApplication.run(TokenDemoApplication.class, args);
	}

}
