//package com.mycompany.firstapplication.RestServices;
//
//import com.mycompany.firstapplication.Security.JWTGeneratorVerifier;
//import io.restassured.RestAssured;
//import io.restassured.http.Header;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//import org.apache.commons.lang3.StringUtils;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//
//import javax.security.enterprise.identitystore.CredentialValidationResult;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//public class EmploymentAndAuthorizationTest {
//    @Test
//    public void adminAuthorization() throws URISyntaxException {
//        RequestSpecification requestPost = getBasicRequest();
//        requestPost.contentType("application/json");
//        String JSON = "{\n" +
//                "        \"login\": \"aAdamski\",\n" +
//                "        \"password\": \"adamski\"\n" +
//                "    }";
//        requestPost.body(JSON);
//        Response responseFromAuth = requestPost.post(new URI("https://localhost:8181/PAS/rest/auth"));
//        String token = responseFromAuth.getBody().asString();
//
//        RequestSpecification requestGet = getBasicRequest();
//        requestGet.header(new Header("Authorization", "Bearer " + token));
//        String responseFromSelf = requestGet.get(new URI("https://localhost:8181/PAS/rest/users/_self")).asString();
//
//        assertTrue(responseFromSelf.contains("Admin"));
//        assertTrue(responseFromSelf.contains("aAdamski"));
//        assertTrue(responseFromSelf.contains("Adam"));
//        assertFalse(responseFromSelf.contains("adamski"));
//    }
//
//    @Test
//    public void superUserAuthorization() throws URISyntaxException {
//        RequestSpecification requestPost = getBasicRequest();
//        requestPost.contentType("application/json");
//        String JSON = "{\n" +
//                "        \"login\": \"tTomkowski\",\n" +
//                "        \"password\": \"tomkowski\"\n" +
//                "    }";
//        requestPost.body(JSON);
//        Response responseFromAuth = requestPost.post(new URI("https://localhost:8181/PAS/rest/auth"));
//        String token = responseFromAuth.getBody().asString();
//
//        RequestSpecification requestGet = getBasicRequest();
//        requestGet.header(new Header("Authorization", "Bearer " + token));
//        String responseFromSelf = requestGet.get(new URI("https://localhost:8181/PAS/rest/users/_self")).asString();
//
//        assertTrue(responseFromSelf.contains("SuperUser"));
//        assertTrue(responseFromSelf.contains("tTomkowski"));
//        assertTrue(responseFromSelf.contains("Tomek"));
//        assertFalse(responseFromSelf.contains("tomkowski"));
//    }
//
//    @Test
//    public void clientAuthorization() throws URISyntaxException {
//        RequestSpecification requestPost = getBasicRequest();
//        requestPost.contentType("application/json");
//        String JSON = "{\n" +
//                "        \"login\": \"tHajto\",\n" +
//                "        \"password\": \"hajto\"\n" +
//                "    }";
//        requestPost.body(JSON);
//        Response responseFromAuth = requestPost.post(new URI("https://localhost:8181/PAS/rest/auth"));
//        String token = responseFromAuth.getBody().asString();
//
//        RequestSpecification requestGet = getBasicRequest();
//        requestGet.header(new Header("Authorization", "Bearer " + token));
//        String responseFromSelf = requestGet.get(new URI("https://localhost:8181/PAS/rest/users/_self")).asString();
//
//        assertTrue(responseFromSelf.contains("Client"));
//        assertTrue(responseFromSelf.contains("tHajto"));
//        assertTrue(responseFromSelf.contains("Tomasz"));
//        assertFalse(responseFromSelf.contains("hajto"));
//    }
//
//    @Test
//    public void clientAuthorizationAndEmployment() throws URISyntaxException {
//        String strToFind = "client";
//        RequestSpecification requestPost = getBasicRequest();
//
//        requestPost.contentType("application/json");
//        String JSON = "{\n" +
//                "        \"login\": \"aWiadro\",\n" +
//                "        \"password\": \"wiadro\"\n" +
//                "    }";
//        requestPost.body(JSON);
//
//        Response responseFromAuth = requestPost.post(new URI("https://localhost:8181/PAS/rest/auth"));
//        String token = responseFromAuth.getBody().asString();
//
//        RequestSpecification request = getBasicRequest();
//        request.header(new Header("Authorization", "Bearer " + token));
//        request.contentType("application/json");
//
//        Response responseFromEmployment = request.get(new URI("https://localhost:8181/PAS/rest/employment"));
//
//        String responseFromEmploymentString = responseFromEmployment.asString();
//        int initialSize = StringUtils.countMatches(responseFromEmploymentString, strToFind);
//
//        String firstUuid = getFirstUUID(token);
//        request.post(new URI("https://localhost:8181/PAS/rest/employment/" + firstUuid));
//
//        responseFromEmployment = request.get(new URI("https://localhost:8181/PAS/rest/employment"));
//
//        responseFromEmploymentString = responseFromEmployment.asString();
//        int sizeAfterEmploy = StringUtils.countMatches(responseFromEmploymentString, strToFind);
//
//        assertEquals(initialSize + 1, sizeAfterEmploy);
//        assertTrue(responseFromEmploymentString.contains("aWiadro"));
//        assertTrue(responseFromEmploymentString.contains("Agata"));
//    }
//
//    private RequestSpecification getBasicRequest() {
//        RequestSpecification request = RestAssured.given();
//        request.relaxedHTTPSValidation();
//        return request;
//    }
//
//    private String getFirstUUID(String token) throws URISyntaxException {
//        RequestSpecification request = getBasicRequest();
//        request.header(new Header("Authorization", "Bearer " + token));
//        Response getAllResponse = request.get(new URI("https://localhost:8181/PAS/rest/resources"));
//        String responseString = getAllResponse.asString();
//        Pattern pattern = Pattern.compile("(?<=\"uuid\":\")\\d{8}");
//        Matcher m = pattern.matcher(responseString);
//        if (m.find()) {
//            return m.group();
//        }
//        return "";
//    }
//
//}
