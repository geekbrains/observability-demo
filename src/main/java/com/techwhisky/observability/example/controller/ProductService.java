package com.techwhisky.observability.example.controller;


import com.techwhisky.observability.example.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductService {

    List<Product> products;

    public ProductService(){

        this.products=List.of(new Product(1,"Apple watch","A nice watch with smart features",50000,2),
                new Product(2,"Samsung watch","A smart watch with smart dial",30000,4),
                new Product(3,"LG Washing machine","Fully automatch 10kg",20000,10),
                new Product(4,"Sony TV","65 inch LED Panel",60000,5),
                new Product(5,"Voltas AC","1.5 ton split AC",35000,5));
    }

    @GetMapping(value = {"","/"},produces = "application/json")
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.of(Optional.ofNullable(products));
    }

}
