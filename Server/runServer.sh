javac -sourcepath ./src -d bin src/server/csv/*.java
javac -sourcepath ./src -d bin -classpath ./bin:lib/gson-2.8.5.jar:lib/javax.mail.jar src/server/russel/*.java
java -classpath ./bin:./lib/gson-2.8.5.jar:lib/javax.mail.jar:lib/postgresql-42.2.5.jar server.russel.Server 8000
