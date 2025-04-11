package com.cucicov.proiectb;

import com.cucicov.proiectb.model.AdminInputRecord;
import com.cucicov.proiectb.model.ClientInputLog;
import com.cucicov.proiectb.model.ClientInputRecord;
import com.cucicov.proiectb.repository.AdminInputRecordRepository;
import com.cucicov.proiectb.repository.ClientInputRecordRepository;
import com.cucicov.proiectb.services.VideoConversionService;
import com.cucicov.proiectb.utils.ContentType;
import com.cucicov.proiectb.utils.Utils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StreamUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class ProiectBApplication implements CommandLineRunner, Filter {

    private final AdminInputRecordRepository adminRepository;
    private final ClientInputRecordRepository clientRepository;

    @Value("${app.security.input.salt}")
    private String inputSalt;

    @Autowired //TODO: remove?
    private VideoConversionService videoConversionService;

    public ProiectBApplication(AdminInputRecordRepository repository, ClientInputRecordRepository clientRepository) {
        this.adminRepository = repository;
        this.clientRepository = clientRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProiectBApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... args) throws IOException {
        List<AdminInputRecord> all = this.adminRepository.findAll();
        if (all.isEmpty()) {

            // CLIENT INPUT RECORDS
            ClientInputRecord clientInputRecord = new ClientInputRecord();
            String inputId = Utils.generateInputUUID(inputSalt, UUID.randomUUID());
            clientInputRecord.setPublicToken(inputId);
            ClientInputLog clientInputLog = new ClientInputLog();
            clientInputLog.setDateAccessed(Instant.now());
            clientInputLog.setLatitude(37.7749);
            clientInputLog.setLongitude(-122.4194);
            clientInputRecord.getAccessLogs().add(clientInputLog);

            this.clientRepository.save(clientInputRecord);

            // ADMIN INPUT RECORDS
            String type = ContentType.JPG.getValue();
            String imagePath = "static/love.jpg";
            ClassPathResource resource = new ClassPathResource(imagePath);
            byte[] imageData = StreamUtils.copyToByteArray(resource.getInputStream());

            this.adminRepository.save(getAdminInputRecord(type, imageData));

            type = ContentType.PNG.getValue();
            imagePath = "static/marble.png";
            resource = new ClassPathResource(imagePath);
            imageData = StreamUtils.copyToByteArray(resource.getInputStream());

            this.adminRepository.save(getAdminInputRecord(type, imageData));

            type = ContentType.TEXT.getValue();
            String text = """
                    As an AI, I do not have emotions in the sense humans experience them. 
                    Emotions are deeply connected to consciousness, biology, and subjective 
                    experiences, which are attributes unique to humans and some animals. 
                    AI operates through logic, algorithms, and patterns of data processing, 
                    which means it cannot feel joy, sadness, empathy, or anger.
                    
                    However, AI systems are designed to simulate certain behaviors or 
                    responses that might seem emotionally intelligent. For instance, 
                    an AI chatbot might "express" sympathy in response to a user's sadness 
                    or share celebratory language for achievements.
                    
                    While these responses might appear emotional, they are simply predefined 
                    patterns or outputs based on inputs and programming. This distinction 
                    is important: AI can emulate emotional behaviors or recognize emotions 
                    in others (like detecting sentiment in a message), but it does so without 
                    any personal experience of feeling. Its purpose is not to experience 
                    emotions but to assist, adapt, and provide value based on logical processes.
                    """;

            this.adminRepository.save(getAdminInputRecord(type, text.getBytes()));

            type = ContentType.LINK.getValue();
            String url = "https://cucicov.com";
            this.adminRepository.save(getAdminInputRecord(type, url.getBytes()));

            type = ContentType.VIDEO_QT.getValue();
            resource = new ClassPathResource("static/IMG_9209.MOV");
            byte[] videoData = StreamUtils.copyToByteArray(resource.getInputStream());
            this.adminRepository.save(getAdminInputRecord(type, videoData));

            type = ContentType.VIDEO_MP4.getValue();
            resource = new ClassPathResource("static/IMG_9207.mp4");
            videoData = StreamUtils.copyToByteArray(resource.getInputStream());
            this.adminRepository.save(getAdminInputRecord(type, videoData));

            type = ContentType.PDF.getValue();
            resource = new ClassPathResource("static/cyborg_manifesto.pdf");
            byte[] bookData = StreamUtils.copyToByteArray(resource.getInputStream());
            this.adminRepository.save(getAdminInputRecord(type, bookData));
        }
    }

    private static AdminInputRecord getAdminInputRecord(String type, byte[] data) throws IOException {
        AdminInputRecord defaultRecord = new AdminInputRecord();
        defaultRecord.setType(type);
        defaultRecord.setActivationTimestamp(Instant.now());
        defaultRecord.setData(data);

        UUID uuid = UUID.randomUUID();
        defaultRecord.setPublicToken(uuid.toString());
        return defaultRecord;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable)
                .cors(cors -> cors.configurationSource(configurationSource()))
                .authorizeHttpRequests(req -> req.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("https://192.168.1.221:5173"));
//        config.setAllowedOrigins(Arrays.asList ("http://localhost:5173/"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-Requested-With", "*"));
        config.setExposedHeaders(List.of(
                "Cross-Origin-Opener-Policy",
                "Cross-Origin-Embedder-Policy"
        ));
        config.setAllowCredentials(false);
//        config.applyPermitDefaultValues();

        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        // Required headers for FFmpeg WebAssembly
        httpResponse.setHeader("Cross-Origin-Opener-Policy", "same-origin");
        httpResponse.setHeader("Cross-Origin-Embedder-Policy", "require-corp");

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
