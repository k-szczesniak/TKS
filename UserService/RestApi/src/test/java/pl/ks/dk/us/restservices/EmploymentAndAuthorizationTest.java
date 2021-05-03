package pl.ks.dk.us.restservices;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;


public class EmploymentAndAuthorizationTest {

    @Test
    public void adminAuthorization() throws URISyntaxException {
        RequestSpecification requestPost = getBasicRequest();
        requestPost.contentType("application/json");
        String JSON = "{\n" +
                "        \"login\": \"aAdamski\",\n" +
                "        \"password\": \"adamski\"\n" +
                "    }";
        requestPost.body(JSON);
        Response responseFromAuth = requestPost.post(new URI("https://localhost:8181/UserService/rest/auth"));
        String token = responseFromAuth.getBody().asString();

        RequestSpecification requestGet = getBasicRequest();
        requestGet.header(new Header("Authorization", "Bearer " + token));
        String responseFromSelf = requestGet.get(new URI("https://localhost:8181/UserService/rest/users/_self")).asString();

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
        Response responseFromAuth = requestPost.post(new URI("https://localhost:8181/UserService/rest/auth"));
        String token = responseFromAuth.getBody().asString();

        RequestSpecification requestGet = getBasicRequest();
        requestGet.header(new Header("Authorization", "Bearer " + token));
        String responseFromSelf = requestGet.get(new URI("https://localhost:8181/UserService/rest/users/_self")).asString();

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
        Response getAllResponse = request.get(new URI("https://localhost:8181/UserService/rest/resources"));
        String responseString = getAllResponse.asString();
        Pattern pattern = Pattern.compile("(?<=\"uuid\":\")\\d{8}");
        Matcher m = pattern.matcher(responseString);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

}
