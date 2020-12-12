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

create table car_type
(
    id          integer     not null auto_increment,
    type        varchar(50) not null,
    extra_price integer     not null,
    primary key (id)
);

insert into car_type
values (1, 'Sport', 1000);
insert into car_type
values (2, 'Business', 100);
insert into car_type
values (3, 'Normal', 0);

create table driver_car_type
(
    id          integer not null auto_increment,
    driver      integer not null,
    car_type_id integer not null,
    primary key (id),
    foreign key (driver) references user (id),
    foreign key (car_type_id) references car_type (id)
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

create table activity
(
    id          integer      not null auto_increment,
    name        varchar(128) not null,
    extra_price integer      not null,
    start_day   date,
    expire_day  date,
    primary key (id)
);
insert into activity
values (1, 'ValentinesDay', 30, '2020-02-14', '2020-02-14');

create table activity_driver
(
    id          integer not null auto_increment,
    activity_id integer not null,
    driver_id   integer not null,
    car_type_id integer not null,
    primary key (id),
    foreign key (activity_id) references activity (id),
    foreign key (driver_id) references user (id),
    foreign key (car_type_id) references car_type (id)
);