CREATE TABLE IF NOT EXISTS usuario (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100),
    email VARCHAR(100),
    senha VARCHAR(100),
    acess VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS pacientes (
    paciente_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    paciente_name VARCHAR(100),
    idade INT,
    data_nascimento DATE,
    sexo VARCHAR(10),
    cns VARCHAR(20),
    cpf VARCHAR(20),
    rg VARCHAR(20),
    cep VARCHAR(10),
    endereco VARCHAR(100),
    complemento VARCHAR(100),
    user_id INT
);

CREATE TABLE IF NOT EXISTS symptons (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    transtorno VARCHAR(100),
    cid VARCHAR(20),
    sintomas_biologicos VARCHAR(255),
    consequencias_sociais VARCHAR(255),
    caracteristicas VARCHAR(255)
);