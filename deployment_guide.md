# Deployment Guide

## Environment
- JDK Version: 21
- Apache Tomcat: 10.1.41
- MySQL: 8.0.36
- Maven: 3.9.9
- Libraries: `jjwt`, `jakarta.servlet-api`, `mysql-connector-java`

## Deployment Instructions
1. Build the WAR using Maven:
   `mvn clean package`
2. Copy `target/qcevents.war` to Tomcat's `webapps/` folder.
3. Start Tomcat and check logs for deployment confirmation.
4. Use Postman or browser to verify `/events` and `/login` endpoints.
