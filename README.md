# spring-boot-reactive-webflux

# Overview
This Spring Boot application showcases the capabilities of Spring Reactive Webflux. By leveraging non-blocking asynchronous processing, we efficiently parse CSV files and create dummy shipments. Traditionally, handling around 2000 records with individual POST requests could take several minutes. However, our solution achieves this task in about 3 seconds (see screenshot below).

Note: The dummy endpoint used here for shipment creation is - https://jsonplaceholder.typicode.com/posts

# Features
Non-blocking Asynchronous Processing: Spring Webflux ensures efficient handling of requests without blocking threads.

CSV Parsing: We parse CSV files to extract relevant shipment data.

Fast Execution: The application demonstrates impressive performance, even with a large dataset.


# Screenshot
<img width="802" alt="image" src="https://github.com/tanvir01/spring-boot-reactive-webflux/assets/15964741/c4a350d1-e16f-4981-b681-875f555f32c6">

# Getting Started
Clone this repository.

Build and run the Spring Boot application.

Access the API endpoints to test CSV parsing and shipment creation.

# Usage
Upload a CSV file containing shipment data.

Observe the rapid processing time and successful creation of dummy shipments.

# Dependencies
Spring Boot

Spring Webflux

Lombok (for cleaner code)
