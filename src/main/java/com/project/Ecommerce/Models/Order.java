package com.project.Ecommerce.Models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Email
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Size(max = 20)
    private String name;

    @OneToMany(mappedBy =  "order_id",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<OrderItem> orderItems =new ArrayList<OrderItem>();

    private LocalDate orderDate;

    @OneToOne
    @JoinColumn(name="payment_id")
    private Payment payment;

    private Double totalAmount;
    private String orderStatus;

    @ManyToOne
    @JoinColumn(name="address_id")
    private Address address;
}
