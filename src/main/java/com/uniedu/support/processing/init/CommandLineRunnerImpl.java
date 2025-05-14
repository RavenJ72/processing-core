package com.uniedu.support.processing.init;

import com.uniedu.support.processing.models.entities.*;
import com.uniedu.support.processing.models.enums.RoomGroup;
import com.uniedu.support.processing.models.enums.TicketStatus;
import com.uniedu.support.processing.models.enums.UserRoleType;
import com.uniedu.support.processing.models.enums.WorkerStatus;
import com.uniedu.support.processing.repositories.*;
import com.uniedu.support.processing.services.notification.NotificationService;
import com.uniedu.support.processing.services.implementations.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
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
    private final NotificationService notificationService;
    private final UserServiceImpl userService;

    @Override
    public void run(String... args) throws Exception {

        log.info("Starting processing...");

        // Создание ролей
        Role adminRole = roleRepository.save(new Role(UserRoleType.ADMIN));
        Role teacherRole = roleRepository.save(new Role(UserRoleType.TEACHER));
        Role workerRole = roleRepository.save(new Role(UserRoleType.WORKER));

        // Создание аудиторий (значительно больше кабинетов)
        Room room101 = roomRepository.save(Room.builder().name("101").roomGroup(RoomGroup.FIRST_FLOOR_GROUP).build());
        Room room102 = roomRepository.save(Room.builder().name("102").roomGroup(RoomGroup.FIRST_FLOOR_GROUP).build());
        Room room103 = roomRepository.save(Room.builder().name("103").roomGroup(RoomGroup.FIRST_FLOOR_GROUP).build());
        Room room201 = roomRepository.save(Room.builder().name("201").roomGroup(RoomGroup.SECOND_FLOOR_GROUP).build());
        Room room202 = roomRepository.save(Room.builder().name("202").roomGroup(RoomGroup.SECOND_FLOOR_GROUP).build());
        Room room203 = roomRepository.save(Room.builder().name("203").roomGroup(RoomGroup.SECOND_FLOOR_GROUP).build());
        Room room301 = roomRepository.save(Room.builder().name("301").roomGroup(RoomGroup.THIRD_FLOOR_GROUP).build());
        Room room302 = roomRepository.save(Room.builder().name("302").roomGroup(RoomGroup.THIRD_FLOOR_GROUP).build());
        Room room401 = roomRepository.save(Room.builder().name("401").roomGroup(RoomGroup.FOURTH_FLOOR_GROUP).build());
        Room comp1 = roomRepository.save(Room.builder().name("Comp1").roomGroup(RoomGroup.COMPUTER_CLUSTER).build());
        Room comp2 = roomRepository.save(Room.builder().name("Comp2").roomGroup(RoomGroup.COMPUTER_CLUSTER).build());
        Room lab1 = roomRepository.save(Room.builder().name("Lab1").roomGroup(RoomGroup.LABORATORIES_GROUP).build());
        Room lab2 = roomRepository.save(Room.builder().name("Lab2").roomGroup(RoomGroup.LABORATORIES_GROUP).build());

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

        // Преподаватели (несколько)
        User teacher1 = createTeacher("Ivan", "Ivanov", "teacher1", Set.of(room101, room201, lab1));


        // Работники (значительно больше)
        User worker1 = createWorker("Sergey", "Petrov", "worker1", WorkerStatus.ACTIVE,
                Set.of(room101, room102));


        // Создаем насыщенное расписание (на всю неделю)
//        createWorkerSchedules(worker1, LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(16, 0));


        // Создаем несколько тикетов
        createTicket("Не работает проектор", "В аудитории 101 не включается проектор", teacher1, worker1, room101);


        log.info("End data initialization !!!");


    }

    private User createTeacher(String firstName, String lastName, String username, Set<Room> rooms) {
        User teacher = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .email(username + "@example.com")
                .password(passwordEncoder.encode("teacher"))
                .isActive(WorkerStatus.ACTIVE)
                .roles(Set.of(roleRepository.findByRole(UserRoleType.TEACHER)))
                        .assignedRooms(rooms)
                        .build();
        return userRepository.save(teacher);
    }

    private User createWorker(String firstName, String lastName, String username,
                              WorkerStatus status, Set<Room> rooms) {
        User worker = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .email(username + "@example.com")
                .password(passwordEncoder.encode("worker"))
                .isActive(status)
                .roles(Set.of(roleRepository.findByRole(UserRoleType.WORKER)))
                .assignedRooms(rooms)
                .build();
        return userRepository.save(worker);
    }

    private void createWorkerSchedules(User worker, LocalDate date, LocalTime start, LocalTime end) {
        // Создаем расписание на всю неделю
        for (int i = 0; i < 5; i++) { // Только рабочие дни (пн-пт)
            workerScheduleRepository.save(WorkerSchedule.builder()
                    .worker(worker)
                    .date(date.plusDays(i))
                    .startTime(start)
                    .endTime(end)
                    .build());
        }
    }

    private void createTicket(String title, String description, User creator, User worker, Room room) {
        Ticket ticket = Ticket.builder()
                .title(title)
                .description(description)
                .status(TicketStatus.NEW)
                .chat(chatRepository.save(new Chat()))
                .creator(creator)
                .assignedTo(worker)
                .room(room)
                .build();
        ticketRepository.save(ticket);
    }




}