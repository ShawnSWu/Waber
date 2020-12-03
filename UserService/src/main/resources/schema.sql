create table roles
(
    id   integer not null,
    name varchar(10)
);
insert into roles
values (1, 'driver');
insert into roles
values (2, 'passenger');

create table user
(
    id              integer      not null auto_increment,
    email           varchar(128) not null,
    hashed_password varchar(64)  not null,
    name            varchar(50)  not null,
    role            integer      not null,
    primary key (id),
    foreign key (role) references roles (id)
);

create table driver_car_type
(
    id       integer     not null auto_increment,
    driver   integer     not null,
    car_type varchar(50) not null,
    primary key (id),
    foreign key (driver) references user (id)
);



create table user_location
(
    id        integer  not null auto_increment,
    user_id   integer  not null,
    datetime  datetime not null,
    latitude  double   not null,
    longitude double   not null,
    primary key (id),
    foreign key (user_id) references user (id)
);