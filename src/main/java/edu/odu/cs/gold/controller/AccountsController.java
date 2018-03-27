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

    public AccountsController(UserRepository userRepository,
                              UserService userService,
                              EmailService emailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping({"","/","/index"})
    public String index(Model model) {
        List<User> users = new ArrayList<>(userRepository.findAll());
        users.sort(Comparator.comparing(User::getFirstName));
        model.addAttribute("user", users);
        return "accounts/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "accounts/create";
    }

    @PostMapping("/create")
    public String create(User user) {
        User existingUser0 = null;
        User existingUser1 = null;
        try {
            existingUser0 = userRepository.findByKey(user.getUserKey());
            existingUser1 = userRepository.findByKey(user.getEmail());
            if (existingUser0 == null && existingUser1 == null) {
                user.setConfirmationToken(UUID.randomUUID().toString());
                userRepository.save(user);
            }
            if (existingUser0 != null && existingUser1 == null) {
                user.setUserKey(UUID.randomUUID().toString());
                user.setConfirmationToken(UUID.randomUUID().toString());
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
        model.addAttribute("user", user);
        return "accounts/newuser";
    }

    @PostMapping("/newuser")
    public String newuser(User user, HttpServletRequest request, Model model, BindingResult bindingResult) {
        boolean userExists = userService.userExists(user.getEmail());
        System.out.println("User exists: " + userExists);
        if (userExists) {
            model.addAttribute("alreadyRegisteredMessage", "Oops!  There is already a user registered with the email provided.");
            bindingResult.reject("email");
        }
        else {
            // Disable user until they click on confirmation link in email
            user.setEnabled(false);
            // Generate random 36-character string token for confirmation link
            user.setConfirmationToken(UUID.randomUUID().toString());
            user.setRole("user");
            userService.saveUser(user);
            String appUrl = request.getScheme() + "://" + request.getServerName();
            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(user.getEmail());
            registrationEmail.setSubject("Registration Confirmation");
            registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
                    + appUrl + ":8083/user/confirm?token=" + user.getConfirmationToken());
            registrationEmail.setFrom("noreply@ParkODU.cs.odu.edu");
            emailService.sendEmail(registrationEmail);
            model.addAttribute("confirmationMessage", "A confirmation e-mail has been sent to " + user.getEmail());
        }
        return "accounts/newuser";
    }

    @GetMapping("/edit/{userKey}")
    public String updateGet(@PathVariable("userKey") String userKey, Model model) {
        User user = userRepository.findByKey(userKey);
        model.addAttribute("user", user);
        return "accounts/edit";
    }

    @PostMapping("/edit")
    public String updatePost(User user) {
        User existingUser = null;
        try {
            existingUser = userRepository.findByKey(user.getUserKey());
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

    @PostMapping("/set_enabled")
    @ResponseBody
    public String setAvailability(@RequestParam("userEnabled") boolean userEnabled,
                                  @RequestParam("userKey") String userKey) {
        User user = userRepository.findByKey(userKey);
        user.setEnabled(userEnabled);
        userRepository.save(user);
        return userKey + " enabled: " + userEnabled;
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
