package com.auctiononline.warbidrestful.services.implementation;

import com.auctiononline.warbidrestful.exception.AppException;
import com.auctiononline.warbidrestful.models.Category;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;
import com.auctiononline.warbidrestful.repository.CategoryRepository;
import com.auctiononline.warbidrestful.services.ilterface.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public GetAllResponse getAll(){
        try{
            List<Category> categories = categoryRepository.findAll();
            return new GetAllResponse(200, "OK","Get all category", categories);
        }catch (AppException ex){
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(),null);
        }
    }
    public GetAllResponse getById(Integer id){
        try{
            List<Category> categories = categoryRepository.findCategoryById(id);
            if(categories.isEmpty()){
                return new GetAllResponse(404, HttpStatus.NOT_FOUND.toString(),"Id not found!", null);
            }
            return new GetAllResponse(200, "OK", "Get category by id", categories);
        }catch (AppException ex){
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(),null);
        }
    }

    public GetAllResponse getByName(String name){
        try{
            List<Category> categories = categoryRepository.findCategoryByName(name);
            if(categories.isEmpty()){
                return new GetAllResponse(404, HttpStatus.NOT_FOUND.toString(),"Name not found", null);
            }
            return new GetAllResponse(200,"OK","Get Category by name", categories);
        }catch (AppException ex){
            return new GetAllResponse(500, ex.getHttpStatus().toString(), ex.getMessage(),null);
        }
    }

    public MessageResponse add(Category category){
        try{
            categoryRepository.save(category);
            return new MessageResponse(200, HttpStatus.OK, "Add category successful");
        }catch (AppException ex){
            return new MessageResponse(500, ex.getHttpStatus(), ex.getMessage(),null);
        }
    }

    public MessageResponse update(Category category){
        try{
            categoryRepository.save(category);
            return new MessageResponse(200, HttpStatus.OK, "Update category successful");
        }catch (AppException ex){
            return new MessageResponse(500, ex.getHttpStatus(), ex.getMessage(),null);
        }
    }
}
