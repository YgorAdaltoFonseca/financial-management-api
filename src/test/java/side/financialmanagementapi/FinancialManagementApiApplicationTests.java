package side.financialmanagementapi;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import side.financialmanagementapi.repository.user.UserEntityRepository;
import side.financialmanagementapi.repository.transaction.TransactionRepository;
import side.financialmanagementapi.repository.subscription.SubscriptionRepository;
import side.financialmanagementapi.repository.category.CategoryTypeRepository;

import java.time.Instant;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FinancialManagementApiApplicationTests {
    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    @LocalServerPort
    int port;

    @Autowired
    UserEntityRepository users;

    @Autowired TransactionRepository transactions;
    @Autowired SubscriptionRepository subscriptions;
    @Autowired CategoryTypeRepository categories;
    @Autowired JwtEncoder jwtEncoder;

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("jwt.secret", () -> "integration-test-secret-32-bytes-minimum");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        transactions.deleteAll();
        subscriptions.deleteAll();
        categories.deleteAll();
        users.deleteAll();
    }

    @Test
    void unauthenticatedAndMalformedTokenAreRejected() {
        given().get("/api/user/me").then().statusCode(401);
        given().header("Authorization", "Bearer malformed.token.value")
                .get("/api/user/me").then().statusCode(401);
    }

    @Test
    void registrationRejectsInvalidInput() {
        given().contentType("application/json")
                .body("{\"name\":\"\",\"email\":\"not-an-email\",\"senhaHash\":\"short\"}")
                .post("/api/user/createUser").then().statusCode(400);
    }

    @Test
    void expiredTokenAndOrdinaryUserCannotAccessAdminArea() {
        register("user@example.test", "Strong-test-password-123");
        String token = login("user@example.test", "Strong-test-password-123");
        Instant now = Instant.now();
        String expired = jwtEncoder.encode(JwtEncoderParameters.from(JwtClaimsSet.builder()
                .subject(users.findByEmail("user@example.test").orElseThrow().getId().toString())
                .issuedAt(now.minusSeconds(7200)).expiresAt(now.minusSeconds(3600)).build())).getTokenValue();

        given().auth().oauth2(expired).get("/api/user/me").then().statusCode(401);
        given().auth().oauth2(token).get("/admin/area").then().statusCode(403);
    }

    @Test
    void registerLoginAndProtectedProfileUseHashedPasswordAndJwt() {
        String password = "Strong-test-password-123";
        register("one@example.test", password);
        assertThat(users.findByEmail("one@example.test").orElseThrow().getPassword())
                .isNotEqualTo(password).startsWith("$2");

        String token = login("one@example.test", password);
        given().auth().oauth2(token).get("/api/user/me").then()
                .statusCode(200).body("email", equalTo("one@example.test"));
    }

    @Test
    void transactionsAndCategoriesCannotBeAccessedAcrossUsers() {
        register("one@example.test", "Strong-test-password-123");
        register("two@example.test", "Strong-test-password-456");
        String firstToken = login("one@example.test", "Strong-test-password-123");
        String secondToken = login("two@example.test", "Strong-test-password-456");

        Response category = given().auth().oauth2(firstToken)
                .contentType("application/json")
                .body("{\"name\":\"Salary\",\"categoryType\":\"ENTRY\"}")
                .post("/api/category/createCategory").then().statusCode(200).extract().response();
        long categoryId = category.jsonPath().getLong("id");
        Response transaction = given().auth().oauth2(firstToken)
                .contentType("application/json")
                .body("{\"value\":1200.00,\"type\":\"ENTRY\",\"origin\":\"MANUAL\",\"description\":\"Salary\"}")
                .post("/api/transaction/createTransaction/" + categoryId)
                .then().statusCode(200).extract().response();
        long transactionId = transaction.jsonPath().getLong("id");

        given().auth().oauth2(secondToken).get("/api/transaction/listTransaction")
                .then().statusCode(200).body("size()", equalTo(0));
        given().auth().oauth2(secondToken).delete("/api/transaction/delete/" + transactionId)
                .then().statusCode(404);
        given().auth().oauth2(secondToken).put("/api/transaction/updateTransaction/" + transactionId + "/" + categoryId)
                .contentType("application/json")
                .body("{\"value\":1,\"type\":\"EXIT\",\"origin\":\"MANUAL\",\"description\":\"tamper\"}")
                .then().statusCode(404);
        given().auth().oauth2(secondToken).get("/api/category/categoriesList")
                .then().statusCode(200).body("size()", equalTo(0));
    }

    private void register(String email, String password) {
        given().contentType("application/json")
                .body("{\"name\":\"Test User\",\"email\":\"" + email + "\",\"senhaHash\":\"" + password + "\"}")
                .post("/api/user/createUser").then().statusCode(200);
    }

    private String login(String email, String password) {
        return given().contentType("application/json")
                .body("{\"email\":\"" + email + "\",\"senha\":\"" + password + "\"}")
                .post("/api/user/login").then().statusCode(200).extract().path("token");
    }
}
