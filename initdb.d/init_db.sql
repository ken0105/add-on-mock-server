create database if not exists test;

create table mock_response(
    path varchar(255) primary key,
    expected_status_code int,
    expected_body json
);

insert into mock_response
values('/mock', 200, '{"message": "This is a mock response."}');
