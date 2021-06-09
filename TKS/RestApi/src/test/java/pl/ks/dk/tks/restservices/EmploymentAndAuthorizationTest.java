package pl.ks.dk.tks.restservices;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class EmploymentAndAuthorizationTest {

    static final Logger LOGGER = LoggerFactory.getLogger(EmploymentAndAuthorizationTest.class);
    private String stringURL;

    public EmploymentAndAuthorizationTest() {
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
    public void adminAuthorization() throws URISyntaxException {
        RequestSpecification requestPost = getBasicRequest();
        requestPost.contentType("application/json");
        String JSON = "{\n" +
                "        \"login\": \"aAdamski\",\n" +
                "        \"password\": \"adamski\"\n" +
                "    }";
        requestPost.body(JSON);
        Response responseFromAuth = requestPost.post(new URI(stringURL + "/rest/auth"));
        String token = responseFromAuth.getBody().asString();

        RequestSpecification requestGet = getBasicRequest();
        requestGet.header(new Header("Authorization", "Bearer " + token));
        String responseFromSelf = requestGet.get(new URI(stringURL + "/rest/users/_self")).asString();

        assertTrue(responseFromSelf.contains("Admin"));
        assertTrue(responseFromSelf.contains("aAdamski"));
        assertTrue(responseFromSelf.contains("Adam"));
        assertFalse(responseFromSelf.contains("adamski"));
    }

    @Test
    public void superUserAuthorization() throws URISyntaxException {
        RequestSpecification requestPost = getBasicRequest();
        requestPost.contentType("application/json");
        String JSON = "{\n" +
                "        \"login\": \"tTomkowski\",\n" +
                "        \"password\": \"tomkowski\"\n" +
                "    }";
        requestPost.body(JSON);
        Response responseFromAuth = requestPost.post(new URI(stringURL + "/rest/auth"));
        String token = responseFromAuth.getBody().asString();

        RequestSpecification requestGet = getBasicRequest();
        requestGet.header(new Header("Authorization", "Bearer " + token));
        String responseFromSelf = requestGet.get(new URI(stringURL + "/rest/users/_self")).asString();

        assertTrue(responseFromSelf.contains("SuperUser"));
        assertTrue(responseFromSelf.contains("tTomkowski"));
        assertTrue(responseFromSelf.contains("Tomek"));
        assertFalse(responseFromSelf.contains("tomkowski"));
    }

    private RequestSpecification getBasicRequest() {
        RequestSpecification request = RestAssured.given();
        request.relaxedHTTPSValidation();
        return request;
    }

    private String getFirstUUID(String token) throws URISyntaxException {
        RequestSpecification request = getBasicRequest();
        request.header(new Header("Authorization", "Bearer " + token));
        Response getAllResponse = request.get(new URI(stringURL + "/rest/resources"));
        String responseString = getAllResponse.asString();
        Pattern pattern = Pattern.compile("(?<=\"uuid\":\")\\d{8}");
        Matcher m = pattern.matcher(responseString);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

}
