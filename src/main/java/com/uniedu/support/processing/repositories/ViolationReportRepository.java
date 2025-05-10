package com.uniedu.support.processing.repositories;

import com.uniedu.support.processing.models.entities.ViolationReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViolationReportRepository extends JpaRepository<ViolationReport, Long> {
}
