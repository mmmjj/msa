package com.example.catalogservice.dto;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class CatalogDto implements Serializable {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;//주문번호
    private String userId;//주문자

}
