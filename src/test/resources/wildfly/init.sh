#!/bin/bash
set -e

echo ">>> Preparing PostgreSQL driver..."

# Letöltés ideiglenes mappába
mkdir -p /tmp/driver
curl -L https://repo1.maven.org/maven2/org/postgresql/postgresql/42.6.0/postgresql-42.6.0.jar \
     -o /tmp/driver/postgresql.jar

# WildFly modul mappa létrehozása (root jogosultság kell)
mkdir -p /opt/jboss/wildfly/modules/org/postgresql/main
cp /tmp/driver/postgresql.jar /opt/jboss/wildfly/modules/org/postgresql/main/

# Jogosultságok átadása jboss usernek
chown -R jboss:jboss /opt/jboss/wildfly/modules/org/postgresql
chown -R jboss:jboss /opt/jboss/wildfly/standalone/deployments
chown jboss:jboss /opt/jboss/wildfly/bin/add-datasource.cli

echo ">>> Adding datasource..."
# A CLI-t már jboss user-ként futtatjuk
/opt/jboss/wildfly/bin/jboss-cli.sh --file=/opt/jboss/wildfly/bin/add-datasource.cli

echo ">>> Starting WildFly..."
exec /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0