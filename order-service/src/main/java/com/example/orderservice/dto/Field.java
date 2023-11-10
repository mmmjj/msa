package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * kafka connect - 첫번째로 작은 단위
 * 어떤 데이터를 저장하는지 구분
 */
@Data
@AllArgsConstructor
public class Field {

    /**
     * 카프카 커넥터로 데이터를 보낼때 어떤 정보가 저장되는지 필드가 필요하 3개
     */
    private String type;
    private boolean optional;
    private String field;
}
