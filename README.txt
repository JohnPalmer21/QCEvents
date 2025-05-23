QC Events 
A full-stack campus event management application for Queens College students, built with Java 
Servlets (backend) and HTML/CSS/JavaScript (frontend). The platform enables students to 
discover events, RSVP with calendar integration, and allows club officers to create and manage 
events with AI-powered content moderation. 
Overview 
QC Events is designed to connect Queens College students with campus activities through 
three main user types: 
● Students: Discover and RSVP to events matching their interests 
● Club Officers: Create, edit, and manage their organization's events 
Project Structure 
QCEvents/ 
├── README.md 
├── pom.xml 
├── src/ 
│   ├── main/ 
│   │   ├── java/em/ 
│   │   │   ├── AdminApproveServlet.java    # Admin event approval 
│   │   │   ├── AdminRejectServlet.java     # Admin event rejection 
│   │   │   ├── AdminReviewServlet.java     # Admin review interface 
│   │   │   ├── AuthFilter.java             # Authentication 
middleware 
│   │   │   ├── DB.java                     # Database connection 
utility 
│   │   │   ├── EventCreateServlet.java     # Event creation logic 
│   │   │   ├── EventDeleteServlet.java     # Event deletion 
│   │   │   ├── EventEditServlet.java       # Event editing 
│   │   │   ├── EventsServlet.java          # Event listing API 
│   │   │   ├── FilterEventsServlet.java    # Event filtering by 
interests 
│   │   │   ├── GeminiFlagService.java      # AI content moderation 
│   │   │   ├── JWTUtil.java               # JWT token management 
│   │   │   ├── LoginServlet.java          # User authentication 
│   │   │   ├── MyEventsServlet.java       # User's personal events 
│   │   │   └── RSVPServlet.java           # RSVP management 
│   
│   
│   
│   
│   
│   
│   
│   
│   
│   
│   
│   
└── webapp/ 
├── WEB-INF/ 
│   
│   
│       
│       
│       
│       
│       
│       
│       
│       
│       
│       
└── test/java/                         
└── web.xml                
├── admin-script.js            
├── admin.html                 
├── event.html                 
├── index.html                 
├── login.html                 
├── my-events.html             
├── script.js                  
└── style.css                  
└── target/                                
Prerequisites 
Install the following tools before running the application: 
● Java 17 or higher: Oracle JDK Downloads 
● Apache Maven: Maven Installation Guide 
● Apache Tomcat 11: Tomcat Downloads 
● MySQL Database: MySQL Downloads 
Verify installation with: 
bash 
java -version 
mvn -v 
Running the Application 
Backend (Java Servlet with Apache Tomcat) 
bash 
cd QCEvents 
mvn clean package 
cp target/qcevents.war 
# Servlet configuration 
# Admin panel JavaScript 
# Admin interface 
# Event details page 
# Main event listing 
# Authentication page 
# Personal events page 
# Main frontend logic 
# Application styling 
# Unit tests 
# Maven build output 
/opt/homebrew/Cellar/tomcat/11.0.5/libexec/webapps/ 
cd /opt/homebrew/Cellar/tomcat/11.0.5/libexec/bin 
./shutdown.sh 
./startup.sh 
The backend will run at: 
 
 
 http://localhost:8080/qcevents 
Frontend (HTML/CSS/JavaScript - Served via Tomcat) 
No separate server is needed. The frontend is part of the .war file. Just go to: 
 
 
 http://localhost:8080/qcevents/index.html 
Key Features 
Student Features 
● Event Discovery: Browse upcoming campus events with filtering by interests/major 
● Event Details: View comprehensive event information including time, location, and 
description 
● RSVP System: One-click RSVP with automatic calendar integration 
● Interactive Campus Map: Google Maps integration showing event locations 
● Personalized Feed: Events tailored to student interests and academic major 
Club Officer Features 
● Event Management: Create, edit, and delete organization events 
● AI Content Moderation: Automatic content screening via Gemini API integration 
● Event Analytics: Track RSVPs and engagement metrics 
● Approval Workflow: Submit events for administrative review 
API Endpoints 
Authentication 
● POST /qcevents/login – User authentication with QC email 
● GET /qcevents/logout – Session termination 
Events 
● GET /qcevents/events – List all approved events 
● POST /qcevents/event/create – Create new event (club officers) 
● PUT /qcevents/event/edit – Edit existing event 
● DELETE /qcevents/event/delete – Delete event 
● GET /qcevents/filter-events – Filter events by criteria 
RSVP Management 
● POST /qcevents/rsvp – RSVP to event with calendar sync 
● GET /qcevents/my-events – User's RSVP'd events 
Administration 
● GET /qcevents/admin/review – Pending events for review 
● POST /qcevents/admin/approve – Approve submitted event 
● POST /qcevents/admin/reject – Reject submitted event 
Database Configuration 
The application requires a MySQL database with the following setup: 
1. Create database: qc_events 
2. Configure connection in DB.java 
3. Tables will include: 
○ users (student accounts with phone numbers) 
○ events (event details and metadata) 
○ rsvps (user event registrations) 
○ clubs (student organizations) 
○ categories (event classification) 
Third-Party Integrations 
Google Maps API 
● Purpose: Interactive campus map with event location markers 
● Usage: Event location visualization and navigation 
Gemini API 
● Purpose: AI-powered content moderation 
● Usage: Automatic screening of event titles and descriptions for policy violations 
Google Calendar API 
● Purpose: Calendar integration for RSVP'd events 
● Usage: Automatic event addition to student calendars 
Security Features 
● JWT-based Authentication: Secure session management with token-based access 
control 
● Role-based Authorization: Different access levels for students, club officers, and 
administrators 
● Content Filtering: AI-powered moderation to prevent policy violations 
● Input Validation: Server-side validation for all user inputs 
User Stories 
As a Queens College student, I want to easily discover events so I can stay engaged with 
academic, social, and extracurricular opportunities. 
As a Queens College student organization, I want to create, promote, and manage events 
effectively so we can grow our reach and engage more students. 
As Queens College administration, I want to oversee and moderate all campus event activity 
so we can ensure safety, compliance, and resource allocation. 
Current Limitations 
1. Gemini API: Currently simulated, not live integration 
2. Google Calendar: Uses static ICS download instead of OAuth integration 
3. Password Reset: No password recovery functionality implemented 
4. File Uploads: No image upload capability for events 
5. Mobile Interface: Web-only interface, native mobile app not implemented 
These limitations may be addressed in future development iterations. 
Development Status 
This application represents a comprehensive campus event management solution with modern 
web technologies and AI integration. The platform successfully bridges the gap between 
students, student organizations, and administration through a unified event discovery and 
management system. 
Contributors 
Built for Queens College to enhance campus life and student engagement through better event 
discovery and management.