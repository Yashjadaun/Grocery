package com.project.Ecommerce.Services;

import com.project.Ecommerce.DTO.*;
import com.project.Ecommerce.Models.*;
import com.project.Ecommerce.Repo.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private PaymentRepository paymentrepo;

    @Autowired
    private OrderRepository orderrepo;

    @Autowired
    private CartItemsRepo cartitemrepo;

    @Autowired
    private OrderItemRepository orderitemrepo;

    @Autowired
    private  Product_Repo productRepo;

    @Autowired
    private CartService cartService;

    @Autowired
    private ModelMapper modelmapper;

    @Transactional
    public ResponseEntity<?> placeorder(User user, String paymentMethod, OrderRequestDTO orderrequest) {


//  1 Getting User Cart and user address
        Cart cart=cartRepo.findByUser_Email(user.getEmail());
        Address address=addressRepo.findById(orderrequest.getAddressId()).orElseThrow(()->new RuntimeException("Address have some issue"));

        if(cart==null){
            return ResponseEntity.badRequest().build();
        }
//  2 Creating order
        Order orders=new Order();
        orders.setEmail(user.getEmail());
        orders.setName(user.getUserName());
        orders.setOrderDate(LocalDate.now());
        orders.setAddress(address);
        orders.setOrderStatus("Order Placed");

        Payment payment=new Payment(paymentMethod,orderrequest.getPgPaymentId(),orderrequest.getPgStatus(),orderrequest.getPgResponseMessage(),orderrequest.getPgName());
        payment.setOrders(orders);
        payment=paymentrepo.save(payment);

        orders.setPayment(payment);
        orders.setTotalAmount(cart.getTotalprice());


        orders=orderrepo.save(orders);


//  3 Get items from the cart into a orderItems
        List<CartItems>cartItemsList=cart.getItems();
        if (cartItemsList.size() == 0) {

            throw new RuntimeException("Cart is empty");

        }

        List< OrderItem>orderitemlist=new ArrayList<>();

        for(CartItems items:cartItemsList){
             OrderItem orderitem=new OrderItem();
             orderitem.setOrder_id(orders);
             orderitem.setProduct(items.getProduct());
             orderitem.setQuantity(items.getQuantity());
             orderitem.setDiscount(items.getDiscount());
             orderitem.setOrderedProductPrice(items.getProductPrice());

            orderitemlist.add(orderitem);

//  4 update product stock
            Product product=items.getProduct();
            product.setQuantity(product.getQuantity()- items.getQuantity());
            product=productRepo.save(product);



        }

        orderitemlist=orderitemrepo.saveAll(orderitemlist);

        cart.getItems().clear();
        cart.setTotalprice(0.0);
        cartRepo.save(cart);

        OrderDTO orderDTO=modelmapper.map(orders,OrderDTO.class);
        orderDTO.setOrderItemList(new ArrayList<>());

          for(OrderItem orderitem:  orderitemlist){
              OrderItemDTO itemDto=modelmapper.map(orderitem, OrderItemDTO.class);
              itemDto.setProduct(modelmapper.map(orderitem.getProduct(), ProductDTO.class));
              orderDTO.getOrderItemList().add(itemDto);
          }
          orderDTO.setPaymentDTO(modelmapper.map(orders.getPayment(), PaymentDTO.class));
          orderDTO.setAddressId(orders.getAddress().getAddressId());

          return ResponseEntity.ok(orderDTO);
    }
}
