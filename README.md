# doti
gamification-doti

# Requirement:
using JDK 17
maven project using auto import maven

# Deployment
$ mvn install -> build {name}.jar in folder target
- in folder location target/ .jar
- Deploy yml inside using command:
- **java -jar {name}.jar**
- Deploy with external jar using command:
- **java -jar app.jar --spring.config.location=file:{location external yml}**
