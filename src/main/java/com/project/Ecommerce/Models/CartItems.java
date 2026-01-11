package com.project.Ecommerce.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="cart_items")
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemsId;

    @ManyToOne
    @JoinColumn(name= "cartid")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "productId")
    private  Product product;

    private Integer quantity;
    private double discount;
    private double productPrice;


}
