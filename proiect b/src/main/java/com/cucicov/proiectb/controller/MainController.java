package com.cucicov.proiectb.controller;

import com.cucicov.proiectb.exception.ContentRecordNotFound;
import com.cucicov.proiectb.exception.GeneralException;
import com.cucicov.proiectb.model.dto.AdminInputRecordDTO;
import com.cucicov.proiectb.model.dto.ClientInputLogDTO;
import com.cucicov.proiectb.services.MainService;
import com.cucicov.proiectb.services.VideoConversionService;
import com.cucicov.proiectb.utils.Utils;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/qrsource")
@CrossOrigin(origins = "https://192.168.1.221:5173")
public class MainController {

    private final MainService service;

    @Value("${app.security.input.salt}")
    private String inputSalt;

    public MainController(MainService service, VideoConversionService videoConversionService) {
        this.service = service;
    }

    @GetMapping("/content")
    public ResponseEntity<List<AdminInputRecordDTO>> getAllContent() {
        return ResponseEntity.ok(this.service.getAllRecords());
    }

    @GetMapping("/content/{token}")
    public ResponseEntity<byte[]> getContentByToken(@PathVariable String token) throws ContentRecordNotFound {
        AdminInputRecordDTO record = this.service.getDataByPublicToken(token).orElseThrow(
                () -> new ContentRecordNotFound("404", "Content Record not found", token));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(record.getType()));

        System.out.println("GET CONTENT FOR: " + token);

        return ResponseEntity.ok().headers(headers).body(record.getData());
    }

    @GetMapping("/recentcontent/metadata/{uuid}")
    public ResponseEntity<AdminInputRecordDTO> getContent(@PathVariable String uuid) {
        System.out.println("GET METADATA (" + LocalDateTime.now().format(DateTimeFormatter.ISO_TIME) + ")");
        boolean inputIdValid = !StringUtils.isBlank(uuid) && Utils.isInputIdValid(inputSalt, uuid);
        if (inputIdValid) {
            this.service.recordClientCall(uuid);
            return ResponseEntity.ok(this.service.getLatestContent());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/recentcontent/metadata/{uuid}")
    public ResponseEntity<AdminInputRecordDTO> getInput(@PathVariable String uuid, @RequestBody ClientInputLogDTO log) {
        System.out.println("UPDATE METADATA (" + LocalDateTime.now().format(DateTimeFormatter.ISO_TIME) + ")");
        boolean inputIdValid = !StringUtils.isBlank(uuid) && Utils.isInputIdValid(inputSalt, uuid);
        if (inputIdValid) {
            this.service.recordClientCall(uuid, log);
            return ResponseEntity.ok(this.service.getLatestContent());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/input")
    public ResponseEntity<String> getInput(@RequestBody AdminInputRecordDTO inputDTO) {
        System.out.println("INPUT:" + inputDTO);
        //TODO:
        return ResponseEntity.ok(inputDTO.getPublicToken());
    }

    @GetMapping("/input/generate") //TODO: hide behind password?
    public ResponseEntity<String> generateInputId() {
        String inputId = Utils.generateInputUUID(inputSalt, UUID.randomUUID());
        return ResponseEntity.ok(inputId);
    }

    @GetMapping("/input/validate/{uuid}") //TODO: remove? is it used?
    public ResponseEntity<String> validateInputId(@PathVariable String uuid) {
        boolean inputIdValid = Utils.isInputIdValid(inputSalt, uuid);
        if (inputIdValid)
            return ResponseEntity.ok(uuid);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input id");
    }

    @ExceptionHandler(ContentRecordNotFound.class)
    public ResponseEntity<ContentRecordNotFound> handleContentRecordNotFound(ContentRecordNotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex);
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<String> handleContentRecordNotFound(GeneralException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }


}
