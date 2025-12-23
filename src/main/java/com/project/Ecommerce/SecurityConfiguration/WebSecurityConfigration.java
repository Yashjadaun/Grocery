package com.project.Ecommerce.SecurityConfiguration;

import com.project.Ecommerce.Models.AppRole;
import com.project.Ecommerce.Models.Role;
import com.project.Ecommerce.Models.User;
import com.project.Ecommerce.Repo.Role_Repo;
import com.project.Ecommerce.Repo.UserRepository;
import com.project.Ecommerce.Security.jwt.AuthEntryPoint;
import com.project.Ecommerce.Security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;


@Configuration
@EnableWebSecurity
public class WebSecurityConfigration {

     @Autowired
  private   UserDetailsService userDetailsService;

     @Autowired
    private AuthEntryPoint unauthorizedHandler;


@Autowired
private AuthTokenFilter tokenFilter;


//Security filter
     @Bean
    public SecurityFilterChain filterChain(HttpSecurity http){
        return http.csrf(csrf->csrf.disable())
                 .exceptionHandling(ex->ex.authenticationEntryPoint(unauthorizedHandler))
                 .sessionManagement(session ->
                         session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                  .authorizeHttpRequests(auth ->

                        auth .requestMatchers("/api/auth/**").permitAll()
                         //.requestMatchers("/api/public/**").permitAll()
                         .requestMatchers("/api/admin/**").permitAll()
                         .anyRequest().authenticated())
                 .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                 .build();
     }

    // 🔐 Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔑 Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 🧠 Authentication Provider
  @Bean
    public  DaoAuthenticationProvider  authenticationProvider(){
         DaoAuthenticationProvider authenticationProvider =new DaoAuthenticationProvider(userDetailsService);
         authenticationProvider.setPasswordEncoder(passwordEncoder());
         return authenticationProvider;
    }


    @Bean
    public CommandLineRunner initData(Role_Repo  roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(AppRole.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });

            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> {
                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(sellerRole);
            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);


            // Create users if not already present
            if (!userRepository.existsByUserName("user1")) {
                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("seller1")) {
                User seller1 = new User("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
                userRepository.save(seller1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
                userRepository.save(admin);
            }

            // Update roles for existing users
            userRepository.findByUserName("user1").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUserName("seller1").ifPresent(seller -> {
                seller.setRoles(sellerRoles);
                userRepository.save(seller);
            });

            userRepository.findByUserName("admin").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });
        };
    }
}
