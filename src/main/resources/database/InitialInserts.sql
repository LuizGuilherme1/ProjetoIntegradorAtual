

INSERT INTO usuario (nome, email, senha, acess)
VALUES ('Administrador', 'admin@admin.com', '123456', 'root');

INSERT INTO pacientes (
    paciente_name, idade, data_nascimento, sexo, cns, cpf, rg, cep,
    endereco, complemento, user_id
) VALUES (
    'João da Silva', 30, '1995-05-20', 'Masculino', '123456789000000',
    '11122233344', '11223344', '01001-000',
    'Rua Exemplo, 123', 'Apto 202', 1
);


INSERT INTO symptons (
    transtorno, cid, sintomas_biologicos, consequencias_sociais, caracteristicas
) VALUES (
    'Ansiedade Generalizada',
    'F41.1',
    'Taquicardia, suor excessivo, tensão muscular',
    'Dificuldade no trabalho, isolamento social',
    'Crises recorrentes, preocupação excessiva'
);
