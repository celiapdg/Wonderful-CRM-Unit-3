DROP SCHEMA IF EXISTS wonderful_crm;
CREATE SCHEMA wonderful_crm;
USE wonderful_crm;


CREATE TABLE sales_rep(
sales_rep_id INT AUTO_INCREMENT NOT NULL,
`name` VARCHAR(255),
PRIMARY KEY(sales_rep_id)
);

CREATE TABLE `lead`(
lead_id INT AUTO_INCREMENT NOT NULL,
`name` VARCHAR(255),
email VARCHAR(255),
company_name VARCHAR(255),
phone_number VARCHAR(255),
sales_rep_id INT,
PRIMARY KEY(lead_id),
FOREIGN KEY(sales_rep_id) REFERENCES sales_rep(sales_rep_id)
);

CREATE TABLE `account`(
account_id INT AUTO_INCREMENT NOT NULL,
industry VARCHAR(255),
employee_count INT,
city VARCHAR(255),
country VARCHAR(255),
PRIMARY KEY(account_id)
);

CREATE TABLE contact(
contact_id INT AUTO_INCREMENT NOT NULL,
`name` VARCHAR(255),
email VARCHAR(255),
company_name VARCHAR(255),
phone_number VARCHAR(255),
account_id INT,
PRIMARY KEY(contact_id),
FOREIGN KEY(account_id) REFERENCES `account`(account_id)
);

CREATE TABLE opportunity(
opportunity_id INT AUTO_INCREMENT NOT NULL,
product VARCHAR(255),
quantity INT,
contact_id INT,
`status` VARCHAR(255),
sales_rep_id INT,
account_id INT,
PRIMARY KEY(opportunity_id),
FOREIGN KEY(contact_id) REFERENCES contact(contact_id),
FOREIGN KEY(account_id) REFERENCES `account`(account_id),
FOREIGN KEY(sales_rep_id) REFERENCES sales_rep(sales_rep_id)
);





 
  