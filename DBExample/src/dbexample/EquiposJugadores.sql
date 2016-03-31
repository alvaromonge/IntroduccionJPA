
-- eliminar todos los datos
ALTER TABLE equipos DROP CONSTRAINT equipos_entrenador_fk;
DROP TABLE personas;
DROP TABLE equipos;

-- crear la tabla que tiene los equipos
CREATE TABLE equipos
(
    id          INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
    nombre      VARCHAR(50) NOT NULL,
    ciudad      VARCHAR(100) NOT NULL,
    entrenador  INTEGER,
    CONSTRAINT equipos_pk PRIMARY KEY(id),
    CONSTRAINT unico_entrenador UNIQUE(entrenador)
);

-- crear la tabla que mantiene a las personas (jugadores, entrenadores)
CREATE TABLE personas
(
    id                  INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
    nombre              VARCHAR(50) NOT NULL,
    apellido            VARCHAR(50) NOT NULL,
    fecha_de_nacimiento DATE,
    equipo              INTEGER,
    CONSTRAINT personas_pk PRIMARY KEY(id),
    CONSTRAINT personas_equipo_fk FOREIGN KEY(equipo) REFERENCES equipos(id)
);

-- incluir la foreign key en el equipo a las persona quien es el entrenador
ALTER TABLE equipos ADD 
    CONSTRAINT equipos_entrenador_fk FOREIGN KEY(entrenador) REFERENCES personas(id);


-- insertar los entrenadores... tienen null el la columna equipo
INSERT INTO personas(nombre, apellido) VALUES
    ('Steve', 'Kerr'),
    ('Erik', 'Spoelstra'),
    ('Tyronn', 'Lue'),
    ('Byron', 'Scott');

-- equipos...
INSERT INTO equipos(nombre, ciudad, entrenador) VALUES
    ('Golden State Warriors', 'Oakland', 1),
    ('Heat', 'Miami', 2),
    ('Lakers', 'Los Angeles', 4),
    ('Cavaliers', 'Cleveland', 3);

-- insertar los jugadores... asociandolos con la id del equipo en el que juegan
INSERT INTO personas(nombre, apellido, equipo) VALUES
    ('Stephen', 'Curry', 1),
    ('Leandro', 'Barbosa', 1),
    ('Dwyane','Wade', 2),
    ('Chris','Bosh', 2),
    ('Udonis','Haslem', 2),
    ('LeBrown','James', 4),
    ('Kevin','Love', 4),
    ('Kyrie','Irving', 4);
 
