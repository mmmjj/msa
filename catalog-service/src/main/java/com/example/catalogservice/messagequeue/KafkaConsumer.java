package com.example.catalogservice.messagequeue;

import com.example.catalogservice.jpa.CatalogEntity;
import com.example.catalogservice.jpa.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 컨슈머
 * 컨슈머데이터로 가져온 데이터를 이용해 db에 업데이트
 */
@Service
@Slf4j
public class KafkaConsumer {

    CatalogRepository repository;

    @Autowired
    public KafkaConsumer(CatalogRepository repository) {
        this.repository = repository;
    }

    /**
     * example-catalog-topic에 데이터전달이 되면 updqty를 실행
     * @param kafkaMessage
     */
    @KafkaListener(topics = "example-catalog-topic") //example-catalog-topic으로만 사용하기때문에 example-catalog-topic로 이름을 지음
    public void updateQty(String kafkaMessage) {
        log.info("kafka message : -> ", kafkaMessage);

        //역직렬화해서 가져온것 다시 직렬화
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        CatalogEntity entity = repository.findByProductId((String) map.get("productId"));
        if(entity != null) {
            entity.setStock(entity.getStock() - (Integer) map.get("qty"));
            repository.save(entity);
        }


    }
}
