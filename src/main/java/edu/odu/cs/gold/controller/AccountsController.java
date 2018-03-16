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
@RequestMapping("/accounts")
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

    @GetMapping("/edit/{userKey}")
    public String updateGet(@PathVariable String userKey, Model model) {
        User user = userRepository.findByKey(userKey);
        return "accounts/update";
    }

    @PostMapping("/edit/{userKey}")
    public String updatePost(@PathVariable String userKey, Model model) {

        return "accounts/update";
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
        return "redirect:/accounts/index";
    }
}
