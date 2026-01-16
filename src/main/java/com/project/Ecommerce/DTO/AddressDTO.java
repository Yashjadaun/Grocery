package com.project.Ecommerce.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {


    private String Street;

    private String BuildingName;

    private String city;


    private String State;

    private String country;


    private String Pincode;


}
