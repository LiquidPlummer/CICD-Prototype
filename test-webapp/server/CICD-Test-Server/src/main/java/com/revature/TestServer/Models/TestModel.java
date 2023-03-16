package com.revature.TestServer.Models;

import jakarta.persistence.*;

@Entity(name = "test")
@Table(schema = "public")
public class TestModel {
    @Id
    @Column(name = "test_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String timestamp;
}
