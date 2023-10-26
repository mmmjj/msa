package com.example.userservice.security;

import com.example.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();//비활성화
//        http.authorizeRequests().antMatchers("/users/**").permitAll();//인증작업없이 사용할수있는범위
        //인증된상태에서만 허용
        http.authorizeRequests().antMatchers("/**")//모든api
                .hasIpAddress("127.0.0.1")//ip제한
                .and()
                .addFilter(getAuthenticationFilter());//필터통과된것만

        //메모리h2인경우필요 프레임으로구성된화면 액박남
        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        //인증처리할것, sSec에 매니저사용
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManager());

        return authenticationFilter;
    }
}
