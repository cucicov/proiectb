package com.cucicov.proiectb.services;

import com.cucicov.proiectb.exception.GeneralException;
import com.cucicov.proiectb.model.AdminInputRecord;
import com.cucicov.proiectb.model.ClientInputLog;
import com.cucicov.proiectb.model.ClientInputRecord;
import com.cucicov.proiectb.model.dto.AdminInputRecordDTO;
import com.cucicov.proiectb.model.dto.ClientInputLogDTO;
import com.cucicov.proiectb.repository.AdminInputRecordRepository;
import com.cucicov.proiectb.repository.ClientInputLogRepository;
import com.cucicov.proiectb.repository.ClientInputRecordRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MainService {

    private final AdminInputRecordRepository adminRepository;
    private final ClientInputRecordRepository clientRepository;
    private final ClientInputLogRepository clientLogRepository;
    private final ModelMapper modelMapper;

    public MainService(AdminInputRecordRepository repository, ClientInputRecordRepository clientRepository,
                       ClientInputLogRepository clientLogRepository, ModelMapper modelMapper) {
        this.adminRepository = repository;
        this.clientRepository = clientRepository;
        this.clientLogRepository = clientLogRepository;
        this.modelMapper = modelMapper;
    }

    public List<AdminInputRecordDTO> getAllRecords() {
        List<AdminInputRecord> inputs = this.adminRepository.findAll();
        List<AdminInputRecordDTO> returnInputs = new ArrayList<>();
        for (AdminInputRecord input : inputs) {
            returnInputs.add(this.modelMapper.map(input, AdminInputRecordDTO.class));
        }
        return returnInputs;
    }

    public AdminInputRecordDTO getLatestContent() {
        List<AdminInputRecord> latestRecordMetadata =
                this.adminRepository.findLatestRecordMetadata(PageRequest.of(0, 1)).getContent();
        if (latestRecordMetadata.size() != 1) {
            throw new GeneralException("Entries found for the latest content: " + latestRecordMetadata.size());
        }
        return this.modelMapper.map(latestRecordMetadata.get(0), AdminInputRecordDTO.class);
    }

    public Optional<AdminInputRecordDTO> getDataByPublicToken(String publicToken) {
        AdminInputRecord dataByPublicToken = this.adminRepository.getDataByPublicToken(publicToken);
        return Optional.ofNullable(
                this.modelMapper.map(dataByPublicToken, AdminInputRecordDTO.class)
        );
    }

    @Async
    public void recordClientCall(String uuid) {
        this.recordClientCall(uuid, new ClientInputLogDTO());
    }

    @Async
    public void recordClientCall(String uuid, ClientInputLogDTO log) {
        ClientInputLog newClientLog = this.modelMapper.map(log, ClientInputLog.class);
        if (newClientLog.getDateAccessed() == null) {
            newClientLog.setDateAccessed(Instant.now());
        }

        Optional<ClientInputRecord> clientInput = this.clientRepository.findById(uuid);
        if (clientInput.isPresent()) {
            ClientInputRecord clientEntity = clientInput.get();
            clientEntity.getAccessLogs().add(newClientLog);
            this.clientRepository.save(clientEntity);
        } else {
            ClientInputRecord newClient = new ClientInputRecord();
            newClient.setPublicToken(uuid);
            newClient.getAccessLogs().add(newClientLog);
            this.clientRepository.save(newClient);
        }
    }

}
