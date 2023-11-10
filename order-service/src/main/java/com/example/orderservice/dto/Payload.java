package com.example.orderservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * kafka connect - 두번째로 작은 단위
 */
@Data
@Builder
public class Payload {
    /**
     * db 테이블 컬럼 내용과 동일한것들
     */
    private String order_id;
    private String user_id;
    private String product_id;
    private int qty;
    private int total_price;
    private int unit_price;
}
