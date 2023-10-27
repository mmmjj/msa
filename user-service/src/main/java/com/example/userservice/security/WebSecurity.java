package com.example.userservice.security;

import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();//비활성화
        //인증작업없이 사용할수있는범위
//        http.authorizeRequests().antMatchers("/users/**").permitAll();
        http.authorizeRequests().antMatchers("/actuator/**").permitAll();
        http.authorizeRequests().antMatchers("/health_check/**").permitAll();
        //인증된상태에서만 허용
        http.authorizeRequests()
                .antMatchers("/error/**").permitAll() // public abstract java.lang.String javax.servlet.ServletRequest.getRemoteAddr() is not supported 보기 싫을때 활성화 https://www.inflearn.com/chats/789887
                .antMatchers("/**")//모든api
                .hasIpAddress("192.168.35.112")//ip제한
                .and()
                .addFilter(getAuthenticationFilter());//필터통과된것만

        //메모리h2인경우필요 프레임으로구성된화면 액박남
        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        //인증처리할것, sSec에 매니저사용
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(
                authenticationManager()
                , userService
                , env
        );
        authenticationFilter.setAuthenticationManager(authenticationManager());

        return authenticationFilter;
    }

    //인증처리매서드
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //select pwd from user...
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }


}
