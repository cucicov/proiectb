package com.cucicov.proiectb.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/*
    Represents a READ entity.
 */
@Entity
public class ClientInputRecord {

    @Id
    @Column(name="public_token", nullable = false, unique = true)
    private String publicToken;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    private List<ClientInputLog> accessLogs = new ArrayList<>();

    public String getPublicToken() {
        return publicToken;
    }

    public void setPublicToken(String publicToken) {
        this.publicToken = publicToken;
    }

    public List<ClientInputLog> getAccessLogs() {
        return accessLogs;
    }

    public void setAccessLogs(List<ClientInputLog> accessLogs) {
        this.accessLogs = accessLogs;
    }
}
