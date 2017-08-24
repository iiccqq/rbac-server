package com.rbac.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rbac.dao.SysUserTokenrDao;
import com.rbac.filter.SecurityFilter;

@Configuration
public class FilterConfig {
	  
	@Resource
	SysUserTokenrDao sysUseTokenrDao;
	
    @Bean  
    public FilterRegistrationBean  filterRegistrationBean() {  
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();        
        SecurityFilter securityFilter = new SecurityFilter();  
        securityFilter.setSysUseTokenrDao(sysUseTokenrDao);
        registrationBean.setFilter(securityFilter);  
        List<String> urlPatterns = new ArrayList<String>();  
        urlPatterns.add("/sys/*");  
        registrationBean.setUrlPatterns(urlPatterns);  
        return registrationBean;  
    }  
}
