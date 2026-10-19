/* 
schema.sql
This script creates the rescue_animals database and its tables.
Run once as root or admin account in MySQL workbench
Can be re-run, does not drop or duplicate tables
*/

CREATE DATABASE IF NOT EXISTS rescue_animals;

USE rescue_animals;

CREATE TABLE IF NOT EXISTS animals (
    animal_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    animal_type VARCHAR(10) NOT NULL CHECK (animal_type IN ('dog', 'monkey')),
    gender VARCHAR(20) NOT NULL,
    age INT NOT NULL CHECK (age >= 0),
    weight DECIMAL(5,2) NOT NULL CHECK (weight > 0),
    acquisition_date DATE NOT NULL,
    acquisition_country VARCHAR(50) NOT NULL,
    training_status VARCHAR(30) NOT NULL,
    reserved BOOLEAN NOT NULL DEFAULT FALSE,
    in_service_country VARCHAR(50) NOT NULL

);


CREATE TABLE IF NOT EXISTS dogs (
    animal_id INT NOT NULL PRIMARY KEY,
    breed VARCHAR(50) NOT NULL,

    CONSTRAINT fk_dogs_animal FOREIGN KEY (animal_id) REFERENCES animals (animal_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS monkeys (
    animal_id INT NOT NULL PRIMARY KEY,
    tail_length DECIMAL(5,2) NOT NULL CHECK (tail_length > 0),
    height DECIMAL(5,2) NOT NULL CHECK (height > 0),
    body_length DECIMAL(5,2) NOT NULL CHECK (body_length >0),
    species VARCHAR(50) NOT NULL,

    CONSTRAINT fk_monkeys_animal FOREIGN KEY (animal_id) REFERENCES animals (animal_id) ON DELETE CASCADE
);

