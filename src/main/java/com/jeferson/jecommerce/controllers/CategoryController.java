package com.jeferson.jecommerce.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeferson.jecommerce.dto.CategoryDTO;
import com.jeferson.jecommerce.services.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/categories")
@Tag(name = "Categories", description = "Controllers for Categories")
public class CategoryController {

    private final CategoryService service;

    CategoryController(CategoryService service) {
        this.service = service;
    }

    @Operation(
			description = "Get all categories",
			summary = "Get all categories",
			responses = {
					@ApiResponse(description = "OK", responseCode = "200")
			}
	)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDTO>> findAll(){           
        List<CategoryDTO> list = service.findAll(); 
        return ResponseEntity.ok(list);
    }
}
