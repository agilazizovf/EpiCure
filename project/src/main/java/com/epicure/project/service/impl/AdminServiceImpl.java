package com.epicure.project.service.impl;

import com.epicure.project.dao.entity.AdminEntity;
import com.epicure.project.dao.entity.AuthorityEntity;
import com.epicure.project.dao.entity.UserEntity;
import com.epicure.project.dao.repository.AdminRepository;
import com.epicure.project.dao.repository.UserRepository;
import com.epicure.project.dto.exception.AlreadyExistsException;
import com.epicure.project.dto.exception.ResourceNotFoundException;
import com.epicure.project.dto.request.AdminRequest;
import com.epicure.project.dto.request.LoginRequest;
import com.epicure.project.dto.response.JwtAuthenticationResponse;
import com.epicure.project.dto.response.MessageResponse;
import com.epicure.project.service.AdminService;
import com.epicure.project.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


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
    public JwtAuthenticationResponse login(LoginRequest request)throws IllegalArgumentException {
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
