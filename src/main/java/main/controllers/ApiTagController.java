package main.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.service.TagServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class ApiTagController {
    private final TagServiceImpl tagServiceImpl;

    @GetMapping(value = "tag")
    public ResponseEntity<?> getTags() {
        log.info("Получен GET запрос api/tag");
        return new ResponseEntity<>(tagServiceImpl.getAllTags(), HttpStatus.OK);
    }

    @GetMapping(value = "tag/", params = {"query"})
    public ResponseEntity<?> getTagsWithParameters(@RequestParam(value = "query") String query) {
        log.info("Получен GET запрос api/tag с параметром query");
        return new ResponseEntity<>(tagServiceImpl.getTagsWithQuery(query), HttpStatus.OK);
    }
}
