package pl.ks.dk.tks.restservices;

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
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class NegativeTests {

    private final String adminToken = JWTGeneratorVerifier
            .generateJWTString(new CredentialValidationResult("aAdamski", new HashSet<>(
                    Arrays.asList("Admin"))));

    private final String userToken = JWTGeneratorVerifier
            .generateJWTString(new CredentialValidationResult("jUrban", new HashSet<>(
                    Arrays.asList("Client"))));

    static final Logger LOGGER = LoggerFactory.getLogger(NegativeTests.class);
    private String stringURL;

    public NegativeTests() {
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
    public void resourcesValidationCheck() throws URISyntaxException {
        String JSON = "{\n" +
                "        \"basePriceForHour\": -1,\n" +
                "        \"maxNumberOfChildrenInTheFamily\": 5,\n" +
                "        \"minChildAge\": 1,\n" +
                "        \"name\": \"Anna\",\n" +
                "        \"surname\": \"Panna\"\n" +
                "    }";

        RequestSpecification requestPost = getBasicAdminRequest();
        requestPost.contentType("application/json");
        requestPost.body(JSON);

        Response response = requestPost.post(new URI(stringURL + "/rest/resources/standard"));
        assertEquals(422, response.statusCode());
    }

    @Test
    public void authorizationFailedLogin() throws URISyntaxException {
        RequestSpecification requestPost = RestAssured.given();
        requestPost.relaxedHTTPSValidation();
        requestPost.contentType("application/json");

        String JSON = "{\n" +
                "        \"login\": \"nieMaTakiegoLoginu\",\n" +
                "        \"password\": \"adamski\"\n" +
                "    }";
        requestPost.body(JSON);

        Response responseFromAuth = requestPost.post(new URI(stringURL + "/rest/auth"));
        assertEquals(401, responseFromAuth.statusCode());
    }

    @Test
    public void authorizationFailedPassword() throws URISyntaxException {
        RequestSpecification requestPost = RestAssured.given();
        requestPost.relaxedHTTPSValidation();
        requestPost.contentType("application/json");

        String JSON = "{\n" +
                "        \"login\": \"aAdamski\",\n" +
                "        \"password\": \"nieMaTakiegoHasła\"\n" +
                "    }";
        requestPost.body(JSON);

        Response responseFromAuth = requestPost.post(new URI(stringURL + "/rest/auth"));
        assertEquals(401, responseFromAuth.statusCode());
    }

    @Test
    public void employingEmployedBabysitter() throws URISyntaxException {


//        RequestSpecification authRequestClient = RestAssured.given();
//        authRequestClient.relaxedHTTPSValidation();
//        authRequestClient.contentType("application/json");
//        String Client = "{\n" +
//                "        \"login\": \"jUrban\",\n" +
//                "        \"password\": \"urban\"\n" +
//                "    }";
//        authRequestClient.body(Client);
//        Response responseFromAuthClient = authRequestClient.post(new URI("https://localhost:8181/TKS/rest/auth"));
//        assertEquals(202, responseFromAuthClient.statusCode());
//        String token = responseFromAuthClient.getBody().asString();
//
//        RequestSpecification employmentRequest = RestAssured.given();
//        employmentRequest.relaxedHTTPSValidation();
//        employmentRequest.header(new Header("Authorization", "Bearer " + token));

        RequestSpecification employmentRequest = RestAssured.given();
        employmentRequest.relaxedHTTPSValidation();
        employmentRequest.header(new Header("Authorization", "Bearer " + userToken));
        employmentRequest.contentType("application/json");

        Response responseFromEmployment = employmentRequest.get(new URI(stringURL + "/rest/employment"));
        assertEquals(200, responseFromEmployment.statusCode());
        String responseFromEmploymentString = responseFromEmployment.asString();

        Pattern pattern = Pattern.compile("(?<=\"uuid\":\")\\d{8}");
        Matcher m = pattern.matcher(responseFromEmploymentString);
        m.find();
        String babysitterUuid = m.group();

        responseFromEmployment =
                employmentRequest.post(new URI(stringURL + "/rest/employment/" + babysitterUuid));
        assertEquals(422, responseFromEmployment.statusCode());
    }

    @Test
    public void JWTTest() throws URISyntaxException {
        RequestSpecification request = RestAssured.given();
        request.relaxedHTTPSValidation();
        Response response = request.get(new URI(stringURL + "/rest/employment"));
        assertEquals(401, response.statusCode());
    }

    @Test
    public void roleTest() throws URISyntaxException {
        RequestSpecification adminRequest = getBasicAdminRequest();
        Response adminResponse = adminRequest.get(new URI(stringURL + "/rest/users"));
        assertEquals(200, adminResponse.statusCode());

        RequestSpecification clientRequest = RestAssured.given();
        clientRequest.relaxedHTTPSValidation();
        clientRequest.header(new Header("Authorization", "Bearer " + userToken));
        Response clientResponse = clientRequest.get(new URI(stringURL + "/rest/users"));
        assertEquals(403, clientResponse.statusCode());
    }

    private RequestSpecification getBasicAdminRequest() {
        RequestSpecification request = RestAssured.given();
        request.relaxedHTTPSValidation();
        request.header(new Header("Authorization", "Bearer " + adminToken));
        return request;
    }

    private String getUuid(int number) throws URISyntaxException {
        RequestSpecification request = getBasicAdminRequest();
        request.header(new Header("Authorization", "Bearer " + adminToken));
        Response getAllResponse = request.get(new URI(stringURL + "/rest/users"));
        String responseString = getAllResponse.asString();
        Pattern pattern = Pattern.compile("(?<=\"uuid\":\")\\d{8}");
        Matcher m = pattern.matcher(responseString);
        for (int i = 0; i < number; i++) {
            m.find();
        }
        return m.group();
    }
}
