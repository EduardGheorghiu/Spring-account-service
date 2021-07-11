CREATE TABLE account
(
 id INT NOT NULL,
 iban VARCHAR NOT NULL,
 currency VARCHAR NOT NULL ,
 balance DECIMAL  NULL,
 last_update_date TIMESTAMP NULL,
 PRIMARY KEY (id)
);