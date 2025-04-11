package com.cucicov.proiectb.model;

import jakarta.persistence.*;

import java.time.Instant;

/*
    Represents a WRITE entity and the content for an Input.
 */
@Entity
public class AdminInputRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private long id;

    private String type;

    private Instant activationTimestamp;
    private Instant expirationTimestamp;

    @Column(nullable = false, updatable = false, name = "public_token")
    private String publicToken;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB") // max 16 MB.
    private byte[] data;

    private long latitude;

    private long longitude;

    public AdminInputRecord() {
        // nix.
    }

    public AdminInputRecord(String publicToken, Instant timestamp, String type) {
        this.publicToken = publicToken;
        this.activationTimestamp = timestamp;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getActivationTimestamp() {
        return activationTimestamp;
    }

    public void setActivationTimestamp(Instant timestamp) {
        this.activationTimestamp = timestamp;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getPublicToken() {
        return publicToken;
    }

    public void setPublicToken(String publicToken) {
        this.publicToken = publicToken;
    }

    public long getLatitude() {
        return latitude;
    }
    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }
    public long getLongitude() {
        return longitude;
    }

    public Instant getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(Instant expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "ContentRecord{" +
                "type='" + type + '\'' +
                ", timestamp=" + activationTimestamp +
                ", publicToken='" + publicToken + '\'' +
                ", data=" + (data.length > 0) +
                '}';
    }
}
