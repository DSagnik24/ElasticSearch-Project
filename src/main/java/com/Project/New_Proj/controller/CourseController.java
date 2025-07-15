package com.Project.New_Proj.controller;

import com.Project.New_Proj.model.CourseDocument;
import com.Project.New_Proj.service.CourseIndexService;
import com.Project.New_Proj.service.CourseSearchService;
import com.Project.New_Proj.util.JsonLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CourseController {

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
        return ResponseEntity.ok(loadedCourses);
    }

    @PostMapping("/courses")
    public ResponseEntity<CourseDocument> createOrUpdateCourse(@RequestBody CourseDocument course) {
        return ResponseEntity.ok(indexService.createOrUpdateCourse(course));
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<CourseDocument> updateCourse(@PathVariable String id, @RequestBody CourseDocument course) {
        course.setId(id);
        return ResponseEntity.ok(indexService.createOrUpdateCourse(course));
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        indexService.deleteCourseById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseDocument> getCourseById(@PathVariable String id) {
        CourseDocument course = searchService.getCourseById(id);
        return course != null ? ResponseEntity.ok(course) : ResponseEntity.notFound().build();
    }




}
