package com.project.Ecommerce.Repo;

import com.project.Ecommerce.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Category_Repo extends JpaRepository<Category,Long> {
}
