package pl.ks.dk.tks.restservices;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import pl.ks.dk.tks.dtomodel.users.AdminDTO;
import pl.ks.dk.tks.security.JWTGeneratorVerifier;
import pl.ks.dk.tks.utils.EntityIdentitySignerVerifier;

import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class UsersCRUTest {
    private final String token = JWTGeneratorVerifier.generateJWTString(new CredentialValidationResult("aAdamski", new HashSet<>(
            Arrays.asList("Admin"))));

    @Test
    public void getAllUsersTest() throws URISyntaxException {
        RequestSpecification request = getBasicRequest();
        Response response = request.get(new URI("https://localhost:8181/TKS/rest/users"));
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
        Response response = request.get(new URI("https://localhost:8181/TKS/rest/users/" + firstUUID));

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

    @Test
    public void createClientTest() throws URISyntaxException {
        String randomLogin = RandomStringUtils.randomAlphanumeric(8);
        String JSON = "{\n" +
                "        \"ageOfTheYoungestChild\": 7,\n" +
                "        \"name\": \"testName\",\n" +
                "        \"numberOfChildren\": 14,\n" +
                "        \"role\": \"Client\",\n" +
                "        \"password\": \"testTEST\",\n" +
                "        \"surname\": \"testSurname\",\n" +
                "        \"login\": \"" + randomLogin + "\"\n" +
                "    }";

        RequestSpecification requestAll = getBasicRequest();
        RequestSpecification requestPost = getBasicRequest();

        requestPost.contentType("application/json");
        requestPost.body(JSON);

        String getAllResponseString = requestAll.get(new URI("https://localhost:8181/TKS/rest/users")).asString();
        int initialSize = getAllResponseString.split("},").length;

        requestPost.post(new URI("https://localhost:8181/TKS/rest/users/client"));

        getAllResponseString = requestAll.get(new URI("https://localhost:8181/TKS/rest/users")).asString();
        assertEquals(initialSize + 1, getAllResponseString.split("},").length);

        assertTrue(getAllResponseString.contains("\"login\":\"" + randomLogin + "\""));
    }


    @Test
    public void createAdminTest() throws URISyntaxException {
        String randomLogin = RandomStringUtils.randomAlphanumeric(8);
        String JSON = "{\n" +
                "        \"name\": \"testName\",\n" +
                "        \"role\": \"Admin\",\n" +
                "        \"password\": \"testTEST\",\n" +
                "        \"surname\": \"testSurname\",\n" +
                "        \"login\": \"" + randomLogin + "\"\n" +
                "    }";

        RequestSpecification requestAll = getBasicRequest();
        RequestSpecification requestPost = getBasicRequest();

        requestPost.contentType("application/json");
        requestPost.body(JSON);

        String getAllResponseString = requestAll.get(new URI("https://localhost:8181/TKS/rest/users")).asString();
        int initialSize = getAllResponseString.split("},").length;

        requestPost.post(new URI("https://localhost:8181/TKS/rest/users/admin"));

        getAllResponseString = requestAll.get(new URI("https://localhost:8181/TKS/rest/users")).asString();
        assertEquals(initialSize + 1, getAllResponseString.split("},").length);

        assertTrue(getAllResponseString.contains("\"login\":\"" + randomLogin + "\""));
    }

    @Test
    public void createSuperUserTest() throws URISyntaxException {
        String randomLogin = RandomStringUtils.randomAlphanumeric(8);
        String JSON = "{\n" +
                "        \"name\": \"testName\",\n" +
                "        \"role\": \"SuperUser\",\n" +
                "        \"password\": \"testTEST\",\n" +
                "        \"surname\": \"testSurname\",\n" +
                "        \"login\": \"" + randomLogin + "\"\n" +
                "    }";

        RequestSpecification requestAll = getBasicRequest();
        RequestSpecification requestPost = getBasicRequest();

        requestPost.contentType("application/json");
        requestPost.body(JSON);

        String getAllResponseString = requestAll.get(new URI("https://localhost:8181/TKS/rest/users")).asString();
        int initialSize = getAllResponseString.split("},").length;

        requestPost.post(new URI("https://localhost:8181/TKS/rest/users/superUser"));

        getAllResponseString = requestAll.get(new URI("https://localhost:8181/TKS/rest/users")).asString();
        assertEquals(initialSize + 1, getAllResponseString.split("},").length);

        assertTrue(getAllResponseString.contains("\"login\":\"" + randomLogin + "\""));
    }

    @Test
    public void updateAdmin() throws URISyntaxException {
        String firstUUID = getUUID();
        RequestSpecification requestGet = getBasicRequest();
        Response getResponse = requestGet.get(new URI("https://localhost:8181/TKS/rest/users/" + firstUUID));

        Pattern surnamePattern = Pattern.compile("(?<=\"surname\":\")[^\"]+");
        Matcher m = surnamePattern.matcher(getResponse.asString());
        String originalSurname = m.find() ? m.group() : "";
        String ETag = getResponse.header("ETag");

        RequestSpecification requestPut = getBasicRequest();
        requestPut.contentType("application/json");
        requestPut.header("If-Match", ETag);

        String randomSurname = RandomStringUtils.randomAlphabetic(8);
        String JSON = "{\n" +
                "        \"name\": \"Adam\",\n" +
                "        \"role\": \"Admin\",\n" +
                "        \"password\": \"adamski1\",\n" +
                "        \"surname\": \"" + randomSurname + "\",\n" +
                "        \"login\": \"" + "aAdamski" + "\",\n" +
                "        \"uuid\": \"" + firstUUID + "\"\n" +
                "    }";
        requestPut.body(JSON);
        requestPut.put("https://localhost:8181/TKS/rest/users/admin/" + firstUUID);

        getResponse = requestGet.get(new URI("https://localhost:8181/TKS/rest/users/" + firstUUID));
        m = surnamePattern.matcher(getResponse.asString());
        String changedSurname = m.find() ? m.group() : "";
        assertNotEquals(originalSurname, changedSurname);
    }

    private RequestSpecification getBasicRequest() {
        RequestSpecification request = RestAssured.given();
        request.relaxedHTTPSValidation();
        request.header(new Header("Authorization", "Bearer " + token));
        return request;
    }

    private String getUUID() throws URISyntaxException {
        RequestSpecification request = getBasicRequest();

        Response getAllResponse = request.get(new URI("https://localhost:8181/TKS/rest/users"));
        String responseString = getAllResponse.asString();
        Pattern pattern = Pattern.compile("(?<=\"uuid\":\")\\d{8}");
        Matcher m = pattern.matcher(responseString);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

}
