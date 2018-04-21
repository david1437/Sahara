CREATE TABLE shopping_cart (
  u_recid int NOT NULL,
  p_recid int NOT NULL,
  pr_recid int NOT NULL,
  sc_quantity int DEFAULT 0,
  PRIMARY KEY(u_recid, p_recid, pr_recid)
);
CREATE TABLE purchase_history (
  u_recid int NOT NULL,
  p_recid int NOT NULL,
  pr_recid int NOT NULL,
  ph_quantity int DEFAULT 0,
  ph_dt_utc TIMESTAMP NOT NULL,
  PRIMARY KEY(u_recid, p_recid, pr_recid)
);
CREATE TABLE shipping_type (
  st_recid int NOT NULL AUTO_INCREMENT,
  st_name VARCHAR(255) NOT NULL,
  st_duration int DEFAULT 7,
  st_price decimal(10,2) NOT NULL,
  ut_recid int DEFAULT NULL,
  PRIMARY KEY(st_recid)
);
CREATE TABLE producers (
  pr_recid int NOT NULL AUTO_INCREMENT,
  pt_recid int NOT NULL,
  pt_name VARCHAR(255),
  pri_recid INT NOT NULL,
  PRIMARY KEY(pr_recid)
);
CREATE TABLE producer_inventory (
  p_recid int NOT NULL,
  pr_recid int NOT NULL,
  pri_quantity int DEFAULT 0,
  PRIMARY KEY(pr_recid, p_recid)
);
CREATE TABLE products (
  p_recid int NOT NULL AUTO_INCREMENT,
  pr_recid int NOT NULL,
  p_name VARCHAR(255),
  p_price decimal(10,2) NOT NULL,
  c_recid INT NOT NULL,
  PRIMARY KEY(p_recid, pr_recid)
);
CREATE TABLE product_category (
  c_recid INT NOT NULL AUTO_INCREMENT,
  c_name VARCHAR(255),
  PRIMARY KEY(c_recid)
);
CREATE TABLE users (
  u_recid INT NOT NULL AUTO_INCREMENT,
  u_email VARCHAR(255) NOT NULL,
  u_pword VARCHAR(255) NOT NULL,
  u_fn VARCHAR(255) DEFAULT NULL,
  u_ln VARCHAR(255) DEFAULT NULL,
  u_mn VARCHAR(255) DEFAULT NULL,
  u_dob DATE DEFAULT NULL,
  u_phone_number VARCHAR(10) DEFAULT NULL,
  u_address VARCHAR(255) DEFAULT NULL ,
  u_zcode VARCHAR(5) DEFAULT NULL,
  ut_recid int NOT NULL,
  PRIMARY KEY(u_recid)
);
CREATE TABLE payment_info (
  pay_recid INT NOT NULL AUTO_INCREMENT,
  u_recid int NOT NULL,
  pi_credit BIT DEFAULT 1,
  cc_recid int not null,
  pi_number VARCHAR(255) NOT NULL,
  pi_sec_code VARCHAR(10) NOT NULL,
  pi_val_through VARCHAR(255) NOT NULL,
  pi_fn VARCHAR(255) NOT NULL,
  pi_ln VARCHAR(255) NOT NULL,
  pi_mn VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY(pay_recid)
);
CREATE TABLE card_company (
  cc_recid INT NOT NULL AUTO_INCREMENT,
  cc_type INT DEFAULT 2,
  cc_company_name VARCHAR(255) NOT NULL,
  PRIMARY KEY(cc_recid)
);
CREATE TABLE user_type (
  ut_recid INT NOT NULL AUTO_INCREMENT,
  ut_name VARCHAR(255) DEFAULT 'Bronze',
  PRIMARY KEY(ut_recid)
);
CREATE TABLE producer_type (
  pt_recid INT NOT NULL AUTO_INCREMENT,
  pt_name VARCHAR(255) DEFAULT 'Bronze',
  PRIMARY KEY(pt_recid)
);
