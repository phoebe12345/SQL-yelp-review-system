DROP TABLE IF EXISTS user_yelp;
DROP TABLE IF EXISTS friendship;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS tip;
DROP TABLE IF EXISTS checkin;
DROP TABLE IF EXISTS business;
CREATE TABLE user_yelp(
 user_id CHAR(22) PRIMARY KEY,
 name CHAR(35) NOT NULL,
 review_count INT DEFAULT 0
CHECK (review_count >= 0),
 yelping_since DATE DEFAULT GETDATE() NOT NULL,
 useful INT DEFAULT 0
CHECK (useful >= 0),
 funny INT DEFAULT 0
CHECK (funny >= 0),
 cool INT DEFAULT 0
CHECK (cool >= 0),
 fans INT DEFAULT 0
CHECK (fans >= 0),
 average_stars DECIMAL(3, 2)
CHECK (average_stars >= 1 AND average_stars <= 5)
);
CREATE TABLE business(
business_id CHAR(22) PRIMARY KEY,
name CHAR(60) NOT NULL,
address CHAR(75),
city CHAR(30) NOT NULL,
postal_code CHAR(7),
stars DECIMAL(2,1)
CHECK (stars > 0 AND stars < 6),
review_count INT DEFAULT 0
CHECK (review_count >= 0)
);
CREATE TABLE checkin (
 checkin_id INT IDENTITY(1,1) PRIMARY KEY,
 business_id CHAR(22) NOT NULL,
 date DATE DEFAULT GETDATE() NOT NULL,
 FOREIGN KEY (business_id) REFERENCES business(business_id) ON DELETE NO ACTION
ON UPDATE NO ACTION
);
CREATE TABLE tip (
 tip_id INT IDENTITY(1,1) PRIMARY KEY,
 user_id CHAR(22) NOT NULL,
 business_id CHAR(22) NOT NULL,
 date DATE DEFAULT GETDATE() NOT NULL,
 compliment_count INT DEFAULT 0
CHECK (compliment_count >= 0),
 FOREIGN KEY (user_id) REFERENCES user_yelp(user_id) ON DELETE NO ACTION ON
UPDATE NO ACTION,
 FOREIGN KEY (business_id) REFERENCES business(business_id) ON DELETE NO ACTION
ON UPDATE NO ACTION
);
CREATE TABLE review (
 review_id CHAR(22) PRIMARY KEY,
 user_id CHAR(22) NOT NULL,
 business_id CHAR(22) NOT NULL,
 stars INT NOT NULL
CHECK (stars >= 1 AND stars <= 5),
 useful INT DEFAULT 0
CHECK (useful >= 0),
 funny INT DEFAULT 0
CHECK (funny >= 0),
 cool INT DEFAULT 0
CHECK (cool >= 0),
 date DATE DEFAULT GETDATE(),
 --FOREIGN KEY (user_id) REFERENCES user_yelp(user_id) ON DELETE NO ACTION ON
UPDATE NO ACTION,
 FOREIGN KEY (business_id) REFERENCES business(business_id) ON DELETE NO ACTION
ON UPDATE NO ACTION
);
CREATE TABLE friendship (
 user_id CHAR(22) NOT NULL,
 friend CHAR(22) NOT NULL,
 PRIMARY KEY (user_id, friend),
 --FOREIGN KEY (user_id) REFERENCES user_yelp(user_id) ON DELETE NO ACTION ON
UPDATE NO ACTION,
 FOREIGN KEY (friend) REFERENCES user_yelp(user_id) ON DELETE NO ACTION ON
UPDATE NO ACTION
);
BULK INSERT dbo.business FROM 'd:\userdata\assignment3\business.csv' WITH
(fieldterminator=',',
rowterminator='\n', firstrow=2, FORMAT = 'csv')
BULK INSERT dbo.checkin FROM 'd:\userdata\assignment3\checkin.csv' WITH
(fieldterminator=',',
rowterminator='\n', firstrow=2, FORMAT = 'csv')
BULK INSERT dbo.friendship FROM 'd:\userdata\assignment3\friendship.csv' WITH
(fieldterminator=',',
rowterminator='\n', firstrow=2, FORMAT = 'csv')
BULK INSERT dbo.review FROM 'd:\userdata\assignment3\review.csv' WITH
(fieldterminator=',',
rowterminator='\n', firstrow=2, FORMAT = 'csv')
BULK INSERT dbo.tip FROM 'd:\userdata\assignment3\tip.csv' WITH
(fieldterminator=',',
rowterminator='\n', firstrow=2, FORMAT = 'csv')
BULK INSERT dbo.user_yelp FROM 'd:\userdata\assignment3\user_yelp.csv' WITH
(fieldterminator=',',
rowterminator='\n', firstrow=2, FORMAT = 'csv')
--(5573 rows affected) -> business
--(208923 rows affected) -> checkin
--(2473 rows affected) -> friendship
--(109435 rows affected) -> review
--(15513 rows affected) -> tip
--(23863 rows affected) -> user_yelp