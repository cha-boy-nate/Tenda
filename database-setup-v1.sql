#mysql -h localhost -u root -p <-log into ubuntu server's database

CREATE TABLE testTable(
   user_id INT AUTO_INCREMENT PRIMARY KEY,
   email VARCHAR(255),
   password VARCHAR(255)
);

#insert into user(username, password, email) values ('nathanj@spu.edu', 'password');