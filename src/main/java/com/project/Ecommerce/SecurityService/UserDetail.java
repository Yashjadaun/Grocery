package com.project.Ecommerce.SecurityService;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.project.Ecommerce.Models.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class UserDetail implements UserDetails {

    Long id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    UserDetail(Long id,String username,String email,String password,Collection<? extends GrantedAuthority> authorities){
       this.id=id;
       this.username=username;
       this.email=email;
       this.password=password;
       this.authorities=authorities;
    }


    public static UserDetail build(User user){
        List<GrantedAuthority>authorities= user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());

        return new UserDetail(user.getUserId(), user.getUserName(),user.getEmail(),user.getPassword(),authorities);
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
         return this.authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
