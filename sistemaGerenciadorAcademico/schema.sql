-- =====================================================
-- Sistema Gerenciador Academico - estrutura do banco
-- Banco de dados: PostgreSQL
-- =====================================================

-- Tabela auxiliar usada pela funcao gerar_matricula():
-- guarda o ultimo numero de matricula gerado em cada ano
CREATE TABLE contador_matricula (
    ano           INTEGER PRIMARY KEY,
    ultimo_numero INTEGER NOT NULL
);

-- Gera a matricula do aluno no formato MAT + ano + numero (ex: MAT202620000)
CREATE OR REPLACE FUNCTION gerar_matricula()
RETURNS VARCHAR
LANGUAGE plpgsql
AS $$
DECLARE
    ano_atual INTEGER;
    proximo_numero INTEGER;
BEGIN
    ano_atual := EXTRACT(YEAR FROM CURRENT_DATE);

    INSERT INTO contador_matricula (ano, ultimo_numero)
    VALUES (ano_atual, 20000)
    ON CONFLICT (ano)
    DO UPDATE
       SET ultimo_numero = contador_matricula.ultimo_numero + 1
    RETURNING ultimo_numero INTO proximo_numero;

    RETURN 'MAT'
           || ano_atual
           || LPAD(proximo_numero::TEXT, 5, '0');
END;
$$;

CREATE TABLE aluno (
    matricula VARCHAR(20)  PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    idade     INTEGER      CHECK (idade BETWEEN 1 AND 120),
    celular   VARCHAR(15)
);

CREATE TABLE professor (
    matricula VARCHAR(20)  PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    telefone  VARCHAR(15)
);

CREATE TABLE disciplina (
    codigo        VARCHAR(20)  PRIMARY KEY,
    nome          VARCHAR(100) NOT NULL,
    carga_horaria INTEGER      NOT NULL CHECK (carga_horaria > 0)
);

CREATE TABLE matricula (
    id                INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    data_matricula    DATE        NOT NULL DEFAULT CURRENT_DATE,
    aluno_matricula   VARCHAR(20) NOT NULL REFERENCES aluno (matricula),
    disciplina_codigo VARCHAR(20) NOT NULL REFERENCES disciplina (codigo),
    CONSTRAINT uk_matricula_aluno_disciplina UNIQUE (aluno_matricula, disciplina_codigo)
);