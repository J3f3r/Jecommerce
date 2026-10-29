package com.jeferson.jecommerce.controllers;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jeferson.jecommerce.dto.ProductDTO;
import com.jeferson.jecommerce.dto.ProductMinDTO;
import com.jeferson.jecommerce.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Products", description = "Controllers for Products")
public class ProductController {

    private final ProductService service;

    ProductController(ProductService service) {
        this.service = service;
    }

    @Operation(
			description = "Get product by id",
			summary = "Get product by id",
			responses = {
					@ApiResponse(description = "OK", responseCode = "200"),
					@ApiResponse(description = "Not Found", responseCode = "404")
			}
	)
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
        ProductDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(
			description = "Get all products pageable",
			summary = "Get all products pageable",
			responses = {
					@ApiResponse(description = "OK", responseCode = "200")
			}
	)
    @GetMapping
    public ResponseEntity<Page<ProductMinDTO>> findAll(
            @RequestParam(value = "name", defaultValue = "") String name, 
            Pageable pageable) {
        Page<ProductMinDTO> list = service.findAll(name, pageable); 
        return ResponseEntity.ok(list);
    }

    @Operation(
			description = "Create a new product",
			summary = "Create a new product",
			responses = {
					@ApiResponse(description = "Created", responseCode = "201"),
					@ApiResponse(description = "Bad Request", responseCode = "400"),
					@ApiResponse(description = "Unauthorized", responseCode = "401"),
					@ApiResponse(description = "Forbidden", responseCode = "403"),
					@ApiResponse(description = "Unprocessable Entity", responseCode = "422")
			}
	)
	@SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto){
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
        // ou return service.insert(dto);
    }

    @Operation(
			description = "Update a product",
			summary = "Update a product",
			responses = {
					@ApiResponse(description = "Created", responseCode = "201"),
					@ApiResponse(description = "Bad Request", responseCode = "400"),
					@ApiResponse(description = "Unauthorized", responseCode = "401"),
					@ApiResponse(description = "Forbidden", responseCode = "403"),
					@ApiResponse(description = "Not Found", responseCode = "404"),
					@ApiResponse(description = "Unprocessable Entity", responseCode = "422")
			}
	)
	@SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(
			description = "Delete a product",
			summary = "Delete a product",
			responses = {
					@ApiResponse(description = "Success", responseCode = "204"),
					@ApiResponse(description = "Bad Request", responseCode = "400"),
					@ApiResponse(description = "Unauthorized", responseCode = "401"),
					@ApiResponse(description = "Forbidden", responseCode = "403"),
					@ApiResponse(description = "Not Found", responseCode = "404"),
					@ApiResponse(description = "Unprocessable Entity", responseCode = "422")
			}
	)
	@SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
