package com.jeferson.jecommerce.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private LocalDate birtDate;
    private String password;

    @OneToMany
    private List<Order> orders = new ArrayList<>();

    public User(){}

    public User(Long id, String name, String email, LocalDate birtDate, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birtDate = birtDate;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirtDate() {
        return birtDate;
    }

    public void setBirtDate(LocalDate birtDate) {
        this.birtDate = birtDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
