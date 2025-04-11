package com.cucicov.proiectb.repository;

import com.cucicov.proiectb.model.AdminInputRecord;
import com.cucicov.proiectb.model.ClientInputRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientInputRecordRepository extends JpaRepository<ClientInputRecord, String> {
}
