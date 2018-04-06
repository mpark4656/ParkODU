package edu.odu.cs.gold.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;

import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.repository.RoleTypeRepository;
import edu.odu.cs.gold.repository.UserRepository;
import edu.odu.cs.gold.service.EmailService;
import edu.odu.cs.gold.service.UserService;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    private UserService userService;
    private EmailService emailService;
    private UserRepository userRepository;

    /**
     * Constructor for the RegisterController class that implements user registration
     * @param userService handles processing of users
     * @param emailService handles processing of email notifications and sending information
     * @param userRepository handles methods within Hazelcast
     */

    public RegisterController(UserService userService,
                              EmailService emailService,
                              UserRepository userRepository) {

        this.userService = userService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    /**
     * GET method user/regiser for user registration
     * @param model MVC
     * @param user user model
     * @return user/register
     */

    @GetMapping("/user/register")
    public String showRegistrationPage(Model model, User user){
        model.addAttribute("user", user);
        return "user/register";
    }

    /**
     * POST method user/register for user registration
     * @param model MVC
     * @param user user model
     * @param bindingResult validate method for email
     * @param request handler for confirmation link processing
     * @return user/register
     */

    @PostMapping("/user/register")
    public String processRegistrationForm(Model model,
                                          @Valid User user,
                                          BindingResult bindingResult,
                                          HttpServletRequest request) {

        // Lookup user in database by e-mail
        user.setUsername(user.getUserKey().toLowerCase());
        boolean emailExists = userService.userExists(user.getEmail());
        boolean userExists = userService.userExists(user.getUsername());

        System.out.println("User exists: " + emailExists);
        if (emailExists ) {
            model.addAttribute("dangerMessage", "Oops! An existing user is currently has account registered with that email!");
            bindingResult.reject("email");
        }
        if (userExists) {
            model.addAttribute("dangerMessage", "Oops! The "+ user.getUsername() +" is not available!");
            bindingResult.reject("userName");
        }
        else {
            // Disable user until they click on confirmation link in email
            user.setEnabled(false);
            // Generate random 36-character string token for confirmation link
            user.generateConfirmationToken();
            user.generateUserKey();
            user.setRoleType("user");
            user.getPermissions().add("USER");
            userService.saveUser(user);
            String appUrl = request.getScheme() + "://" + request.getServerName();
            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(user.getEmail());
            registrationEmail.setSubject("Registration Confirmation");
            registrationEmail.setText("You have been registered with the username:\n\n" + user.getUsername() + "\n\nTo confirm your e-mail address, please click the link below:\n"
                    + "https://411golds18.cs.odu.edu/user/confirm?token=" + user.getConfirmationToken());
            registrationEmail.setFrom("noreply@ParkODU.cs.odu.edu");
            emailService.sendEmail(registrationEmail);
            model.addAttribute("successMessage", "A confirmation e-mail has been sent to " + user.getEmail());
        }
        return "user/register";
    }

    /**
     * GET method user/confirm that prepares model and processes confirmation link for user account enabling
     * @param model MVC
     * @param token request paramater for confirmation token
     * @param redirectAttributes prepares attributes for redirection
     * @return redirect:/user/login
     */

    @GetMapping("/user/confirm")
    public String showConfirmationPage(Model model,
                                       @RequestParam("token") String token,
                                       RedirectAttributes redirectAttributes) {

        Predicate predicate = Predicates.equal("confirmationToken", token);
        List<User> userList = userRepository.findByPredicate(predicate);
        System.out.println("Confirmation Token: " + token);
        if (userList != null && !userList.isEmpty()) {
            if(userList.get(0).getEnabled() == true) {
                model.addAttribute("dangerMessage", "Oops! Confirmation link not valid!");
                redirectAttributes.addAttribute("attr","confirmationLinkError");
            } else {
                userList.get(0).setEnabled(true);
                userService.saveUser(userList.get(0));
                model.addAttribute("successMessage", "Confirmation link valid!");
                redirectAttributes.addAttribute("successMessage","Confirmation link valid!");
            }
        } else {
            System.out.println("");
        }
        return "redirect:user/login";
    }

    /**
     * GET method user/login the prepares login page after confirmation link submission
     * @param model MVC
     * @param param parameter passed from redirectionAttributes in user/confirm GET method
     * @return String home/login
     */

    @GetMapping("/user/login")
    public String login(Model model,
                        @RequestParam("attr") String param) {

        if(param == "confirmationLinkSuccess") {
            model.addAttribute("successMessage","Confirmation link verified!");

        }
        if(param == "confirmationLinkError") {
            model.addAttribute("dangerMessage", "Oops! Confirmation link not valid!");

        }
        else {
            // DO NOTHING
        }
        return "redirect:home/login";
    }
}
