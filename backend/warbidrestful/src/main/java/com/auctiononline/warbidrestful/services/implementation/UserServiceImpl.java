package com.auctiononline.warbidrestful.services.implementation;

import com.auctiononline.warbidrestful.exception.AppException;
import com.auctiononline.warbidrestful.models.ERole;
import com.auctiononline.warbidrestful.models.Role;
import com.auctiononline.warbidrestful.models.User;
import com.auctiononline.warbidrestful.payload.request.UserRequest;
import com.auctiononline.warbidrestful.payload.response.GetAllUserResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;
import com.auctiononline.warbidrestful.repository.RoleRepository;
import com.auctiononline.warbidrestful.repository.UserRepository;
import com.auctiononline.warbidrestful.services.ilterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public GetAllUserResponse getAllUser(){
        try {
            List<User> activeUsers = userRepository.findAllActiveUsers();
             return new GetAllUserResponse(
                    200,
                    "OK",
                    "Get all user list",
                    activeUsers
            );
        } catch (AppException ex) {
            return new GetAllUserResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error",
                    "An unexpected error occurred. Please try again later or contact support for assistance.",
                    null
            );

        }
    }

    public GetAllUserResponse getAllUserBySearch(String searchTerm){
        try {
            List<User> activeUsers = userRepository.searchUsersByKeyword(searchTerm);
            return new GetAllUserResponse(
                    200,
                    "OK",
                    "Get all user list",
                    activeUsers
            );

        } catch (AppException ex) {
            return new GetAllUserResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error",
                    "An unexpected error occurred. Please try again later or contact support for assistance.",
                    null
            );

        }
    }

    @Override
    public MessageResponse add(UserRequest userRequest){
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            return new MessageResponse(400, HttpStatus.BAD_REQUEST,"Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return new MessageResponse(400, HttpStatus.BAD_REQUEST,"Error: Email is already in use!");
        }

        // Create new user's account
        User user = createUserFromRequest(userRequest);

        Set<Role> roles = mapStringRolesToRoles(userRequest.getRole());

        user.setRoles(roles);
        user.setDeleted(false);
        try{
            userRepository.save(user);
            return new MessageResponse(200,HttpStatus.OK,"User registered successfully!");
        }catch (AppException ex){
            return new MessageResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred. Please try again later or contact support for assistance."
            );
        }

    }

    public MessageResponse update(UserRequest userRequest){
        if (userRepository.existsByUsernameAndDifferentId(userRequest.getUsername(),userRequest.getId() )) {
            return new MessageResponse(400, HttpStatus.BAD_REQUEST,"Error: Username is already taken!");
        }

        if (userRepository.existsByEmailAndDifferentId(userRequest.getEmail(), userRequest.getId())) {
            return new MessageResponse(400, HttpStatus.BAD_REQUEST,"Error: Email is already in use!");
        }

        // Update user's account
        User user = createUserFromRequest(userRequest);

        Set<Role> roles = mapStringRolesToRoles(userRequest.getRole());

        user.setRoles(roles);
        user.setId(userRequest.getId());
        user.setDeleted(false);
        try{
            userRepository.save(user);
            return new MessageResponse(200,HttpStatus.OK,"User updated successfully!");
        }catch (AppException ex){
            return new MessageResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred. Please try again later or contact support for assistance."
            );
        }
    }
    @Override
    public MessageResponse delete(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return new MessageResponse(404, HttpStatus.NOT_FOUND, "ID not found");
        }
        User user = optionalUser.get();
        user.setDeleted(true);
        try{
            userRepository.save(user);
            return new MessageResponse(200, HttpStatus.OK, "User deleted successfully");
        }catch (AppException ex){
            return new MessageResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred. Please try again later or contact support for assistance."
            );
        }
    }

    @Override
    public MessageResponse restore(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return new MessageResponse(404, HttpStatus.NOT_FOUND, "ID not found");
        }
        User user = optionalUser.get();
        user.setDeleted(false);

        try{
            userRepository.save(user);
            return new MessageResponse(200, HttpStatus.OK, "User recovery successfully");
        }catch (AppException ex){
            return new MessageResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred. Please try again later or contact support for assistance."
            );
        }
    }

    private User createUserFromRequest(UserRequest userRequest) {
        String encodedPassword = encoder.encode(userRequest.getPassword());
            return new User(
                    userRequest.getUsername(),
                    userRequest.getEmail(),
                    encodedPassword,
                    userRequest.getPhone(),
                    userRequest.getAddress()
            );

    }

    private Set<Role> mapStringRolesToRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();
        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                Role foundRole;
                switch (role) {
                    case "admin" -> foundRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Error: Role is not found."));
                    case "mod" -> foundRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Error: Role is not found."));
                    default -> foundRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Error: Role is not found."));
                }
                roles.add(foundRole);
            });
        }
        return roles;
    }
}
