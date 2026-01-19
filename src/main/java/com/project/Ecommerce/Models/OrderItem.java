package com.project.Ecommerce.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "productId")
    private  Product product;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private  Order order_id;
    private  Integer quantity;
    private double discount;
    private double orderedProductPrice;

}
