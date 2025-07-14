package com.Project.New_Proj.service;

import com.Project.New_Proj.model.CourseDocument;
import com.Project.New_Proj.repository.CourseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class CourseIndexService {

    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void loadCoursesAtStartup(){
        try{
            log.info("Loading courses.json...");
            InputStream inputStream = new ClassPathResource("courses.json").getInputStream();
            List<CourseDocument> courses = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<CourseDocument>>(){}
            );

            courseRepository.saveAll(courses);
            log.info("✅ Indexed {} courses into Elasticsearch", courses.size());

        } catch (Exception e) {
            log.error("❌ Failed to load or index sample courses",e);
        }
    }

    public CourseDocument createOrUpdateCourse(CourseDocument course) {
        try {
            CourseDocument savedCourse = courseRepository.save(course);
            log.info("✅ Successfully indexed course with ID: {}", savedCourse.getId());
            return savedCourse;
        } catch (Exception e) {
            log.error("❌ Failed to index course with ID: {}", course.getId(), e);
            throw new RuntimeException("Failed to index course", e);
        }
    }

    public void deleteCourseById(String id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            log.info("🗑️ Deleted course with ID: {}", id);
        } else {
            throw new RuntimeException("Course not found");
        }
    }

}
