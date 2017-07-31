package com.company;

public enum Status {
    INITIAL ("Initial"),
    ASSIGNED ("Assigned"),
    IN_PROGRESS ("In Progress"),
    DONE ("Done");

    private String status;

    Status (String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
