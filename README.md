
# Instalar o postgres, recomendado o 16

## Criar os bancos necessários

    CREATE DATABASE tradeflow;
    CREATE USER tradeflow WITH PASSWORD 'tradeflow';
    GRANT ALL PRIVILEGES ON DATABASE tradeflow TO tradeflow;
    
    CREATE DATABASE keycloak;
    CREATE USER keycloak WITH PASSWORD 'keycloak';
    GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak;

## Permitir Conexões do Docker ao PostgreSQL

Verifique o arquivo postgresql.conf para configurar o PostgreSQL para ouvir em todas as interfaces de rede:]

    listen_addresses = '*'

E no arquivo pg_hba.conf, adicione uma linha para permitir conexões do Docker:

    host    all             all             172.17.0.0/16          md5

## Inicie o Keyclock

Execute o seguinte comando na pasta arquivo/docker:

    docker-compose up -d

Configure o keycloak

    1. Acesse http://localhost:8081/, login admin, senha admin (conforme configurado no docker-compose.yml)
    2. Crie um realm chamado tradeflow-realm
    3. Crie um client chamado tradeflow-client, preencha http://localhost:8080/* no campo "Valid redirect URIs". Valor default para os demais campos.
    4. Crie uma role admin (Realm roles).
    5. Crie um usuário para você. Configure uma senha na aba Credentials,
    6. Atribua a rola admin ao seu usuário na aba Role Mapping, clique em Assign role, selecione "Filter by realm roles", marque a role admin e clique em Assign.
    7. No menu Client scopes, clique em profile -> Mappers > Add mapper Fom predefined mappers -> groups

Artigo de referência: https://www.baeldung.com/spring-boot-keycloak

## Configurar Kibana

    1. Rode a aplicação e acesse alguns endpoints para gerar os primeiros logs
    2. Acesso o kibana em http://localhost:5601/
    3. Crie o index: Discover > Index Patterns > Create index pattern 
        Preencha o campo "Index pattern name" com "tradeflow-*"
        clique em "Next step" 
    4. Clique novamente em "Discover" e veja os logs

## Documentação da API

    Swagger:
    http://localhost:8080/tradeflow/v3/api-docs
    
    Swagger-ui:
    http://localhost:8080/tradeflow/swagger-ui/index.html

