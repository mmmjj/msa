package com.example.catalogservice.entity;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "catalog")
public class CatalogEntity implements
        Serializable {//직렬화 추가 TODO 뭐지..

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 120, unique = true)
    private String productId;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private Integer stock;
    @Column(nullable = false)
    private Integer unitPrice;

    @Column(nullable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private Date createdAt;

}
