package com.example.userservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();//비활성화
        http.authorizeRequests().antMatchers("/users/**").permitAll();//인증작업없이 사용할수있는범위

        //메모리h2인경우필요 프레임으로구성된화면 액박남
        http.headers().frameOptions().disable();
    }
}
