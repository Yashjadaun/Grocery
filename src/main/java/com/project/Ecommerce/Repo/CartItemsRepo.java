package com.project.Ecommerce.Repo;

import com.project.Ecommerce.Models.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemsRepo extends JpaRepository<CartItems ,Long> {
    @Query("select c from CartItems c where c.cart.cartid =?2 and c.product.productId=?1 ")
    CartItems findCartItemsByCartIdandproductId(Long productID, Long cartId);
//    @Modifying
//    @Query("DELETE FROM CartItems ci WHERE ci.product.productId = :productId")
//    void deleteAllByProductId(@Param("productId") Long productId);
}
