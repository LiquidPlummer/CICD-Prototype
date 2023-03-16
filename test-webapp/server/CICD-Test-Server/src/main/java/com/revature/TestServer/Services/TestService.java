package com.revature.TestServer.Services;

import com.revature.TestServer.Repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    private TestRepository repo;

    @Autowired
    public TestService(TestRepository repo) {
        this.repo = repo;
    }
}
