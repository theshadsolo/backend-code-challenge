create table User
(
    id        bigint              not null primary key,
    addressId bigint,
    firstName varchar(255)        not null,
    lastName  varchar(255)        not null,
    username  varchar(255) unique not null,
    password  varchar(255)        not null -- WHAT!? NOT ENCRYPTED!? ;-)
);

create table Address
(
    id        bigint              not null primary key auto_increment,
    address1  varchar(255)        not null,
    address2  varchar(255),
    city      varchar(255)        not null,
    state     varchar(100)        not null,
    postal    varchar(10)        not null
);

alter table User
  add foreign key (addressId) references Address(id);

insert into User
    (id, firstName, lastName, username, password)
values (1, 'Phil', 'Ingwell', 'PhilIngwell', 'Password123') ,
    (2, 'Anna', 'Conda', 'AnnaConda', 'Password234');

insert into Address (id, address1, city, state, postal)
values (1, '1234 Park St.', 'Brooklyn', 'New York', '12345'),
       (2, '5678 Main St.', 'Detroit', 'Michigan', '67890');

update User
  set addressId = 2
  where id = 1;

update User
  set addressId = 1
  where ID = 2;
