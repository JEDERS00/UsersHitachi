Hi, this project is working with **Spring Boot v2.7.3** and **Java 8**

The purpose of this project is user management.
I developed the application with NetBeans 13 working in Windows 11 Pro.

***STEP 1:***  **Create table users in database H2**

```
CREATE TABLE users (
   id BIGINT NOT NULL AUTO_INCREMENT,
   first_name VARCHAR(50) NOT NULL,
   last_name VARCHAR(50) NOT NULL,
   PRIMARY KEY (id),
   UNIQUE (fist_name, last_name)
);
```

***STEP 2:***  **Release port 8080**

The application is working in port 8080
http://localhost:8080/


***STEP 3:***  **Create the Users-1.0.0-jar-with-dependencies.jar**

Doing a Cleand and Build from NetBeans you can get the .jar in ...\Users\target


***STEP 4:***  **Run the Users-1.0.0-jar-with-dependencies.jar with next command**
```
java -jar Users-1.0.0-jar-with-dependencies.jar
```
Or in the project path directory run 
```
mvnw spring-boot:run
```

***Note:***

The project is going to show you a menu to do the actions.

The URLs are:

http://localhost:8080/addUser
This URL works with method POST and process a JSON like this:
{
	"firstName" : "",
	"lastName" : "",
}

http://localhost:8080/getUsers
This URL works with method GET and return all users in alphabetical order descending by last name as a json


http://localhost:8080/getUsers/{userId}
This URL works with method GET and return a users by the ID as a json


http://localhost:8080/deleteUser
This URL works with method POST and delete a users by the ID, the key is USER_ID
