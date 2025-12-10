package com.project.Ecommerce.Expception.AllExeption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiExpetion extends RuntimeException{
   public String message;
}
