package com.flightDB.DBApp.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<LanguageCookieFilter> languageCookieFilterFilter() {
        FilterRegistrationBean<LanguageCookieFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LanguageCookieFilter());
        registrationBean.addUrlPatterns("/");
        return registrationBean;
    }
}
