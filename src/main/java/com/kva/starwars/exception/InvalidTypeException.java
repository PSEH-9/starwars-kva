package com.kva.starwars.exception;

import com.kva.starwars.model.SearchRequest;

public class InvalidTypeException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 5820963010585583332L;

    private SearchRequest searchRequest;

    public InvalidTypeException(SearchRequest searchRequest) {
        super();
        this.searchRequest = searchRequest;
    }

    @Override
    public String getMessage() {

        if (searchRequest != null) {
            return searchRequest.getType() + " is a Invaild Type.";
        }

        return "Type is a Invaild Type.";
    }

}
