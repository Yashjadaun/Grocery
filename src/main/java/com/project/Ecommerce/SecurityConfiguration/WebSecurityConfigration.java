package com.project.Ecommerce.SecurityConfiguration;

import com.project.Ecommerce.Security.jwt.AuthEntryPoint;
import com.project.Ecommerce.Security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
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
                         .requestMatchers("/api/public/**").permitAll()
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

}
