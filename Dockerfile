FROM eclipse-temurin:21-jre-jammy

#LABEL org.opencontainers.image.source=https://github.com/nn-sj-park/micro
LABEL authors="sj"

WORKDIR /app

COPY twitter-service/build/libs/*.jar twitter-to-kafka-service.jar

#ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -Xms128m -Xmx256m"

EXPOSE 5005

ENTRYPOINT exec java $JAVA_OPTS -jar twitter-to-kafka-service.jar

#./gradlew :twitter-service:build
#docker build -t io.pg.demo/twitter.to.kafka.service:latest .