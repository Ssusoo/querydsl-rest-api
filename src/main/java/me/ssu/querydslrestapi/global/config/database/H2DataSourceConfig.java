package me.ssu.querydslrestapi.global.config.database;

import me.ssu.querydslrestapi.global.constant.CommonConstant;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(CommonConstant.ActiveProfile.LOCAL)
public class H2DataSourceConfig {

	@Bean
	public ServletRegistrationBean<WebServlet> h2servletRegistration(){
		var registrationBean = new ServletRegistrationBean<>( new WebServlet());
		registrationBean.addUrlMappings("/h2-console/*");
		return registrationBean;
	}
}