package com.example.orderservice.vo;

import lombok.Data;

@Data
public class RequestOrder {
    
    private String productId; //상품아이디
    private Integer qty; //수량
    private Integer unitPrice; //단가
    
}
