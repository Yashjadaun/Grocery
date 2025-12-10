package com.project.Ecommerce.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category_Response {
    private List<Category_Request> category;
    private  Integer pageNumer;
    private  Integer pageSize;
    private Integer totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
