﻿# moneybox-auditlog


git clone -b master https://github.com/donkhan/moneybox-auditlog.git
cd moneybox-auditlog
mvn clean package
install mongo db https://docs.mongodb.com/manual/tutorial/install-mongodb-on-windows/
run mongo db. mongod -dbpath dbfolder
java -jjar target/auditlog-0.0.1-SNAPSHOT.jar
