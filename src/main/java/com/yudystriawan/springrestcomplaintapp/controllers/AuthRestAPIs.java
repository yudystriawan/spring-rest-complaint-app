package com.yudystriawan.springrestcomplaintapp.controllers;

import com.yudystriawan.springrestcomplaintapp.securities.JwtTokenProvider;
import com.yudystriawan.springrestcomplaintapp.models.request.LoginForm;
import com.yudystriawan.springrestcomplaintapp.models.request.SignUpForm;
import com.yudystriawan.springrestcomplaintapp.models.response.JwtTokenResponse;
import com.yudystriawan.springrestcomplaintapp.models.*;
import com.yudystriawan.springrestcomplaintapp.repositories.RoleRepository;
import com.yudystriawan.springrestcomplaintapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/auth")
public class AuthRestAPIs {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostMapping(path = "/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginForm.getUsername(),
                        loginForm.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtTokenResponse(token));
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpForm) {
        if (userRepository.existsByUsername(signUpForm.getUsername())) {
            return new ResponseEntity<String>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signUpForm.getEmail())) {
            return new ResponseEntity<String>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        User user = new User(
                signUpForm.getUsername(),
                signUpForm.getName(),
                signUpForm.getEmail(),
                passwordEncoder.encode(signUpForm.getPassword())
        );

        String strRoles = signUpForm.getRole();
        Set<Role> roles = new HashSet<>();

        switch (strRoles) {
            case "admin":
                Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("User Role not find."));
                roles.add(adminRole);
                break;
            default:
                Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("User Role not find."));
                roles.add(userRole);
        }
        ;

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok().body("User registered successfully!");

    }
}
