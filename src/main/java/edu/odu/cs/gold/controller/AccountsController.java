package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.RoleType;
import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.repository.RoleTypeRepository;
import edu.odu.cs.gold.repository.UserRepository;
import edu.odu.cs.gold.service.EmailService;
import edu.odu.cs.gold.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/settings/accounts")
public class AccountsController {

    private UserRepository userRepository;
    private RoleTypeRepository roleTypeRepository;

    public AccountsController(UserRepository userRepository,
                              RoleTypeRepository roleTypeRepository) {
        this.userRepository = userRepository;
        this.roleTypeRepository = roleTypeRepository;
    }

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

    @GetMapping("/create")
    public String create(Model model) {
        User user = new User();
        List<RoleType> roleTypes = new ArrayList<> (roleTypeRepository.findAll());
        model.addAttribute("user", user);
        model.addAttribute("roleTypes", roleTypes);
        return "settings/accounts/create";
    }

    @PostMapping("/create")
    public String create(User user,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        boolean isSuccessful = false;
        boolean isUsernameDuplicate = false;
        boolean isEmailDuplicate = false;
        try {
            user.setUsername(user.getUsername().toLowerCase());

            Predicate predicate = Predicates.or(
                    Predicates.equal("userKey", user.getUserKey()),
                    Predicates.equal("username", user.getUsername())
            );
            int existingUsernameCount = userRepository.countByPredicate(predicate);
            predicate = Predicates.or(
                    Predicates.equal("userKey", user.getUserKey()),
                    Predicates.equal("email",user.getEmail())
            );
            int existingEmailCount = userRepository.countByPredicate(predicate);
            if (existingUsernameCount == 0 && existingEmailCount == 0) {
                user.setConfirmationToken(UUID.randomUUID().toString());
                user.generateUserKey();
                user.getPermissions().add("USER");
                userRepository.save(user);
                isSuccessful = true;
            }
            if (existingUsernameCount > 0) {
                isUsernameDuplicate = true;
            }
            if (existingEmailCount > 0) {
                isEmailDuplicate = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Alerts
        if (isSuccessful) {
            redirectAttributes.addAttribute("successMessage", "The user " + user.getEmail() + " was successfully created.");
        }
        else if (isUsernameDuplicate || isEmailDuplicate) {
            if (isUsernameDuplicate && isEmailDuplicate) {
                model.addAttribute("dangerMessage", "A user with the username " + user.getUsername() + " and email " + user.getEmail() + " already exists.");
            }
            else if (isUsernameDuplicate) {
                model.addAttribute("dangerMessage", "A user with the username " + user.getUsername() + " already exists.");
            }
            else if (isEmailDuplicate) {
                model.addAttribute("dangerMessage", "A user with the email " + user.getEmail() + " already exists.");
            }
            model.addAttribute("user", user);
            return "settings/accounts/create";
        }
        else {
            redirectAttributes.addAttribute("dangerMessage", "An error occurred when attempting to create a User.");
        }

        return "redirect:/settings/accounts/index";
    }

    @GetMapping("/edit/{userKey}")
    public String edit(@PathVariable("userKey") String userKey,
                       Model model) {
        User user = userRepository.findByKey(userKey);
        List<RoleType> roleTypes = new ArrayList<> (roleTypeRepository.findAll());
        model.addAttribute("user", user);
        model.addAttribute("roleTypes", roleTypes);
        return "settings/accounts/edit";
    }

    @PostMapping("/edit")
    public String edit(User user,
                       @RequestParam(value = "isAdmin", required = false) Boolean isAdmin,
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
                existingUser.setRoleType(user.getRoleType());
                existingUser.setEnabled(user.getEnabled());
                if (isAdmin != null && isAdmin == true) {
                    existingUser.getPermissions().add("ADMIN");
                }
                else {
                    for (Iterator<String> permissionIterator = existingUser.getPermissions().iterator(); permissionIterator.hasNext(); ) {
                        String permission = permissionIterator.next();
                        if (permission.equals("ADMIN")) {
                            permissionIterator.remove();
                        }
                    }
                }
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

    @PostMapping("/reset_password")
    public String reset_password(@RequestParam("userKey") String userKey,
                                 @RequestParam("password") String password,
                                 @RequestParam("passwordAgain") String passwordAgain,
                                 RedirectAttributes redirectAttributes) {

        boolean isSuccessful = false;
        boolean passwordMismatch = false;
        String username = null;

        try {
            if (!password.equals(passwordAgain)) {
                passwordMismatch = true;
            }
            else {
                User existingUser = userRepository.findByKey(userKey);
                existingUser.setPassword(password);
                userRepository.save(existingUser);
                isSuccessful = true;
                username = existingUser.getUsername();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Alerts
        if (isSuccessful && username != null) {
            redirectAttributes.addAttribute("successMessage", "The password for " + username + " was successfully changed.");
        }
        else if (passwordMismatch) {
            redirectAttributes.addAttribute("dangerMessage", "Failed to change the password of a User due to a password mismatch.");
        }
        else {
            redirectAttributes.addAttribute("dangerMessage", "An error occurred when attempting to change a User's password.");
        }
        return "redirect:/settings/accounts/index";
    }

    @PostMapping("/set_enabled")
    @ResponseBody
    public String setEnabled(@RequestParam("userEnabled") boolean userEnabled,
                                  @RequestParam("userKey") String userKey) {
        User user = userRepository.findByKey(userKey);
        user.setEnabled(userEnabled);
        userRepository.save(user);
        return userKey + " enabled: " + userEnabled;
    }

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