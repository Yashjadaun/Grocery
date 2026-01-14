package com.project.Ecommerce.Services;

import ch.qos.logback.classic.Logger;
import com.project.Ecommerce.DTO.CartDTO;
import com.project.Ecommerce.DTO.CartItemDTo;
import com.project.Ecommerce.Expception.AllExeption.ApiExpetion;
import com.project.Ecommerce.Models.Cart;
import com.project.Ecommerce.Models.CartItems;
import com.project.Ecommerce.Models.Product;
import com.project.Ecommerce.Repo.CartItemsRepo;
import com.project.Ecommerce.Repo.CartRepo;
import com.project.Ecommerce.Repo.Product_Repo;
import com.project.Ecommerce.utils.Authuils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Stream;

@Service
public class CartService {

    private static final Logger logger =
            (Logger) LoggerFactory.getLogger(CartService.class);


    @Autowired
     private CartRepo cartRepo;

    @Autowired
    private Product_Repo productRepo;

    @Autowired
    private  CartItemsRepo cartItemsRepo;

    @Autowired
    private ModelMapper modelmapper;

    @Autowired
    private Authuils authUtils;

    @Transactional
    public ResponseEntity<?> addProducttocart(Long productID, Integer quantity) {

        // 1. Find existing cart or create one
        Cart cart=findcartByUsername();




        // 2.Retrieve Product  Details
        Product product=productRepo.findById(productID)
                .orElseThrow(()->new RuntimeException("Product not found"));


        //3.perform Validation

        if(quantity==0){
            throw new ApiExpetion("Product "+quantity+" is zero");
        }

        if(product.getQuantity()<quantity){
            throw new ApiExpetion("Product "+quantity+" too large");
        }

        //4. Create CartItem abd save it

          CartItems cartItems=findCarItems(product.getProductId(), cart.getCartid());



        logger.info(
                "--------- Cartitem me kitne pade h : {}, quantity kitni h {}, Product me kitne pade h :{} ",cartItems.getQuantity(),quantity,product.getQuantity()
        );

        cartItems.setQuantity(cartItems.getQuantity()+quantity);
        cartItems.setCart(cart);
        cartItems.setProduct(product);
        cartItems.setDiscount(product.getDiscount());
        cartItems.setProductPrice(product.getSpecialPrice());

        cartItemsRepo.save(cartItems);


        //5. Return updated cart

      //  System.out.println("quantity kitn h "+ quantity+" kitni quantity hui toal "+max+" newadd kitna h "+newadd+" product me kitne the phele  "+product.getQuantity());

        product.setQuantity(product.getQuantity()-quantity);
        productRepo.save(product);
        logger.info(
                "Caritems me abb kitne h {}, product me abb kitne h {}",
                cartItems.getQuantity(),  product.getQuantity()
        );
        //System.out.println("Caritems me abb kitne h "+cartItems.getQuantity()+" product me abb kitne h "+product.getQuantity());

        cart.setTotalprice(cart.getTotalprice()+(product.getSpecialPrice()*quantity));

    cartRepo.save(cart);

  List<CartItemDTo>cartItemsList=cart.getItems()
          .stream()
          .map( item ->new CartItemDTo(
                  item.getCartItemsId(),
                  item.getProduct().getProductId(),
                  item.getProduct().getProductName(),
                  item.getQuantity(),

                  item.getDiscount(),
                  item.getProductPrice()
          )).toList();


        CartDTO cartdto= new CartDTO();
        cartdto.setTotalprice(cart.getTotalprice());
        cartdto.setCartid(cart.getCartid());
        cartdto.setItems(cartItemsList);


        return ResponseEntity.ok(cartdto);
    }



    public CartItems findCarItems(Long productID, Long cartId){
        CartItems  cartItems= cartItemsRepo.findCartItemsByCartIdandproductId(productID,cartId);

        if(cartItems==null){
            CartItems  newcartItems=new CartItems();
            newcartItems.setQuantity(0);
            return newcartItems;
        }
        return cartItems;
    }



    public Cart findcartByUsername(){
       String email=authUtils.getCurrentUserEmail();



            Cart cart=cartRepo.findByUser_Email(email);

            if(cart==null){
                Cart newcart=new Cart();
                newcart.setTotalprice(0.0);
                newcart.setUser(authUtils.getUser());
                return cartRepo.save(newcart);
            }
           return cart;

    }

    public ResponseEntity<?> getAllCart() {
        List<Cart> cartList=cartRepo.findAll();
        List<CartDTO>cartDTOList=new ArrayList<>();

           for(int i=0;i<cartList.size();i++){
               CartDTO dto=convertCartToDto(cartList.get(i));
               cartDTOList.add(dto);
        }
        return ResponseEntity.ok(cartDTOList);
    }

    public ResponseEntity<?> findUserCart() {
        Cart cart=findcartByUsername();
        CartDTO cartDTO=convertCartToDto(cart);
        return ResponseEntity.ok(cartDTO);
    }

    public CartDTO convertCartToDto(Cart cart) {

        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartid(cart.getCartid());
        cartDTO.setTotalprice(cart.getTotalprice());

        List<CartItemDTo> itemDtos = new ArrayList<>();

        List<CartItems>cartItems=cart.getItems();

//        carItems ko carItemsDTO

        for(int i=0;i<cartItems.size();i++){
            CartItemDTo newdto=new CartItemDTo();
            newdto.setCartItemsId(cartItems.get(i).getCartItemsId());
            newdto.setProduct_id(cartItems.get(i).getProduct().getProductId());
            newdto.setProductName(cartItems.get(i).getProduct().getProductName());
            newdto.setQuantity(cartItems.get(i).getQuantity());
            newdto.setDiscount(cartItems.get(i).getDiscount());
            newdto.setProductPrice(cartItems.get(i).getProductPrice());

            itemDtos.add(newdto);
        }


        cartDTO.setItems(itemDtos);

        return cartDTO;
    }

    @Transactional
    public ResponseEntity<?> updatequantity(Long productID, int quantity) {
        Cart cart=cartRepo.findByUser_Email(authUtils.getCurrentUserEmail());
        Product product=productRepo.findById(productID).orElseThrow(()->new RuntimeException("No product Found"));


        if(quantity==0){
            throw new ApiExpetion("Product "+quantity+" is zero");
        }

        if(product.getQuantity()<quantity){
            throw new ApiExpetion("Product "+quantity+" too large");
        }


        CartItems cartitems=cartItemsRepo.findCartItemsByCartIdandproductId(productID,cart.getCartid());

        if(cartitems==null){
            throw new ApiExpetion("Product "+product.getProductName()+" Is not present in cart");
        }

//        cartitems.setQuantity(cartitems.getQuantity()+quantity);
        if(cartitems.getQuantity()==1 && quantity==-1){
            return deletefromcart(cart.getCartid(),productID);
        }
        else {
            cartitems.setQuantity(cartitems.getQuantity()+quantity);
            cartItemsRepo.save(cartitems);

            cart.setTotalprice(cart.getTotalprice() + (product.getSpecialPrice() * quantity));

            product.setQuantity(product.getQuantity() - quantity);
            productRepo.save(product);

            return ResponseEntity.ok(convertCartToDto(cartRepo.save(cart)));
        }
    }

    @Transactional
    public ResponseEntity<?> deletefromcart(Long cartId, Long productId) {
        Product product=productRepo.findById(productId).orElseThrow(()->new RuntimeException("No product Found"));
        CartItems cartitems=cartItemsRepo.findCartItemsByCartIdandproductId(productId,cartId);

        if(cartitems==null){
            throw new ApiExpetion("Product "+product.getProductName()+" Is not present in cart");

        }
        Cart cart=cartRepo.findById(cartId).orElseThrow(()->new RuntimeException("Cart Not Found"));

        cart.setTotalprice(Math.max(0,cart.getTotalprice()-(cartitems.getProductPrice()*cartitems.getQuantity())));
        product.setQuantity(product.getQuantity()+cartitems.getQuantity());

        cart.getItems().remove(cartitems);
        cartItemsRepo.delete(cartitems);

        cartRepo.save(cart);
        productRepo.save(product);


        return ResponseEntity.ok(convertCartToDto(cart));

    }

    public void updateInProduct(Product product) {
        List<Cart>cartlist= cartRepo.findCartsByProductId(product.getProductId());


        for(Cart cart: cartlist){
              CartItems cartitems =cartItemsRepo.findCartItemsByCartIdandproductId(cart.getCartid(),product.getProductId());
              cart.setTotalprice(cart.getTotalprice()-(cartitems.getQuantity()*cartitems.getProductPrice()));
              cartitems.setProductPrice(product.getSpecialPrice());
              cart.setTotalprice(cart.getTotalprice()+(cartitems.getQuantity()*cartitems.getProductPrice()));
              cartItemsRepo.save(cartitems);
              cartRepo.save(cart);
        }
    }

    public void removeproduct(Product product) {
        List<Cart>cartlist= cartRepo.findCartsByProductId(product.getProductId());

        for(Cart cart: cartlist){
            CartItems cartitems =cartItemsRepo.findCartItemsByCartIdandproductId(product.getProductId(),cart.getCartid());

            cart.setTotalprice(cart.getTotalprice()-(cartitems.getQuantity()*cartitems.getProductPrice()));
            cartItemsRepo.delete(cartitems);
            cartRepo.save(cart);
        }

    }
}
