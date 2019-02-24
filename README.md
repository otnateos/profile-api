# User Profile API
This project is a Spring Boot API services to manage profile and address
of a user.

To run the project using Spring Boot Tomcat plugin
```
mvn spring-boot:run
```
Or package the Spring Boot application and run it as Uber jar
```
mvn package
java -jar target/profile-1.0.0.jar
```

# API
API documentation are generated using Swagger Core and [Spring Fox](http://springfox.github.io/springfox/); available from [API Doc](http://localhost:8080/swagger-ui.html)

# Consume API
To consume the API, we can use Postman/Curl or other HTTP client. e.g.

Get profile
```
curl -X GET http://localhost:8080/profile/1
```
Create profile
```
curl -X POST http://localhost:8080/profile \
  -H 'Content-Type: application/json' \
  -d '{"lastName":"Smith","firstName":"John","userId":"1"}'
```
Update profile
```
curl -X PUT http://localhost:8080/profile/1  \
  -H 'Content-Type: application/json'   \
  -d '{"lastName":"Smith","firstName":"John","userId":"1","dateOfBirth":"29-02-2000"}'
```
Create address
```
curl -X POST http://localhost:8080/profile/1/address \
  -H 'Content-Type: application/json'   -d '{"line1":"George St"}'

```
Delete profile
```
curl -X DELETE http://localhost:8080/profile/1

```


# Database
Database used for this project is u
sing MySQL database. In development it should be easily substituted using MySQL/MariaDB
container.

```
docker run --rm --name mariadb -p 3306:3306 -e MYSQL_ROOT_PASSWORD=secret mariadb
```

Then create the schema using

```
docker exec -it mariadb mysql -u root -p -e "create database profile;"
```