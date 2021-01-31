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

INSERT INTO sales_rep (`name`) VALUES
('Pepa Floros'),
('Husd Hasd');

INSERT INTO `lead` (`name`, email, company_name, phone_number, sales_rep_id) VALUES
('Pepa Pig',  'pepa@pig.pp','Pigs','676767676',  1),
('Ana Cardo',  'ana@car.do', 'Cards','656565656', 2),
('Hula Hop', 'hu@la.hop', 'Huli', '656565656',  2);

INSERT INTO `account` (industry, employee_count, city, country) VALUES
('OTHER', 40, 'Albacete', 'ESSSSPAÑA'),
('MEDICAL', 2, 'Turruncun', 'ESSSSPAÑA'),
('OTHER', 6, 'Tarancon', 'Cuenca, republica independiente');

INSERT INTO contact (`name`, email, company_name,phone_number, account_id) VALUES
('Pepa Pig', 'pepa@pig.pp', 'Pigs', '676767676', 1),
('Ana Cardo',  'ana@car.do', 'Cards','656565656', 2),
('Hula Hop', 'hu@la.hop', 'Huli','656565656',  3);

INSERT INTO opportunity(product, quantity, contact_id, `status`, sales_rep_id, account_id) VALUES
('BOX', 40, 1, 'OPEN', 1, 1),
('FLATBED', 23, 2, 'CLOSE-WON', 2, 3),
('HYBRID', 77, 3, 'CLOSE-LOST', 1, 3);

-- The median number of Opportunities associated with an Account can be displayed by typing "Median Opps per Account"
SELECT avg(oo.opps) FROM
(SELECT COUNT(o.opportunity_id) AS opps 
FROM `account` a
JOIN opportunity o ON a.account_id = o.account_id 
GROUP BY a.account_id) AS oo;

SELECT MAX(oo.opps) FROM
(SELECT COUNT(o.opportunity_id) AS opps 
FROM `account` a
JOIN opportunity o ON a.account_id = o.account_id 
GROUP BY a.account_id) AS oo;

SELECT MIN(oo.opps) FROM
(SELECT COUNT(o.opportunity_id) AS opps 
FROM `account` a
JOIN opportunity o ON a.account_id = o.account_id 
GROUP BY a.account_id) AS oo;

SELECT account_id, COUNT(*) FROM opportunity GROUP BY account_id LIMIT COUNT;
-- SELECT AVG(dd.account_id) AS median_count FROM (SELECT d.account_id @rownum := @rownum + 1 as `row_number`, @total_rows:=@rownum
  --  FROM opportunity d, (SELECT @rownum:=0) r
  -- WHERE d.account_id is NOT NULL
  -- put some where clause here
  -- ORDER BY d.account_id
-- ) as dd
-- WHERE dd.row_number IN ( FLOOR((@total_rows+1)/2), FLOOR((@total_rows+2)/2) );


SET @rowindex := -1;
SELECT
   AVG(e.views) as Median 
FROM
   (SELECT @rowindex:=@rowindex + 1 AS rowindex,
           entry.views AS views
    FROM entry
    ORDER BY entry.views) AS e
WHERE
e.rowindex IN (FLOOR(@rowindex / 2), CEIL(@rowindex / 2));

