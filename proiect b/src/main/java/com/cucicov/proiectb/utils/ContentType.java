package com.cucicov.proiectb.utils;

public enum ContentType {

    PNG("image/png"),
    JPG("image/jpg"),
    TEXT("text/plain"),
    LINK("text/html"),
    VIDEO_QT("video/quicktime"),
    VIDEO_MP4("video/mp4"),
    PDF("application/pdf");

    private String value;

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
