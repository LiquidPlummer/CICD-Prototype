package com.revature.test.models;

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

    public TestModel() {
    }

    public TestModel(Integer id, String timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public TestModel(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
