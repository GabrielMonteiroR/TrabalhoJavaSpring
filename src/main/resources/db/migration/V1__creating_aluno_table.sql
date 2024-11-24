CREATE TABLE alunos
(
    id              INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome            VARCHAR(100) NOT NULL,
    email           VARCHAR(100) NOT NULL,
    matricula       VARCHAR(20)  NOT NULL,
    data_nascimento DATE
)