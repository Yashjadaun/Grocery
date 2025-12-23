package com.project.Ecommerce.SecurityService;


import com.project.Ecommerce.Models.User;
import com.project.Ecommerce.Repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailService implements UserDetailsService {

     @Autowired
     private UserRepository userRepository;


    @Override
     @Transactional
    public UserDetail loadUserByUsername(String username) throws UsernameNotFoundException {


         User user = userRepository.findByUserName(username)
                 .orElseThrow(() ->
                         new UsernameNotFoundException(
                                 "User Not Found with username: " + username));


         return UserDetail.build(user);
     }
}












