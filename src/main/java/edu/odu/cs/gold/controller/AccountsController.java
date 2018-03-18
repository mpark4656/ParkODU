package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.UserRepository;
import edu.odu.cs.gold.service.UserService;
import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.service.EmailService;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/settings/accounts")
public class AccountsController {

    private UserRepository userRepository;
    private UserService userService;
    private EmailService emailService;

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
                user.generateConfirmationToken();
                userRepository.save(user);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/settings/accounts/index";
    }

    @GetMapping("/newuser")
    public String newuser(Model model) {
       User user = new User();
        user.generateId();
        model.addAttribute("user", user);
        return "accounts/newuser";
    }

    @PostMapping("/newuser")
    public String newuser(@Valid User user, Model model, HttpServletRequest request, BindingResult bindingResult) {
        boolean userExists = userService.userExists(user.getEmail());
        System.out.println("User exists: " + userExists);
        if (!userExists) {
            user.setRole("user");
            user.generateConfirmationToken();
            userRepository.save(user);
            String appUrl = request.getScheme() + "://" + request.getServerName();
            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(user.getEmail());
            registrationEmail.setSubject("Registration Confirmation");
            //TODO - "8083" needs to be removed before push to CS hosted server
            registrationEmail.setText("To confirm your e-mail address, please click the link below:\n" + appUrl + ":8083/user/confirm?token=" + user.getConfirmationToken());
            registrationEmail.setFrom("noreply@ParkODU.cs.odu.edu");
            emailService.sendEmail(registrationEmail);
            model.addAttribute("confirmationMessage", "A confirmation e-mail has been sent to " + user.getEmail());
        }
        else {
            model.addAttribute("alreadyRegisteredMessage", "Oops!  There is already a user registered with the email provided.");
            bindingResult.reject("email");
        }

        return "redirect:/settings/accounts/newuser";
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
