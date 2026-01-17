package com.techwhisky.observability.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Product {

    private int id;

    private String name;

    private String description;

    private double price;

    private int discount;
}
