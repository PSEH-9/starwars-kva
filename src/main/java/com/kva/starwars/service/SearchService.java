package com.kva.starwars.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kva.starwars.exception.InvalidRequestException;
import com.kva.starwars.exception.InvalidTypeException;
import com.kva.starwars.exception.ResultsNotFoundException;
import com.kva.starwars.model.EntityTypes;
import com.kva.starwars.model.SearchRequest;

/**
 * 
 * @author kararora0
 *
 */
@Service
public class SearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchService.class);

    @Value("${swapi.service.url}")
    private String serviceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 
     * @param searchRequest
     * @return
     */
    public JsonNode searchStarWars(final SearchRequest searchRequest) {
        LOGGER.debug("searchStarWars: type:{} name:{} serviceUrl:{}", searchRequest.getType(),
                searchRequest.getName(), serviceUrl);
        ObjectNode responseNode = objectMapper.createObjectNode();
        populateRequestDetailsInResponseNode(searchRequest, responseNode);

        // Validate Request
        validateRequest(searchRequest);
        return invokeStarWarsAPI(searchRequest, responseNode);
    }

    /**
     * 
     * @param searchRequest
     */
    private void validateRequest(final SearchRequest searchRequest) {
        if (!isRequestValid(searchRequest)) {
            StringBuilder errorMessageBuilder = new StringBuilder("Invalid Request:");
            if (searchRequest != null) {
                if (StringUtils.isBlank(searchRequest.getType())) {
                    errorMessageBuilder.append("Type is Blank.");
                }
                if (StringUtils.isBlank(searchRequest.getName())) {
                    errorMessageBuilder.append("Name is Blank.");
                }
            }
            throw new InvalidRequestException(errorMessageBuilder.toString());
        }

        if (!isTypeValid(searchRequest.getType())) {
            throw new InvalidTypeException(searchRequest);
        }
    }

    /**
     * 
     * @param searchRequest
     * @param responseNode
     * @return
     */
    private JsonNode invokeStarWarsAPI(SearchRequest searchRequest, ObjectNode responseNode) {
        String wsURL = StringUtils.join(serviceUrl, searchRequest.getType(), "/", "?search=",
                searchRequest.getName());
        LOGGER.debug("invokeStarWarsAPI: wsURL:{}", wsURL);
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(wsURL, JsonNode.class);
        LOGGER.debug("SWAPI Response: Status:{}", response.getStatusCode());
        JsonNode responseBody = response.getBody();
        processWSResponse(responseNode, responseBody);
        responseNode.put("status", "success");
        return responseNode;
    }

    /**
     * 
     * @param responseNode
     * @param responseBody
     */
    private void processWSResponse(ObjectNode responseNode, JsonNode responseBody) {
        int count = 0;
        if (responseBody.has("count")) {
            count = responseBody.get("count").intValue();
        }
        LOGGER.debug("Results Count:{}", count);
        if (count == 0) {
            LOGGER.error("No results found for the given search request. WS Response:{}",
                    responseBody);
            throw new ResultsNotFoundException("No results found for the given search request");
        }

        responseNode.put("count", count);

        JsonNode results = null;
        if (responseBody.has("results")) {
            results = responseBody.get("results");
        }
        responseNode.set("results", results);

        if (results != null && results.isArray() && results.size() > 0) {
            JsonNode resultNode = results.get(0);
            if (resultNode.has("films")) {
                responseNode.set("films", resultNode.get("films"));
            }
        }
    }

    /**
     * 
     * @param searchRequest
     * @return
     */
    private boolean isRequestValid(SearchRequest searchRequest) {
        return searchRequest != null && StringUtils.isNotBlank(searchRequest.getType())
                && StringUtils.isNotBlank(searchRequest.getName());
    }

    /**
     * 
     * @param searchRequest
     * @param responseNode
     */
    private void populateRequestDetailsInResponseNode(SearchRequest searchRequest,
            ObjectNode responseNode) {
        ObjectNode request = objectMapper.createObjectNode();
        request.put("type", searchRequest.getType());
        request.put("name", searchRequest.getName());
        responseNode.set("request", request);
    }

    /**
     * 
     * @param type
     * @return
     */
    private boolean isTypeValid(String type) {
        try {
            EntityTypes.valueOf(StringUtils.upperCase(type));
            LOGGER.debug("Type :{} is Valid", type);
            return Boolean.TRUE;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid Type:{}", type);
        }
        return Boolean.FALSE;
    }

}
