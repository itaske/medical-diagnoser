package com.medic.respositories;

import com.medic.models.DiagnosisRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DiagnosisRecordRepository extends JpaRepository<DiagnosisRecord, Long> {
}
