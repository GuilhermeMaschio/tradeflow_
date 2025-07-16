
/*
DROP TABLE consulta_externa CASCADE;
DROP TABLE log_alteracao CASCADE;
DROP TABLE parametro CASCADE;
DROP TABLE log_acesso CASCADE;
DROP TABLE usuario CASCADE;
DROP TABLE flyway_schema_history CASCADE;
DROP TABLE endereco_cep CASCADE;
*/

CREATE TABLE IF NOT EXISTS usuario (
    id SERIAL NOT NULL,
    key_cloak_id VARCHAR(100) NOT NULL,
    nome VARCHAR(200) NOT NULL,
    status VARCHAR(30) NOT NULL,
    login VARCHAR(150) NOT NULL,
    email VARCHAR(300) NOT NULL,
    roles VARCHAR(600),
    PRIMARY KEY(id),
    CONSTRAINT usuario_uk UNIQUE (login)
);
ALTER TABLE usuario ALTER COLUMN id TYPE bigint;
CREATE INDEX IF NOT EXISTS usuario_idx2 ON usuario (login);

CREATE TABLE IF NOT EXISTS log_acesso (
    id serial4 NOT NULL,
    ajax bool NULL,
    server varchar(200) NULL,
    locale varchar(7) NULL,
    method varchar(13) NULL,
    parameters varchar(10000) NULL,
    protocol varchar(10) NULL,
    remote_host varchar(30) NULL,
    scheme varchar(11) NULL,
    server_name varchar(310) NULL,
    server_port int4 NULL,
    context_path varchar(22) NULL,
    servlet_path varchar(151) NULL,
    remote_user varchar(50) NULL,
    inicio timestamp NULL,
    exception varchar(20000) NULL,
    forward varchar(101) NULL,
    redirect varchar(500) NULL,
    content_type varchar(100) NULL,
    status int4 NULL,
    fim timestamp NULL,
    tempo int8 NULL,
    user_agent varchar(301) NULL,
    content_size int4 NULL,
    content_length int4 NULL,
    headers varchar(10002) NULL,
    thread_name varchar(140) NULL,
    metadados varchar(10000) NULL,
    CONSTRAINT log_acesso_pkey PRIMARY KEY (id)
);
ALTER TABLE log_acesso ALTER COLUMN id TYPE bigint;
CREATE INDEX IF NOT EXISTS log_acesso_fkindex1 ON log_acesso USING btree (inicio);
CREATE INDEX IF NOT EXISTS log_acesso_idx2 ON log_acesso USING btree (inicio, remote_user);
CREATE INDEX IF NOT EXISTS log_acesso_idx3 ON log_acesso USING btree (remote_user, inicio);

CREATE TABLE IF NOT EXISTS parametro (
    id serial NOT NULL,
    ambiente varchar(40),
	chave varchar(50),
    valor varchar(2000),
    CONSTRAINT parametro_pk PRIMARY KEY (id),
    CONSTRAINT parametro_uk UNIQUE (ambiente, chave)
);
ALTER TABLE parametro ALTER COLUMN id TYPE bigint;

CREATE TABLE IF NOT EXISTS log_alteracao (
    id serial NOT NULL,
    usuario_id bigint,
    data timestamp without time zone NOT NULL,
    tipo_registro varchar(40) NOT NULL,
    tipo_alteracao varchar(20) NOT NULL,
    registro_id bigint NOT NULL,
    dados text,
    CONSTRAINT log_alteracao_pkey PRIMARY KEY (id),
    CONSTRAINT log_alteracao_fk1 FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);
ALTER TABLE log_alteracao ALTER COLUMN id TYPE bigint;
CREATE INDEX IF NOT EXISTS log_alteracao_idx1 ON log_alteracao (usuario_id);

CREATE TABLE IF NOT EXISTS consulta_externa (
    id serial NOT NULL,
    data timestamp without time zone NOT NULL,
    tipo varchar(30) NOT NULL,
    url varchar(300) NOT NULL,
    thread varchar(200) NOT NULL,
    parametros varchar(1000),
    resultado varchar(40000),
    status varchar(20),
    stack_trace varchar(50000),
    mensagem varchar(1000),
    tempo_execucao numeric,
    CONSTRAINT consulta_externa_pkey PRIMARY KEY (id)
);
ALTER TABLE consulta_externa ALTER COLUMN id TYPE bigint;
CREATE INDEX IF NOT EXISTS consulta_externa_idx1 ON consulta_externa (tipo, parametros);

CREATE TABLE IF NOT EXISTS endereco_cep (
    id serial NOT NULL,
    cep varchar(10) NOT NULL,
    uf varchar(2),
    cidade varchar(60),
    bairro varchar(61),
    logradouro varchar(100),
    servico varchar(50),
    localizacao varchar(200),
    CONSTRAINT endereco_cep_pkey PRIMARY KEY (id)
);
ALTER TABLE endereco_cep ALTER COLUMN id TYPE bigint;
CREATE INDEX IF NOT EXISTS endereco_cep_idx1 ON endereco_cep (cep);

insert into parametro (ambiente, chave, valor) values ('local', 'URL_CONSULTA_CEP', 'https://brasilapi.com.br/api/cep/v2/');
