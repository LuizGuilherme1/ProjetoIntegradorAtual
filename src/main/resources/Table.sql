

CREATE TABLE IF NOT EXISTS usuario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    senha TEXT NOT NULL,
    acess TEXT
);

CREATE TABLE IF NOT EXISTS pacientes (
    paciente_id INTEGER PRIMARY KEY AUTOINCREMENT,
    paciente_name TEXT NOT NULL,
    idade INTEGER,
    data_nascimento TEXT,       
    sexo TEXT,
    cns TEXT,
    cpf TEXT,
    rg TEXT,
    cep TEXT,
    endereco TEXT,
    complemento TEXT,
    user_id INTEGER,
    FOREIGN KEY(user_id) REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS symptons (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    transtorno TEXT,
    cid TEXT,
    sintomas_biologicos TEXT,
    consequencias_sociais TEXT,
    caracteristicas TEXT
);