
insert into product(name, description, price) values ('3d printer', 'A beginner friendly 3d printer', 1840.25);
insert into product(name, description, price) values ('PC computer', 'A gaming laptop', 2300.01);
insert into product(name, description, price) values ('Mobile phone', 'A 5g smartphone', 850.0);
insert into product(name, description, price) values ('Scanner', 'A WIFI scanner', 260.45);
insert into product(name, description, price) values ('Mouse', 'An optical mouse', 65.99);
insert into product(name, description, price) values ('Keyboard', 'A simple keyboard', 105.99);

-- Lab account: usera / pwd123
INSERT INTO app_user (username, password) VALUES ('usera', '$2b$12$HcALfiaKCi1WtWTELPfKWOiE49l.w70PE65qms7XRVrxBGbsAolRe') ON DUPLICATE KEY UPDATE id = id;
-- Lab account: userb / pwd456
INSERT INTO app_user (username, password) VALUES ('userb', '$2b$12$5BARW3rFSamusKyiGRCgPemHI2sRlDw2k7uZ5VqmfF36Q/.dNljom') ON DUPLICATE KEY UPDATE id = id;
