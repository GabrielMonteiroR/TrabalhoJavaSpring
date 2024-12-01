CREATE TABLE notas (
                       id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       matricula_id INT NOT NULL,
                       disciplina_id INT NOT NULL,
                       nota DECIMAL(5, 2) NOT NULL,
                       data_lancamento DATE NOT NULL,
                       FOREIGN KEY (matricula_id) REFERENCES matriculas(id),
                       FOREIGN KEY (disciplina_id) REFERENCES disciplinas(id)
);