# Build stage
FROM openjdk:21-jdk AS builder
WORKDIR /app

# Alpine Linux의 패키지 매니저 사용
RUN microdnf install findutils

COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar

# Run stage
FROM openjdk:21-jdk
WORKDIR /app

# 타임존 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 기본 환경변수 설정
ENV SPRING_PROFILES_ACTIVE=prod \
    SERVER_PORT=8080

# 런타임 환경변수 설정 (CI/CD에서 주입)
ARG DB_HOST
ARG DB_PORT
ARG DB_NAME
ARG DB_USER
ARG DB_PASSWORD
ARG APT_TRADE_SERVICE_KEY
ARG APT_LOTTO_SERVICE_KEY

ENV DB_HOST=${DB_HOST} \
    DB_PORT=${DB_PORT} \
    DB_NAME=${DB_NAME} \
    DB_USER=${DB_USER} \
    DB_PASSWORD=${DB_PASSWORD} \
    APT_TRADE_SERVICE_KEY=${APT_TRADE_SERVICE_KEY} \
    APT_LOTTO_SERVICE_KEY=${APT_LOTTO_SERVICE_KEY}

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", \
    "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", \
    "-Duser.timezone=Asia/Seoul", \
    "app.jar"]

EXPOSE 8080
