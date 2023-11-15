package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@Slf4j
public class UserController {

    //autowire말고 생성자로주입하기
    private Environment env;

    //@value사용하기
    @Autowired
    private Greeting greeting;

    private UserService userService;

    public UserController(Environment env, UserService userService) {
        this.env = env;
        this.userService = userService;
    }

    @GetMapping("/health_check")
    @Timed(value = "users.status", longTask = true)
    public String status() {
        log.info("/health_check");
        return String.format("it work"
                + ", port(local.server.port) = " + env.getProperty("local.server.port")
                + ", port(server.port) = " + env.getProperty("server.port")
                + ", token key = " + env.getProperty("token.secret")
                + ", token expiration time= " + env.getProperty("token.expiration_time")
                + ", token expiration time= " + env.getProperty("order_service.url"), "test"
                + env.getProperty("order_service.exception.orders_is_empty")
        );
    }

    @GetMapping("/welcome")
    @Timed(value = "users.welcome", longTask = true)
    public String welcome() {
        log.info("/welcome");
//        return env.getProperty("greeting.message");
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        log.info("/users");
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto dto = mapper.map(user, UserDto.class);
        UserDto returnDto = userService.createUser(dto);

        //TODO 만든거 조회해서 반환하는게 맞지않나ㅏㅏ..
        ResponseUser responseUser = mapper.map(returnDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("users")
    public ResponseEntity<List<ResponseUser>> getUser() {
        log.info("/users");
        List<ResponseUser> userList = userService.getUserByAll();

        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
        log.info("/users/userId");
        ResponseUser userList = userService.getUserByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }
}
