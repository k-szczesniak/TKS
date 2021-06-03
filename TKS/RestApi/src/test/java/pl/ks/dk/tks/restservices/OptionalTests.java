package pl.ks.dk.tks.restservices;

import com.nimbusds.jwt.SignedJWT;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.ks.dk.tks.security.JWTGeneratorVerifier;

import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class OptionalTests {

    private final String adminToken = JWTGeneratorVerifier
            .generateJWTString(new CredentialValidationResult("aAdamski", new HashSet<>(
                    Arrays.asList("Admin"))));

    private final String userToken = JWTGeneratorVerifier
            .generateJWTString(new CredentialValidationResult("jUrban", new HashSet<>(
                    Arrays.asList("Client"))));

    static final Logger LOGGER = LoggerFactory.getLogger(BabysittersCRUDTest.class);
    private String stringURL;

    public OptionalTests() {
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
    public void updateToken() throws URISyntaxException, InterruptedException {
        RequestSpecification authRequestClient = RestAssured.given();
        authRequestClient.relaxedHTTPSValidation();
        authRequestClient.contentType("application/json");
        String Client = "{\n" +
                "        \"login\": \"jUrban\",\n" +
                "        \"password\": \"urban\"\n" +
                "    }";
        authRequestClient.body(Client);
        Response responseFromAuthClient = authRequestClient.post(new URI(stringURL + "/rest/auth"));
        assertEquals(202, responseFromAuthClient.statusCode());
        String firstToken = responseFromAuthClient.getBody().asString();
        Thread.sleep(1000);

        RequestSpecification updateTokenRequest = RestAssured.given();
        updateTokenRequest.relaxedHTTPSValidation();
        updateTokenRequest.header(new Header("Authorization", "Bearer " + firstToken));
        updateTokenRequest.contentType("application/json");
        Response updateResponse = updateTokenRequest.get(new URI(stringURL + "/rest/auth/update"));
        assertEquals(202, updateResponse.statusCode());
        String updatedToken = updateResponse.getBody().asString();

        assertNotEquals(firstToken, updatedToken);

        Date firstTokenDate = getDateFromToken(firstToken);
        Date updatedTokenDate = getDateFromToken(updatedToken);
        assertTrue(updatedTokenDate.after(firstTokenDate));
    }

    @Test
    public void resourcesETag() throws URISyntaxException {
        String uuid = getUUID(true, 1);
        String newUuid = "00001111";
        assertNotEquals(uuid, newUuid);

        RequestSpecification getRequest = getBasicAdminRequest();
        Response getResponse = getRequest.get(new URI(stringURL + "/rest/resources/" + uuid));
        String ETag = getResponse.header("ETag");

        RequestSpecification requestPut = getBasicAdminRequest();
        requestPut.contentType("application/json");
        requestPut.header("If-Match", ETag);

        String JSON = "{\n" +
                "        \"basePriceForHour\": 123,\n" +
                "        \"maxNumberOfChildrenInTheFamily\": 4,\n" +
                "        \"minChildAge\": 12,\n" +
                "        \"name\": \"Zofia\",\n" +
                "        \"uuid\": \"" + newUuid + "\",\n" +
                "        \"surname\": \"Zofiowska\"\n" +
                "    }";
        requestPut.body(JSON);

        Response responseFromPut = requestPut.put(stringURL + "/rest/resources/standard/" + uuid);
        assertEquals(406, responseFromPut.statusCode());
    }

    private RequestSpecification getBasicAdminRequest() {
        RequestSpecification request = RestAssured.given();
        request.header(new Header("Authorization", "Bearer " + adminToken));
        request.relaxedHTTPSValidation();
        return request;
    }

    private Date getDateFromToken(String token) {
        Date date = null;
        try {
            SignedJWT jwtToken = SignedJWT.parse(token);
            date = (Date) (jwtToken.getJWTClaimsSet().getClaim("exp"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private String getUUID(boolean fromResources, int number) throws URISyntaxException {
        RequestSpecification request = getBasicAdminRequest();
        Response getAllResponse;
        if (fromResources) {
            getAllResponse = request.get(new URI(stringURL + "/rest/resources"));
        } else {
            getAllResponse = request.get(new URI(stringURL + "/rest/users"));
        }
        String responseString = getAllResponse.asString();
        Pattern pattern = Pattern.compile("(?<=\"uuid\":\")\\d{8}");
        Matcher m = pattern.matcher(responseString);
        for(int i = 0; i < number; i++) {
            m.find();
        }
        return m.group();
    }
}
