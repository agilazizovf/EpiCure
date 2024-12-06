package com.epicure.project.service.impl;

import com.epicure.project.dao.entity.AdminEntity;
import com.epicure.project.dao.entity.AuthorityEntity;
import com.epicure.project.dao.entity.OrderEntity;
import com.epicure.project.dao.entity.UserEntity;
import com.epicure.project.dao.repository.AdminRepository;
import com.epicure.project.dao.repository.OrderRepository;
import com.epicure.project.dao.repository.UserRepository;
import com.epicure.project.model.ExceptionDTO;
import com.epicure.project.model.dto.response.IncomeReportResponse;
import com.epicure.project.model.enums.OrderStatus;
import com.epicure.project.model.exception.AlreadyExistsException;
import com.epicure.project.model.dto.request.AdminRequest;
import com.epicure.project.model.dto.request.LoginRequest;
import com.epicure.project.model.dto.response.LoginResponse;
import com.epicure.project.model.dto.response.MessageResponse;
import com.epicure.project.service.AdminService;
import com.epicure.project.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public ResponseEntity<MessageResponse> register(AdminRequest request) {
        // Check if the user already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AlreadyExistsException("User already exists");
        }

        // Create the UserEntity
        UserEntity user = new UserEntity(request.getUsername(), passwordEncoder.encode(request.getPassword()));
        AuthorityEntity authority = new AuthorityEntity("ADMIN");
        Set<AuthorityEntity> authorityEntitySet = Set.of(authority);
        user.setAuthorities(authorityEntitySet);

        // Save the UserEntity first
        userRepository.save(user); // Save UserEntity first

        // Create the AdminEntity
        AdminEntity admin = new AdminEntity();
        admin.setUsername(request.getUsername());
        admin.setEmail(request.getEmail());
        admin.setRegisterDate(LocalDateTime.now());
        admin.setUpdateDate(LocalDateTime.now());

        // Associate the saved UserEntity with the AdminEntity
        admin.setUser(user); // Now the UserEntity is already persisted

        // Save the AdminEntity
        adminRepository.save(admin); // Save AdminEntity separately

        // Create the response message
        MessageResponse response = new MessageResponse();
        response.setMessage("User created successfully with username: " + request.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Override
    public ResponseEntity<?> login(LoginRequest loginReq)throws IllegalArgumentException {
        log.info("authenticate method started by: {}", loginReq.getUsername());
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getUsername(),
                            loginReq.getPassword()));
            log.info("authentication details: {}", authentication);
            String username = authentication.getName();
            UserEntity client = new UserEntity(username, "");
            String token = jwtUtil.createToken(client);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            LoginResponse loginRes = new LoginResponse(username, token);
            log.info("user: {} logged in", client.getUsername());
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(loginRes);

        } catch (BadCredentialsException e) {
            ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.BAD_REQUEST.value(), "Invalid username or password");
            log.error("Error due to {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
        } catch (Exception e) {
            ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            log.error("Error due to {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
        }
    }
}
