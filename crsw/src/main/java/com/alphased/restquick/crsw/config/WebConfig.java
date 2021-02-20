package com.alphased.restquick.crsw.config;

import com.alphased.restquick.crsw.CRSWContainer;
import com.alphased.restquick.crsw.filter.CRSWFilter;
import com.alphased.restquick.crsw.processor.AuthorizationProcessor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    private final CRSWContainer crswContainer;
    private final AuthorizationProcessor authorizationProcessor;

    public WebConfig(CRSWContainer crswContainer, AuthorizationProcessor authorizationProcessor) {
        this.crswContainer = crswContainer;
        this.authorizationProcessor = authorizationProcessor;
    }

    @Bean
    public FilterRegistrationBean<CRSWFilter> crswFilter() {
        FilterRegistrationBean<CRSWFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CRSWFilter(crswContainer, authorizationProcessor));
        registrationBean.addUrlPatterns("/generated/*");
        return registrationBean;
    }

}
