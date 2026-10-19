/*
create_app_user.sql

BEFORE RUNNING THIS SCRIPT
replace CHANGE_THIS_PASSWORD with your own password. Use this same
password in db.properties
DO NOT commit or submit this file with your real password

This script creates an account that the java application will use.
Run once as root or admin account in MySQL workbench AFTER running schema.sql

*/

CREATE USER IF NOT  EXISTS 'rescue_app'@'localhost' IDENTIFIED BY 'CHANGE_THIS_PASSWORD';

GRANT SELECT, INSERT, UPDATE, DELETE ON rescue_animals.* TO 'rescue_app'@'localhost';

GRANT DELETE ON rescue_animals.animals TO 'rescue_app'@'localhost';

SHOW GRANTS FOR 'rescue_app'@'localhost';