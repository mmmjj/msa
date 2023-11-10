package com.example.orderservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * kafka connect - 3번째로 작은 단위
 */
@Data
@Builder
public class Schema {

    private String type;
    private List<Field> fields;
    private boolean optional;
    private String name;
}
