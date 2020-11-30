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

create table activity_participant
(
    id          integer not null auto_increment,
    activity    integer not null,
    participant integer not null,
    primary key (id),
    foreign key (activity) references activity (id)
);