# Doodle Dashboard - manage user's dashboard on doodle.com 
When users go to their dashboard on doodle.com, they will see a list of the doodles (polls) they have either created or participated.

Doodle Dashboard is a simple Restful API with following functionalities:

1. List all polls created by a user
2. Search polls by its title
3. List all polls created after a certain date

## Architectural decisions
Given the limited timeframe the focus was on providing functionality with good test coverage and api documentation which is automatically generated from integration tests.

<p><b>1. Framework</b><br/> Latest version of Spring Boot is used (2.3.4) with Java 14 and embedded Tomcat server. Spring Boot is proven in production, bootstrapping REST API can be done very fast. It also has a big community and it's regularly updated. Maven is used for building the solution and for dependency management.</p>

<p><b>2. Database</b><br/> When analyzing given sample file that contains test data it is obvious that polls don't have a strict structure (for example <b>options</b> node has different structure, some polls have additional nodes like <b>location</b> or <b>multiDay</b> etc.). When we take a look at 3 endpoints that need to be implemented we see that we only have read operations against our database. Furthermore we need to provide the best possible performance in a given timeframe for querying the data.
When we take all these facts into consideration for this use-case non-relational database would be the best choice. NoSQL databases have better performance if number of reads are greater than number of writes. Embedded in-memory MongoDB is chosen to achieve even better performance and easier distribution. All data is stored as documents in MongoDB allowing non-strict structure for each individual document. All data like <b>initiator</b>, <b>options</b> or <b>participants</b> are embedded into documents so we have better performance when querying the data (no joins). To improve performance even more all the fields that are used for querying like <b>initiator.email</b>, <b>title</b> and <b>initiated</b> are indexed. Search in MongoDB is done using regex which is more powerful and with better performance comparing to LIKE in relational databases.
Mongock is used for DB migrations and for populating the DB with initial test data (only in dev and test environments). This enables us to have some test data in DB and use our API instantly upon application startup.</p>

<p><b>3. Testing</b><br/> For unit and integration tests JUnit 5 and Mockito are used. Tests are run using Maven which enables easy integration into pipelines as part of CI/CD process. Initial tests cover basic functionality and some edge cases.</p>

<p><b>4. Containerization</b><br/> Api is containerized using Docker. All project dependencies (Maven, Java) are resolved within Docker and the fact that Tomcat and MongoDB are embedded in application enables us to have easy distribution and app setup especially for dev and test environments.
All this enables easy deployment and scaling using some orchestration tools like Kubernetes.</p>

<p><b>5. Documentation</b><br/> Spring REST Docs in combination with ASCIIDoctor are used to automatically generate API documentation upon execution of integration tests. Having no documentation is better than wrong documentation so Spring REST docs will help generate accurate documentation for RESTful services. The output is in open-api html format with syntax highlighting. This method of generating documentation can help support a test-driven approach to developing and documenting RESTful APIs.
When documenting fields, the test fails if an undocumented field is found in the payload. Similarly, the test also fails if a documented field is not found in the payload and the field has not been marked as optional.</p>

## Possible improvements
<p><b>1. Authentication (if needed)</b><br/> Given the limited timeframe and to keep the solution simple authentication was not implemented. It is expected that user needs to login first to use this API so Spring Security in combination with JWT tokens can be used for adding authentication to API endpoints. Stateless communication would be implemented between service and client. Users can be given roles and privileges so based on that we can have custom access to api endpoints.</p>

<p><b>2. More tests</b><br/> More tests can be added to cover additional scenarios and edge cases.</p>

<p><b>3. Introducing Elasticsearch (if needed)</b><br/> If the amount of data grows and if performance issues come up or some complex searches are needed we could consider using Elasticsearch for indexing and searching the data.</p>

<p><b>4. Separate container for Mongo DB on other environments</b><br/> We should have separate container for MongoDB on environments that are not dev or test (staging, production).</p>

## Installation (using Docker)
You have to have Docker installed and all other dependencies are automatically resolved.
To run the application via docker go to the root folder of the application and execute
```
docker-compose up
```

## Installation (without Docker)
Here is how you can manually install and run the application. First you need to install following dependencies
1. Java JDK 14
2. Apache Maven

Navigate to project root and execute
```
mvn spring-boot:run
```

## Documentation
API Documentation is automatically generated upon package phase and can be found in **/target/generated-docs/index.html**.
Latest copy of API documentation and Postman collection containing all API calls can be found in **/distribution**

## Testing
To execute tests (both unit and integration) against test data, once you have Maven installed, navigate to project root and execute
```
mvn clean install
```
This also triggers automatic API documentation generation process and the result is placed in **/target/generated-docs/index.hml** file

## Test data
Upon application startup initial test data are automatically inserted in both dev and test environments. You can start using the API right after it loads.
Embedded in-memory MongoDB is used to store data and the schema gets dropped when service is stopped. Schema name is **doodle-dashboard**.
Please refer to API documentation for more details.