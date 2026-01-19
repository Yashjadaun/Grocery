package com.project.Ecommerce.Controller;

import com.project.Ecommerce.DTO.OrderRequestDTO;
import com.project.Ecommerce.Models.User;
import com.project.Ecommerce.Services.OrderService;
import com.project.Ecommerce.utils.Authuils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private Authuils authuils;

    @Autowired
    private OrderService orderservice;

    @PostMapping("/order/user/payments/{paymentMethod}")
    public ResponseEntity<?> createOrder(@PathVariable String paymentMethod, @RequestBody OrderRequestDTO orderrequest){
         User user=authuils.getUser();

         return orderservice.placeorder(user,paymentMethod, orderrequest);
    }

}
