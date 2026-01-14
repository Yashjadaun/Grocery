package com.project.Ecommerce.Repo;

import com.project.Ecommerce.Models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepo extends JpaRepository<Cart,Long> {
    Cart findByUser_Email(String email);

    @Query(value = """
    SELECT DISTINCT c.*
    FROM carts c
    JOIN cart_items ci ON ci.cartid = c.cartid
    WHERE ci.product_id = ?1
""", nativeQuery = true)
    List<Cart> findCartsByProductId(Long product_id);
}
