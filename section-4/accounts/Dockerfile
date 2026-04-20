# START WITH BASE IMAGE CONTAINING JAVA RUNTIME
FROM eclipse-temurin:17-jre-jammy

# Information Who is Maintaining
LABEL maintainer="Aman Rawat"

# Add the application jar to the image
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

# Execute the Application
ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]