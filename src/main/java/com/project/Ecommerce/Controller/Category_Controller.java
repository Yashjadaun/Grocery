package com.project.Ecommerce.Controller;

import com.project.Ecommerce.DTO.Category_Request;
import com.project.Ecommerce.DTO.Category_Response;
import com.project.Ecommerce.Models.Category;
import com.project.Ecommerce.Services.Category_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class Category_Controller {

    @Autowired
   private Category_Service service;

    @GetMapping("/public/categories")
    public ResponseEntity<?> get(
            @RequestParam(name ="pageNumber",defaultValue = "0", required = false)Integer pageNumber,
            @RequestParam(name="pageSize",defaultValue = "10",required = false)Integer  pagesize,
            @RequestParam(name="sortby" ,defaultValue = "categoryId",required = false)String  sortby,
            @RequestParam(name="sortorder",defaultValue = "asc",required = false)String sortorder
    ){
        return service.getAll(pageNumber,pagesize,sortby,sortorder);
    }


    @PostMapping("/admin/category")
    public ResponseEntity<?> create(@RequestBody Category_Request c){
        return service.create(c);
    }



    @PutMapping("admin/categories/{categoryId}")
    public ResponseEntity<?> update(@RequestBody Category_Request c,@PathVariable long categoryId){
        return service.update(c,categoryId);
    }

     @DeleteMapping("admin/categories/{categoryId}")
    public ResponseEntity<?> delete(@PathVariable long categoryId ){
        return service.delete(categoryId);
     }
}
