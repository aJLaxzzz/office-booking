FROM gradle:latest AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM openjdk:21
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]