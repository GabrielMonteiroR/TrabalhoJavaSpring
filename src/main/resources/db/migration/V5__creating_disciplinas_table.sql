CREATE TABLE disciplinas (
                             id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             nome VARCHAR(100) NOT NULL,
                             codigo VARCHAR(20) NOT NULL UNIQUE,
                             curso_id INT NOT NULL,
                             professor_id INT NOT NULL,
                             FOREIGN KEY (curso_id) REFERENCES cursos(id),
                             FOREIGN KEY (professor_id) REFERENCES professores(id)
);