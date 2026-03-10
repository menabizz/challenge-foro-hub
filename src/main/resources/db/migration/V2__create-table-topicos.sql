CREATE TABLE topicos (
                         id SERIAL PRIMARY KEY,
                         titulo VARCHAR(255) NOT NULL,
                         mensaje TEXT NOT NULL,
                         fecha TIMESTAMP NOT NULL,
                         status VARCHAR(20) NOT NULL,
                         author_id BIGINT,
                         curso VARCHAR(255) NOT NULL,
                         active BOOLEAN NOT NULL,
                         FOREIGN KEY (author_id) REFERENCES usuarios(id)
);