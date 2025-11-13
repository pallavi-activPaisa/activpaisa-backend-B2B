package com.activpaisa.loan_app.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.activpaisa.loan_app.services.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class Controller {

    @Autowired
    private ProductService service;

    @GetMapping("/columns")
    public List<String> getColumns() {
        return service.getColumnNames();
    }

}
