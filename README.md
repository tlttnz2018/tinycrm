This project is implemented following the request from Propellerhead.

Below is some explanations and information on how to do the common task

## Table of Contents
- [Folder structure](#folder-structure)
    - [Front end stuffs](#front-end-stuffs)
    - [Back end stuffs](#back-end-stuffs)
- [Backend architecture](#backend-architecture)
- [Front end architecture](#front-end-architecture)
- [How to build and run the application](#how-to-build-and-run)
- [How to develop for front end](#how-to-develop-for-front-end)
- [How to develop for back end](#how-to-develop-for-back-end)
- [Known issues](#known-issues)

##Folder structure
```
tinycrm/
  README.md
  frontend/
    node/
    public/
        index.html
    src/
        components/
        index.css
        index.js
    pom.xml
    package.json
    yarn.log
  backend/
    .mvn/
    src/
        main/
        test/
    pom.xml
  pom.xml
  README.md
```
### Front end stuffs
* `tinycrm/frontend` contains all the things related to **front end**. It's following **Create React App** convention.
* `tinycrm/frontend/node` contains yarn binary for running yarn command in case you don't install yarn.
* `tinycrm/frontend/src` contains all the front end source.
* `tinycrm/frontend/public/index.html` is main index.html file template for mounting React.

Basically, front end developer just need to stay in `tinycrm/frontend` to do all front end stuffs.

### Back end stuffs
* `tinycrm/backend` contains all the things related to **back end**. It's following maven directory convention.
* `tinycrm/backend/main` contains main source code of the server
* `tinycrm/backend/test` contains test code for the server.

## Backend architecture
### Layer oriented organized
This server code is organized following the layers. Each layer is organized in java package below
* Package `domain` contains all VO domain objects including **Customer** and **Note**
* Package `repositories` contains all repository interfaces. Spring Boot will based on those interfaces to create appropriate repository objects for the application.
* Package `controller` contains REST api controllers for all **api** of customer allowing search/add/change customer and manipulating theirs' notes.
* Package `business` contains object for doing the normal server business and do not belong to domain objects. It's including search criteria classes and a custom exception class.

### Customer and note relationship
Current implementation don't use JPA `OneToMany` and `ManyToOne` relationship for both `Customer` and `Note`. Using two ways relationship like that will take all the notes along with each customer when querying for list of customers. Putting `FETCH_LAZY` doesn't take any effect due to Jackson JSON serialization. Therefore, author wants to put `ManyToOne` to only `Note` to have more flexibility control on how to query the notes attached to client. And it also allows to lazy load list of notes as well as finer control on how to query it, for example: allow sorting, paging.

### Why author don't use Spring Data Rest
It's simpler to enable Spring Data Rest to expose all REST apis for CRUD `Customer` and `Note`. But it has the following issue

Author want
* Query customer only when showing list of customers. No other information
* Query notes of one customer only when showing user details. It must also allow paging for huge list of notes.

Spring Data Rest implementation is not satisfied because
* Jackson serialization forces eager loading, therefore, Spring Data Rest will get all notes for each customer when querying list of customers. It will cause a huge load if customers have too many notes.
* It doesn't allow finer control for list of notes for each of customer, for example sorting, paging.

Therefore, author developed custom REST controllers for `Customer` and `Note`.

## Front end architecture
Author wants to implement this app as SPA to separate front end and backend development flow. It will reduce the code of the backend part to very primitive and easy to understand and allow more flexible on front end side to implement the logic. 

Author chooses React as front end technology to implement because the component approach enables author develop it faster.

Because the allowed time is limited so author don't use any other technologies like Redux, GraphQL to introduce better separation in front end implementation and frontend-backend communication.

## How to build and run
From the parent directory, issue the following command to build front end and backend into one uber jar file
#### `mvn clean install`

To run the whole front end and back end, use the following command
#### `java -jar backend/target/backend-0.0.1-SNAPSHOT.jar`

To initialize dummy data for demoing, add the following properties
#### `java -Dspring.profiles.active=dev -jar backend/target/backend-0.0.1-SNAPSHOT.jar`

## How to develop for front end
In order to develop front end, you can run the following commands

* To build
#### `yarn build`

* To start the front end
####`yarn start`

* To test front end code
####`yarn test`

You can also add or remove packages in developing front end using yarn
#### `yarn add moment`
or 
####`yarn remove jquery`

You may need to start the backend to test your front end by using the following command in **backend** directory
#### `mvn -Dspring.profiles.active=dev spring-boot:run`

### Support CI/CD
Please note that this will install node and yarn on the build server independently using versions specified in **pom.xml** file.

## How to develop for back end
In order to develop back end, you can run the following commands

* To build Spring Boot
#### `mvn clean install`

* To start the server with development mode to allow CORS from localhost:3000 and load some dummy data
#### `mvn -Dspring.profiles.active=dev spring-boot:run`

* To test backend code
#### `mvn clean test` 

## Known issues
### JDK requirement
This project uses Lombok to reduce code boilerplate. However, Lombok has a severe issue with JDK 10. Therefore, you should use JDK 8 to build this project.

