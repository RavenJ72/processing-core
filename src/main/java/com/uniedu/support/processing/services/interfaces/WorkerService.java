package com.uniedu.support.processing.services.interfaces;

import com.uniedu.support.processing.models.enums.WorkerStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;


public interface WorkerService {
    void completeTicket(Long id, UserDetails userDetails);
    void changeWorkerStatus(WorkerStatus status, UserDetails userDetails);
}
