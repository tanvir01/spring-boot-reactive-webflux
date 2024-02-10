# spring-boot-reactive-webflux

This is a Spring Boot Application to demonstrate Spring Reactive Webflux capablities. In this application, we are parsing CSV file to create dummy shipments. Traditionally, with iteractive approach, to parse a file and send POST requests one by one for around 2000 records, it might take about couple of minutes or more (approximate estimation - variable)! However, thanks to spring webflux, it parses and sends respective POST requests in a non-blocking async manner. This results in the job being done just under 3 seconds (see screenshot - also, approximate estimation - variable).

Note: The dummy endpoint used here from shipment creation is - https://jsonplaceholder.typicode.com/posts

<img width="802" alt="image" src="https://github.com/tanvir01/spring-boot-reactive-webflux/assets/15964741/c4a350d1-e16f-4981-b681-875f555f32c6">
