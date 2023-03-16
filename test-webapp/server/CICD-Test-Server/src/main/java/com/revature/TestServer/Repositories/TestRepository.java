package com.revature.TestServer.Repositories;

import com.revature.TestServer.Models.TestModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestModel, Integer> {

}
