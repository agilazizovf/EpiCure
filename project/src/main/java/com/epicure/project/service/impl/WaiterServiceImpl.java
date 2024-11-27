package com.epicure.project.service.impl;

import com.epicure.project.dao.entity.AuthorityEntity;
import com.epicure.project.dao.entity.UserEntity;
import com.epicure.project.dao.entity.WaiterEntity;
import com.epicure.project.dao.repository.UserRepository;
import com.epicure.project.dao.repository.WaiterRepository;
import com.epicure.project.dto.exception.AlreadyExistsException;
import com.epicure.project.dto.exception.ResourceNotFoundException;
import com.epicure.project.dto.request.LoginRequest;
import com.epicure.project.dto.request.WaiterRequest;
import com.epicure.project.dto.response.JwtAuthenticationResponse;
import com.epicure.project.dto.response.MessageResponse;
import com.epicure.project.service.JwtService;
import com.epicure.project.service.WaiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WaiterServiceImpl implements WaiterService {

    private final WaiterRepository waiterRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<MessageResponse> register(WaiterRequest request) {

        // Check if the user already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AlreadyExistsException("User already exists");
        }

        // Create the UserEntity
        UserEntity user = new UserEntity(request.getUsername(), passwordEncoder.encode(request.getPassword()));
        AuthorityEntity authority = new AuthorityEntity("WAITER");
        Set<AuthorityEntity> authorityEntitySet = Set.of(authority);
        user.setAuthorities(authorityEntitySet);

        // Save the UserEntity first
        userRepository.save(user); // Save UserEntity first


        WaiterEntity waiter = new WaiterEntity();
        waiter.setUsername(request.getUsername());
        waiter.setEmail(request.getEmail());
        waiter.setHireDate(LocalDateTime.now());
        waiter.setUpdateDate(LocalDateTime.now());
        waiter.setUser(user);

        waiterRepository.save(waiter);

        MessageResponse response = new MessageResponse();
        response.setMessage("User created successfully with username: " + request.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public JwtAuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + request.getUsername()));
        var jwt = jwtService.generateToken(user);
        JwtAuthenticationResponse jwtAuthenticationResponse=new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        return jwtAuthenticationResponse;
    }
}
