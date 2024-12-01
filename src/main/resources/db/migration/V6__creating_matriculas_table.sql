CREATE TABLE matriculas (
                            id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            aluno_id INT NOT NULL,
                            turma_id INT NOT NULL,
                            FOREIGN KEY (aluno_id) REFERENCES alunos(id),
                            FOREIGN KEY (turma_id) REFERENCES turmas(id)
);
