package com.szl.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.szl.wechat.common.PropertiesListener;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		 SpringApplication application = new SpringApplication(Application.class);
	        application.addListeners(new PropertiesListener("application.properties"));
	        application.run(args);
	}
}
