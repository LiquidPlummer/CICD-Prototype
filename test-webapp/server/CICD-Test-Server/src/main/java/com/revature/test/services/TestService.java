package com.revature.test.services;

import com.revature.test.models.TestModel;
import com.revature.test.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TestService {
    private TestRepository repo;

    @Autowired
    public TestService(TestRepository repo) {
        this.repo = repo;
    }

    public TestModel logPing() {
        TestModel model = new TestModel(LocalDateTime.now().toString());
        repo.save(model);
        return model;
    }
}
