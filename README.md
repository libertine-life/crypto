Crypto Investment Recommendation Service

Build the application using the command mvn clean install

To run the application you will need Docker and Docker Compose.
You just need to run the docker-compose up --build command.

Endpoints documentation is located at http://localhost:8080/swagger-ui/index.html#/

When you run docker-compose, the database structure described in the init.sql file will be created automatically.

In order to fill the database with information about crypto, you can use the endpoint
/api/v1/file/upload-csv. Test data is located in the directory src/main/resources/prices.

The application works only with those currencies that are specified in the currency dictionary 
(table crypto_symbol in the database). At the moment, there is no user interface for updating the dictionary, 
but the existence of this directory assumes the further use of new currencies.
The values in the directory are preset during database creation.

The application has a limit on the number of requests from one IP address. After the specified time period, 
the limit is removed. The settings of these parameters and their description can be found in the application.properties file

