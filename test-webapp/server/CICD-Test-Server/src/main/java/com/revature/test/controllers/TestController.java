package com.revature.test.controllers;

import com.revature.test.models.TestModel;
import com.revature.test.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health")
public class TestController {
    private TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping(value = "/ping")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody TestModel ping() {
        return testService.logPing();
    }



}
