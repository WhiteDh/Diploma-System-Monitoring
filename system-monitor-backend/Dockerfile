FROM gradle:8.13-jdk21 AS build

WORKDIR /app

COPY --chown=gradle:gradle build.gradle settings.gradle gradlew /app/
COPY --chown=gradle:gradle gradle /app/gradle
COPY --chown=gradle:gradle src /app/src
COPY --chown=gradle:gradle src/main/resources /app/src/main/resources


RUN gradle clean build -x test --no-daemon


FROM eclipse-temurin:21-jdk-jammy AS runtime

WORKDIR /app

COPY --from=build /app/build/libs/system-monitor-java-0.0.1-SNAPSHOT.jar /app/system-monitor.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "system-monitor.jar"]
