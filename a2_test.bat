rem A2 batch test
echo A2 batch test
echo username:%USERNAME%
echo CD

start "mysql" cmd /k mysql_start
sleep 5
start "wildfly" cmd /k standalone -c standalone-full.xml
sleep 10

cd stats-client
call mvn clean package -q
rem java -cp target/stats-client.jar ec.stats.db.StatsDBDrop
rem java -cp target/stats-client.jar ec.stats.db.StatsDBCreate
call java -cp target/stats-client.jar ec.stats.db.StatsDBDelete -table ecuser -name admin
call java -cp target/stats-client.jar ec.stats.db.StatsDBInsert -table ecuser -name admin -password cp630 -role 1
call java -cp target/stats-client.jar ec.stats.db.StatsDBSelect -table ecuser -name admin
call java -cp target/stats-client.jar ec.stats.db.StatsDBInsert -table ecmodel -name stats -modelfile C:/enterprise/tmp/model/stats.bin
call java -cp target/stats-client.jar ec.stats.db.StatsDBSelect -table ecmodel -name stats
call java -cp target/stats-client.jar ec.stats.db.StatsDBDelete -table ecmodel -name stats

cd ../stats-app
call mvn clean install -q
call mvn wildfly:undeploy -q
call mvn wildfly:deploy  -q

cd ../stats-client
call mvn clean package -q
call java -cp target/stats-client.jar ec.stats.jms.StatsJMSProducer save
call java -cp target/stats-client.jar ec.stats.jms.StatsJMSPublisher 10

call curl http://localhost:8080/stats-web/producer?message=save
call curl http://localhost:8080/stats-web/publisher?message=10
call curl http://localhost:8080/stats-web/sbproducer?message=save
call curl http://localhost:8080/stats-web/sbpublisher?message=10

rem call curl -X GET http://localhost:8080/stats-web/login -d "username=admin&password=cp630"
call java -cp target/stats-client.jar ec.stats.db.StatsDBDelete -table ecmodel -name mystats
call curl -X GET http://localhost:8080/stats-web/modelsave -d "modelname=mystats"
call curl -X GET http://localhost:8080/stats-web/modelget -d "modelname=mystats&query=count"
call curl -X GET http://localhost:8080/stats-web/modelget -d "modelname=mystats&query=mean" 

cd ../stats-app
call mvn wildfly:undeploy -q
call mvn clean -q

cd stats-client
call mvn clean -q
cd ..

taskkill /FI "WindowTitle eq mysql*"  /T /F >null
taskkill /FI "WindowTitle eq wildfly*"  /T /F >null

echo END

