package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
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
@RequestMapping("/user-service")
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
    public String status() {
        return String.format("it work port = %s"
                , env.getProperty("local.server.port")
        );
    }

    @GetMapping("/welcome")
    public String welcome() {
//        return env.getProperty("greeting.message");
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
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
        List<ResponseUser> userList = userService.getUserByAll();

        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
        ResponseUser userList = userService.getUserByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }
}
