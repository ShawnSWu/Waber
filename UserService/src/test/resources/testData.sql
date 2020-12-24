
insert into user (id, email, hashed_password, name, role)
values (1, 'Johnny@passenger.com', '$2a$10$Hdve6ucUYFJS8tXFpVC1YuS0qIFt5jaVwZesHgwy4CMHRUZsuprRu', 'Johnny', 2);

insert into user (id, email, hashed_password, name, role)
values (2, 'Johnny@driver.com', '$2a$10$Hdve6ucUYFJS8tXFpVC1YuS0qIFt5jaVwZesHgwy4CMHRUZsuprRu', 'Johnny', 1);

insert into driver_car_type (id, driver, car_type_id)
values (1, 2, 1);

insert into user_location (id, user_id, datetime, latitude, longitude)
values (1, 1, '2020-09-27 12:00:00', 25.047713, 121.5174007);