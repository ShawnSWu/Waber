create table match_status
(
    id     integer     not null auto_increment,
    status varchar(50) not null,
    primary key (id)
);

insert into match_status
values (1, 'Waiting to confirm match');
insert into match_status
values (2, 'Successfully matched');
insert into match_status
values (3, 'Driver cancel matched');
insert into match_status
values (4, 'Passenger cancel matched');


create table match_trip
(
    id                       integer   not null auto_increment,
    driver_id                integer   not null,
    passenger_id             integer   not null,
    activity_id              integer   not null,
    match_status             integer   not null,
    start_position_latitude  double    not null,
    start_position_longitude double    not null,
    car_type_id              integer   not null,
    date                     timestamp not null,
    time                     timestamp not null,
    primary key (id),
    foreign key (match_status) references match_status (id)
);