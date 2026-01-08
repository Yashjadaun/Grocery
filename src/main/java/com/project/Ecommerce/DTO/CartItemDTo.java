package com.project.Ecommerce.DTO;


import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTo {

    private Long cartItemsId;


    private CartDTO cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductDTO product_id;

    private Integer quantity;
    private double discount;
    private double productPrice;
}
