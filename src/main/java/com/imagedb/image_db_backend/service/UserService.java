package com.imagedb.image_db_backend.service;

import com.imagedb.image_db_backend.model.UserSchema;
import com.imagedb.image_db_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<UserSchema> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserSchema> getUserById(String id) {
        return userRepository.findById(id);
    }

    public UserSchema getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createUser(UserSchema user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword("");
        }
        userRepository.save(user);
    }

    public void updateUser(String Id, UserSchema user) {
        Optional<UserSchema> userOptional = userRepository.findById(Id);
        if (userOptional.isPresent()) {
            UserSchema updatedUser = userOptional.get();
            updatedUser.setName(user.getName());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(user.getPassword());
            userRepository.save(updatedUser);
        }
    }

    public boolean checkPassword(String email, String password) {
        UserSchema user = userRepository.findByEmail(email);
        return passwordEncoder.matches(password, user.getPassword());
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
