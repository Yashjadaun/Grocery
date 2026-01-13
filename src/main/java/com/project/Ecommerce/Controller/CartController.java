package com.project.Ecommerce.Controller;

import com.project.Ecommerce.DTO.CartDTO;
import com.project.Ecommerce.Services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ModelMapper modelmapper;


    @PostMapping("/carts/products/{productID}/quantity/{quantity}")
    public ResponseEntity<?> addProducttocart(@PathVariable Long productID,@PathVariable Integer quantity){
        return cartService.addProducttocart(productID,quantity);
    }


    @GetMapping("/carts")
        public ResponseEntity<?>getCart(){
            return cartService.getAllCart();
        }

    @GetMapping("/carts/user/cart")
    public ResponseEntity<?>getUserCart(){
      return cartService.findUserCart();
    }

    @PutMapping("/carts/products/{productID}/quantity/{operation}")
    public ResponseEntity<?>updatequantity(@PathVariable Long productID,@PathVariable String operation){
        return cartService.updatequantity(productID,operation.equalsIgnoreCase("delete")?-1:1);
    }


    @DeleteMapping("/carts/{cartId}/product/{productsId}")
    public ResponseEntity<?>deletefromcart(@PathVariable Long cartId,@PathVariable Long productsId){
        return cartService.deletefromcart(cartId,productsId);
    }

}
