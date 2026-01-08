package com.project.Ecommerce.DTO;

import com.project.Ecommerce.Models.CartItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartDTO {
    private  Long cartid;
    private List<CartItemDTo> Items;

    private Double totalprice;
}
