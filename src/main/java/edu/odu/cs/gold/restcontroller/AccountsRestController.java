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
        if (user.getId() != null)  {
            user.setId(UUID.randomUUID().toString());
            user.setEnabled(true);
            userRepository.save(user);
            userService.refresh(user.getId());
            System.out.println("Saved User: " + user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody User user) {


        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody User user) {
        if (user.getId() != null) {
            User existingUser = userRepository.findByKey(user.getId());
            if (existingUser != null) {
                userRepository.delete(user.getId());
                System.out.println("Deleted User with userKey: " + user.getId());
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.ok().build();
    }
}
