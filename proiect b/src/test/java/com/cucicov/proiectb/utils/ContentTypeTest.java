package com.cucicov.proiectb.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContentTypeTest {

    @Test
    public void testValueOfString() {
        // Test all enum values
        assertEquals(ContentType.PNG, ContentType.valueOfString("image/png"));
        assertEquals(ContentType.JPG, ContentType.valueOfString("image/jpg"));
        assertEquals(ContentType.TEXT, ContentType.valueOfString("text/plain"));
        assertEquals(ContentType.LINK, ContentType.valueOfString("text/html"));
        assertEquals(ContentType.VIDEO_QT, ContentType.valueOfString("video/quicktime"));
        assertEquals(ContentType.VIDEO_MP4, ContentType.valueOfString("video/mp4"));
        assertEquals(ContentType.PDF, ContentType.valueOfString("application/pdf"));
        
        // Test exception for invalid value
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ContentType.valueOfString("invalid/value");
        });
        
        assertTrue(exception.getMessage().contains("No enum constant with value: invalid/value"));
    }
}