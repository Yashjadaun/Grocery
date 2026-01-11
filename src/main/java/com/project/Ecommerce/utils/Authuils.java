package com.project.Ecommerce.utils;

import com.project.Ecommerce.Models.User;
import com.project.Ecommerce.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Authuils {
    @Autowired
    private UserRepository userRepo;


    public String getCurrentUserEmail() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        User user=userRepo.findByUserName(authentication.getName()).orElseThrow(()->new RuntimeException("User not find"));

        return user.getEmail();
    }

    public User getUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        User user=userRepo.findByUserName(authentication.getName()).orElseThrow(()->new RuntimeException("User not find"));
        return user;
    }
}
