package com.jeferson.jecommerce.dto;


import com.jeferson.jecommerce.entities.Product;

public class ProductMinDTO {
    private Long id;
    //private List<CategoryDTO> categories = new ArrayList<>();// coloquei agora
    private String name;
    private Double price;
    private String imgUrl;

    public ProductMinDTO(){}

    public ProductMinDTO(Long id, String name, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public ProductMinDTO(Product entity){
        id = entity.getId();
        name = entity.getName();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }
    
//    public List<CategoryDTO> getCategories() {
//        return categories;
//    }
}
