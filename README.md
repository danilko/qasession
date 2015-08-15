Code License
===============
By using the software in any shape or form, you agree with following license

The MIT License

Copyright (c) Kai-Ting (Danil) Ko

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.


QA Session
===============
Allows user to easily create an organized question and answer session.
Purposed by Kent Huang

Build Instruction
===============
Git clone the repo, and perform following
`mvn clean package - DskipTests`

One will need to have a postgresql running with the schema at
https://raw.githubusercontent.com/danilko/qasession/master/sql_scripts/postgresql_ods.sql


Local Test with Docker PSQL Instruction
===============

Please generate a new app for website at Facebook API Website:
https://developers.facebook.com/

Under Facebook App Settings, please go to Settings
For "basic" section, website url should be (assume it is controller.war)
```
http://<HOST>:<HOST_PORT>/controller/OAuthConsumerResponseCode
```
Example
```
http://localhost:8080/controller/OAuthConsumerResponseCode
```

For "advanced" section, redirect uri should be 
```
http://<HOST>:<HOST_PORT>/controller/OAuthConsumerResponseCode
```
Example
```
http://localhost:8080/controller/OAuthConsumerResponseCode
```

Please note the current support Facebook Graph API is minumum
```
v2.4 
```

Please download docker and other require tools
JDK 7 or Above 
http://www.oracle.com/technetwork/java/javase/downloads/index.html

Maven 
http://maven.apache.org/

Docker 
https://www.docker.com/

PSQL Client Tools 
https://wiki.postgresql.org/wiki/Community_Guide_to_PostgreSQL_GUI_Tools

LC_ALL Detail Descriptions
http://www.jvmhost.com/articles/locale-breaks-unicode-utf-8-java-tomcat

```
export CONTAINER=$(docker run -p 127.0.0.1:5432:5432 -d stackbrew/postgres:latest);
export CONTAINER_IP=$(docker inspect $CONTAINER | grep IPAddress | awk '{ print $2 }' | tr -d ',"');

sleep 30;

docker exec -i postgresql bash -c 'cat > /tmp/file' < sql_scripts/postgresql_ods.sql;
docker exec -i postgresql bash -c 'export PGPASSWORD=postgres; psql -U postgres < /tmp/file';


export JDBC_DRIVERCLASSNAME="org.postgresql.Driver"
export JDBC_MAXCONNECTIONPERPARTITION="2"
export JDBC_MINCONNECTIONPERPARTITION="2"
export JDBC_USERNAME="postgres"
export JDBC_PASSWORD="postgres"
export JDBC_URL="jdbc:postgresql://127.0.0.1:5432/postgres"
export OAUTH_API_CLIENT_ID="<FB_OAUTH_CLIENT_ID>"
export LC_ALL="en_US.UTF-8"
export OAUTH_API_CLIENT_SECRET=<FB_OAUTH_CLIENT_SECRET>
export OAUTH_API_SCOPES="email,public_profile"
export OAUTH_AUTH_URI="https://graph.facebook.com"
export OAUTH_REDUCT_URI="http://localhost:8080/controller/OAuthConsumerResponseCode"

// Pass in FB Profile ID to allow admin access across application level wide
export APP_ADMIN_OAUTH_IDENTITIY_ID_LIST="<FB_IDENTITY_ID_1>,<FB_IDENTITY_ID_2>"

mvn clean tomcat:run - DskipTests
```

Swagger API Documentation
===============

API documentation can be found at
```
http://<hostname>/controller/api
```

