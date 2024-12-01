CREATE TABLE cursos (
                        id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        nome VARCHAR(100) NOT NULL,
                        codigo VARCHAR(20) NOT NULL UNIQUE,
                        carga_horaria INT NOT NULL
);