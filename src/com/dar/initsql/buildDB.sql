-- Dans ce fichier, nous créons toutes les bases de données. Nous n'en avons besoin que d'une fois (normalement !)

CREATE TABLE user
(
    id_user INT PRIMARY KEY NOT NULL,
    login VARCHAR(100),
    password VARCHAR(32), --MD5 encryption, see : http://stackoverflow.com/questions/247304/what-data-type-to-use-for-hashed-password-field-and-what-length
    firstname VARCHAR(100),
    lastname VARCHAR(100),
    birthday DATE,
    country VARCHAR(100),
    email VARCHAR(255),
    picture bytea, -- see : https://www.postgresql.org/docs/7.3/static/jdbc-binary-data.html
    description text
)

CREATE TABLE event
(
    id_event INT PRIMARY KEY NOT NULL,
    name VARCHAR(100),
    url VARCHAR(32),
    location VARCHAR(100),
    date DATE,
    picture bytea,
    description text
)

CREATE TABLE travel
(
    id_travel INT PRIMARY KEY NOT NULL,
    login VARCHAR(100),
    name VARCHAR(32), 
    picture bytea, 
    description text
)

CREATE TABLE category
(
    id_category INT PRIMARY KEY NOT NULL,
    name VARCHAR(100),
    picture bytea, -- see : https://www.postgresql.org/docs/7.3/static/jdbc-binary-data.html
    description text
)

CREATE TABLE vote
(
    id_user INT PRIMARY KEY NOT NULL,
    id_event VARCHAR(100),
    id_travel VARCHAR(32), --MD5 encryption, see : http://stackoverflow.com/questions/247304/what-data-type-to-use-for-hashed-password-field-and-what-length
    is_like VARCHAR(100)
)

CREATE TABLE message
(
    id_user INT PRIMARY KEY NOT NULL,
    id_travel VARCHAR(100),
    date DATE,
    description text
)

CREATE TABLE involded
(
    id_user INT PRIMARY KEY NOT NULL,
    id_travel VARCHAR(100),
    is_admin VARCHAR(32)
)

CREATE TABLE tagged
(
    id_event INT PRIMARY KEY NOT NULL,
    id_category VARCHAR(100)
)