package com.project.Ecommerce.Repo;

import com.project.Ecommerce.Models.AppRole;
import com.project.Ecommerce.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface Role_Repo extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
