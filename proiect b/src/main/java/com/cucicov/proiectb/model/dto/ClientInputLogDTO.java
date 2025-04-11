package com.cucicov.proiectb.model.dto;

import java.time.Instant;

public class ClientInputLogDTO {
    private Instant dateAccessed;
    private double latitude;
    private double longitude;

    public Instant getDateAccessed() {
        return dateAccessed;
    }

    public void setDateAccessed(Instant dateAccessed) {
        this.dateAccessed = dateAccessed;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
