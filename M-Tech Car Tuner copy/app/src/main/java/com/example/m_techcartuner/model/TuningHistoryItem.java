package com.example.m_techcartuner.model;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class TuningHistoryItem {
    private String make;
    private String model;
    private String year;
    private String engine;
    private String goal;
    private String documentId;
    private String aiAdvice;
    @ServerTimestamp
    private Date timestamp;

    public TuningHistoryItem() {}

    // Getters and setters

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getEngine() { return engine; }
    public void setEngine(String engine) { this.engine = engine; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public String getAiAdvice() { return aiAdvice; }
    public void setAiAdvice(String aiAdvice) { this.aiAdvice = aiAdvice; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
