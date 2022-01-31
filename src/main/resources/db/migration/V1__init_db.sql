create table Users
(
    id        int auto_increment not null primary key,
    firstName varchar(255)       not null,
    lastName  varchar(255)       not null
);

insert into Users
    (firstName, lastName)
values ('Joe', 'Blow'),
       ('Jane', 'Doe')