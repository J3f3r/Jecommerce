package com.jeferson.jecommerce.dto;


import java.util.ArrayList;
import java.util.List;

import com.jeferson.jecommerce.entities.Category;
import com.jeferson.jecommerce.entities.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductDTO {
	
	@Schema(description = "Product ID", example = "1")
    private Long id;
    
	@Schema(description = "Product name", example = "Mechanical Keyboard")
    @Size(min = 3, max = 80, message = "Nome precisa ter de 3 a 80 caracteres")
    @NotBlank(message = "Campo requerido")
    private String name;

	@Schema(description = "Product description", example = "RGB Mechanical Keyboard with blue switches")
    @Size(min = 10, message = "Descrição precisa ter no mínimo 10 caracteres")
    @NotBlank(message = "Campo requerido")
    private String description;

	@Schema(description = "Product price", example = "350.00")
    @NotNull(message = "Campo requerido")
    @Positive(message = "O preço precisa ser positivo")
    private Double price;
    
	@Schema(description = "Product image URL", example = "https://raw.githubusercontent.com/devsuperior/apirest-resources/master/bds-ds.jpg")
    private String imgUrl;
    
    @NotEmpty(message = "Deve ter pelo menos uma categoria")
    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO(){}

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public ProductDTO(Product entity){// copia os dados basicos
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();
        
        for (Category cat : entity.getCategories()) {// copia tambem as categorias e cria dtos associados a entidade ProductDTO
        	categories.add(new CategoryDTO(cat));
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }
    
    public List<CategoryDTO> getCategories() {
        return categories;
    }
}
