package edu.odu.cs.gold.restcontroller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;

import edu.odu.cs.gold.repository.UserRepository;
import edu.odu.cs.gold.model.User;

import edu.odu.cs.gold.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rest/accounts/")
public class AccountsRestController {

    private UserRepository userRepository;
    private UserService userService;

    public AccountsRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody User user) {
        if (user.getUserKey() != null)  {
            user.setUserKey(UUID.randomUUID().toString());
            user.setConfirmationToken(UUID.randomUUID().toString());
            user.setEnabled(true);
            userRepository.save(user);
            userService.refresh(user.getUserKey());
            System.out.println("Saved User: " + user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> edit(@RequestBody User user) {
        User existingUser = userRepository.findByKey(user.getUserKey());
        if (existingUser != null) {
            // Upsert - if null, don't update the field
            if (user.getFirstName() != null) {
                existingUser.setFirstName(user.getFirstName());
            }
            if (user.getLastName() != null) {
                existingUser.setLastName(user.getLastName());
            }
            if (user.getEmail() != null) {
                existingUser.setEmail(user.getEmail());
            }
            if (user.getPassword() != null) {
                existingUser.setPassword(user.getPassword());
            }
            if (user.getRole() != null) {
                existingUser.setRole(user.getRole());
            }
            existingUser.setEnabled(user.getEnabled());

            userRepository.save(existingUser);
            System.out.println("Updated User: " + existingUser);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody User user) {
        if (user.getUserKey() != null) {
            User existingUser = userRepository.findByKey(user.getUserKey());
            if (existingUser != null) {
                userRepository.delete(user.getUserKey());
                System.out.println("Deleted User with userKey: " + user.getUserKey());
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.ok().build();
    }
}
