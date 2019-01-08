package com.kva.starwars.model;

import java.io.Serializable;

public class SearchRequest implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6309275225785172405L;

    private String type;
    private String name;

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
