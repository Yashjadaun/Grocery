package com.project.Ecommerce.Repo;

import com.project.Ecommerce.Models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart,Long> {
    Cart findByUser_Email(String email);
}
