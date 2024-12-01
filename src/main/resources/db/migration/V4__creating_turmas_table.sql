CREATE TABLE turmas (
                        id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        ano INT NOT NULL,
                        semestre INT NOT NULL,
                        curso_id INT NOT NULL,
                        FOREIGN KEY (curso_id) REFERENCES cursos(id)
);