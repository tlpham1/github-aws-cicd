FROM openjdk:19-oracle
COPY target/*.jar TaxExercise-0.0.1-SNAPSHOT.jar 
EXPOSE 8080
ENTRYPOINT ["java","-jar","/TaxExercise-0.0.1-SNAPSHOT.jar"]