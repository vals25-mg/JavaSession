create database haproxy;

\c haproxy
CREATE TABLE session_data (
      session_id VARCHAR(255) PRIMARY KEY,
      session_data bytea,
      creation_time TIMESTAMP,
      last_accessed_time TIMESTAMP
);

alter table session_data
    add column ipaddress varchar(15);