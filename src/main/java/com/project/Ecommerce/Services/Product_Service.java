package com.project.Ecommerce.Services;

import com.project.Ecommerce.DTO.ProductDTO;
import com.project.Ecommerce.DTO.ProductResponse;
import com.project.Ecommerce.Expception.AllExeption.ApiExpetion;
import com.project.Ecommerce.Models.Category;
import com.project.Ecommerce.Models.Product;
import com.project.Ecommerce.Repo.Category_Repo;
import com.project.Ecommerce.Repo.Product_Repo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class Product_Service {

    @Autowired
    Product_Repo product_repo;

    @Autowired
    Category_Repo category_repo;

    @Autowired
    ModelMapper moddelmapper;

    @Autowired
    FileService File;

    @Value("${project.image}")
    private String uploadDir;

    @Autowired
    private CartService carservice;

    public ResponseEntity<?> addproduct(ProductDTO productDTO, long id) {

        Optional<Category> cat=category_repo.findById(id);


        if (cat.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Category with id " + id + " not found");
        }

        Category c = cat.get();


        boolean isProductpresent=false;

        for(Product p:c.getProducts()){
            if(p.getProductName().equals(productDTO.getProductName())){
                isProductpresent=true;
                break;
            }
        }

        if( isProductpresent) throw new ApiExpetion("Product already present");
        Product p=moddelmapper.map(productDTO, Product.class);
        p.setImage("default.png");
        p.setCategory(c);

        double sprice=p.getPrice()-((p.getDiscount()*0.01)* p.getPrice());
        p.setSpecialPrice(sprice);

        return new ResponseEntity<>(moddelmapper.map(product_repo.save(p), ProductDTO.class),HttpStatus.CREATED);
    }



    public ResponseEntity<ProductResponse> getAllproduct(Integer pageNumber, Integer pagesize, String sortby, String sortorder) {

        Sort Sorting=sortorder.equals("asc")?Sort.by(sortby).ascending():Sort.by(sortby).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pagesize,Sorting);
        Page<Product> ProductPage= product_repo.findAll(pageDetails);

          List<Product> list1=ProductPage.getContent();
          List<ProductDTO>finallist=list1.stream()
                                    .map(p->moddelmapper.map(p, ProductDTO.class)).toList();
        ProductResponse p=new ProductResponse();
        p.setContent(finallist);
        p.setPageNumer(ProductPage.getNumber());
        p.setPageSize(ProductPage.getSize());
        p.setTotalElements(ProductPage.getNumberOfElements());
        p.setTotalPages(ProductPage.getTotalPages());
        p.setLastPage(ProductPage.isLast());
          return new ResponseEntity<>(p,HttpStatus.OK);
    }

    public ResponseEntity<ProductResponse> getproductbyCategory(long categoryId,Integer pageNumber, Integer pagesize, String sortby, String sortorder) {
        Category c=category_repo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));

        Sort Sorting=sortorder.equals("asc")?Sort.by(sortby).ascending():Sort.by(sortby).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pagesize,Sorting);
        Page<Product> ProductPage= (Page<Product>) product_repo.findByCategoryOrderByPriceAsc(c);

        List<Product> list1=ProductPage.getContent();
        if(list1.isEmpty()){
            throw new RuntimeException("Product  not found with Category ID: " + categoryId);
        }

        List<ProductDTO>finallist=list1.stream()
                .map(p->moddelmapper.map(p, ProductDTO.class)).toList();


        ProductResponse p=new ProductResponse();
        p.setContent(finallist);
        p.setPageNumer(ProductPage.getNumber());
        p.setPageSize(ProductPage.getSize());
        p.setTotalElements(ProductPage.getNumberOfElements());
        p.setTotalPages(ProductPage.getTotalPages());
        p.setLastPage(ProductPage.isLast());
        return new ResponseEntity<>(p,HttpStatus.OK);

    }


    public ResponseEntity<ProductResponse> getproductbykeyword( String keyword,Integer pageNumber, Integer pagesize, String sortby, String sortorder){


        Sort Sorting=sortorder.equals("asc")?Sort.by(sortby).ascending():Sort.by(sortby).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pagesize,Sorting);
        Page<Product> ProductPage= (Page<Product>) product_repo.findByProductNameLikeIgnoreCase("%"+keyword+"%");;
        List<Product> list1=ProductPage.getContent();
        List<ProductDTO>finallist=list1.stream()
                .map(p->moddelmapper.map(p, ProductDTO.class)).toList();


        ProductResponse p=new ProductResponse();
        p.setContent(finallist);
        p.setPageNumer(ProductPage.getNumber());
        p.setPageSize(ProductPage.getSize());
        p.setTotalElements(ProductPage.getNumberOfElements());
        p.setTotalPages(ProductPage.getTotalPages());
        p.setLastPage(ProductPage.isLast());
        return new ResponseEntity<>(p,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ProductDTO> updateProduct(ProductDTO productdto, Long productId) {
        Product p=product_repo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        Product product=moddelmapper.map(productdto, Product.class);
        p.setProductName(product.getProductName());
        p.setDescription(product.getDescription());
        p.setQuantity(product.getQuantity());
        p.setDiscount(product.getDiscount());
        p.setPrice(product.getPrice());
        p.setSpecialPrice(product.getSpecialPrice());

        Product Saveproduct=product_repo.save(p);

//        update in cart
        carservice.updateInProduct(product);

        return new ResponseEntity<>(moddelmapper.map(Saveproduct, ProductDTO.class),HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ProductDTO> delete(Long productId) {
        Product p=product_repo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        carservice.removeproduct(p);
        product_repo.delete(p);
         return new ResponseEntity<>(moddelmapper.map(p, ProductDTO.class),HttpStatus.OK);
    }

    public ResponseEntity<?> updateProductImage(Long productId, MultipartFile file) throws IOException {
        Product product = product_repo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Image cannot be empty");
        }
        // ✔ Folder where you want to save images
        // String uploadDir ="uploads/";
        //upload image
        String Filename=File.uploadimage(uploadDir,file);

        product.setImage(Filename);
        product_repo.save(product);
        return ResponseEntity.ok("Image updated successfully: " + Filename);
    }



}
