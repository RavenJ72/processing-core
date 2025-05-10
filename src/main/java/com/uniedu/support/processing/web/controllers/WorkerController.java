package com.uniedu.support.processing.web.controllers;

import com.uniedu.support.processing.dto.authEntities.MessageResponse;
import com.uniedu.support.processing.models.enums.WorkerStatus;
import com.uniedu.support.processing.services.implementations.WorkerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/worker")
public class WorkerController {

    private final WorkerServiceImpl workerServiceImpl;

    @PreAuthorize("hasRole('WORKER')")
    @GetMapping("/completeTicket/{id}")
    public ResponseEntity<?> completeTicket(@PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        workerServiceImpl.completeTicket(id, userDetails);
        return ResponseEntity.ok(new MessageResponse("User complete ticket successfully!"));
    }

    @PreAuthorize("hasRole('WORKER')")
    @GetMapping("/changeStatus/{status}")
    public ResponseEntity<?> completeTicket(@PathVariable WorkerStatus status) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        workerServiceImpl.changeWorkerStatus(status, userDetails);
        return ResponseEntity.ok(new MessageResponse("User changed own status successfully!"));
    }
}