package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.repository.UserRepository;
import edu.odu.cs.gold.service.EmailService;
import edu.odu.cs.gold.service.UserService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
public class RegisterController {

    private UserService userService;
    private EmailService emailService;
    private UserRepository userRepository;

    public RegisterController(UserService userService,
                              EmailService emailService,
                              UserRepository userRepository) {
        this.userService = userService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    // Return registration form template
    @GetMapping("/user/register")
    public String showRegistrationPage(Model model, User user){
        model.addAttribute("user", user);
        return "user/register";
    }

    // Process form input data
    @PostMapping("/user/register")
    public String processRegistrationForm(Model model, @Valid User user, BindingResult bindingResult, HttpServletRequest request) {
        // Lookup user in database by e-mail
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
            user.setId(UUID.randomUUID().toString());
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
        return "user/register";
    }

    // Process confirmation link
    @GetMapping("/user/confirm")
    public String showConfirmationPage(Model model, @RequestParam("token") String token) {
        Predicate predicate = Predicates.equal("confirmationToken", token);
        List<User> userList = userRepository.findByPredicate(predicate);
        System.out.println("Confirmation Token: " + token);
        if (userList != null && !userList.isEmpty()) {
            if(userList.get(0).getEnabled() == true) {
                // Do nothing
            } else {
                userList.get(0).setEnabled(true);
                userService.saveUser(userList.get(0));
            }
        } else {
            System.out.println("");
        }
        return "redirect:/login";
    }
}
