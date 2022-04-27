FROM openjdk:16-alpine

EXPOSE 8080

ADD target/javadiplom-0.0.1-SNAPSHOT.jar javadiplom.jar

ENTRYPOINT ["java","-jar","/javadiplom.jar"]