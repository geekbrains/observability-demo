package com.techwhisky.observability.example.controller;


import com.techwhisky.observability.example.model.Product;
import com.techwhisky.observability.example.model.ProductDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductService {

    List<Product> products;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${product.description.service.endpoint}")
    private String productDescriptionServiceBaseUrl;

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


    @GetMapping(value = {"/{productId}"},produces = "application/json")
    public ResponseEntity<Product> getProductById(@PathVariable("productId") int productId){
       Optional<Product> product=products.stream().filter(p -> p.getId()==productId).findFirst();
       if(product.isPresent()){
          ResponseEntity<ProductDescription> responseEntity=restTemplate.exchange(productDescriptionServiceBaseUrl+"/"+productId, HttpMethod.GET,null, ProductDescription.class);
          if(responseEntity.getStatusCode()== HttpStatusCode.valueOf(200)) {
              Product product1=product.get();
                      product1.setDescription(responseEntity.getBody().getDescription());
              return ResponseEntity.of(Optional.of(product1));
          }
       }else{
          return ResponseEntity.badRequest().build();
       }
       return null;
    }

}
