package com.kva.starwars.exception;

import com.kva.starwars.model.SearchRequest;

public class InvalidRequestException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 753147791678770625L;

    private SearchRequest searchRequest;

    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(SearchRequest searchRequest) {
        super();
        this.searchRequest = searchRequest;
    }

    public InvalidRequestException(String message) {
        super(message);
    }

}
