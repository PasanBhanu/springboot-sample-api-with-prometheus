package com.pasan.samples.springbootapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleApi {

    Logger log = LoggerFactory.getLogger(SampleApi.class);

    @GetMapping(path = "/sample", produces = "application/json")
    public ResponseEntity<SampleResponse> sampleGetApi() {
        log.info("Responding to GET /sample");
        return ResponseEntity.ok(new SampleResponse("success"));
    }

}
