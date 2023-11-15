package com.example.userservice.client;

import com.example.userservice.vo.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderServiceClient {

    /**
     * ResponseEntity<List<ResponseOreder>> getOrder 에서 List... 이 여기 반환타입이 됨
     * @param userId
     * @return
     */
    @GetMapping("/order-service/{userId}/orders")
    List<ResponseOrder> getOrders(@PathVariable String userId);
}
