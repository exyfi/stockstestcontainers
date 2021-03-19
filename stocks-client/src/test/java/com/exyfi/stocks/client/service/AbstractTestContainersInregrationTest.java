package com.exyfi.stocks.client.service;


import com.exyfi.stocks.client.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = {AbstractTestContainersInregrationTest.PropertiesInitializer.class})
public class AbstractTestContainersInregrationTest {

    @Autowired
    private UserController userController;

    @Container
    public static final PostgreSQLContainer postgres = new PostgreSQLContainer()
            .withDatabaseName("users")
            .withUsername("postgres")
            .withPassword("password");


    static class PropertiesInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.r2dbc.url=" + postgres.getJdbcUrl().replace("jdbc", "r2dbc"),
                    "spring.r2dbc.username=" + postgres.getUsername(),
                    "spring.r2dbc.password=" + postgres.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());

        }
    }
}
