package com.beaconfire.quizapp.service;

import com.beaconfire.quizapp.model.User;
import com.beaconfire.quizapp.repository.UserRepository;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> getUserById(int userId) {
        return userRepository.getUserById(userId); // Ensure you have this in UserRepository as well
    }

    public List<User> getPaginatedUsers(int pageNumber, int pageSize) {
        int offset = pageNumber * pageSize;
        return userRepository.getUsersWithPagination(offset, pageSize);
    }

    public boolean registerUser(User user) {
        if (userRepository.getUserByUsername(user.getUsername()).isPresent() ||
                userRepository.getUserByEmail(user.getEmail()).isPresent()) {
            return false; // Username or email already exists
        }

        userRepository.createUser(user);
        return true;
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }


    // Authenticate user login
    public Optional<User> loginUser(String username, String password) {
        Optional<User> user = userRepository.getUserByUsername(username);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }


    public boolean activateUser(String username) {
        Optional<User> user = userRepository.getUserByUsername(username);
        if (user.isPresent()) {
            userRepository.activateUserByUsername(username);
            return true;
        }
        return false;
    }

    public boolean suspendUser(String username) {
        Optional<User> user = userRepository.getUserByUsername(username);
        if (user.isPresent()) {
            userRepository.inactivateUserByUsername(username);
            return true;
        }
        return false;
    }

    public Optional<Integer> getUserIdByUsername(String username) {
        return userRepository.getUserIDWithUsername(username);
    }

    public void activateUserById(int userId) {
        userRepository.activateUserById(userId);
    }

    public void suspendUserById(int userId) {
        userRepository.inactivateUserById(userId);
    }

    public List<User> getNonAdminUsers(int page, int pageSize) {
        int offset = page * pageSize;
        return userRepository.getNonAdminUsers(offset, pageSize);
    }

}
