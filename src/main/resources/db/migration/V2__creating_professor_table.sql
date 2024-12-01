CREATE TABLE professores (
                             id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             nome VARCHAR(100) NOT NULL,
                             email VARCHAR(100) NOT NULL UNIQUE,
                             telefone VARCHAR(15),
                             especialidade VARCHAR(100)
);