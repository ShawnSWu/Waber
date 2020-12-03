create table activity
(
    id         integer      not null auto_increment,
    name       varchar(128) not null,
    start_day  date,
    expire_day date,
    primary key (id)
);
insert into activity
values (1, 'ValentinesDay', '2020-01-01', '2020-12-31');

create table activity_driver
(
    id        integer     not null auto_increment,
    activity  integer     not null,
    driver_id integer     not null,
    car_type  varchar(50) not null,
    primary key (id),
    foreign key (activity) references activity (id)
);

create table waiting_match_passenger
(
    id              integer     not null auto_increment,
    passenger       integer     not null,
    prefer_activity integer     not null,
    prefer_car_type varchar(50) not null,
    primary key (id),
    foreign key (prefer_activity) references activity (id)
);



create table matched_trip
(
    id            integer not null auto_increment,
    driver        integer not null,
    passenger     integer not null,
    activity      integer not null,
    match_success integer not null,
    primary key (id),
    foreign key (activity) references activity (id)
);