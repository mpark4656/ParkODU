package edu.odu.cs.gold.service;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.repository.UserRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

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

    public User findByUsername(String username) {
        Predicate predicate = Predicates.equal("username", username);
        List<User> users = userRepository.findByPredicate(predicate);
        for (User user : users) {
            return user;
        }
        return null;
    }

    public boolean userExists(String userName) {
        Predicate predicate = Predicates.equal("username", userName);
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
        userRepository.delete(userKey);
    }

    public void refresh(String userId) {

    }
}
