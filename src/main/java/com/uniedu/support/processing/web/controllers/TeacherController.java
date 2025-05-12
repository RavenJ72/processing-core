package com.uniedu.support.processing.web.controllers;

import com.uniedu.support.processing.dto.createRequests.TicketCreateRequest;
import com.uniedu.support.processing.dto.standart.RoomDto;
import com.uniedu.support.processing.dto.standart.TicketDto;
import com.uniedu.support.processing.models.enums.TicketStatus;
import com.uniedu.support.processing.services.implementations.TicketServiceImpl;
import com.uniedu.support.processing.services.interfaces.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final TeacherService teacherService;
    private final TicketServiceImpl ticketServiceImpl;

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/ticket")
    public ResponseEntity<TicketDto> createTicket(
            @Valid @RequestBody TicketCreateRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        TicketDto ticket = teacherService.createTicket(request, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/ticket/{ticketId}/status")
    public ResponseEntity<Void> changeTicketStatus(
            @PathVariable Long ticketId,
            @RequestParam TicketStatus status) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        teacherService.changeTicketStatus(ticketId, status, userDetails);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDto>> getAvailableRooms() {
        return ResponseEntity.ok(ticketServiceImpl.getAvailableRooms());
    }

    @GetMapping("/ticket")
    public ResponseEntity<List<TicketDto>> getAvailableTickets() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(teacherService.getAllActiveTickets(userDetails));
    }
}
