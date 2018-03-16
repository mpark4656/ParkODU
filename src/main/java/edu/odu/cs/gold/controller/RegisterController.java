package edu.odu.cs.gold.controller;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.security.crypto.password.PasswordEncoder;

import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.service.EmailService;
import edu.odu.cs.gold.service.UserService;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

@Controller
public class RegisterController {

    private PasswordEncoder bCryptPasswordEncoder;
    private UserService userService;
    private EmailService emailService;

    @Autowired
    public RegisterController(PasswordEncoder bCryptPasswordEncoder, UserService userService, EmailService emailService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.emailService = emailService;
    }

    // Return registration form template
    @RequestMapping(value="/user/register", method = RequestMethod.GET)
    public ModelAndView showRegistrationPage(ModelAndView model, User user){
        model.addObject("user", user);
        model.setViewName("user/register");
        return model;
    }

    // Process form input data
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public ModelAndView processRegistrationForm(ModelAndView model, @Valid User user, BindingResult bindingResult, HttpServletRequest request) {

        // Lookup user in database by e-mail
        User userExists = userService.findByEmail(user.getEmail());

        System.out.println(userExists);

        if (userExists != null) {
            model.addObject("alreadyRegisteredMessage", "Oops!  There is already a user registered with the email provided.");
            model.setViewName("user/register");
            bindingResult.reject("email");
        }

        if (bindingResult.hasErrors()) {
            model.setViewName("user/register");
        } else { // new user so we create user and send confirmation e-mail

            // Disable user until they click on confirmation link in email
            user.setEnabled(false);

            // Generate random 36-character string token for confirmation link
            user.setConfirmationToken(UUID.randomUUID().toString());

            userService.saveUser(user);

            String appUrl = request.getScheme() + "://" + request.getServerName();

            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(user.getEmail());
            registrationEmail.setSubject("Registration Confirmation");
            registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
                    + appUrl + "/confirm?token=" + user.getConfirmationToken());
            registrationEmail.setFrom("noreply@ParkODU.cs.odu.edu");

            emailService.sendEmail(registrationEmail);

            model.addObject("confirmationMessage", "A confirmation e-mail has been sent to " + user.getEmail());
            model.setViewName("user/register");
        }

        return model;
    }

    // Process confirmation link
    @RequestMapping(value="/user/confirm", method = RequestMethod.GET)
    public ModelAndView showConfirmationPage(ModelAndView model, @RequestParam("token") String token) {

        User user = userService.findByConfirmationToken(token);

        if (user == null) { // No token found in DB
            model.addObject("invalidToken", "Oops!  This is an invalid confirmation link.");
        } else { // Token found
            model.addObject("confirmationToken", user.getConfirmationToken());
        }

        model.setViewName("confirm");
        return model;
    }

    // Process confirmation link
    @RequestMapping(value="/user/confirm", method = RequestMethod.POST)
    public ModelAndView processConfirmationForm(ModelAndView model, BindingResult bindingResult, @RequestParam Map requestParams, RedirectAttributes redir) {

        model.setViewName("user/confirm");

        Zxcvbn passwordCheck = new Zxcvbn();

        Strength strength = passwordCheck.measure((String)requestParams.get("password"));

        if (strength.getScore() < 3) {
            bindingResult.reject("password");

            redir.addFlashAttribute("errorMessage", "Your password is too weak.  Choose a stronger one.");

            model.setViewName("redirect:confirm?token=" + requestParams.get("token"));
            System.out.println(requestParams.get("token"));
            return model;
        }

        // Find the user associated with the reset token
        User user = userService.findByConfirmationToken((String)requestParams.get("token"));

        // Set new password
        user.setPassword(bCryptPasswordEncoder.encode((String)requestParams.get("password")));

        // Set user to enabled
        user.setEnabled(true);

        // Save user
        userService.saveUser(user);

        model.addObject("successMessage", "Your password has been set!");
        return model;
    }

}
