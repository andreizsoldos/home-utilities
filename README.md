# Home Utilities

Get statistics for monthly home utilities consumption.

---

With this web application you can add/remove data and view statistics in order to have a better overview of different types of utilities consumption.

MAVEN was used to build the project.
Also, all data was handled by Spring Framework, Spring Security, in usage with Thymeleaf engine.

---

## Build the Project

---

- ### Database configuration
The project uses Postgresql database.
Please make sure you have locally installed Postgresql.
After that follow the below steps:
      
   1. create database with 'name' = `maintenancedb`
   2. username = postgres
   3. password = postgres

---

- ### Running application locally

To build this project you will need Maven. You can get it at:

     http://maven.apache.org

1. Change directory to your project root folder in command line.
   
         e.g. C:\Users\`username`\Desktop\`home-utilities`\ 

2. You should run below command to skip steps 3-5:

        mvn clean install

3. Clean project:

        mvn clean

4. Compile:

        mvn compile

5. If you are looking to package the project, then you should run:

        mvn package

6. Execute:

        mvn exec:java -Dexec.mainClass=com.home.utilities.UtilitiesApplication

Once started, the local application will be available at:

     http://localhost:8080/

---

- ### Running application hosted on Heroku client

      https://homeutilities.herokuapp.com/
      
