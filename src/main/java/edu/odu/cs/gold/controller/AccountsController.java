package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.repository.RoleTypeRepository;
import edu.odu.cs.gold.repository.UserRepository;
import edu.odu.cs.gold.service.UserService;
import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.model.RoleType;
import edu.odu.cs.gold.service.EmailService;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private RoleTypeRepository roleTypeRepository;

    /**
     *
     * @param userRepository
     * @param userService
     * @param emailService
     * @param roleTypeRepository
     */

    public AccountsController(UserRepository userRepository,
                              UserService userService,
                              EmailService emailService,
                              RoleTypeRepository roleTypeRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.emailService = emailService;
        this.roleTypeRepository = roleTypeRepository;
    }

    /**
     *
     * @param successMessage
     * @param infoMessage
     * @param warningMessage
     * @param dangerMessage
     * @param model
     * @return
     */

    @GetMapping({"", "/", "/index"})
    public String index(@RequestParam(value = "successMessage", required = false) String successMessage,
                        @RequestParam(value = "infoMessage", required = false) String infoMessage,
                        @RequestParam(value = "warningMessage", required = false) String warningMessage,
                        @RequestParam(value = "dangerMessage", required = false) String dangerMessage,
                        Model model) {

        List<User> users = new ArrayList<>(userRepository.findAll());
        users.sort(Comparator.comparing(User::getFirstName));
        model.addAttribute("users", users);

        // Alerts
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }
        if (infoMessage != null) {
            model.addAttribute("infoMessage", infoMessage);
        }
        if (warningMessage != null) {
            model.addAttribute("warningMessage", warningMessage);
        }
        if (dangerMessage != null) {
            model.addAttribute("dangerMessage", dangerMessage);
        }
        return "settings/accounts/index";
    }

    /**
     *
     * @param model
     * @return
     */

    @GetMapping("/create")
    public String create(Model model) {
        User user = new User();
        List<RoleType> roleTypes = new ArrayList<> (roleTypeRepository.findAll());
        model.addAttribute("user", user);
        model.addAttribute("roleTypes", roleTypes);
        return "settings/accounts/create";
    }

    /**
     *
     * @param user
     * @param model
     * @param redirectAttributes
     * @return
     */

    @PostMapping("/create")
    public String create(User user,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        boolean isSuccessful = false;
        boolean isDuplicate = false;
        try {
            Predicate predicate = Predicates.or(
                    Predicates.equal("userKey", user.getUserKey()),
                    Predicates.equal("email",user.getEmail())
            );
            int existingCount = userRepository.countByPredicate(predicate);
            if (existingCount == 0) {
                user.setConfirmationToken(UUID.randomUUID().toString());
                user.generateUserKey();
                userRepository.save(user);
                isSuccessful = true;
            }
            else {
                isDuplicate = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Alerts
        if (isSuccessful) {
            redirectAttributes.addAttribute("successMessage", "The user " + user.getEmail() + " was successfully created.");
        }
        else if (isDuplicate) {
            model.addAttribute("dangerMessage", "The user " + user.getEmail() + " already exists.");
            model.addAttribute("user", user);
            return "settings/accounts/create";
        }
        else {
            redirectAttributes.addAttribute("dangerMessage", "An error occurred when attempting to create a User.");
        }

        return "redirect:/settings/accounts/index";
    }

    /**
     *
     * @param userKey
     * @param model
     * @return
     */

    @GetMapping("/edit/{userKey}")
    public String edit(@PathVariable("userKey") String userKey,
                       Model model) {
        User user = userRepository.findByKey(userKey);
        List<RoleType> roleTypes = new ArrayList<> (roleTypeRepository.findAll());
        model.addAttribute("user", user);
        model.addAttribute("roleTypes", roleTypes);
        return "settings/accounts/edit";
    }

    /**
     *
     * @param user
     * @param model
     * @param redirectAttributes
     * @return
     */

    @PostMapping("/edit")
    public String edit(User user,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        boolean isSuccessful = false;
        boolean isDuplicate = false;

        try {
            Predicate predicate = Predicates.and(
                    Predicates.equal("userKey", user.getUserKey()),
                    Predicates.equal("email", user.getEmail())
            );
            int existingCount = userRepository.countByPredicate(predicate);
            if (existingCount == 1) {
                User existingUser = userRepository.findByKey(user.getUserKey());
                existingUser.setFirstName(user.getFirstName());
                existingUser.setLastName(user.getLastName());
                existingUser.setEmail(user.getEmail());
                existingUser.setPassword(user.getPassword());
                existingUser.setRole(user.getRoleType());
                existingUser.setEnabled(user.isEnabled());
                userRepository.save(existingUser);
                isSuccessful = true;
            } else {
                isDuplicate = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Alerts
        if (isSuccessful) {
            redirectAttributes.addAttribute("successMessage", "The user " + user.getEmail() + " was successfully updated.");
        } else if (isDuplicate) {
            model.addAttribute("dangerMessage", "The user " + user.getEmail() + " already exists.");
            model.addAttribute("user", user);
            return "settings/accounts/edit";
        } else {
            redirectAttributes.addAttribute("dangerMessage", "An error occurred when attempting to update " + user.getEmail() + ".");
        }

        return "redirect:/settings/accounts/index";
    }

    /**
     *
     * @param userEnabled
     * @param userKey
     * @return
     */

    @PostMapping("/set_enabled")
    @ResponseBody
    public String setAvailability(@RequestParam("userEnabled") boolean userEnabled,
                                  @RequestParam("userKey") String userKey) {
        User user = userRepository.findByKey(userKey);
        user.setEnabled(userEnabled);
        userRepository.save(user);
        return userKey + " enabled: " + userEnabled;
    }

    /**
     *
     * @param userKey
     * @param redirectAttributes
     * @return
     */

    @PostMapping("/delete")
    public String delete(@RequestParam("userKey") String userKey,
                         RedirectAttributes redirectAttributes) {
        boolean isSuccessful = false;
        User user = null;
        try {
            user = userRepository.findByKey(userKey);
            userRepository.delete(userKey);
            isSuccessful = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Alerts
        if (isSuccessful) {
            redirectAttributes.addAttribute("successMessage", "The user " + user.getEmail() + " was successfully deleted.");
        } else {
            redirectAttributes.addAttribute("dangerMessage", "An error occurred when attempting to delete the user" + user.getEmail() + ".");
        }
        return "redirect:/settings/accounts/index";
    }
}