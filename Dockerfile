FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/dependency/ ./

COPY target/*.jar servers-management.jar

HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

CMD ["java", "-jar", "servers-management.jar"]