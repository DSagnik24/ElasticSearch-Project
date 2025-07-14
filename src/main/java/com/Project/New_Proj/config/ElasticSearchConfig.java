package com.Project.New_Proj.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.Project.New_Proj.repository")
public class ElasticSearchConfig {

}
