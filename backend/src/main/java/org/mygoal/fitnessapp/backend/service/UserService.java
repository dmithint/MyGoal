package org.mygoal.fitnessapp.backend.service;

import org.mygoal.fitnessapp.backend.dto.CredentialsDto;
import org.mygoal.fitnessapp.backend.dto.SignUpDto;
import org.mygoal.fitnessapp.backend.dto.UserDto;
import org.mygoal.fitnessapp.backend.exceptions.AppException;
import org.mygoal.fitnessapp.backend.mappers.UserMapper;
import org.mygoal.fitnessapp.backend.model.*;
import org.mygoal.fitnessapp.backend.repository.AthleteRepository;
import org.mygoal.fitnessapp.backend.repository.CoachRepository;
import org.mygoal.fitnessapp.backend.repository.RoleRepository;
import org.mygoal.fitnessapp.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final CoachRepository coachRepository;
    private final AthleteRepository athleteRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByEmail(credentialsDto.getEmail())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(credentialsDto.getPassword(), user.getPassword())) {
            throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
        }
        return userMapper.toUserDto(user);
    }

    public UserDto register(SignUpDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new AppException("Email already exists", HttpStatus.BAD_REQUEST);
        }

        User user = signUpToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        if (user instanceof Coach) {
            coachRepository.save((Coach) user);
        } else {
            athleteRepository.save((Athlete) user);
        }

        return userMapper.toUserDto(user);
    }

    private User signUpToUser(SignUpDto signUpDto) {
        User user = createUserByRoles(signUpDto.getRoles());
        user.setFirstName(signUpDto.getFirstName());
        user.setLastName(signUpDto.getLastName());
        user.setEmail(signUpDto.getEmail());

        Set<Role> userRoles = signUpDto.getRoles().stream()
                .map(roleType -> roleRepository.findByName(roleType)
                        .orElseThrow(() -> new AppException("Role not found: " + roleType, HttpStatus.BAD_REQUEST)))
                .collect(Collectors.toSet());

        user.setRoles(userRoles);
        return user;
    }

    private User createUserByRoles(Set<RoleType> roles) {
        if (roles.contains(RoleType.COACH)) {
            return new Coach();
        } else {
            return new Athlete();
        }
    }


    public UserDto findByEmail(String email) {
        return userMapper.toUserDto(userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND)));
    }

    public UserDto getUserDtoById(Long id) {
        return userMapper.toUserDto(getUserById(id));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
    }

    public UserDto changeUserEmail(Long id, String email) {
        User user = getUserById(id);
        user.setEmail(email);
        return userMapper.toUserDto(userRepository.save(user));
    }

//    public UserDto changeUserParams(Long id, UserParametersDto parameters) {
//        if (!(getUserById(id) instanceof Client client)) {
//            throw new AppException("User is not a client", HttpStatus.BAD_REQUEST);
//        }
//
//        userMapper.updateClientFromDto(client, parameters);
//        return userMapper.toUserDto(userRepository.save(client));
//    }
}
