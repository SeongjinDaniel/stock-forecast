# Build stage
FROM container-registry.oracle.com/java/openjdk:21-jdk AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar

# Run stage
FROM container-registry.oracle.com/java/openjdk:21
WORKDIR /app

# 타임존 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 애플리케이션 실행을 위한 사용자 생성
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# 빌드된 jar 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", \
    "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:prod}", \
    "-Duser.timezone=Asia/Seoul", \
    "app.jar"]

EXPOSE 8080
