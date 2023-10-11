create database haproxy;

\c haproxy
CREATE TABLE session_data (
      session_id VARCHAR(255) PRIMARY KEY,
      session_data VARCHAR(255),
      creation_time TIMESTAMP,
      last_accessed_time TIMESTAMP
);