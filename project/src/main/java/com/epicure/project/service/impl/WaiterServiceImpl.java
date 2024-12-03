package com.epicure.project.service.impl;

import com.epicure.project.dao.entity.AuthorityEntity;
import com.epicure.project.dao.entity.UserEntity;
import com.epicure.project.dao.entity.WaiterEntity;
import com.epicure.project.dao.repository.UserRepository;
import com.epicure.project.dao.repository.WaiterRepository;
import com.epicure.project.model.ExceptionDTO;
import com.epicure.project.model.exception.AlreadyExistsException;
import com.epicure.project.model.dto.request.LoginRequest;
import com.epicure.project.model.dto.request.WaiterRequest;
import com.epicure.project.model.dto.response.LoginResponse;
import com.epicure.project.model.dto.response.MessageResponse;
import com.epicure.project.model.dto.response.PageResponse;
import com.epicure.project.model.dto.response.WaiterInfoResponse;
import com.epicure.project.mapper.WaiterMapper;
import com.epicure.project.service.WaiterService;
import com.epicure.project.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WaiterServiceImpl implements WaiterService {

    private final WaiterRepository waiterRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

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
    public ResponseEntity<?> login(LoginRequest loginReq) {
        log.info("authenticate method started by: {}", loginReq.getUsername());
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getUsername(),
                            loginReq.getPassword()));
            log.info("authentication details: {}", authentication);
            String username = authentication.getName();
            UserEntity client = new UserEntity(username,"");
            String token = jwtUtil.createToken(client);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            LoginResponse loginRes = new LoginResponse(username,token);
            log.info("user: {} logged in",  client.getUsername());
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(loginRes);

        }catch (BadCredentialsException e){
            ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.BAD_REQUEST.value(),"Invalid username or password");
            log.error("Error due to {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
        }catch (Exception e){
            ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            log.error("Error due to {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
        }
    }

    @Override
    public PageResponse<WaiterInfoResponse> getWaiters(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WaiterEntity> waiterEntities = waiterRepository.findAll(pageable);

        List<WaiterInfoResponse> waiterInfoResponses = waiterEntities
                .stream()
                .map(WaiterMapper::toWaiterDTO)
                .collect(Collectors.toList());

        PageResponse<WaiterInfoResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(waiterInfoResponses);
        pageResponse.setPage(waiterEntities.getPageable().getPageNumber());
        pageResponse.setSize(waiterEntities.getPageable().getPageSize());
        pageResponse.setTotalElements(waiterEntities.getTotalElements());
        pageResponse.setTotalPages(waiterEntities.getTotalPages());
        pageResponse.setLast(waiterEntities.isLast());
        pageResponse.setFirst(waiterEntities.isFirst());

        return pageResponse;
    }
}
