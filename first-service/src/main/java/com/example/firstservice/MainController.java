package com.example.firstservice;

import com.sun.tools.javac.Main;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/first-service")
@Slf4j
public class MainController {

    //yml에서 설정한 변수가져오기
    Environment env;

    @Autowired
    public MainController(Environment env) {
        this.env = env;
    }

    @GetMapping("/welcome")
    public String selcome () {
        return "Welcome to the first service";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("first-request") String header) {
        return header;
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        log.info("Server prot={}", request.getServerPort());
        return String.format("hi second custom filter PORT = %s"
                , env.getProperty("local.server.port"));//랜덤포트로실제할당받은정보가져오기
    }
}
