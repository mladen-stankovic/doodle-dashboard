#
# Maven
#
FROM maven:3.6.3-openjdk-14 AS build

#
# Setup folders
#
RUN mkdir -p /home/doodle-dashboard
WORKDIR /usr/src/doodle-dashboard

#
# Copy app source code
#
COPY pom.xml /usr/src/doodle-dashboard
COPY src /usr/src/doodle-dashboard/src

#
# Resolve app dependencies
#
RUN mvn dependency:resolve-plugins dependency:go-offline || true

#
# Package
#
RUN mvn -f /usr/src/doodle-dashboard/pom.xml clean package -DskipTests -Dasciidoctor-skip-doc=true

#
# Run
#
FROM openjdk:14-slim
COPY --from=build /usr/src/doodle-dashboard/target/doodle-dashboard-1.0.0-SNAPSHOT.jar /home/doodle-dashboard/doodle-dashboard-1.0.0-SNAPSHOT.jar
CMD java -jar /home/doodle-dashboard/doodle-dashboard-1.0.0-SNAPSHOT.jar