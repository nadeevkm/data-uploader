CREATE USER data_uploader WITH password 'data_uploader';

CREATE SCHEMA data_uploader AUTHORIZATION data_uploader;

GRANT ALL ON SCHEMA data_uploader TO data_uploader;