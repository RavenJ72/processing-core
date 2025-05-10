package com.uniedu.support.processing.models.entities;

import com.uniedu.support.processing.models.baseEntities.TimestampedEntity;
import com.uniedu.support.processing.models.enums.WorkerStatus;
import jakarta.persistence.*;
import lombok.*;


import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

@Entity
@Table(name = "users")
public class User extends TimestampedEntity {
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(length = 100, nullable = false)
    private String username;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    private String phoneNumber;

    private WorkerStatus isActive;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    @ManyToMany
    @JoinTable(
            name = "worker_rooms",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private Set<Room> assignedRooms = new HashSet<>();
}