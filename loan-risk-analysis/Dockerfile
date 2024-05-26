FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build

COPY loan-risk-analysis/pom.xml .
COPY loan-risk-analysis/src ./src

RUN mvn dependency:go-offline

RUN mvn clean package spring-boot:repackage -DskipTests

FROM amazoncorretto:17
WORKDIR /app

ENV ACTIVE_PROFILE=dev
ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}

COPY --from=build /build/target/*.jar /app/app.jar

EXPOSE 8080

CMD ["sh", "-c", "java -jar \
  -Dspring.profiles.active=$ACTIVE_PROFILE \
  -Dspring.datasource.url=$DB_URL \
  -Dspring.datasource.username=$DB_USERNAME \
  -Dspring.datasource.password=$DB_PASSWORD \
  app.jar"]
