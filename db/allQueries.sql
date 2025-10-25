create table users
(
    id         serial primary key,
    username   varchar(50)  not null,
    email      varchar(100) not null unique,
    password   varchar(255) not null,
    created_at timestamp default current_timestamp
);
create table forums
(
    id         serial primary key,
    user_id    int          not null,
    content    text not null,
    created_at timestamp default current_timestamp,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
create table users
(
    id         serial primary key,
    username   varchar(50)  not null,
    email      varchar(100) not null unique,
    password   varchar(255) not null,
    role       varchar(10)  not null,
    created_at timestamp default current_timestamp
);
create table forums
(
    id      serial primary key,
    user_id int  not null,
    content text not null,
    date    timestamp default current_timestamp,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
create table forums
(
    id      serial primary key,
    user_id int ,
    content text not null,
    date    timestamp default current_timestamp,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
create table forums
(
    id      serial primary key,
    user_id int ,
    text text not null,
    date    timestamp default current_timestamp,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
INSERT INTO category (name) VALUES ('Лекция');
INSERT INTO category (name) VALUES ('Семинар');
INSERT INTO category (name) VALUES ('Выставка');
INSERT INTO category (name) VALUES ('Мастер-класс');
INSERT INTO category (name) VALUES ('Конкурс');
INSERT INTO events (title, description, location, time, seatCount, category_id)
VALUES ('Лекция по квантовой физике', 'Чтение лекции профессора Иванова.', 'Аудитория 101', '2023-09-25 10:00:00', 100, 1);
INSERT INTO events (title, description, location, time, seatCount, category_id)
VALUES ('Семинар по программированию', 'Интерактивный семинар для студентов.', 'Аудитория 202', '2023-10-05 13:00:00', 50, 2);
INSERT INTO events (title, description, location, time, seatCount, category_id)
VALUES ('Выставка научных проектов', 'Выставка работ студентов выпускного курса.', 'Выставочный зал университета', '2023-11-15 14:00:00', 200, 3);
INSERT INTO events (title, description, location, time, seatCount, category_id)
VALUES ('Мастер-класс по фотографии', 'Научитесь основам фотографии.', 'Аудитория 303', '2023-12-10 11:00:00', 30, 4);
INSERT INTO events (title, description, location, time, seatCount, category_id)
VALUES ('Конкурс стартапов', 'Презентация стартапов студентов.', 'Конференц-зал', '2023-12-20 09:00:00', 150, 5);
SELECT id, title, description, location, time, seatCount, category_id FROM events;
drop table category;
create table category
(
    id   serial primary key,
    name varchar(100)
);
create table events
(
    id          serial primary key,
    title       varchar(100),
    description text,
    location    varchar(255),
    time        timestamp,
    seatCount   int,
    category_id int,
    foreign key (category_id) references category (id)

);
INSERT INTO category (name) VALUES ('Lecture');
INSERT INTO category (name) VALUES ('Seminar');
INSERT INTO category (name) VALUES ('Exhibition');
INSERT INTO category (name) VALUES ('Workshop');
INSERT INTO category (name) VALUES ('Competition');
INSERT INTO events (title, description, location, time, seatCount, category_id)
VALUES ('Lecture on Quantum Physics', 'A lecture by Professor Ivanov.', 'Room 101', '2023-09-25 10:00:00', 100, 1);
INSERT INTO events (title, description, location, time, seatCount, category_id)
VALUES ('Programming Seminar', 'An interactive seminar for students.', 'Room 202', '2023-10-05 13:00:00', 50, 2);
INSERT INTO events (title, description, location, time, seatCount, category_id)
VALUES ('Scientific Projects Exhibition', 'An exhibition of graduation projects by students.', 'University Exhibition Hall', '2023-11-15 14:00:00', 200, 3);
INSERT INTO events (title, description, location, time, seatCount, category_id)
VALUES ('Photography Workshop', 'Learn the basics of photography.', 'Room 303', '2023-12-10 11:00:00', 30, 4);
INSERT INTO events (title, description, location, time, seatCount, category_id)
VALUES ('Startup Competition', 'Presentation of students startup projects.', 'Conference Hall', '2023-12-20 09:00:00', 150, 5);
drop table events;
drop table forums;
drop table users;
create table users
(
    id           serial primary key,
    username     varchar(50) unique not null,
    email        varchar(100)       not null unique,
    password     varchar(255)       not null,
    role         varchar(10)        not null,
    group_number varchar(10),
    created_at   timestamp default current_timestamp
);
create table topic
(
    id    serial primary key,
    title varchar(100)
);
create table forums
(
    id      serial primary key,
    user_id int,
    text    text not null,
    date    timestamp default current_timestamp,
    topic   int,
    likes   int       default 0,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
create table event
(
    id          serial primary key,
    title       varchar(100),
    description text,
    location    varchar(255),
    time        timestamp,
    seatCount   int,
    category_id int,
    created_by int,
    foreign key (category_id) references category (id),
    foreign key (created_by) references users(id)
);
create table registration
(
    id                serial primary key,
    user_id           int,
    event_id          int,
    registration_time timestamp,
    status            varchar(100),
    comment text,
    foreign key (user_id) references users(id),
    foreign key (event_id) references  event(id)
);
create table comment(
    id serial primary key ,
    text text,
    user_id int,
    event_id int,
    date timestamp,
    likes int default 0,
    foreign key (user_id) references users(id),
    foreign key (event_id) references event(id)
);
INSERT INTO event (title, description, location, time, seatCount, category_id)
VALUES ('Lecture on Quantum Physics', 'A lecture by Professor Ivanov.', 'Room 101', '2023-09-25 10:00:00', 100, 1);
INSERT INTO event (title, description, location, time, seatCount, category_id)
VALUES ('Programming Seminar', 'An interactive seminar for students.', 'Room 202', '2023-10-05 13:00:00', 50, 2);
INSERT INTO event (title, description, location, time, seatCount, category_id)
VALUES ('Scientific Projects Exhibition', 'An exhibition of graduation projects by students.',
        'University Exhibition Hall', '2023-11-15 14:00:00', 200, 3);
INSERT INTO event (title, description, location, time, seatCount, category_id)
VALUES ('Photography Workshop', 'Learn the basics of photography.', 'Room 303', '2023-12-10 11:00:00', 30, 4);
INSERT INTO event (title, description, location, time, seatCount, category_id)
VALUES ('Startup Competition', 'Presentation of students startup projects.', 'Conference Hall', '2023-12-20 09:00:00',
        150, 5);
drop table registration;
drop table event;
create table event
(
    id          serial primary key,
    title       varchar(100),
    description text,
    location    varchar(255),
    time        timestamp,
    seat_count   int,
    category_id int,
    created_by int,
    foreign key (category_id) references category (id),
    foreign key (created_by) references users(id)
);
create table registration
(
    id                serial primary key,
    user_id           int,
    event_id          int,
    registration_time timestamp default current_timestamp,
    status            varchar(100),
    comment text,
    foreign key (user_id) references users(id),
    foreign key (event_id) references  event(id)
);
create table comment(
    id serial primary key ,
    text text,
    user_id int,
    event_id int,
    date timestamp default current_timestamp,
    likes int default 0,
    foreign key (user_id) references users(id),
    foreign key (event_id) references event(id)
);
INSERT INTO event (title, description, location, time, seat_count, category_id)
VALUES ('Lecture on Quantum Physics', 'A lecture by Professor Ivanov.', 'Room 101', '2023-09-25 10:00:00', 100, 1);
INSERT INTO event (title, description, location, time, seat_count, category_id)
VALUES ('Scientific Projects Exhibition', 'An exhibition of graduation projects by students.',
        'University Exhibition Hall', '2023-11-15 14:00:00', 200, 3);
INSERT INTO event (title, description, location, time, seat_count, category_id)
VALUES ('Photography Workshop', 'Learn the basics of photography.', 'Room 303', '2023-12-10 11:00:00', 30, 4);
INSERT INTO event (title, description, location, time, seat_count, category_id)
VALUES ('Startup Competition', 'Presentation of students startup projects.', 'Conference Hall', '2023-12-20 09:00:00',
        150, 5);
alter table users drop column group_number;
alter table comment rename column  date to time;
truncate table event;
CREATE TABLE likes
(
    id         serial PRIMARY KEY,
    user_id    int NOT NULL,
    forum_id   int,
    comment_id int,
    created_at timestamp DEFAULT current_timestamp,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (forum_id) REFERENCES forums (id),
    FOREIGN KEY (comment_id) REFERENCES comment (id),
    CONSTRAINT unique_like UNIQUE (user_id, forum_id, comment_id),
    CONSTRAINT check_likes CHECK (
        (forum_id IS NOT NULL AND comment_id IS NULL) OR
        (forum_id IS NULL AND comment_id IS NOT NULL)
        )
);
drop table likes;
drop table comment;
create table comment
(
    id       serial primary key,
    text     text,
    user_id  int,
    event_id int,
    time     timestamp default current_timestamp,
    is_deleted boolean default false,
    foreign key (user_id) references users (id),
    foreign key (event_id) references event (id)
);
ALTER TABLE users
    ADD COLUMN about varchar(255) DEFAULT 'Привет, я пользуюсь сайтом для мероприятий университета';
update users set about = 'Hi, I use the website for university events';
update event set created_by =3;
alter table users alter column about varchar(255) default 'Hi, I use the website for university events';
alter table users alter column about set default 'Hi, I use the website for university events';
delete from registration where event_id==1 and user_id==9;
delete from registration where event_id=1 and user_id=9;
delete from registration where event_id=9 and user_id=1;
delete from registration where event_id=6 and user_id=2;
delete from event;
ALTER SEQUENCE event_id_seq RESTART WITH 1;
INSERT INTO event (title, description, location, time, seat_count, category_id)
VALUES ('Lecture on Quantum Physics', 'A lecture by Professor Ivanov.', 'Room 101', '2025-09-25 10:00:00', 100, 1);
INSERT INTO event (title, description, location, time, seat_count, category_id)
VALUES ('Programming Seminar', 'An interactive seminar for students.', 'Room 202', '2023-10-05 13:00:00', 50, 2);
INSERT INTO event (title, description, location, time, seat_count, category_id)
VALUES ('Scientific Projects Exhibition', 'An exhibition of graduation projects by students.',
        'University Exhibition Hall', '2025-01-15 14:00:00', 200, 3);
INSERT INTO event (title, description, location, time, seat_count, category_id)
VALUES ('Photography Workshop', 'Learn the basics of photography.', 'Room 303', '2024-12-10 11:00:00', 30, 4);
INSERT INTO event (title, description, location, time, seat_count, category_id)
VALUES ('Startup Competition', 'Presentation of students startup projects.', 'Conference Hall', '2024-12-20 09:00:00',
        150, 5);
insert into event (created_by) values (3);
delete from  event where id = 6;
update  event set  created_by = 3;
update users set password = 'ce4344d1e6215367670df3dfeb157b2c22f09f135a1cc20604907833e19db447' where id = 1;
update users set password = '9ce20be011ee64c56da3b137d3831a978ec0218a6702bdc0e8889ca8727717c3' where id = 3;
delete from category where id>5;
alter table event
    add image_path varchar(255);
update event set image_path = '1.jpg' where id = 1;
update event set image_path = '2.jpg' where id = 2;
update event set image_path = '3.jpg' where id = 3;
update event set image_path = '4.jpg' where id = 4;
update event set image_path = '5.jpg' where id = 5;
alter table topic
    add column description text not null,
    add column created_at  timestamp default current_timestamp,
    add column updated_at  timestamp default current_timestamp,
    add column created_by  int  not null references users (id);
alter table forums add column is_pinned boolean   default false;
alter table forums drop column likes;
update event set category_id = 0;
update event set category_id = null;
delete from category;
ALTER SEQUENCE category_id_seq RESTART WITH 1;
INSERT INTO category (name)
VALUES ('Lecture');
INSERT INTO category (name)
VALUES ('Seminar');
INSERT INTO category (name)
VALUES ('Exhibition');
INSERT INTO category (name)
VALUES ('Workshop');
INSERT INTO category (name)
VALUES ('Competition');
INSERT INTO category (name)
VALUES ('Conference');
INSERT INTO category (name)
VALUES ('Webinar');
INSERT INTO category (name)
VALUES ('Masterclass');
INSERT INTO category (name)
VALUES ('Hackathon');
INSERT INTO category (name)
VALUES ('Symposium');
update event set category_id = 1 where  id = 1;
update event set category_id = 2 where  id = 2;
update event set category_id = 3 where  id = 3;
update event set category_id = 4 where  id = 4;
update event set category_id = 5 where  id = 5;
update event set created_by = 3 where  id = 7;
delete from event where id>5;
ALTER SEQUENCE event_id_seq RESTART WITH 6;
delete from registration;
alter sequence registration_id_seq restart with 1;
delete from comment;
delete from forums;
ALTER SEQUENCE comment_id_seq RESTART WITH 1;
ALTER SEQUENCE forums_id_seq RESTART WITH 1;
update topic set updated_at = created_at where id=1;
create table bonus_points(
    id serial primary key ,
    event_id int not null references event(id),
    user_id int not null references users(id),
    points int not null ,
    date timestamp default current_timestamp,
    expiration_date timestamp not null
);
INSERT INTO registration (user_id, event_id, registration_time, status, comment)
VALUES (1, 2, '2023-10-01 10:00:00', 'Registered', 'Looking forward to the seminar!'),
       (2, 2, '2023-10-02 11:00:00', 'Registered', 'Excited to learn!'),
       (5, 2, '2023-10-03 12:00:00', 'Registered', 'Can not wait to participate!'),
       (6, 2, '2023-10-04 09:30:00', 'Registered', 'Ready for the seminar!'),
       (7, 2, '2023-10-04 15:00:00', 'Registered', 'Interested in the topics covered!'),
       (9, 2, '2023-10-04 08:00:00', 'Registered', 'Hope to gain new insights!'),
       (10, 2, '2023-10-04 09:00:00', 'Registered', 'Looking forward to networking!'),
       (11, 2, '2023-10-04 10:00:00', 'Registered', 'Eager to share my thoughts!');
update registration set status = 'REGISTERED';
delete from registration where user_id=11;
alter sequence registration_id_seq restart with 10;
create table topic_participants(
    id serial primary key ,
    user_id int not null references users(id) on delete cascade ,
    topic_id int not null references topic(id) on delete cascade ,
    joined_at timestamp default current_timestamp
);
alter table forums rename column topic to topic_id;
INSERT INTO topic_participants (user_id, topic_id, joined_at)
values (13, 2, '2024-12-18 11:43:45.040900'),
       (13, 3, '2024-12-18 11:47:11.214048'),
       (12, 2, '2024-12-18 11:47:40.884231'),
       (12, 2, '2024-12-18 11:41:28.678762');
update topic_participants set topic_id=3 where id=3;
INSERT INTO registration (user_id, event_id, registration_time, status, comment)
VALUES (1, 5, '2023-12-01 10:00:00', 'REGISTERED', 'Can not wait to participate!'),
       (2, 5, '2023-12-02 11:00:00', 'REGISTERED', 'Ready for the seminar!'),
       (5, 5, '2023-12-03 12:00:00', 'REGISTERED', 'Looking forward to the seminar!'),
       (6, 5, '2023-12-04 09:30:00', 'REGISTERED', 'Excited to learn!'),
       (7, 5, '2023-12-04 15:00:00', 'REGISTERED', 'Interested in the topics covered!'),
       (9, 5, '2023-12-04 08:00:00', 'REGISTERED', 'Hope to gain new insights!');