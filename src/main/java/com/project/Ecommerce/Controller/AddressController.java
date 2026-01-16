package com.project.Ecommerce.Controller;


import ch.qos.logback.classic.Logger;
import com.project.Ecommerce.DTO.AddressDTO;
import com.project.Ecommerce.Models.User;
import com.project.Ecommerce.Services.AddressService;
import com.project.Ecommerce.Services.CartService;
import com.project.Ecommerce.utils.Authuils;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AddressController {

    private static final Logger logger =
            (Logger) LoggerFactory.getLogger(CartService.class);

    @Autowired
    private Authuils authutils;

    @Autowired
    private AddressService addservice;



    @PostMapping("/address")
    public ResponseEntity<?> createAddress(@Valid @RequestBody AddressDTO address){

        User user=authutils.getUser();

          return addservice.createAddress(address,user);
    }

    @GetMapping("/address")
    public ResponseEntity<?> getAllAddress(){
        return addservice.getAll();
    }

    @GetMapping("/address/{addressId}")
    public ResponseEntity<?> getAddressByid(@PathVariable Long addressId){
        return addservice.getAddressById(addressId);
    }


   @GetMapping("/address/user")
    public ResponseEntity<?> getAddressByUsername(){
       logger.info(
               authutils.getCurrentUserEmail()   );

       return addservice.findaddresByUsername(authutils.getCurrentUserEmail());

   }


   @PutMapping("/address/{addressId}")
     public ResponseEntity<?> updateaddress(@PathVariable Long addressId,@RequestBody AddressDTO addressDTO ){
        return  addservice.update(addressId,addressDTO);
   }


   @DeleteMapping("/address/{addressId}")
    public ResponseEntity<?> deleteaddress(@PathVariable Long addressId){
        return addservice.delete(addressId);
   }


}
