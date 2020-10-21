DROP TABLE IF EXISTS user;

create table user( ID int primary key AUTO_INCREMENT NOT NULL, 
						userName varchar(10),
						password varchar(10),
						firstName varchar(20),
						lastName varchar(20),
						email varchar(25),						
						phone varchar(15)
					);
					
DROP TABLE IF EXISTS address;

create table address( ID int primary key AUTO_INCREMENT NOT NULL, 
						houseNumber varchar(6),
						street varchar(20),
						city varchar(20),
						state varchar(2),
						zipcode varchar(5),
 						country varchar(20)
					);
					
ALTER TABLE address ADD  COLUMN userId VARCHAR(10);
ALTER TABLE address ADD CONSTRAINT userId FOREIGN KEY (ID) REFERENCES user (ID);
