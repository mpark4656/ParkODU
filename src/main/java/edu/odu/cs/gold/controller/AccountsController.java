package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.UserRepository;
import edu.odu.cs.gold.service.UserService;
import edu.odu.cs.gold.model.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/settings/accounts")
public class AccountsController {

    private UserRepository userRepository;
    private UserService userService;

    public AccountsController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping({"","/","/index"})
    public String index(Model model) {

        List<User> users = new ArrayList<>(userRepository.findAll());
        for( User entity: users) {
            System.out.println(entity.toString());
        }
        users.sort(Comparator.comparing(User::getEmail));
        model.addAttribute("user", users);
        return "accounts/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        User user = new User();
        user.generateId();
        model.addAttribute("user", user);
        return "accounts/create";
    }

    @PostMapping("/create")
    public String create(User user) {
        User existingUser = null;
        try {
            existingUser = userRepository.findByKey(user.getId());
            if (existingUser == null) {
                userRepository.save(user);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/settings/accounts/index";
    }

    @GetMapping("/edit/{userKey}")
    public String updateGet(@PathVariable("userKey") String userKey, Model model) {
        User user = userRepository.findByKey(userKey);
        model.addAttribute("user", user);
        return "accounts/edit";
    }

    @PostMapping("/edit/{userKey}")
    public String updatePost(User user) {
        User existingUser = null;
        try {
            existingUser = userRepository.findByKey(user.getId());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setRole(user.getRole());
            existingUser.setEnabled(user.getEnabled());
            userRepository.save(existingUser);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/settings/accounts/index";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("userKey") String userKey) {
        System.out.println("Deleting User: " + userKey);
        try {
            userRepository.delete(userKey);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/settings/accounts/index";
    }
}
