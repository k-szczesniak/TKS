package pl.ks.dk.tks.restservices;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.ks.dk.tks.dtomodel.users.AdminDTO;
import pl.ks.dk.tks.security.JWTGeneratorVerifier;
import pl.ks.dk.tks.utils.EntityIdentitySignerVerifier;

import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UsersCRUTest {
    private final String token = JWTGeneratorVerifier.generateJWTString(new CredentialValidationResult("aAdamski", new HashSet<>(
            Arrays.asList("Admin"))));

    static final Logger LOGGER = LoggerFactory.getLogger(BabysittersCRUDTest.class);
    private String stringURL;

    public UsersCRUTest() {
        Formatter formatter = new Formatter();
        stringURL = formatter.format("https://localhost:%d/TKS", restServices.getMappedPort(8181)).toString();
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
    }

    @Container
    private static final GenericContainer restServices = new GenericContainer<>(
            new ImageFromDockerfile()
                    .withDockerfileFromBuilder(builder -> builder
                            .from("payara/server-full:5.2021.2-jdk11")
                            .copy("REST.war", "/opt/payara/deployments")
                            .build())
                    .withFileFromPath("REST.war", Path.of("target", "REST.war"))
    )
            .withExposedPorts(8080, 4848)
            .waitingFor(Wait.forHttp("/TKS/rest/java").forPort(8080).forStatusCode(200))
            .withLogConsumer(new Slf4jLogConsumer(LOGGER));

    @Test
    public void getAllUsersTest() throws URISyntaxException {
        RequestSpecification request = getBasicRequest();
        Response response = request.get(new URI(stringURL + "/rest/users"));
        String responseString = response.asString();

        assertTrue(responseString.contains("\"login\":\"aAdamski\""));
        assertTrue(responseString.contains("\"login\":\"tTomkowski\""));
        assertTrue(responseString.contains("\"login\":\"aWiadro\""));
        assertTrue(responseString.contains("\"login\":\"jKwiatkowska\""));
        assertTrue(responseString.contains("\"login\":\"jUrban\""));
        assertTrue(responseString.contains("\"login\":\"tHajto\""));
    }

    @Test
    public void getUserTest() throws URISyntaxException {
        AdminDTO adminDTO = new AdminDTO("aAdamski", "Adam", "Adamski", "adamski", "Admin");

        RequestSpecification request = getBasicRequest();
        String firstUUID = getUUID();
        Response response = request.get(new URI(stringURL + "/rest/users/" + firstUUID));

        adminDTO.setUuid(firstUUID);
        String expectedETag = EntityIdentitySignerVerifier.calculateETag(adminDTO);

        String responseString = response.asString();
        String ETag = response.header("ETag");

        assertFalse(ETag.isEmpty());
        assertEquals(expectedETag, ETag);
        assertTrue(responseString.contains("aAdamski"));
        assertTrue(responseString.contains("Adam"));
        assertTrue(responseString.contains("Adamski"));
        assertTrue(responseString.contains("Admin"));
        assertFalse(responseString.contains("password"));
    }

    private RequestSpecification getBasicRequest() {
        RequestSpecification request = RestAssured.given();
        request.relaxedHTTPSValidation();
        request.header(new Header("Authorization", "Bearer " + token));
        return request;
    }

    private String getUUID() throws URISyntaxException {
        RequestSpecification request = getBasicRequest();

        Response getAllResponse = request.get(new URI(stringURL + "/rest/users"));
        String responseString = getAllResponse.asString();
        Pattern pattern = Pattern.compile("(?<=\"uuid\":\")\\d{8}");
        Matcher m = pattern.matcher(responseString);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

}
