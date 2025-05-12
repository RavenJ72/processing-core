package com.uniedu.support.processing.init;

import com.uniedu.support.processing.models.entities.*;
import com.uniedu.support.processing.models.enums.RoomGroup;
import com.uniedu.support.processing.models.enums.TicketStatus;
import com.uniedu.support.processing.models.enums.UserRoleType;
import com.uniedu.support.processing.models.enums.WorkerStatus;
import com.uniedu.support.processing.repositories.*;
import com.uniedu.support.processing.services.implementations.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
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
    private final NotificationService notificationService;

    @Override
    public void run(String... args) throws Exception {

        notificationService.sendNewMessageNotification("12L", "12L", 12L);


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
        User teacher2 = createTeacher("Petr", "Petrov", "teacher2", Set.of(room102, room202, lab2));
        User teacher3 = createTeacher("Anna", "Sidorova", "teacher3", Set.of(room103, room203, comp1));

        // Работники (значительно больше)
        User worker1 = createWorker("Sergey", "Petrov", "worker1", WorkerStatus.ACTIVE,
                Set.of(room101, room102, room201, comp1));
        User worker2 = createWorker("Elena", "Smirnova", "worker2", WorkerStatus.ACTIVE,
                Set.of(room103, room202, room203, lab1));
        User worker3 = createWorker("Dmitry", "Sidorov", "worker3", WorkerStatus.ACTIVE,
                Set.of(room301, room302, comp2));
        User worker4 = createWorker("Olga", "Kuznetsova", "worker4", WorkerStatus.ACTIVE,
                Set.of(room401, lab2));
        User worker5 = createWorker("Alexey", "Volkov", "worker5", WorkerStatus.ACTIVE,
                Set.of(room101, room201, room301));
        User worker6 = createWorker("Maria", "Fedorova", "worker6", WorkerStatus.ACTIVE,
                Set.of(room102, room202, room302));

        // Создаем насыщенное расписание (на всю неделю)
        createWorkerSchedules(worker1, LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(16, 0));
        createWorkerSchedules(worker2, LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(17, 0));
        createWorkerSchedules(worker3, LocalDate.now().plusDays(2), LocalTime.of(10, 0), LocalTime.of(18, 0));
        createWorkerSchedules(worker4, LocalDate.now().plusDays(3), LocalTime.of(8, 30), LocalTime.of(16, 30));
        createWorkerSchedules(worker5, LocalDate.now().plusDays(4), LocalTime.of(7, 30), LocalTime.of(15, 30));
        createWorkerSchedules(worker6, LocalDate.now().plusDays(5), LocalTime.of(11, 0), LocalTime.of(19, 0));

        // Создаем несколько тикетов
        createTicket("Не работает проектор", "В аудитории 101 не включается проектор", teacher1, worker1, room101);
        createTicket("Сломан стул", "В аудитории 202 сломан стул", teacher2, worker2, room202);
        createTicket("Не работает компьютер", "В компьютерном классе Comp1 не включается ПК", teacher3, worker3, comp1);
        createTicket("Протекает кран", "В лаборатории Lab2 протекает кран", teacher1, worker4, lab2);

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