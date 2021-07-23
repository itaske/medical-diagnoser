package com.medic.respositories;

import com.medic.models.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

}
