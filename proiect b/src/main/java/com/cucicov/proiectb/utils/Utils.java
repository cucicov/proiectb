package com.cucicov.proiectb.utils;

import com.cucicov.proiectb.model.dto.AdminInputRecordDTO;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public class Utils {

    private static final String HMAC_ALGO = "HmacSHA256";
    private static AppConfigurationProperties appConfigurationProperties;

    public static void setAppConfigurationProperties(AppConfigurationProperties appConfigurationProperties) {
        Utils.appConfigurationProperties = appConfigurationProperties;
    }

    public static byte[] convertVideoToBytes(String filePath) throws IOException {
        File videoFile = new File(filePath);

        // Check if the file exists
        if (!videoFile.exists()) {
            throw new IOException("File not found: " + filePath);
        }

        // Read the video file into a byte array
        return Files.readAllBytes(videoFile.toPath());
    }

    public static String generateInputUUID(String salt, UUID id) {

        try {
            Mac hmac = Mac.getInstance(HMAC_ALGO);
            SecretKeySpec keySpec = new SecretKeySpec(salt.getBytes(), HMAC_ALGO);
            hmac.init(keySpec);

            byte[] signature = hmac.doFinal(id.toString().getBytes());

            return id + "." + Base64.getUrlEncoder().withoutPadding().encodeToString(signature);
        } catch (Exception e) {
            throw new RuntimeException("Error generating signed ID", e);
        }
    }

    public static boolean isInputIdValid(String salt, String uuid) {
        try {
            // Split the ID and the signature
            String[] parts = uuid.split("\\.");
            if (parts.length != 2) {
                return false;
            }

            String id = parts[0];
            String providedSignature = parts[1];

            // Recompute the signature for the ID
            Mac hmac = Mac.getInstance(HMAC_ALGO);
            SecretKeySpec keySpec = new SecretKeySpec(salt.getBytes(), HMAC_ALGO);
            hmac.init(keySpec);

            byte[] expectedSignature = hmac.doFinal(id.getBytes());

            // Compare the provided signature with the expected signature securely
            return Base64.getUrlEncoder().withoutPadding().encodeToString(expectedSignature).equals(providedSignature);
        } catch (Exception e) {
            return false; // Return false if any error occurs
        }

    }

    public static void initializeExpirationTime(AdminInputRecordDTO adminInput) {
        int timeoutMinutes = appConfigurationProperties.getTimeoutMinutes();

        if (adminInput.getActivationTimestamp() == null) {
            adminInput.setActivationTimestamp(Instant.now());
        }

        Instant expirationTime = adminInput.getActivationTimestamp().plusSeconds(timeoutMinutes * 60L);
        System.out.println(adminInput.getActivationTimestamp() + " -> " + expirationTime + " (" + timeoutMinutes + " minutes)");
        adminInput.setExpirationTimestamp(expirationTime);
    }

}
