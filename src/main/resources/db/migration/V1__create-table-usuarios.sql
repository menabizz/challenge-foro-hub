CREATE TABLE usuarios (
                          id SERIAL PRIMARY KEY,
                          login VARCHAR(100) NOT NULL,
                          contrasena VARCHAR(255) NOT NULL
);