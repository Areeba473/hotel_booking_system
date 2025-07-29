
Hotel Booking System

A Java-based application for managing hotel room bookings, integrated with a MySQL database to perform CRUD operations.

Features User Roles: Supports user and admin accounts for different functionalities. CRUD Operations: Create, read, update, and delete hotel bookings. Database Integration: Stores booking and user data in MySQL.

User Interface: [Specify UI, e.g., Swing-based GUI, JavaFX, or console-based interface].

Tech Stack

Language: Java Database: MySQL

Tools: [Specify IDE, e.g., IntelliJ IDEA, Eclipse], JDBC for database connectivity, Git

UI Framework: [Specify, e.g., Swing, JavaFX, or console-based]

Setup Instructions Clone the Repository:

git clone https://github.com/Areeba473/hotel_booking_system.git cd hotel_booking_system

Set Up MySQL Database:

Install MySQL (e.g., MySQL Community Server). Create a database named hotel_booking_db:

CREATE DATABASE hotel_booking_db;

Run the provided SQL script to set up tables (e.g., schema.sql in the repository):

SOURCE schema.sql;

Update database credentials in the Java code (e.g., src/config/DatabaseConfig.java):

String url = "jdbc:mysql://localhost:3306/hotel_booking_db"; String user = "your_username"; String password = "your_password";

Build and Run: Open the project in your IDE (e.g., IntelliJ, Eclipse).

Ensure the JDBC driver is included (e.g., via pom.xml for Maven or manual JAR addition).

Run the main class (e.g., src/main/HotelBookingApp.java): java -cp . HotelBookingApp

Folder Structure

├── src/ │ ├── main/ │ │ ├── HotelBookingApp.java # Main application entry point │ │ ├── config/ # Database configuration │ │ ├── models/ # Data models (e.g., Booking, User) │ │ ├── ui/ # UI components (e.g., Swing/JavaFX) ├── schema.sql # MySQL database schema ├── pom.xml # Maven dependencies (if applicable)

Future Improvements Add payment integration for bookings. Implement a web-based interface using Spring Boot. Enhance security with password encryption.

Contributing

Feel free to fork the repository, create a feature branch, and submit a pull request with improvements.

Contact

GitHub: Areeba473

New File at / · Areeba473/hotel_booking_system
