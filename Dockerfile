# 1) build stage
FROM gradle:8-jdk17 AS builder
WORKDIR /app

# 메모리/병렬 제한 (2GB 안전)
ENV GRADLE_OPTS="-Dorg.gradle.jvmargs=-Xmx768m -XX:MaxMetaspaceSize=256m -Dfile.encoding=UTF-8"
COPY . .
RUN gradle --no-daemon --max-workers=1 clean bootWar -x test

# 2) runtime stage
FROM tomcat:10.1-jdk17-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=builder /app/build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

# Tomcat 메모리 제한 (중요)
ENV JAVA_OPTS="-Xms256m -Xmx512m"
EXPOSE 8080
