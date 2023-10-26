package com.example.userservice.security;

import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            //로그인데이터받기
            RequestLogin requestLogin = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            //authtoken으로 변경해서 리턴
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestLogin.getEmail(), 
                            requestLogin.getPassword(), 
                            new ArrayList<>())        
            );
            
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 성공 후 로직 void타입
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
