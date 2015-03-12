QA Session
===============

TODO
===============
Update the frontend with new security/REST API

Instruction
===============

Local Build

Git clone the repo, and perform following
`mvn clean package - DskipTests`

One will need to have a postgresql running with the schema at
https://raw.githubusercontent.com/danilko/qasession/master/sql_scripts/postgresql_ods.sql

Local Test with Docker

`export CONTAINER=$(docker run -p 127.0.0.1:5432:5432 -d stackbrew/postgres:latest)
export CONTAINER_IP=$(docker inspect $CONTAINER | grep IPAddress | awk '{ print $2 }' | tr -d ',"')
export PGPASSWORD=postgres

sleep 30;
psql -h 127.0.0.1 -U postgres < postgresql_ods.sql

export JDBC_DRIVERCLASSNAME=org.postgresql.Driver
export JDBC_MAXCONNECTIONPERPARTITION=2
export JDBC_MINCONNECTIONPERPARTITION=2
export JDBC_USERNAME=postgres
export JDBC_PASSWORD=postgres
export jdbc:postgresql://127.0.0.1:5432/postgres
export JDBC_URL=jdbc:postgresql://127.0.0.1:5432/postgres
export OAUTH_API_CLIENT_ID=<FB_OAUTH_CLIENT_ID>
export OAUTH_API_CLIENT_SECRET=<FB_OAUTH_CLIENT_SECRET>
export OAUTH_API_SCOPES=email,public_profile
export OAUTH_AUTH_URI=https://graph.facebook.com
export OAUTH_REDUCT_URI=http://localhost:8080/controller

mvn clean tomcat:run - DskipTests
`

