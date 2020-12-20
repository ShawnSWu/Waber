create table trip_status
(
    id     integer     not null auto_increment,
    status varchar(50) not null,
    primary key (id)
);

insert into trip_status
values (1, 'On the way.');
insert into trip_status
values (2, 'Arrive at destination.');
insert into trip_status
values (3, 'Cancel trip halfway.');


create table trip
(
    id                       integer     not null auto_increment,
    match_id                 integer     not null,
    date                     timestamp   not null,
    time                     timestamp   not null,
    trip_status              integer     not null,
    trip_distance            integer     not null,
    primary key (id),
    foreign key (trip_status) references trip_status
)