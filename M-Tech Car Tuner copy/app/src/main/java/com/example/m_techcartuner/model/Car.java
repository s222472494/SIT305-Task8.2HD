package com.example.m_techcartuner.model;

public class Car {
    private String id;
    private String make;
    private String model;
    private String year;
    private String engine;


    public Car() {

    }

    public Car(String id, String make, String model, String year, String engine) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.engine = engine;
    }

    // Getters and setters for all fields
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMake() {
        return make;
    }
    public void setMake(String make) {
        this.make = make;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public String getEngine() {
        return engine;
    }
    public void setEngine(String engine) {
        this.engine = engine;
    }
}
