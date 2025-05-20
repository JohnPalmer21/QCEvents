## Build Instructions

### Prerequisites
- **Java 11**: Ensure Java 11 is installed and configured.
- **Maven**: Install Maven (version 3.6+ recommended).
- **Database**: Set up the MySQL database using the provided schema in `backend/resources/mySQLschema`.

### Build Steps
Navigate to the `backend` directory:
   ```bash
   cd backend
   ```
### Build the project using Maven:
   ```bash
   mvn clean install
   ```
### Optional: Create a script to run the application
   ```bash
   'build.sh'(Mac/Linux) or 'build.bat'(Windows)
   
   cd backend 
   mvn clean install
   
   Start on Tomcat and direct the path to .../build.script
   ```