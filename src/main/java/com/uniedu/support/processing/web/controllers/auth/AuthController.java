package com.uniedu.support.processing.web.controllers.auth;

import com.uniedu.support.processing.models.entities.User;
import com.uniedu.support.processing.repositories.UserRepository;
import com.uniedu.support.processing.security.jwt.JwtUtils;
import com.uniedu.support.processing.dto.authEntities.JwtResponse;
import com.uniedu.support.processing.dto.authEntities.LoginRequest;
import com.uniedu.support.processing.dto.authEntities.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Аутентификация", description = "Вход и выход пользователей из системы")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Позволяет пользователю войти в систему и получить JWT токен",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешная аутентификация, возвращён JWT токен"),
                    @ApiResponse(responseCode = "401", description = "Неверный логин или пароль")
            }
    )
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        return ResponseEntity.ok(new JwtResponse(jwt,
                user.getId(),user.getUsername(),roles));

    }

    @Operation(
            summary = "Выход пользователя",
            description = "Очищает контекст безопасности. JWT остаётся валидным, но клиент должен удалить его сам",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь успешно вышел из системы")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
    }
}