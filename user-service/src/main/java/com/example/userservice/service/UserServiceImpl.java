package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import com.example.userservice.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

//    @Autowired
    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;

    Environment env;
    RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if(userEntity == null) throw new UsernameNotFoundException(username);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>());
    }

    @Autowired
    public UserServiceImpl(UserRepository userRepository
                           , BCryptPasswordEncoder passwordEncoder
                           , Environment env
                           , RestTemplate restTemplate
    ) { //생성자를 통해서 주입하는게 왜 더 좋지..
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
        this.restTemplate = restTemplate;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);

        UserDto returnUserDto = modelMapper.map(userEntity, UserDto.class);

        return returnUserDto;
    }

    @Override
    public ResponseUser getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) throw new UsernameNotFoundException("");

        ResponseUser userDto = new ModelMapper().map(userEntity, ResponseUser.class);

        /*List<ResponseOrder> orders = new ArrayList<>();*/
        //rest template 사용해서 주문정보 가져오기
        String orderUrl = String.format(env.getProperty("order_service.url"), userId); //http://127.0.0.1:8000/order-service/%s/orders
        //ResponseEntity<List<ResponseOreder>> order get return type
        ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(orderUrl, HttpMethod.GET
                , null //request param
                , new ParameterizedTypeReference<List<ResponseOrder>>() {
                });
        List<ResponseOrder> orderList = orderListResponse.getBody();

        userDto.setOrders(orderList);

        return userDto;
    }

    @Override
    public List<ResponseUser> getUserByAll() { //TODO entity에서 dto로 변경하기
        Iterable<UserEntity> enList = userRepository.findAll();
        List<ResponseUser> responseUserList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        enList.forEach(e -> {
            responseUserList.add(modelMapper.map(e, ResponseUser.class));
        });
        return responseUserList;
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        //data null체크
        if(userEntity == null) throw new UsernameNotFoundException("존재하지않아요");
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        return userDto;
    }
}
