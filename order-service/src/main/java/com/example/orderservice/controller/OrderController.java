package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOreder;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("order-service")
@AllArgsConstructor
public class OrderController {

    OrderService orderService;

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOreder> createOrder(@PathVariable String userId,
                                      @RequestBody RequestOrder orderDetails) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = modelMapper.map(orderDetails, OrderDto.class);
        orderDto.setUserId(userId);
        OrderDto createDto = orderService.createOrder(orderDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(createDto, ResponseOreder.class));
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOreder>> getOrder(@PathVariable String userId) {
        Iterable<OrderEntity> orderEntities = orderService.getOrderByUserId(userId);

        List<ResponseOreder> result = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        orderEntities.forEach(v -> {
            result.add(modelMapper.map(v, ResponseOreder.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
