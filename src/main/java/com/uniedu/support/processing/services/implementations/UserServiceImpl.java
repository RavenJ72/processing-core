package com.uniedu.support.processing.services.implementations;

import com.uniedu.support.processing.dto.authEntities.MessageResponse;
import com.uniedu.support.processing.dto.authEntities.SignUpRequest;
import com.uniedu.support.processing.dto.standart.UserDto;
import com.uniedu.support.processing.exceptions.RoomNotFoundException;
import com.uniedu.support.processing.models.entities.Room;
import com.uniedu.support.processing.models.entities.User;
import com.uniedu.support.processing.models.enums.UserRoleType;
import com.uniedu.support.processing.models.enums.WorkerStatus;
import com.uniedu.support.processing.repositories.*;
import com.uniedu.support.processing.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService<Long> {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final WorkerScheduleRepository workerScheduleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserDto createUser(SignUpRequest signUpRequest, UserDetails userDetails) {

        log.info("User creation request. Initiator - {}", userDetails.getUsername());

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        UserDto userDto = UserDto.builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .username(signUpRequest.getUsername())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .isActive(WorkerStatus.INACTIVE)
                .roles(Set.of(roleRepository.findByRole(signUpRequest.getUserRole())))
                .build();

        User user = modelMapper.map(userDto, User.class);
        User savedUser = userRepository.saveAndFlush(user);
        log.info("User with roles - ({}) registered successfully!", savedUser.getRoles());
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(user -> modelMapper.map(user, UserDto.class)).orElse(null);
    }


    @Override
    public UserDto updateUser(UserDto userDTO) {
        return null;
    }

    @Override
    public UserDto getUserById(Long integer) {
        return null;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return null;
    }

    @Override
    public List<UserDto> findAllByRoleAsc(UserRoleType role) {
        return userRepository.findByRoleType(role).stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
    }

    @Override
    public User getUserForTicketAssigmentByRoomName(String roomName) {
        Room room = roomRepository.findByName(roomName)
                .orElseThrow(() -> new RoomNotFoundException(roomName));

        val activeUser = userRepository.findAllByIsActive(WorkerStatus.ACTIVE)
                .stream().filter(user -> user.getRoles().stream().anyMatch(role -> role.getRole().equals(UserRoleType.WORKER)))
                .filter(user -> user.getAssignedRooms().stream()
                        .anyMatch(r -> r.getName().equals(room.getName()) &&
                                r.getRoomGroup().equals(room.getRoomGroup())))
                        .findFirst();

       if(activeUser.isPresent()){
           return activeUser.get();
       }else{
           LocalDate today = LocalDate.now();
           LocalTime now = LocalTime.now();
           Optional<User> activeWorkerBySchedule = workerScheduleRepository.findScheduledWorkers(today, now).stream().filter(worker -> worker.getAssignedRooms().contains(room)).findFirst();
           return activeWorkerBySchedule.orElseThrow();
       }
    }
}
