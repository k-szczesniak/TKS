package pl.ks.dk.us.restservices;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import pl.ks.dk.us.security.JWTGeneratorVerifier;

import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NegativeTests {

    private final String adminToken = JWTGeneratorVerifier
            .generateJWTString(new CredentialValidationResult("aAdamski", new HashSet<>(
                    Arrays.asList("Admin"))));

    private final String userToken = JWTGeneratorVerifier
            .generateJWTString(new CredentialValidationResult("jUrban", new HashSet<>(
                    Arrays.asList("Client"))));

    @Test
    public void usersValidationCheck() throws URISyntaxException {
        String JSON = "{\n" +
                "        \"name\": \"1\",\n" +
                "        \"role\": \"Admin\",\n" +
                "        \"password\": \"testTEST\",\n" +
                "        \"surname\": \"testSurname\",\n" +
                "        \"login\": \"testLogin\"\n" +
                "    }";

        RequestSpecification requestPost = getBasicAdminRequest();
        requestPost.contentType("application/json");
        requestPost.body(JSON);

        Response response = requestPost.post(new URI("https://localhost:8181/UserService/rest/users/user"));
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

        Response responseFromAuth = requestPost.post(new URI("https://localhost:8181/UserService/rest/auth"));
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

        Response responseFromAuth = requestPost.post(new URI("https://localhost:8181/UserService/rest/auth"));
        assertEquals(401, responseFromAuth.statusCode());
    }

    @Test
    public void authorizationFailedAccountActivity() throws URISyntaxException {
        RequestSpecification authRequestClient = RestAssured.given();
        authRequestClient.relaxedHTTPSValidation();
        authRequestClient.contentType("application/json");
        String Client = "{\n" +
                "        \"login\": \"tHajto\",\n" +
                "        \"password\": \"hajto\"\n" +
                "    }";
        authRequestClient.body(Client);
        Response responseFromAuthClient = authRequestClient.post(new URI("https://localhost:8181/UserService/rest/auth"));
        assertEquals(202, responseFromAuthClient.statusCode());


        String uuid = getUuid(3);
        RequestSpecification requestGet = getBasicAdminRequest();
        Response getResponse = requestGet.get(new URI("https://localhost:8181/UserService/rest/users/" + uuid));
        assertEquals(200, getResponse.statusCode());

        String ETag = getResponse.header("ETag");
        RequestSpecification requestPut = getBasicAdminRequest();
        requestPut.contentType("application/json");
        requestPut.header("If-Match", ETag);
        String JSON = "{\n" +
                "        \"role\": \"Client\",\n" +
                "        \"password\": \"hajto123\",\n" +
                "        \"name\": \"Tomasz\",\n" +
                "        \"surname\": \"Hajto\",\n" +
                "        \"active\": \"false\",\n" +
                "        \"login\": \"" + "tHajto" + "\",\n" +
                "        \"uuid\": \"" + uuid + "\"\n" +
                "    }";
        requestPut.body(JSON);
        Response responseFromPut = requestPut.put("https://localhost:8181/UserService/rest/users/user/" + uuid);
        assertEquals(200, responseFromPut.statusCode());


        RequestSpecification authRequestClient2 = RestAssured.given();
        authRequestClient2.relaxedHTTPSValidation();
        authRequestClient2.contentType("application/json");
        String Client2 = "{\n" +
                "        \"login\": \"tHajto\",\n" +
                "        \"password\": \"hajto123\"\n" +
                "    }";
        authRequestClient2.body(Client2);
        Response responseFromAuthClient2 = authRequestClient2.post(new URI("https://localhost:8181/UserService/rest/auth"));
        assertEquals(401, responseFromAuthClient2.statusCode());
    }

    @Test
    public void httpsTest() {
        RequestSpecification request = RestAssured.given();
        request.header(new Header("Authorization", "Bearer " + userToken));
        request.redirects().follow(false);

        Response response = request.get("http://localhost:8080/UserService/rest/users");
        assertEquals(302, response.statusCode());
    }

    @Test
    public void JWTTest() throws URISyntaxException {
        RequestSpecification request = RestAssured.given();
        request.relaxedHTTPSValidation();
        Response response = request.get(new URI("https://localhost:8181/UserService/rest/users"));
        assertEquals(401, response.statusCode());
    }

    @Test
    public void roleTest() throws URISyntaxException {
        RequestSpecification adminRequest = getBasicAdminRequest();
        Response adminResponse = adminRequest.get(new URI("https://localhost:8181/UserService/rest/users"));
        assertEquals(200, adminResponse.statusCode());
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
        Response getAllResponse = request.get(new URI("https://localhost:8181/UserService/rest/users"));
        String responseString = getAllResponse.asString();
        Pattern pattern = Pattern.compile("(?<=\"uuid\":\")\\d{8}");
        Matcher m = pattern.matcher(responseString);
        for (int i = 0; i < number; i++) {
            m.find();
        }
        return m.group();
    }
}
