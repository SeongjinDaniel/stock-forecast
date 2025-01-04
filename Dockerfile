# Build stage
FROM container-registry.oracle.com/java/openjdk:23-jdk as builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar

# Run stage
FROM container-registry.oracle.com/java/openjdk:23
WORKDIR /app

# 타임존 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 애플리케이션 실행을 위한 사용자 생성
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# 빌드된 jar 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 컨테이너 실행 시 실행될 명령
ENTRYPOINT ["java", "-jar", \
    "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:prod}", \
    "-Duser.timezone=Asia/Seoul", \
    "app.jar"]

# 헬스체크를 위한 포트 노출
EXPOSE 8080
