package com.changeside.springsecurityjwt.service;

import com.changeside.springsecurityjwt.dto.UserDto;
import com.changeside.springsecurityjwt.dto.UserRequest;
import com.changeside.springsecurityjwt.dto.UserResponse;
import com.changeside.springsecurityjwt.entity.User;
import com.changeside.springsecurityjwt.enums.Role;
import com.changeside.springsecurityjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public UserResponse save(UserDto userDto) {
        User user=User.builder()
                .userName(userDto.getUsername())
                .password(userDto.getPassword())
                .fullName(userDto.getFullName())
                .role(Role.USER).build();
        userRepository.save(user);
        var token=jwtService.generateToken(user);
        return UserResponse.builder().token(token).build();

    }

    public UserResponse auth(UserRequest userRequest) {
        authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(),userRequest.getPassword()));
        User user=userRepository.findByUsername(userRequest.getUsername()).orElseThrow();
        String token=jwtService.generateToken(user);
        return UserResponse.builder().token(token).build();
    }
}
