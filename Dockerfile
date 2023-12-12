FROM openjdk:19
COPY ./target/DevOps_gp4.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "DevOps_gp4.jar", "db:3306", "30000"]