package com.cucicov.proiectb.repository;

import com.cucicov.proiectb.model.AdminInputRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminInputRecordRepository extends JpaRepository<AdminInputRecord, Long> {

    @Query(value = "SELECT new AdminInputRecord(i.publicToken, i.activationTimestamp, i.type) from AdminInputRecord i ORDER BY i.activationTimestamp DESC")
    Page<AdminInputRecord> findLatestRecordMetadata(Pageable pageable);

    @Query(value = "SELECT i FROM AdminInputRecord i WHERE i.publicToken = :publicToken")
    AdminInputRecord getDataByPublicToken(String publicToken);
}
