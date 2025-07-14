package com.Project.New_Proj.controller;

import com.Project.New_Proj.model.CourseDocument;
import com.Project.New_Proj.service.CourseIndexService;
import com.Project.New_Proj.service.CourseSearchService;
import com.Project.New_Proj.util.JsonLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CourseController {

    @Autowired
    private final CourseSearchService searchService;
    private final CourseIndexService indexService;
    private final JsonLoader jsonLoader;

    @GetMapping("/courses/search")
    public Map<String,Object> searchCourses(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String startDate,
            @RequestParam(defaultValue = "upcoming") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        List<CourseDocument> courses = searchService.searchCourses(
                q,minAge,maxAge,category,type,minPrice,maxPrice,startDate,sort
                ,page,size
        );

        Map<String,Object> response = new HashMap<>();
        response.put("total",courses.size());
        response.put("courses",courses);
        return response;
    }

    @GetMapping("/load")
    public ResponseEntity<List<CourseDocument>> loadCourses() {
        List<CourseDocument> loadedCourses = jsonLoader.loadDataFromJson();
        return ResponseEntity.ok(loadedCourses); // Spring will serialize it to JSON
    }


    @PostMapping("/courses")
    public CourseDocument createOrUpdateCourse(@RequestBody CourseDocument course) {
        return indexService.createOrUpdateCourse(course);
    }

    @DeleteMapping("/courses/{id}")
    public void deleteCourse(@PathVariable String id) {
        indexService.deleteCourseById(id);
    }

    @PutMapping("/courses/{id}")
    public CourseDocument updateCourse(@PathVariable String id, @RequestBody CourseDocument course) {
        course.setId(id);
        return indexService.createOrUpdateCourse(course);
    }


}
