//package com.mycompany.firstapplication.RestServices;
//
//import com.mycompany.firstapplication.Security.JWTGeneratorVerifier;
//import com.nimbusds.jwt.SignedJWT;
//import io.restassured.RestAssured;
//import io.restassured.http.Header;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//import org.junit.jupiter.api.Test;
//
//import javax.security.enterprise.identitystore.CredentialValidationResult;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.text.ParseException;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class OptionalTests {
//
//    private final String adminToken = JWTGeneratorVerifier
//            .generateJWTString(new CredentialValidationResult("aAdamski", new HashSet<>(
//                    Arrays.asList("Admin"))));
//
//    private final String userToken = JWTGeneratorVerifier
//            .generateJWTString(new CredentialValidationResult("jUrban", new HashSet<>(
//                    Arrays.asList("Client"))));
//
//    @Test
//    public void updateToken() throws URISyntaxException, InterruptedException {
//        RequestSpecification authRequestClient = RestAssured.given();
//        authRequestClient.relaxedHTTPSValidation();
//        authRequestClient.contentType("application/json");
//        String Client = "{\n" +
//                "        \"login\": \"jUrban\",\n" +
//                "        \"password\": \"urban\"\n" +
//                "    }";
//        authRequestClient.body(Client);
//        Response responseFromAuthClient = authRequestClient.post(new URI("https://localhost:8181/PAS/rest/auth"));
//        assertEquals(202, responseFromAuthClient.statusCode());
//        String firstToken = responseFromAuthClient.getBody().asString();
//        Thread.sleep(1000);
//
//        RequestSpecification updateTokenRequest = RestAssured.given();
//        updateTokenRequest.relaxedHTTPSValidation();
//        updateTokenRequest.header(new Header("Authorization", "Bearer " + firstToken));
//        updateTokenRequest.contentType("application/json");
//        Response updateResponse = updateTokenRequest.get(new URI("https://localhost:8181/PAS/rest/auth/update"));
//        assertEquals(202, updateResponse.statusCode());
//        String updatedToken = updateResponse.getBody().asString();
//
//        assertNotEquals(firstToken, updatedToken);
//
//        Date firstTokenDate = getDateFromToken(firstToken);
//        Date updatedTokenDate = getDateFromToken(updatedToken);
//        assertTrue(updatedTokenDate.after(firstTokenDate));
//    }
//
//    @Test
//    public void resourcesETag() throws URISyntaxException {
//        String uuid = getUUID(true, 1);
//        String newUuid = "00001111";
//        assertNotEquals(uuid, newUuid);
//
//        RequestSpecification getRequest = getBasicAdminRequest();
//        Response getResponse = getRequest.get(new URI("https://localhost:8181/PAS/rest/resources/" + uuid));
//        String ETag = getResponse.header("ETag");
//
//        RequestSpecification requestPut = getBasicAdminRequest();
//        requestPut.contentType("application/json");
//        requestPut.header("If-Match", ETag);
//
//        String JSON = "{\n" +
//                "        \"basePriceForHour\": 123,\n" +
//                "        \"maxNumberOfChildrenInTheFamily\": 4,\n" +
//                "        \"minChildAge\": 12,\n" +
//                "        \"name\": \"Zofia\",\n" +
//                "        \"uuid\": \"" + newUuid + "\",\n" +
//                "        \"surname\": \"Zofiowska\"\n" +
//                "    }";
//        requestPut.body(JSON);
//
//        Response responseFromPut = requestPut.put("https://localhost:8181/PAS/rest/resources/standard/" + uuid);
//        assertEquals(406, responseFromPut.statusCode());
//    }
//
//    @Test
//    public void usersETag() throws URISyntaxException {
//        String uuid = getUUID(false, 1);
//        String newUuid = "00001111";
//        assertNotEquals(uuid, newUuid);
//
//        RequestSpecification getRequest = getBasicAdminRequest();
//        Response getResponse = getRequest.get(new URI("https://localhost:8181/PAS/rest/users/" + uuid));
//        String ETag = getResponse.header("ETag");
//
//        RequestSpecification requestPut = getBasicAdminRequest();
//        requestPut.contentType("application/json");
//        requestPut.header("If-Match", ETag);
//
//        String JSON = "{\n" +
//                "        \"name\": \"Adam\",\n" +
//                "        \"role\": \"Admin\",\n" +
//                "        \"password\": \"adamski1\",\n" +
//                "        \"surname\": \"Adamski\",\n" +
//                "        \"login\": \"" + "aAdamski" + "\",\n" +
//                "        \"uuid\": \"" + newUuid + "\"\n" +
//                "    }";
//        requestPut.body(JSON);
//
//        Response responseFromPut = requestPut.put("https://localhost:8181/PAS/rest/users/admin/" + uuid);
//        assertEquals(406, responseFromPut.statusCode());
//    }
//
//    @Test
//    public void updateTokenInactiveUser() throws URISyntaxException {
//        RequestSpecification clientRequest = RestAssured.given();
//        clientRequest.relaxedHTTPSValidation();
//        clientRequest.header(new Header("Authorization", "Bearer " + userToken));
//        Response clientResponse = clientRequest.get(new URI("https://localhost:8181/PAS/rest/auth/update"));
//        assertEquals(202, clientResponse.statusCode());
//
//        String uuid = getUUID(false, 4);
//        RequestSpecification requestGet = getBasicAdminRequest();
//        Response getResponse = requestGet.get(new URI("https://localhost:8181/PAS/rest/users/" + uuid));
//        assertEquals(200, getResponse.statusCode());
//
//        String ETag = getResponse.header("ETag");
//        RequestSpecification requestPut = getBasicAdminRequest();
//        requestPut.contentType("application/json");
//        requestPut.header("If-Match", ETag);
//        String JSON = "{\n" +
//                "        \"ageOfTheYoungestChild\": 4,\n" +
//                "        \"numberOfChildren\": 3,\n" +
//                "        \"role\": \"Client\",\n" +
//                "        \"password\": \"urban123\",\n" +
//                "        \"name\": \"Jan\",\n" +
//                "        \"surname\": \"Urban\",\n" +
//                "        \"active\": \"false\",\n" +
//                "        \"login\": \"" + "jUrban" + "\",\n" +
//                "        \"uuid\": \"" + uuid + "\"\n" +
//                "    }";
//        requestPut.body(JSON);
//        Response responseFromPut = requestPut.put("https://localhost:8181/PAS/rest/users/client/" + uuid);
//        assertEquals(200, responseFromPut.statusCode());
//
//
//        clientResponse = clientRequest.get(new URI("https://localhost:8181/PAS/rest/auth/update"));
//        assertEquals(401, clientResponse.statusCode());
//    }
//
//    private RequestSpecification getBasicAdminRequest() {
//        RequestSpecification request = RestAssured.given();
//        request.header(new Header("Authorization", "Bearer " + adminToken));
//        request.relaxedHTTPSValidation();
//        return request;
//    }
//
//    private Date getDateFromToken(String token) {
//        Date date = null;
//        try {
//            SignedJWT jwtToken = SignedJWT.parse(token);
//            date = (Date) (jwtToken.getJWTClaimsSet().getClaim("exp"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return date;
//    }
//
//    private String getUUID(boolean fromResources, int number) throws URISyntaxException {
//        RequestSpecification request = getBasicAdminRequest();
//        Response getAllResponse;
//        if (fromResources) {
//            getAllResponse = request.get(new URI("https://localhost:8181/PAS/rest/resources"));
//        } else {
//            getAllResponse = request.get(new URI("https://localhost:8181/PAS/rest/users"));
//        }
//        String responseString = getAllResponse.asString();
//        Pattern pattern = Pattern.compile("(?<=\"uuid\":\")\\d{8}");
//        Matcher m = pattern.matcher(responseString);
//        for(int i = 0; i < number; i++) {
//            m.find();
//        }
//        return m.group();
//    }
//}
