FROM mysql:8.0

ENV MYSQL_DATABASE=test_db

COPY init/ /docker-entrypoint-initdb.d/

EXPOSE 3306
