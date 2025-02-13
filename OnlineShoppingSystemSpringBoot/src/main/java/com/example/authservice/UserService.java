package com.example.authservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.model.Address;
import com.example.model.User;
import com.example.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Optional<User> findbyemail(String email) {
    	return this.userRepository.findByEmail(email);
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }
    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void addAddressToUser(Integer userId, Address newAddress) {
        User user = getUserById(userId);
        user.getAddresses().add(newAddress);
        userRepository.save(user);
    }
}

