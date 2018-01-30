package com.mkcheong;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MkblogApplication {
	public static void main(String[] args) {
		SpringApplication.run(MkblogApplication.class, args);
	}

	@Bean(name = "uploadPath")
	public String uploadPath() {
		return "/Users/jeongmungu/mkblog/image/";
	}

	@Bean(name = "uploadFilePath")
	public String uploadFilePath() {
		return "/Users/jeongmungu/mkblog/uploadFile/";
	}

	/*
     * lucy-xss-filter
     *
     * */
	@Bean
	public FilterRegistrationBean getFilterRegistrationBean(){
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new XssEscapeServletFilter());
		registrationBean.setOrder(1);
		registrationBean.addUrlPatterns("/*");//filter를 거칠 url patterns
		return registrationBean;
	}
}
