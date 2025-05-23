# How to Install QCEvents

## System Requirements
- Java JDK 21+
- Apache Tomcat 10.1.x
- MySQL 8.0+
- Maven 3.8+
- Internet connection (for Google Maps API and Gemini moderation)

## Installation Steps
1. Clone or unzip the project files.
2. Install MySQL and create the database using `schema.sql`.
3. Insert sample data using `insert_sample_data.sql`.
4. Import the project into Eclipse as a Maven project.
5. Build the project: `mvn clean package`
6. Copy the `qcevents.war` into Tomcat's `webapps/` folder.
7. Start Tomcat: `startup.bat` (Windows) or `./startup.sh` (Mac/Linux)
8. Access the app at: `http://localhost:8080/qcevents/`
