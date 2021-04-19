package pl.ks.dk.tks.restservices;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import pl.ks.dk.tks.dtomodel.babysitters.BabysitterDTO;
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


public class BabysittersCRUDTest {
    private final String token = JWTGeneratorVerifier.generateJWTString(new CredentialValidationResult("aAdamski", new HashSet<>(
            Arrays.asList("Admin"))));

    @Test
    public void getAllBabysittersTest() throws URISyntaxException {
        RequestSpecification request = getBasicRequest();
        Response response = request.get(new URI("https://localhost:8181/TKS/rest/resources"));
        String responseString = response.asString();

        assertTrue(responseString.contains("\"surname\":\"Kwiatkowska\""));
        assertTrue(responseString.contains("\"surname\":\"Rusin\""));
        assertTrue(responseString.contains("\"surname\":\"Krupa\""));
        assertTrue(responseString.contains("\"surname\":\"Taczka\""));
        assertTrue(responseString.contains("\"surname\":\"Jajko\""));
    }

    @Test
    public void getBabysitterTest() throws URISyntaxException {
        BabysitterDTO babysitterDTO = new BabysitterDTO("Anna", "Kwiatkowska", 123.0, 12, 4);

        RequestSpecification request = getBasicRequest();
        String firstUUID = getUUID(1);
        Response response = request.get(new URI("https://localhost:8181/TKS/rest/resources/" + firstUUID));

        babysitterDTO.setUuid(firstUUID);
        String expectedETag = EntityIdentitySignerVerifier.calculateETag(babysitterDTO);

        String responseString = response.asString();
        String ETag = response.header("ETag");

        assertFalse(ETag.isEmpty());
        assertEquals(expectedETag, ETag);
        assertTrue(responseString.contains("Anna"));
        assertTrue(responseString.contains("Kwiatkowska"));
        assertTrue(responseString.contains("123"));
        assertTrue(responseString.contains("12"));
        assertTrue(responseString.contains("4"));
    }

    @Test
    public void createBabysitterTest() throws URISyntaxException {
        String JSON = "{\n" +
                "        \"basePriceForHour\": 123,\n" +
                "        \"maxNumberOfChildrenInTheFamily\": 5,\n" +
                "        \"minChildAge\": 1,\n" +
                "        \"name\": \"Zofia\",\n" +
                "        \"surname\": \"Alabaster\"\n" +
                "    }";

        RequestSpecification requestAll = getBasicRequest();
        RequestSpecification requestPost = getBasicRequest();

        requestPost.contentType("application/json");
        requestPost.body(JSON);

        String getAllResponseString = requestAll.get(new URI("https://localhost:8181/TKS/rest/resources")).asString();
        int initialSize = getAllResponseString.split("},").length;

        requestPost.post(new URI("https://localhost:8181/TKS/rest/resources/standard"));

        getAllResponseString = requestAll.get(new URI("https://localhost:8181/TKS/rest/resources")).asString();
        assertEquals(initialSize + 1, getAllResponseString.split("},").length);

        assertTrue(getAllResponseString.contains("\"name\":\"Zofia\""));
    }

    @Test
    public void createTeachingSitterTest() throws URISyntaxException {
        String JSON = "{\n" +
                "        \"basePriceForHour\": 123,\n" +
                "        \"maxNumberOfChildrenInTheFamily\": 3,\n" +
                "        \"minChildAge\": 1,\n" +
                "        \"name\": \"Zuzanna\",\n" +
                "        \"yearsOfExperienceInTeaching\": 10,\n" +
                "        \"surname\": \"Chryzantema\"\n" +
                "    }";

        RequestSpecification requestAll = getBasicRequest();
        RequestSpecification requestPost = getBasicRequest();

        requestPost.contentType("application/json");
        requestPost.body(JSON);

        String getAllResponseString = requestAll.get(new URI("https://localhost:8181/TKS/rest/resources")).asString();
        int initialSize = getAllResponseString.split("},").length;

        requestPost.post(new URI("https://localhost:8181/TKS/rest/resources/teaching"));

        getAllResponseString = requestAll.get(new URI("https://localhost:8181/TKS/rest/resources")).asString();
        assertEquals(initialSize + 1, getAllResponseString.split("},").length);

        assertTrue(getAllResponseString.contains("\"yearsOfExperienceInTeaching\":10"));
    }

    @Test
    public void createTidingSitterTest() throws URISyntaxException {
        String JSON = "{\n" +
                "        \"basePriceForHour\": 123,\n" +
                "        \"maxNumberOfChildrenInTheFamily\": 6,\n" +
                "        \"minChildAge\": 1,\n" +
                "        \"name\": \"Zuzanna\",\n" +
                "        \"valueOfCleaningEquipment\": 40.0,\n" +
                "        \"surname\": \"Rozmarzona\"\n" +
                "    }";

        RequestSpecification requestAll = getBasicRequest();
        RequestSpecification requestPost = getBasicRequest();

        requestPost.contentType("application/json");
        requestPost.body(JSON);

        String getAllResponseString = requestAll.get(new URI("https://localhost:8181/TKS/rest/resources")).asString();
        int initialSize = getAllResponseString.split("},").length;

        requestPost.post(new URI("https://localhost:8181/TKS/rest/resources/tiding"));

        getAllResponseString = requestAll.get(new URI("https://localhost:8181/TKS/rest/resources")).asString();
        assertEquals(initialSize + 1, getAllResponseString.split("},").length);

        assertTrue(getAllResponseString.contains("\"valueOfCleaningEquipment\":40.0"));
    }

    @Test
    public void updateBabysitter() throws URISyntaxException {
        String firstUUID = getUUID(4);
        RequestSpecification requestGet = getBasicRequest();
        Response getResponse = requestGet.get(new URI("https://localhost:8181/TKS/rest/resources/" + firstUUID));

        String originalSurname = getSurname(1, getResponse.asString());
        String ETag = getResponse.header("ETag");

        RequestSpecification requestPut = getBasicRequest();
        requestPut.contentType("application/json");
        requestPut.header("If-Match", ETag);

        String randomSurname = RandomStringUtils.randomAlphabetic(8);
        String JSON = "{\n" +
                "        \"basePriceForHour\": 123,\n" +
                "        \"maxNumberOfChildrenInTheFamily\": 4,\n" +
                "        \"minChildAge\": 12,\n" +
                "        \"name\": \"Złocista\",\n" +
                "        \"uuid\": \"" + firstUUID + "\",\n" +
                "        \"surname\": \"" + randomSurname + "\"\n" +
                "    }";
        requestPut.body(JSON);

        requestPut.put("https://localhost:8181/TKS/rest/resources/standard/" + firstUUID);

        getResponse = requestGet.get(new URI("https://localhost:8181/TKS/rest/resources/" + firstUUID));
        String changedSurname = getSurname(1, getResponse.asString());
        assertNotEquals(originalSurname, changedSurname);
    }

    @Test
    public void deleteBabysitter() throws URISyntaxException {
        String firstUUID = getUUID(5);
        RequestSpecification requestAll = getBasicRequest();
        RequestSpecification requestDelete = getBasicRequest();

        requestDelete.contentType("application/json");

        String getAllResponseString = requestAll.get(new URI("https://localhost:8181/TKS/rest/resources")).asString();
        int initialSize = getAllResponseString.split("},").length;


        String originalSurname = getSurname(5, getAllResponseString);

        requestDelete.delete(new URI("https://localhost:8181/TKS/rest/resources/" + firstUUID));

        getAllResponseString = requestAll.get(new URI("https://localhost:8181/TKS/rest/resources")).asString();
        assertEquals(initialSize - 1, getAllResponseString.split("},").length);
        assertFalse(getAllResponseString.contains(originalSurname));
    }

    private RequestSpecification getBasicRequest() {
        RequestSpecification request = RestAssured.given();
        request.relaxedHTTPSValidation();
        request.header(new Header("Authorization", "Bearer " + token));
        return request;
    }

    private String getUUID(int number) throws URISyntaxException {
        RequestSpecification request = getBasicRequest();

        Response getAllResponse = request.get(new URI("https://localhost:8181/TKS/rest/resources"));
        String responseString = getAllResponse.asString();
        Pattern pattern = Pattern.compile("(?<=\"uuid\":\")\\d{8}");
        Matcher m = pattern.matcher(responseString);
        for (int i = 0; i < number; i++) {
            m.find();
        }
        return m.group();
    }

    private String getSurname(int number, String response) {
        Pattern surnamePattern = Pattern.compile("(?<=\"surname\":\")[^\"]+");
        Matcher m = surnamePattern.matcher(response);
        for (int i = 0; i < number; i++) {
            m.find();
        }
        return m.group();
    }

}
