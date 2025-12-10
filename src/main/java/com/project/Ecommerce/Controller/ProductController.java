package com.project.Ecommerce.Controller;

import com.project.Ecommerce.DTO.ProductDTO;
import com.project.Ecommerce.DTO.ProductResponse;
import com.project.Ecommerce.Models.Product;
import com.project.Ecommerce.Services.Product_Service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    Product_Service service;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<?> addproduct(@Valid @RequestBody ProductDTO p, @PathVariable long categoryId){
       return  service.addproduct(p,categoryId);
    }


    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse>getAllproduct(   @RequestParam(name ="pageNumber",defaultValue = "0", required = false)Integer pageNumber,
                                                           @RequestParam(name="pageSize",defaultValue = "10",required = false)Integer  pagesize,
                                                           @RequestParam(name="sortby" ,defaultValue = "productId",required = false)String  sortby,
                                                           @RequestParam(name="sortorder",defaultValue = "asc",required = false)String sortorder
    ){
        return service.getAllproduct(pageNumber,pagesize,sortby,sortorder);
    }


    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse>getproductbycategory(@PathVariable long categoryId, @RequestParam(name ="pageNumber",defaultValue = "0", required = false)Integer pageNumber,
                                                               @RequestParam(name="pageSize",defaultValue = "10",required = false)Integer  pagesize,
                                                               @RequestParam(name="sortby" ,defaultValue = "productId",required = false)String  sortby,
                                                               @RequestParam(name="sortorder",defaultValue = "asc",required = false)String sortorder
    ){
        return service.getproductbyCategory(categoryId,pageNumber,pagesize,sortby,sortorder);
    }


    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse>getproductbykeyword(@PathVariable String keyword, @RequestParam(name ="pageNumber",defaultValue = "0", required = false)Integer pageNumber,
                                                              @RequestParam(name="pageSize",defaultValue = "10",required = false)Integer  pagesize,
                                                              @RequestParam(name="sortby" ,defaultValue = "productId",required = false)String  sortby,
                                                              @RequestParam(name="sortorder",defaultValue = "asc",required = false)String sortorder
    ){
        return service.getproductbykeyword(keyword,pageNumber,pagesize,sortby,sortorder);
    }


    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO product,@PathVariable Long productId){
        return service.updateProduct(product,productId);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO>deleteProduct(@PathVariable Long productId){
        return service.delete(productId);
    }


    @PutMapping("/products/{productId}/image")
    public ResponseEntity<?> updateProductImage( @PathVariable Long productId,
                                                          @RequestParam("image") MultipartFile file) throws IOException {
        return service.updateProductImage(productId,file);
    }


}
