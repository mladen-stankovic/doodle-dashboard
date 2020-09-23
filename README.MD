# Doodle Dashboard - manage user's dashboard on doodle.com 
When users go to their dashboard on doodle.com, they will see a list of the doodles (polls) they have either created or participated.

Doodle Dashboard is a simple Restful API with with following functionalities:

1. List all polls created by a user
2. Search polls by its title
3. List all polls created after a certain date

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
Latest copy of API documentation and Postman collection containing all API calls can be found in folder **/dist**

## Testing
To execute tests (both unit and integration) against test data, navigate to project root and execute
```
mvn clean install
```
This also triggers automatic API documentation gereration process and the result is placed in **/target/generated-docs/index.hml** file

## Test data
Upon application startup initial test data are automatically inserted in both dev and test environments. You can start using the API right after it loads.
Embedded in-memory MongoDB is used to store data and the schema gets dropped when service is stopped. Schema name is **doodle-dashboard**.
Please refer to API documentation for more details
