package com.dhanashri.Question.service.Service;

import com.dhanashri.Question.service.Dao.CategoryDao;
import com.dhanashri.Question.service.Dao.QuestionDao;
import com.dhanashri.Question.service.Module.Category;
import com.dhanashri.Question.service.Module.CategoryStatsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryDao categoryDao;

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<List<Category>> getAllCategories() {
        try{
            List<Category> categories = categoryDao.findAll();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<CategoryStatsResponse>> getCategoryStats() {
        try{
            List<CategoryStatsResponse> categoryStatsResponses = categoryDao.getCategoryStats();

            return new ResponseEntity<>(categoryStatsResponses,HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<String> addCategory(Category category) {
        try{
            System.out.println(category.getCategory());
            categoryDao.save(category);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Error Occurred while saving the Category",HttpStatus.BAD_REQUEST);
        }
    }
}
