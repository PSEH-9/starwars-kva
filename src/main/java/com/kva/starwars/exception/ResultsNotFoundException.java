package com.kva.starwars.exception;

/**
 * 
 * @author kararora0
 *
 */
public class ResultsNotFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -1655946808037395851L;

    public ResultsNotFoundException() {
        super();
    }

    public ResultsNotFoundException(String message) {
        super(message);
    }
}
