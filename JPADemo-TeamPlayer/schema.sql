CREATE TABLE EQUIPO (ID INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL, CITY VARCHAR(255), LEAGUE VARCHAR(255), nombre VARCHAR(100) NOT NULL UNIQUE, PRIMARY KEY (ID))
CREATE TABLE PERSONA (ID INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL, nombre VARCHAR(255), JERSEYNUMBER INTEGER, apellido VARCHAR(50) NOT NULL, EQUIPO_ID INTEGER, PRIMARY KEY (ID))
ALTER TABLE PERSONA ADD CONSTRAINT PERSONA_EQUIPO_ID FOREIGN KEY (EQUIPO_ID) REFERENCES EQUIPO (ID) ON DELETE CASCADE
