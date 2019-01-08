package com.kva.starwars.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.kva.starwars.model.SearchRequest;
import com.kva.starwars.service.SearchService;

@RestController
@RequestMapping("/swapi")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
    public JsonNode searchStarWars(@RequestParam(value="type", required=false, defaultValue="") String type,
            @RequestParam(value="name", required=false, defaultValue="") String name) {
        return searchService.searchStarWars(constructSearchRequest(type, name));
    }

    private SearchRequest constructSearchRequest(String type, String name) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setType(type);
        searchRequest.setName(name);
        return searchRequest;
    }

}
