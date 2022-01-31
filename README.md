# Backend Code Challenge

A coding challenge built with spring boot to make the code challenge self-contained.

We can't be as cool as the Node.js kids with their CodeSandbox

## Running Application
___
To run the application:

    ./mvnw spring-boot:run

Application runs at:

    http://localhost:8080

H2 In-Memory Database Console is at:

    http://localhost:8080/h2-console

## Challenge 1
___
Create an Address table and associate it with the two users

Using the migration script `src/main/resources/db/migration/V1__init_db.sql` perform the following:

* Create an Address table with the following fields
  * id primary key not null auto increment
  * address1 - varchar 255
  * address2 - varchar 255
  * city - size 255
  * state - size 100
  * postal - size 10
* Populate the new Address table with 2 records 
* Each user can have one address associated with it. Update the init script to associate the two new addresses with the two users. 


## Challenge 2
___
Return the Address as part of the User from the `GET /v1/users/{userId}` RESTful endpoint.


## Challenge 3
___
Using the `src/main/resources/schema/schema.graphqls` write out a graphql schema that would query a User and its associated Address using the user id. 


## Challenge 4
___
Add a RESTful compliant endpoint for updating a User. This should update the user in the database.  

