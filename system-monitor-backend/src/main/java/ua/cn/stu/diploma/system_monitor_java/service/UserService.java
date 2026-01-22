package ua.cn.stu.diploma.system_monitor_java.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.cn.stu.diploma.system_monitor_java.dto.AuthResponse;
import ua.cn.stu.diploma.system_monitor_java.dto.UserLoginRequest;
import ua.cn.stu.diploma.system_monitor_java.dto.UserRegisterRequest;
import ua.cn.stu.diploma.system_monitor_java.entity.User;
import ua.cn.stu.diploma.system_monitor_java.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenService jwtTokenService;

    public void register(UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
    }

    public AuthResponse login(UserLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtTokenService.createToken(user.getUsername());
        return new AuthResponse(token, user.getId());
    }
}
