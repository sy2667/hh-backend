# 1) build stage
FROM gradle:8-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle clean bootWar -x test

# 2) runtime stage
FROM tomcat:10.1-jdk17-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=builder /app/build/libs/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
