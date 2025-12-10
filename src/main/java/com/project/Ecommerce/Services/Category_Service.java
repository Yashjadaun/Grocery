package com.project.Ecommerce.Services;

import com.project.Ecommerce.DTO.Category_Request;
import com.project.Ecommerce.DTO.Category_Response;
import com.project.Ecommerce.Expception.AllExeption.ApiExpetion;
import com.project.Ecommerce.Models.Category;
import com.project.Ecommerce.Repo.Category_Repo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class Category_Service {
    @Autowired
    private Category_Repo repo;

    @Autowired
    private ModelMapper modelmapper;

    public ResponseEntity<?> create(Category_Request c){
        Category c1=modelmapper.map(c, Category.class);
         return new ResponseEntity<>(repo.save(c1), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAll(Integer pageNumber,Integer pagesize,String sortBy,String sortOrder) {

        Sort Sorting=sortOrder.equals("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pagesize,Sorting);
        Page<Category> categoryPage= repo.findAll(pageDetails);
        List<Category> list= categoryPage.getContent();

        if(list.isEmpty()){
            throw  new ApiExpetion("Catogory is Empty ");
        }
        List<Category_Request> categoryRequests=new ArrayList<>();

        for (Category c : list) {
            Category_Request dto = modelmapper.map(c, Category_Request.class);
            categoryRequests.add(dto);
        }

        Category_Response  resp= new Category_Response();

            resp.setCategory(categoryRequests);
            resp.setPageNumer(categoryPage.getNumber());
            resp.setPageSize(categoryPage.getSize());
            resp.setTotalElements(categoryPage.getNumberOfElements());
            resp.setTotalPages(categoryPage.getTotalPages());
            resp.setLastPage(categoryPage.isLast());

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    public ResponseEntity<?> update(Category_Request c, long categoryId) {

        Optional<Category> existing=repo.findById(categoryId);

        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Category ID " + categoryId + " not found");
        }

        Category cat = existing.get();
        cat.setCategoryName(c.getCategoryName());

        return ResponseEntity.ok( repo.save(cat)); // 200 OK
    }

    public ResponseEntity<?> delete(long categoryId) {
        Optional<Category> existing=repo.findById(categoryId);

        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Category ID " + categoryId + " not found");
        }
        repo.delete(existing.get());
        return ResponseEntity.ok("Category deleted successfully");
    }
}
