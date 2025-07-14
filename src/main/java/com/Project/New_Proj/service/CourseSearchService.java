package com.Project.New_Proj.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.Project.New_Proj.model.CourseDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public List<CourseDocument> searchCourses(
            String keyword,
            Integer minAge,
            Integer maxAge,
            String category,
            String type,
            Double minPrice,
            Double maxPrice,
            String startDate,
            String sort,
            int page,
            int size
    ) {
        try {
            // Build query as JSON string
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("{\"bool\":{");

            // Must clause for text search
            queryBuilder.append("\"must\":[");
            if (keyword != null && !keyword.isEmpty()) {
                queryBuilder.append("{\"multi_match\":{\"query\":\"").append(keyword)
                        .append("\",\"fields\":[\"title\",\"description\"]}}");
            } else {
                queryBuilder.append("{\"match_all\":{}}");
            }
            queryBuilder.append("]");

            // Filter clauses
            List<String> filters = new ArrayList<>();

            // Age range filter
            if (minAge != null || maxAge != null) {
                StringBuilder ageFilter = new StringBuilder();
                ageFilter.append("{\"range\":{\"minAge\":{");
                if (minAge != null) ageFilter.append("\"gte\":").append(minAge);
                if (minAge != null && maxAge != null) ageFilter.append(",");
                if (maxAge != null) ageFilter.append("\"lte\":").append(maxAge);
                ageFilter.append("}}}");
                filters.add(ageFilter.toString());
            }

            // Price range filter
            if (minPrice != null || maxPrice != null) {
                StringBuilder priceFilter = new StringBuilder();
                priceFilter.append("{\"range\":{\"price\":{");
                if (minPrice != null) priceFilter.append("\"gte\":").append(minPrice);
                if (minPrice != null && maxPrice != null) priceFilter.append(",");
                if (maxPrice != null) priceFilter.append("\"lte\":").append(maxPrice);
                priceFilter.append("}}}");
                filters.add(priceFilter.toString());
            }

            // Category filter
            if (category != null) {
                filters.add("{\"term\":{\"category\":\"" + category + "\"}}");
            }

            // Type filter
            if (type != null) {
                filters.add("{\"term\":{\"type\":\"" + type + "\"}}");
            }

            // Start date filter
            if (startDate != null) {
                filters.add("{\"range\":{\"nextSessionDate\":{\"gte\":\"" + startDate + "\"}}}");
            }

            // Add filters if any
            if (!filters.isEmpty()) {
                queryBuilder.append(",\"filter\":[");
                queryBuilder.append(String.join(",", filters));
                queryBuilder.append("]");
            }

            queryBuilder.append("}}");

            // Create query from JSON string
            Query query = Query.of(q -> q.withJson(new StringReader(queryBuilder.toString())));

            // Sort options
            SortOptions sortOption = switch (sort) {
                case "priceAsc" -> SortOptions.of(s -> s.field(f -> f.field("price").order(SortOrder.Asc)));
                case "priceDesc" -> SortOptions.of(s -> s.field(f -> f.field("price").order(SortOrder.Desc)));
                default -> SortOptions.of(s -> s.field(f -> f.field("nextSessionDate").order(SortOrder.Asc)));
            };

            // Build search request
            SearchRequest request = SearchRequest.of(s -> s
                    .index("courses")
                    .query(query)
                    .sort(sortOption)
                    .from(page * size)
                    .size(size)
            );

            // Execute search
            SearchResponse<CourseDocument> response = elasticsearchClient
                    .search(request, CourseDocument.class);

            // Extract results
            List<CourseDocument> results = new ArrayList<>();
            for (Hit<CourseDocument> hit : response.hits().hits()) {
                results.add(hit.source());
            }

            return results;

        } catch (Exception e) {
            log.error("Search failed", e);
            return List.of();
        }
    }
}