insert into user (email, username, name, surname, role, password) values
    ('mk@fer.hr', 'mk', 'Mario', 'Kušek', 'ROLE_ADMIN', '$2y$12$zWSWt/JT0riS22DM4M/Lfe.j9SMInEZ.3E2CTZ9iFzj.heNjfVOcC'); -- password: samosila
-- ako je korisnik osto zapamcen na symbiote-u odkomentirat ovo i kroz postman obrisat, javite se ak ne kuzite
--    ('vk@fer.hr', 'veki', 'Vedran', 'Kolka', 'ROLE_USER', 'popopo');
--    ('gk@fer.hr', 'gudi', 'Gudi', 'King', 'ROLE_USER', 'kkmsmdfjkmsd');

insert into user_request (email, username, name, surname, role, comment, password, request_time) values
    ('aa@gmail.com', 'ana', 'Ana', 'Anić', 'ROLE_USER', 'Ja bih trebala palit lampe, hvala.', 'ana', '2019-12-24T22:44:24.583088700');

insert into room (room_name) values
    ('C08-18'), ('C08-17'), ('C08-16');

insert into resource (resource_name, internal_id, room_room_name) values
    ('brava1', 'brava1', 'C08-18'), ('brava2', 'brava2', 'C08-17'), ('brava3', 'brava3', 'C08-16'), ('lampa1', 'lampa1', 'C08-18');

--insert into user_resources (user_email, resources_resource_name) values
--    ('mk@fer.hr', 'brava1');