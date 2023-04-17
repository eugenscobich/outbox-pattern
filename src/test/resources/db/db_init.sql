--CREATE DATABASE db_iot_service;

CREATE USER user_migration_iot_service WITH CREATEROLE ENCRYPTED PASSWORD 'password_user_migration_iot_service';

GRANT CONNECT ON DATABASE db_iot_service TO user_migration_iot_service;

GRANT ALL PRIVILEGES ON DATABASE db_iot_service TO user_migration_iot_service;