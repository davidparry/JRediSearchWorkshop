package com.davidparry.controller;

import com.davidparry.model.AjaxResponseBody;
import com.davidparry.model.AjaxResults;
import com.davidparry.model.SearchCriteria;
import com.davidparry.model.Suggestions;
import com.davidparry.services.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class SearchController {

    Logger logger = LoggerFactory.getLogger(SearchController.class);
    @Autowired
    SearchService searchService;

    @PostMapping("/api/search")
    public ResponseEntity<?> getSearchResults(@Valid @RequestBody SearchCriteria search, Errors errors) {
        String msg = "found";
        if (errors.hasErrors()) {
            msg = errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(","));
            logger.error("An error occured with input" + msg);
            return ResponseEntity.badRequest().body(new AjaxResponseBody("error", null));
        }
        AjaxResults results = searchService.findByTerm(search.getTerm());
        if (results.getTotal() < 1) {
            msg = "missed";
        }
        return ResponseEntity.ok(new AjaxResponseBody(msg, results));
    }

    @RequestMapping(value = "/api/partial", method = RequestMethod.GET)
    public ResponseEntity<Suggestions> getSuggestions(@RequestParam("partialTerm") String partialTerm) {
        return ResponseEntity.ok(searchService.suggestTerm(partialTerm));

    }

}
