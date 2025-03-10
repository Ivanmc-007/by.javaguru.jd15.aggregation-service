-- liquibase formatted sql

-- changeset yarakhovich_i:1
create table cv_aggregator
(
    request_id           bigint       not null auto_increment,
    cv_uuid              varchar(255) not null,
    information_request  json         not null,
    information_response json,
    status               varchar(255),
    status_message       varchar(255),
    cv_data              json,
    datetime_creation    datetime(6),
    datetime_end         datetime(6),
    primary key (request_id)
);