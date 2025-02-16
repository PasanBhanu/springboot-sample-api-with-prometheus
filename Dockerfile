FROM eclipse-temurin:21-jre-alpine

COPY target/springbootapi.jar /opt/app.jar

ENV JAVA_OPTS="-Xmx256m"

WORKDIR /opt
RUN mkdir /opt/logs

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /opt/app.jar"]
