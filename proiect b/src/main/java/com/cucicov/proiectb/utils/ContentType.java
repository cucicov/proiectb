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

    /**
     * Returns the enum constant of this type with the specified value.
     * 
     * @param value the string value to search for
     * @return the enum constant with the specified value
     * @throws IllegalArgumentException if no enum constant with the specified value is found
     */
    public static ContentType valueOfString(String value) {
        for (ContentType contentType : values()) {
            if (contentType.value.equals(value)) {
                return contentType;
            }
        }
        throw new IllegalArgumentException("No enum constant with value: " + value);
    }
}
