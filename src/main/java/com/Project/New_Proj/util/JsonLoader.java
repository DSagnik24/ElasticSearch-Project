package com.Project.New_Proj.util;

import com.Project.New_Proj.model.CourseDocument;
import com.Project.New_Proj.repository.CourseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonLoader {

    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public List<CourseDocument> loadDataFromJson() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("courses.json");
            if (inputStream == null) {
                log.error("courses.json file not found in resources!");
                return List.of(); // return empty list
            }

            List<CourseDocument> courses = objectMapper.readValue(inputStream, new TypeReference<>() {});
            courseRepository.saveAll(courses);

            log.info("Loaded {} course documents into Elasticsearch", courses.size());
            return courses;
        } catch (Exception e) {
            log.error("Error loading courses.json", e);
            return List.of();
        }
    }

}
