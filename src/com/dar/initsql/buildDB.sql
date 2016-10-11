-- Dans ce fichier, nous créons toutes les bases de données. Nous n'en avons besoin que d'une fois (normalement !)
-- Postgres data types : https://www.postgresql.org/docs/9.2/static/datatype.html
CREATE TABLE User
(
    id_user serial not null, -- serial : autoincrementing four-byte integer
    login VARCHAR(100),
    password CHAR(32), --MD5 encryption, see : http://stackoverflow.com/questions/247304/what-data-type-to-use-for-hashed-password-field-and-what-length
    firstname VARCHAR(100),
    lastname VARCHAR(100),
    birthday DATE,
    country VARCHAR(100),
    email VARCHAR(255),
    picture bytea, -- see : https://www.postgresql.org/docs/7.3/static/jdbc-binary-data.html
    description text,
    PRIMARY KEY(id_user)
)

CREATE TABLE Event
(
    id_event serial not null,
    name VARCHAR(100),
    url VARCHAR(512),
    location VARCHAR(100),
    date DATE,
    picture bytea,
    description text,
    PRIMARY KEY(id_event)
)

CREATE TABLE Travel
(
    id_travel serial not null,
    name VARCHAR(100), 
    picture bytea, 
    description text,
    PRIMARY KEY (id_travel)
)

CREATE TABLE Category
(
    id_category serial not null,
    name VARCHAR(100),
    picture bytea,
    description text,
    PRIMARY KEY (id_category)
)

CREATE TABLE Vote
(
    id_user integer, -- integer : signed four-byte integer
    id_event integer,
    id_travel integer,
    is_like boolean,
    FOREIGN KEY (id_user) REFERENCES User(id_user),
    FOREIGN KEY (id_event) REFERENCES Event(id_event),
    FOREIGN KEY (id_travel) REFERENCES Travel(id_travel)
)

CREATE TABLE Message
(
    id_user integer,
    id_travel integer,
    posted DATE,
    description text,
    FOREIGN KEY (id_user) REFERENCES User(id_user),
    FOREIGN KEY (id_travel) REFERENCES Travel(id_travel)
)

CREATE TABLE Involded
(
    id_user integer,
    id_travel integer,
    is_admin boolean,
    FOREIGN KEY (id_user) REFERENCES User(id_user),
    FOREIGN KEY (id_travel) REFERENCES Travel(id_travel)
)

CREATE TABLE Tagged
(
    id_event integer,
    id_category integer,
    FOREIGN KEY (id_event) REFERENCES Event(id_event),
    FOREIGN KEY (id_category) REFERENCES Travel(id_category)
)