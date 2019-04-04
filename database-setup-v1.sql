#mysql -h localhost -u root -p <-log into ubuntu server's database

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

Insert Into User(firstName, lastName, email, password) values('Nate', 'Jenkins', 'nathanj@spu.edu', 'password');
Insert Into User(firstName, lastName, email, password) values('Steve', 'Jenson', 'test-email@hotmail.com', 'test');
Insert Into User(firstName, lastName, email, password) values('Bob', 'Boberstien', 'email@aol.com', '1234');
Insert Into User(firstName, lastName, email, password) values('Pharis', 'Fuller', 'test@msn.com', 'password1');
Insert Into Event(manager_id, eventName, eventDate, eventTime, eventDuration, eventRadius, eventDescription) values ("1", "Team Chan Meeting", "2019-03-19", "00:00:00", "00:00:00", "10.00", "Weekly meeting agenda");
Insert Into Event(manager_id, eventName, eventDate, eventTime, eventDuration, eventRadius, eventDescription) values ("2", "Sports Event", "2019-03-19", "00:00:00", "00:00:00", "100", "FIFA World Cup");
Insert Into Event(manager_id, eventName, eventDate, eventTime, eventDuration, eventRadius, eventDescription) values ("2", "Lecture", "2019-03-19", "00:00:00", "00:00:00", "100.0", "P vs NP");
Insert Into Event(manager_id, eventName, eventDate, eventTime, eventDuration, eventRadius, eventDescription) values ("1", "Concert", "2019-03-19", "00:00:00", "00:00:00", "10.9", "Tupac ft Biggie Smalls");
Insert Into Events_to_Attendees(event_id, user_id, response) values ("1", "1", "yes");
Insert Into Events_to_Attendees(event_id, user_id, response) values ("1", "2", "yes");
Insert Into Events_to_Attendees(event_id, user_id, response) values ("1", "3", "no");
Insert Into Events_to_Attendees(event_id, user_id, response) values ("1", "4", "no");
Insert Into Events_to_Attendees(event_id, user_id, response) values ("2", "2", "yes");
Insert Into Events_to_Attendees(event_id, user_id, response) values ("3", "3", "no");
Insert Into Events_to_Attendees(event_id, user_id, response) values ("3", "4", "no");
Insert Into Events_to_Attendees(event_id, user_id, response) values ("4", "2", "yes");