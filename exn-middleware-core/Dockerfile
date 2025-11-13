#Builder stage for maven
FROM docker.io/library/eclipse-temurin:11-jdk-jammy AS builder

RUN apt-get update && \
    apt-get install --no-install-recommends -y \
    maven=3.6.3-5

#Set the working directory
WORKDIR /app

#Copy application POM
COPY pom.xml .

#Download dependencies in local repo
RUN mvn dependency:go-offline

#Copy source code
COPY src ./src

#Build the app
RUN mvn clean package

#Main stage
FROM docker.io/library/eclipse-temurin:11-jre-jammy

#Set the working directory
WORKDIR /app

#Copy compiled jar from builder stage
COPY --from=builder /app/target/exn-middleware-core-0.0.1-SNAPSHOT.jar ./exn-middleware-core-0.0.1-SNAPSHOT.jar

#Copy application configuration
RUN mkdir config
COPY config/application-production.yml ./config/application.yml

#Run java app on container start
CMD ["java", "-jar", "exn-middleware-core-0.0.1-SNAPSHOT.jar"]
