# Bicycle-project

## Backend part of bicycle project

- This is a project in the course Application Development at NTNU Ålesund. 
- Our goal is to make a functional backend with
all the things needed. This includes general things like handling customers, products and orders. 
- This project taught us how to make a backend for a website. 
- There is still room for improvement, but we now have a backend with different functionalities and a good security.

---
## Table of Contents

Here is the content overview: 

- [Preparation](#preparation)
- [Installation and running the project](#installation-and-running-the-project)
- [Credits](#credits)
- [License](#license)
---

## Preparation
1. Install PostgreSQL (optional)
2. Edit the `application.properties` file if you are planning on using another database than PostgreSQL (would recommend to go through either ways) :
    1. change the `server port` to whatever you want.
```
server.port=
```
2. Change the `url`.
```
spring.datasource.url=jdbc:postgresql://localhost:5432/bikeshop
```
3. Change the `username` and `password`.
```
spring.datasource.username=
spring.datasource.password=
```
4. Change the dialect.
```
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```
---

## Installation and running the project

1. To install the project, you can either use `git clone https://github.com/Sebastiannilsen/Backend.git` or download zip and open it on your local computer.
2. Open the project in your selected text editor.
3. Execute `mvn spring-boot:run` in the root of this project, or run the main method in the BikeApplication.
4. If you see the project running and "Importing test data..." in the console, congrats!
---

## Credits

We would like to give credits to NTNU Ålesund for this assignment. 

Collaborators: 
- https://github.com/Sebastiannilsen
- https://github.com/Fereshtaha

We followed the tutorials given by our lecturer Girts Strazdins.

- https://github.com/strazdinsg/app-dev
- https://github.com/strazdinsg/web-examples

---
## License

This project is licenced by the © group 16 in the course [Application Development IDATA2306](https://www.ntnu.edu/studies/courses/IDATA2306#tab=omEmnet) and [Web Technologies IDATA2301](https://www.ntnu.edu/studies/courses/IDATA2301#tab=omEmnet) at the Norwegian University of Science and Technology (NTNU).

---


