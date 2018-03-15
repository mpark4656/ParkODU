package edu.odu.cs.gold.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService() {

    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByConfirmationToken(String confirmationToken) {
        return userRepository.findByConfirmationToken(confirmationToken);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

}
