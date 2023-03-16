package com.revature.TestServer.Controllers;

import com.revature.TestServer.Services.TestService;
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
    public @ResponseBody String ping() {
        return "{\"thing\": \"pong!\"}";
    }

}
