package com.uniedu.support.processing.init;

import com.uniedu.support.processing.models.entities.*;
import com.uniedu.support.processing.models.enums.RoomGroup;
import com.uniedu.support.processing.models.enums.TicketStatus;
import com.uniedu.support.processing.models.enums.UserRoleType;
import com.uniedu.support.processing.models.enums.WorkerStatus;
import com.uniedu.support.processing.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoomRepository roomRepository;
    private final WorkerScheduleRepository workerScheduleRepository;
    private final TicketRepository ticketRepository;
    private final ChatRepository chatRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        log.info("Starting processing...");
        // Создание ролей
        Role adminRole = roleRepository.save(new Role(UserRoleType.ADMIN));
        Role teacherRole = roleRepository.save(new Role(UserRoleType.TEACHER));
        Role workerRole = roleRepository.save(new Role(UserRoleType.WORKER));

        // Создание аудиторий
        Room room101 = roomRepository.save(Room.builder().name("101").roomGroup(RoomGroup.FirstFloor).build());
        Room room202 = roomRepository.save(Room.builder().name("202").roomGroup(RoomGroup.SecondFloor).build());
        Room room303 = roomRepository.save(Room.builder().name("303").roomGroup(RoomGroup.ThirdFloor).build());

        // Администратор
        User admin = User.builder()
                .firstName("Admin")
                .lastName("User")
                .username("admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin"))
                .isActive(WorkerStatus.INACTIVE)
                .roles(Set.of(adminRole))
                .build();
        userRepository.save(admin);

        // Преподаватель
        User teacher = User.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .username("teacher")
                .email("teacher@example.com")
                .password(passwordEncoder.encode("admin"))
                .isActive(WorkerStatus.INACTIVE)
                .roles(Set.of(teacherRole))
                .build();
        userRepository.save(teacher);

        // Работник 1
        User worker1 = User.builder()
                .firstName("Sergey")
                .lastName("Petrov")
                .username("worker1")
                .email("worker1@example.com")
                .password(passwordEncoder.encode("admin"))
                .isActive(WorkerStatus.ACTIVE)
                .roles(Set.of(workerRole))
                .assignedRooms(Set.of(room101, room202))
                .build();
        worker1 = userRepository.save(worker1);

        // Работник 2
        User worker2 = User.builder()
                .firstName("Elena")
                .lastName("Smirnova")
                .username("worker2")
                .email("worker2@example.com")
                .password(passwordEncoder.encode("admin"))
                .isActive(WorkerStatus.INACTIVE)
                .roles(Set.of(workerRole))
                .assignedRooms(Set.of(room202, room303))
                .build();
        worker2 = userRepository.save(worker2);

        // Работник 3
        User worker3 = User.builder()
                .firstName("Dmitry")
                .lastName("Sidorov")
                .username("worker3")
                .email("worker3@example.com")
                .password(passwordEncoder.encode("admin"))
                .isActive(WorkerStatus.INACTIVE)
                .roles(Set.of(workerRole))
                .assignedRooms(Set.of(room101))
                .build();
        worker3 = userRepository.save(worker3);

        // Расписание работников
        workerScheduleRepository.saveAll(List.of(
                WorkerSchedule.builder()
                        .worker(worker1)
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(16, 0))
                        .build(),
                WorkerSchedule.builder()
                        .worker(worker2)
                        .dayOfWeek(DayOfWeek.TUESDAY)
                        .startTime(LocalTime.of(9, 0))
                        .endTime(LocalTime.of(17, 0))
                        .build(),
                WorkerSchedule.builder()
                        .worker(worker3)
                        .dayOfWeek(DayOfWeek.WEDNESDAY)
                        .startTime(LocalTime.of(10, 0))
                        .endTime(LocalTime.of(18, 0))
                        .build()
        ));

        // Пример тикета
        Ticket ticket = Ticket.builder()
                .title("Не работает проектор")
                .description("В аудитории 101 не включается проектор.")
                .status(TicketStatus.NEW)
                .chat(chatRepository.save(new Chat()))
                .creator(teacher)
                .assignedTo(worker1)
                .build();
        ticketRepository.save(ticket);

        log.info("End data initialization !!!");
    }
}
