package side.financialmanagementapi;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import side.financialmanagementapi.entities.UserEntity;
import side.financialmanagementapi.repository.UserEntityRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FinancialManagementApiApplicationTests {

    @LocalServerPort
    Integer port;

    @Autowired
    UserEntityRepository userEntityRepository;

    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:17"));

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);

        System.out.println("url: " + container.getJdbcUrl());
        System.out.println("username: " + container.getUsername());
        System.out.println("password: " + container.getPassword());
        System.out.println("spring.datasource.driver-class-name: " + container.getJdbcDriverInstance());
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        userEntityRepository.deleteAll();
    }

    @Test
    void connectionEstablished() {
        assertThat(container.isCreated()).isTrue();
        assertThat(container.isRunning()).isTrue();
    }

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @Test
    void userEntityCoverage() {

        String name  = "User Name Entity";
        String email = "user.entity@mail.com";
        UserEntity userEntity = UserEntity.builder()
                                          .name(name)
                                          .email(email)
                                          .senhaHash("qwertyhash654987321hashqwerty")
                                          .registrationDate(LocalDateTime.now())
                                          .build();

        userEntityRepository.save(userEntity);

        assertNotNull(userEntity);
        assertEquals(userEntity, userEntity);
    }
}
