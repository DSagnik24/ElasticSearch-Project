# ElasticSearch-Project


# ElasticSearch-Project: Complete Reviewer-Ready Documentation

This documentation provides all necessary information for a reviewer to understand, set up, and run the ElasticSearch-Project repository efficiently.

## Project Overview

ElasticSearch-Project is a Java-based application designed to demonstrate integration with Elasticsearch using modern development practices. The repository includes Docker support for seamless environment setup and Maven for dependency management.

## Prerequisites

Before starting, ensure the following software is installed on your system:

- **Java Development Kit (JDK) 11 or higher**
- **Maven 3.6+** (or use the included Maven Wrapper)
- **Docker** and **Docker Compose** (for containerized setup)
- **Git** (to clone the repository)

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/DSagnik24/ElasticSearch-Project.git
cd ElasticSearch-Project
```

### 2. Environment Configuration

- No environment variables are required by default.
- If customization is needed, edit the `docker-compose.yml` or application properties as necessary.

## Docker Usage

### Start All Services with Docker Compose

This project includes a `docker-compose.yml` file for easy setup of Elasticsearch and the application.

```bash
docker-compose up --build
```

- This command builds and starts all defined services.
- By default, Elasticsearch will be available on `localhost:9200`.

### Stopping Services

```bash
docker-compose down
```

## Build and Run Commands

### Using Maven (Local Build)

If you prefer to run the application locally (outside Docker):

1. **Build the Project**

   ```bash
   ./mvnw clean package
   ```

   Or, if Maven is installed globally:

   ```bash
   mvn clean package
   ```

2. **Run the Application**

   ```bash
   java -jar target/.jar
   ```

   Replace `.jar` with the actual JAR file name generated in the `target` directory.

## Example/Test Guidance

- After starting the services, verify Elasticsearch is running by visiting:  
  [http://localhost:9200](http://localhost:9200)
- The application should log a successful startup message.
- If the project exposes APIs, test them using `curl` or Postman (refer to the source code or add API details here).
- Sample data of nearly 75 entries present "ElasticSearch-Project/src/main/resources
/courses.json". 

## Troubleshooting

- **Port Conflicts:** Ensure ports 9200 (Elasticsearch) and any application ports are free.
- **Docker Issues:** Run `docker system prune` to clean up unused resources if containers fail to start.
- **Build Failures:** Check your Java and Maven versions; ensure dependencies are downloaded.
- **Elasticsearch Not Starting:** Verify Docker memory allocation is at least 2GB.
