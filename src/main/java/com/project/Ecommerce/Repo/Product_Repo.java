package com.project.Ecommerce.Repo;

import com.project.Ecommerce.Models.Category;
import com.project.Ecommerce.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Product_Repo extends JpaRepository<Product,Long> {
   // List<Product> findByCategory(Category c);

    List<Product> findByCategoryOrderByPriceAsc(Category c);

    List<Product> findByProductNameLikeIgnoreCase(String keyword);
}
