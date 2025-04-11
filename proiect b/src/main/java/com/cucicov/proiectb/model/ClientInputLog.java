package com.cucicov.proiectb.model;

import jakarta.persistence.*;

import java.time.Instant;
/*
    Logs all the accesses to individual Input READ locations.
 */
@Entity
public class ClientInputLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, updatable = false, name = "date_accessed")
    private Instant dateAccessed;

    @Column(name="latitude")
    private double latitude;

    @Column(name="longitude")
    private double longitude;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "ClientInputLog{" +
//                "publicToken='" + publicToken + '\'' +
                ", dateAccessed=" + dateAccessed +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
