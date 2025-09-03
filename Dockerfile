# === BUILD STAGE ===
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests -Pdocker

# === WILDFLY STAGE ===
FROM quay.io/wildfly/wildfly:37.0.0.Final-jdk21
WORKDIR /opt/jboss/wildfly

ENV JBOSS_HOME=/opt/jboss/wildfly
ENV PATH=$JBOSS_HOME/bin:$PATH

USER root
# PostgreSQL modul létrehozása
RUN mkdir -p $JBOSS_HOME/modules/org/postgresql/main \
    && mkdir -p $JBOSS_HOME/standalone/deployments \
    && chown -R jboss:jboss $JBOSS_HOME/modules \
    && chown -R jboss:jboss $JBOSS_HOME/standalone/deployments

COPY wildfly/module.xml $JBOSS_HOME/modules/org/postgresql/main/module.xml
RUN curl -L https://repo1.maven.org/maven2/org/postgresql/postgresql/42.6.0/postgresql-42.6.0.jar \
    -o $JBOSS_HOME/modules/org/postgresql/main/postgresql.jar \
    && chown jboss:jboss $JBOSS_HOME/modules/org/postgresql/main/postgresql.jar

# WAR deploy
COPY --from=build /app/target/survey-hw-0.0.1-SNAPSHOT.war $JBOSS_HOME/standalone/deployments/ROOT.war
RUN chown jboss:jboss $JBOSS_HOME/standalone/deployments/ROOT.war

# CLI script a driver és datasource regisztrálására
COPY wildfly/install-postgres.cli /opt/jboss/wildfly/bin/install-postgres.cli
RUN chown jboss:jboss /opt/jboss/wildfly/bin/install-postgres.cli
USER jboss

# Futassuk le a CLI scriptet
RUN $JBOSS_HOME/bin/jboss-cli.sh --file=$JBOSS_HOME/bin/install-postgres.cli

EXPOSE 8080
CMD ["bin/standalone.sh", "-b", "0.0.0.0"]
