package com.kva.starwars.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 
 * @author kararora0
 *
 */
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    
    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
    
    @Autowired
    private ObjectMapper objectMapper;

    @ExceptionHandler(InvalidTypeException.class)
    public final ResponseEntity<JsonNode> handleInvalidTypeException(InvalidTypeException ex,
            WebRequest request) {
        LOGGER.error("handleInvalidTypeException: Exception:", ex);
        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put("status", "failure");
        responseNode.put("errorMessage", ex.getMessage());

        populateRequestDetails(request, responseNode);

        return new ResponseEntity<>(responseNode, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(ResultsNotFoundException.class)
    public final ResponseEntity<JsonNode> handleResultsNotFoundException(ResultsNotFoundException ex,
            WebRequest request) {
        LOGGER.error("handleResultsNotFoundException: Exception:", ex);
        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put("status", "failure");
        responseNode.put("errorMessage", ex.getMessage());

        populateRequestDetails(request, responseNode);

        return new ResponseEntity<>(responseNode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<JsonNode> handleAllExceptions(Exception ex, WebRequest request) {
        LOGGER.error("handleAllExceptions: Exception:", ex);
        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put("status", "failure");
        responseNode.put("errorMessage", ex.getMessage());

        populateRequestDetails(request, responseNode);

        return new ResponseEntity<>(responseNode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void populateRequestDetails(WebRequest request, ObjectNode responseNode) {
        String type = request.getParameter("type");
        String name = request.getParameter("name");

        ObjectNode requestNode = objectMapper.createObjectNode();
        requestNode.put("type", type);
        requestNode.put("name", name);
        responseNode.set("request", requestNode);
    }
    
    
   
}
