package com.example.catalogservice.controller;

import com.example.catalogservice.jpa.CatalogEntity;
import com.example.catalogservice.service.CatalogService;
import com.example.catalogservice.vo.ResponseCatalog;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/catalog-service")
@AllArgsConstructor //수업내용과다름
public class CatalogController {

    Environment env;

    CatalogService catalogService;

//    @Value("${local.server.port}")
//    private static String localPort;


    @GetMapping("/health_check")
    public String status() {
        return String.format("it work port = %s"
//                , localPort 안된당
                , env.getProperty("local.server.port")
        );
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getUser() {
        Iterable<CatalogEntity> enList = catalogService.getAllCatalogs();
        List<ResponseCatalog> responseCatalogList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        enList.forEach(e -> {
            responseCatalogList.add(modelMapper.map(e, ResponseCatalog.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(responseCatalogList);
    }


}
