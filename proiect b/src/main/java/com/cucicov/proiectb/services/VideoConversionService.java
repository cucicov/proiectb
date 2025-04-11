package com.cucicov.proiectb.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class VideoConversionService {

    /**
     * Converts a MOV video file to MP4 format using FFmpeg.
     *
     * @param inputPath Path to the input MOV file.
     * @param outputPath Path for the converted MP4 file.
     * @return true if the conversion succeeds, false otherwise.
     */
    public boolean convertMovToMp4(String inputPath, String outputPath) {
        // Check if the input file exists
        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            System.err.println("Input file not found: " + inputPath);
            return false;
        }

        // Build the ffmpeg command
        String[] ffmpegCommand = {
                "ffmpeg",
                "-i", inputPath,         // Input file path
                "-vcodec", "libx264",    // Video codec
                "-acodec", "aac",        // Audio codec
                outputPath               // Output file path
        };

        try {
            // Run the ffmpeg command
            ProcessBuilder processBuilder = new ProcessBuilder(ffmpegCommand);
            processBuilder.redirectErrorStream(true); // Merge stdout and stderr for better logging
            Process process = processBuilder.start();

            // Wait for completion
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Video successfully converted! Output: " + outputPath);
                return true;
            } else {
                System.err.println("FFmpeg process failed with exit code: " + exitCode);
                return false;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}