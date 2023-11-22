package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.messagequeue.OrderProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOreder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("order-service")
@AllArgsConstructor
@Slf4j
public class OrderController {

    OrderService orderService;
    KafkaProducer kafkaProducer;
    OrderProducer orderProducer;

    /**
     * 주문추가
     * @param userId
     * @param orderDetails
     * @return
     */
    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOreder> createOrder(@PathVariable String userId,
                                      @RequestBody RequestOrder orderDetails) {
        log.info("before add orders data");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = modelMapper.map(orderDetails, OrderDto.class);
        orderDto.setUserId(userId);

        //jpa
        OrderDto createDto = orderService.createOrder(orderDto);
        ResponseOreder responseOreder = modelMapper.map(createDto, ResponseOreder.class);

        //kafka로 메세지 전달하기

//        orderDto.setOrderId(UUID.randomUUID().toString());
//        orderDto.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());

        //프로듀서사용 kafka에 주문 정보 추가하기
        kafkaProducer.send("example-catalog-topic", orderDto);
//        orderProducer.send("orders", orderDto);
        
        //리턴바디
//        ResponseOreder responseOreder = modelMapper.map(orderDto, ResponseOreder.class);
        //kafka로 메세지 전달하기 끝

        log.info("after add orders data");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOreder);
    }

    /**
     * 주문조회
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOreder>> getOrder(@PathVariable String userId) {
        log.info("before retrieve orders data");

        Iterable<OrderEntity> orderEntities = orderService.getOrderByUserId(userId);

        List<ResponseOreder> result = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        orderEntities.forEach(v -> {
            result.add(modelMapper.map(v, ResponseOreder.class));
        });

//        try {
//            Thread.sleep(1000);
//            throw new Exception("장애 발생");
//        } catch (Exception e) {
//            log.warn(e.getMessage());
//        }
        log.info("after retrieve orders data");


        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 주문 추가할때 호출하는 producer api
     */
}
