package com.dynata.surveyhw.configurations;

import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.time.Duration;

@Testcontainers
public class SharedContainers {

    private static final Network network = Network.newNetwork();

    @Container
    public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
            .withExposedPorts(5432)
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password")
            .withFileSystemBind("db/init/schema.sql", "/docker-entrypoint-initdb.d/schema.sql", BindMode.READ_ONLY)
            .withNetwork(network)
            .withNetworkAliases("db");

    @Container
    public static final GenericContainer<?> WILDFLY = new GenericContainer<>(
            "quay.io/wildfly/wildfly:37.0.0.Final-jdk21")
            .withExposedPorts(8080)
            .withCreateContainerCmdModifier(cmd -> cmd.withUser("0"))
            .withCopyFileToContainer(
                    MountableFile.forHostPath("wildfly/module.xml"),
                    "/opt/jboss/wildfly/modules/org/postgresql/main/module.xml")
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("wildfly/add-datasource.cli"),
                    "/opt/jboss/wildfly/bin/add-datasource.cli")
            .withCopyFileToContainer(
                    MountableFile.forHostPath("target/survey-hw-0.0.1-SNAPSHOT-tests.war"),
                    "/opt/jboss/wildfly/standalone/deployments/ROOT.war")
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("war.dodeploy"),
                    "/opt/jboss/wildfly/standalone/deployments/ROOT.war.dodeploy")
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("wildfly/init.sh", 0777),
                    "/opt/jboss/wildfly/bin/init.sh")
            .withEnv("DB_URL", "jdbc:postgresql://db:5432/testdb")
            .withEnv("DB_USER", "user")
            .withEnv("DB_PASSWORD", "password")
            .withNetwork(network)
            .dependsOn(POSTGRES)
            //            .withLogConsumer(outputFrame -> System.out.print(outputFrame.getUtf8String()))
            .withCommand("bash", "/opt/jboss/wildfly/bin/init.sh")
            .waitingFor(Wait.forLogMessage(".*WFLYSRV0025.*", 1)
                    .withStartupTimeout(Duration.ofMinutes(2))
            );
}