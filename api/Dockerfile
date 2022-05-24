FROM openjdk:17
RUN mkdir /opt/app
COPY target/api-0.0.1-SNAPSHOT.jar /opt/app
CMD ["java", "-jar", "/opt/app/api-0.0.1-SNAPSHOT.jar"]