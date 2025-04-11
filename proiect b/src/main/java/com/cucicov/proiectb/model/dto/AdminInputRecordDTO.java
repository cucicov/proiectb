package com.cucicov.proiectb.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;
import java.util.Arrays;

public class AdminInputRecordDTO {
    private String type;
    private Instant activationTimestamp;
    private Instant expirationTimestamp;
    private String publicToken;
    @JsonIgnore
    private long latitude;
    @JsonIgnore
    private long longitude;

    @JsonIgnore
    private byte[] data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPublicToken() {
        return publicToken;
    }

    public void setPublicToken(String publicToken) {
        this.publicToken = publicToken;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Instant getActivationTimestamp() {
        return activationTimestamp;
    }

    public void setActivationTimestamp(Instant activationTimestamp) {
        this.activationTimestamp = activationTimestamp;
    }

    public Instant getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(Instant expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
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

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "AdminInputRecordDTO{" +
                "type='" + type + '\'' +
                ", activationTimestamp=" + activationTimestamp +
                ", expirationTimestamp=" + expirationTimestamp +
                ", publicToken='" + publicToken + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
