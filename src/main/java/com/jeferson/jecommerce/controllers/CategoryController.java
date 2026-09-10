package com.jeferson.jecommerce.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeferson.jecommerce.dto.CategoryDTO;
import com.jeferson.jecommerce.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    private final CategoryService service;

    CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll(){           
        List<CategoryDTO> list = service.findAll(); 
        return ResponseEntity.ok(list);
    }
}
