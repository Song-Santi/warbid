package com.auctiononline.warbidrestful.services.implementation;

import com.auctiononline.warbidrestful.exception.AppException;
import com.auctiononline.warbidrestful.models.ERole;
import com.auctiononline.warbidrestful.models.Role;
import com.auctiononline.warbidrestful.models.Token;
import com.auctiononline.warbidrestful.models.User;
import com.auctiononline.warbidrestful.payload.dto.Paging;
import com.auctiononline.warbidrestful.payload.dto.ProductDTO;
import com.auctiononline.warbidrestful.payload.request.EmailForgotRequest;
import com.auctiononline.warbidrestful.payload.request.PasswordRequest;
import com.auctiononline.warbidrestful.payload.request.TokenRequest;
import com.auctiononline.warbidrestful.payload.request.UserRequest;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;
import com.auctiononline.warbidrestful.repository.RoleRepository;
import com.auctiononline.warbidrestful.repository.TokenRepository;
import com.auctiononline.warbidrestful.repository.UserRepository;
import com.auctiononline.warbidrestful.services.ilterface.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public GetAllResponse getAllUser(int page, int pageSize){
        try {
            List<User> activeUsers = userRepository.findAllActiveUsers();

            int totalItems = activeUsers.size();
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalItems);

            List<User> activeUsersOnPage = new ArrayList<>(activeUsers.subList(startIndex, endIndex));

            Paging paging = new Paging(page, totalPages, pageSize, totalItems);

             return new GetAllResponse(
                    200,
                    "OK",
                    "Get all user list",
                     paging,
                     activeUsersOnPage
            );
        } catch (AppException ex) {
            return new GetAllResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error",
                    "An unexpected error occurred. Please try again later or contact support for assistance.",
                    null
            );

        }
    }

    public GetAllResponse getAllUserBySearch(int page, int pageSize, String searchTerm){
        try {
            List<User> activeUsers = userRepository.searchUsersByKeyword(searchTerm);

            int totalItems = activeUsers.size();
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalItems);

            List<User> activeUsersOnPage = new ArrayList<>(activeUsers.subList(startIndex, endIndex));

            Paging paging = new Paging(page, totalPages, pageSize, totalItems);

            return new GetAllResponse(
                    200,
                    "OK",
                    "Get all user list",
                    paging,
                    activeUsersOnPage
            );

        } catch (AppException ex) {
            return new GetAllResponse(
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
    public MessageResponse updateProfile(UserRequest userRequest, Set<Role> roles){

        if (isUsernameOrEmailAlreadyTaken(userRequest.getUsername(), userRequest.getEmail(), userRequest.getId())) {
            return new MessageResponse(400, HttpStatus.BAD_REQUEST, "Username or Email is already taken!");
        }

        User user = createUserFromRequest(userRequest);
        user.setId(userRequest.getId());
        user.setRoles(roles);
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

    @Override
    public MessageResponse emailSendToken(EmailForgotRequest emailForgotRequest){
        String email = emailForgotRequest.getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(!optionalUser.isPresent()){
            return new MessageResponse(404, HttpStatus.NOT_FOUND, "Email isn't already taken!");
        }

        User user = optionalUser.get();
        String genToken = RandomStringUtils.randomNumeric(5);
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(1);
        Token token = new Token(genToken, expiry, user);
        token.setDeleted(false);

        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("The token is used to change your password");
            message.setText(genToken);
            javaMailSender.send(message);

            tokenRepository.save(token);
            return new MessageResponse(200, HttpStatus.OK, "The token code has been sent to your email!");
        }catch (AppException ex){
            return new MessageResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred. Please try again later or contact support for assistance."
            );
        }
    }

    @Override
    public MessageResponse checkToken(TokenRequest tokenRequest){
        Optional<User> getUserByEmail = userRepository.findByEmail(tokenRequest.getEmail());

        if(!getUserByEmail.isPresent()){
            return new MessageResponse(404, HttpStatus.NOT_FOUND, "Email isn't already taken!");
        }

        User user = getUserByEmail.get();

        List<Token> listTokenByUserIdAndDeleted = tokenRepository.findByUserIdAndDeleted(user.getId(), false);
        if(listTokenByUserIdAndDeleted.isEmpty()){
            return new MessageResponse(404, HttpStatus.NOT_FOUND, "Email not found!");
        }else{
            Token tokenAvaliable = listTokenByUserIdAndDeleted.get(listTokenByUserIdAndDeleted.size() -1);

            int comparsion = tokenAvaliable.getExpiry().compareTo(LocalDateTime.now());
            // check expiry
            if (comparsion < 0) {
                Token tokenUpdateDeleted = new Token(tokenAvaliable.getId(),
                        tokenAvaliable.getToken(),
                        tokenAvaliable.getExpiry(),
                        tokenAvaliable.getFailedCount() + 1,
                        tokenAvaliable.getUser(),
                        tokenAvaliable.getCreatedTime(),
                        LocalDateTime.now(),
                        true
                );
                tokenRepository.save(tokenUpdateDeleted);
                return new MessageResponse(403, HttpStatus.FORBIDDEN,"Token has expired. Please try again later!");
            }
            //check failed count
            if(tokenAvaliable.getFailedCount() > 5){
                Token tokenUpdateDeleted = new Token(tokenAvaliable.getId(),
                        tokenAvaliable.getToken(),
                        tokenAvaliable.getExpiry(),
                        tokenAvaliable.getFailedCount() + 1,
                        tokenAvaliable.getUser(),
                        tokenAvaliable.getCreatedTime(),
                        LocalDateTime.now(),
                        true
                );
                tokenRepository.save(tokenUpdateDeleted);
                return new MessageResponse(429, HttpStatus.TOO_MANY_REQUESTS,"Too many failed attempts. Please try again later!");
            }
            // check token
            if(tokenRequest.getToken() != tokenAvaliable.getToken()){
                Token tokenUpdateFaliledCount = new Token(tokenAvaliable.getId(),
                            tokenAvaliable.getToken(),
                            tokenAvaliable.getExpiry(),
                            tokenAvaliable.getFailedCount() + 1,
                            tokenAvaliable.getUser(),
                            tokenAvaliable.getCreatedTime(),
                            LocalDateTime.now(),
                            tokenAvaliable.getDeleted()
                        );
                    tokenRepository.save(tokenUpdateFaliledCount);
                    return new MessageResponse(401, HttpStatus.UNAUTHORIZED,"Invalid token. Please try again!");
            }

            return new MessageResponse(200, HttpStatus.OK,"Request token successful!");
        }
    }

    @Override
    public MessageResponse changePassword(PasswordRequest passwordRequest){
        Optional<User> user = userRepository.findByUsername(passwordRequest.getUsername());
        if(!user.isPresent()){
            return new MessageResponse(404, HttpStatus.NOT_FOUND,"User not found!");
        }

        User userDb = user.get();

        String encodedPassword = encoder.encode(userDb.getPassword());
        User updateUser = new User(
                userDb.getUsername(),
                userDb.getEmail(),
                encodedPassword,
                userDb.getPhone(),
                userDb.getAddress()
                );
        updateUser.setId(userDb.getId());
        updateUser.setRoles(userDb.getRoles());
        updateUser.setDeleted(false);
        try{
            userRepository.save(updateUser);
            return new MessageResponse(200,HttpStatus.OK,"User updated successfully!");
        }catch (AppException ex){
            return new MessageResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred. Please try again later or contact support for assistance."
            );
        }
    }

    private boolean isUsernameOrEmailAlreadyTaken(String username, String email, Long userId) {
        return userRepository.existsByUsernameAndDifferentId(username, userId) || userRepository.existsByEmailAndDifferentId(email, userId);
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
