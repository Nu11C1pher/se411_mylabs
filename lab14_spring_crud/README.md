# Lab 14: Spring CRUD

Java 17, Spring Boot 4.1.2, Spring Data JDBC and H2.

Run `mvnw.cmd spring-boot:run`, then use `http://localhost:8080/api/v1/courses`.
Run the integration tests with `mvnw.cmd clean verify`.

For Postman, import `Lab14.postman_collection.json` or open the v3 collection at
`../postman/collections/Lab 14 Course CRUD API` in Local View. Run all requests
in their saved order (one iteration). The collection creates and removes course 9001.

For PUTMAN in Eclipse, install from https://civerooni.github.io/putman-site/
and open Window > Show View > Other > Putman. Choose GET, enter the URL above,
and click Send. Choose JSON to format the response. POST and PUT requests need
Content-Type: application/json and a course body. See Lab 14 Answers.txt.

Verified: 12 integration tests and 21 Postman assertions passed. PUTMAN GET returned 200.
