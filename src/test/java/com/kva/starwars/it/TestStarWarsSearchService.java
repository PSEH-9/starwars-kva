package com.kva.starwars.it;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestStarWarsSearchService {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void testSearchPlanet() {
        ResponseEntity<JsonNode> response = invokeSearchAPI("planets", "Tatooine");
        assertSuccessResponse(response);
    }

    @Test
    public void testSearchPeople() {
        ResponseEntity<JsonNode> response = invokeSearchAPI("people", "r2");
        assertSuccessResponse(response);
    }

    @Test
    public void testSearchFilms() {
        ResponseEntity<JsonNode> response = invokeSearchAPI("films", "hope");
        assertSuccessResponse(response);
    }

    @Test
    public void testSearchSpecies() {
        ResponseEntity<JsonNode> response = invokeSearchAPI("species", "Wookie");
        assertSuccessResponse(response);
    }

    @Test
    public void testSearchVehicles() {
        ResponseEntity<JsonNode> response = invokeSearchAPI("vehicles", "Crawler");
        assertSuccessResponse(response);
    }

    @Test
    public void testSearchStarships() {
        ResponseEntity<JsonNode> response = invokeSearchAPI("starships", "Star");
        assertSuccessResponse(response);
    }
    
    
    @Test
    public void testInvalidType() {
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(
                createURLWithPort("/swapi/search?type=planet&name=Tatooine"), JsonNode.class);
        Assert.assertEquals(response.getStatusCode().value(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        Assert.assertNotNull(response.getBody());
        JsonNode responseBody = response.getBody();
        Assert.assertTrue(responseBody.has("request"));
        Assert.assertTrue(responseBody.has("status"));
        String status = responseBody.get("status").asText();
        Assert.assertTrue(StringUtils.equals(status, "failure"));
        Assert.assertTrue(responseBody.has("errorMessage"));
    }
    
    @Test
    public void testBadRequest(){
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(
                createURLWithPort("/swapi/search?type=&name="), JsonNode.class);
        Assert.assertEquals(response.getStatusCode().value(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        Assert.assertNotNull(response.getBody());
        JsonNode responseBody = response.getBody();
        Assert.assertTrue(responseBody.has("request"));
        Assert.assertTrue(responseBody.has("status"));
        String status = responseBody.get("status").asText();
        Assert.assertTrue(StringUtils.equals(status, "failure"));
        Assert.assertTrue(responseBody.has("errorMessage"));
    }
    
    

    private ResponseEntity<JsonNode> invokeSearchAPI(String type, String name) {
        return restTemplate.getForEntity(
                createURLWithPort(
                        MessageFormat.format("/swapi/search?type={0}&name={1}", type, name)),
                JsonNode.class);
    }

    private void assertSuccessResponse(ResponseEntity<JsonNode> response) {
        Assert.assertEquals(response.getStatusCode().value(), HttpStatus.SC_OK);
        Assert.assertNotNull(response.getBody());

        JsonNode responseBody = response.getBody();

        Assert.assertTrue(responseBody.has("request"));

        Assert.assertTrue(responseBody.has("status"));
        String status = responseBody.get("status").asText();
        Assert.assertTrue(StringUtils.equals(status, "success"));

        Assert.assertTrue(responseBody.has("count"));
        int count = responseBody.get("count").intValue();

        Assert.assertTrue(count >= 1);

        Assert.assertTrue(responseBody.has("results"));
    }

    

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
