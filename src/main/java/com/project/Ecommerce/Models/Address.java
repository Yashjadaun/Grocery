package com.project.Ecommerce.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size( min =5 ,message="Street should be at least 4 character")
    private String Street;

    @NotBlank
    @Size( min =5 ,message="BuildingName should be at least 4 character")
    private String BuildingName;
    @NotBlank
    @Size( min =3 ,message="city should be at least 3 character")
    private String city;

    @NotBlank
    @Size( min =3 ,message="State should be at least 3 character")
    private String State;

    @NotBlank
    @Size( min =3 ,message="country should be at least 4 character")
    private String country;

    @NotBlank
    @Size( min =6 ,message="BuildingName should be at least 4 character")
    private String Pincode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users =new ArrayList<>();

}
