create table roles
(
    id   integer not null,
    name varchar(10)
);
insert into roles
values (1, 'driver');
insert into roles
values (2, 'passenger');

create table driver
(
    id              integer      not null auto_increment,
    email           varchar(128) not null,
    hashed_password varchar(64)  not null,
    name            varchar(50)  not null,
    car_type        varchar(50)  not null,
    primary key (id)
);

create table passenger
(
    id              integer      not null auto_increment,
    email           varchar(128) not null,
    hashed_password varchar(64)  not null,
    name            varchar(50)  not null,
    primary key (id)
);

create table activity
(
    id         integer      not null auto_increment,
    name       varchar(128) not null,
    start_day  date,
    expire_day date,
    primary key (id)
);
insert into activity
values (1, 'valentinesDay', '2020-01-01', '2020-12-31');

create table activity_participant
(
    id          integer not null auto_increment,
    activity    integer not null,
    participant integer not null,
    primary key (id),
    foreign key (activity) references activity (id),
    foreign key (participant) references driver (id)
);

create table user_location
(
    id        integer  not null auto_increment,
    user_id   integer  not null,
    datetime  datetime not null,
    latitude  double   not null,
    longitude double   not null,
    primary key (id)
);