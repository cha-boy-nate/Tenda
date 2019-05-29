CREATE DATABASE Tenda;
use Tenda;

CREATE TABLE Event(
	event_id INT AUTO_INCREMENT PRIMARY KEY,
	manager_id INT,
	eventName VARCHAR(255),
	eventDate DATE,
	eventTime TIME,
	eventDuration TIME,
	eventRadius DECIMAL(5, 2),
	eventDescription VARCHAR(255)
);

CREATE TABLE Events_to_Attendees(
	row_id INT AUTO_INCREMENT PRIMARY KEY,
	event_id INT,
	user_id INT,
	response ENUM('yes','no'),
	responseTime timestamp
);

CREATE TABLE User(
   user_id INT AUTO_INCREMENT PRIMARY KEY,
   firstName VARCHAR(255),
   lastName VARCHAR(255),
   email VARCHAR(255),
   password VARCHAR(255)
);

CREATE TABLE Reported_Issues(
	id INT AUTO_INCREMENT PRIMARY KEY,
	user_id INT,
	currectTime timestamp,
	description VARCHAR(255)
);