package edu.odu.cs.gold.service;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByConfirmationToken(String confirmationToken) {
        return userRepository.findByConfirmationToken(confirmationToken);
    }

    public boolean userExists(String userName) {
        Predicate predicate = Predicates.equal("userName", userName);
        List<User> userList = userRepository.findByPredicate(predicate);
        if (userList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(String userKey) {
        Predicate predicate = Predicates.equal("userKey", userKey);
        userRepository.deleteByPredicate(predicate);
    }

    public void refresh(String userId) {

    }
}
