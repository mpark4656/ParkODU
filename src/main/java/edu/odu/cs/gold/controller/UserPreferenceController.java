package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Building;
import edu.odu.cs.gold.model.PermitType;
import edu.odu.cs.gold.model.SpaceType;
import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.repository.BuildingRepository;
import edu.odu.cs.gold.repository.PermitTypeRepository;
import edu.odu.cs.gold.repository.SpaceTypeRepository;
import edu.odu.cs.gold.repository.UserRepository;
import edu.odu.cs.gold.security.AuthenticatedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/user_preference")
public class UserPreferenceController {

    private UserRepository userRepository;
    private PermitTypeRepository permitTypeRepository;
    private SpaceTypeRepository spaceTypeRepository;
    private BuildingRepository buildingRepository;


    public UserPreferenceController(UserRepository userRepository,
                                    PermitTypeRepository permitTypeRepository,
                                    SpaceTypeRepository spaceTypeRepository,
                                    BuildingRepository buildingRepository) {
        this.userRepository = userRepository;
        this.permitTypeRepository = permitTypeRepository;
        this.spaceTypeRepository = spaceTypeRepository;
        this.buildingRepository = buildingRepository;
    }

    @GetMapping({"", "/", "/index"})
    public String index(@RequestParam(value = "successMessage", required = false) String successMessage,
                        @RequestParam(value = "infoMessage", required = false) String infoMessage,
                        @RequestParam(value = "warningMessage", required = false) String warningMessage,
                        @RequestParam(value = "dangerMessage", required = false) String dangerMessage,
                        Model model) {
        try {
            AuthenticatedUser authenticatedUser =
                    (AuthenticatedUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Predicate predicate = Predicates.equal("username", authenticatedUser.getUsername());
            List<User> users = userRepository.findByPredicate(predicate);

            if(users.size() != 1) {
                // Error
                return "home/index";
            } else {
                User user = users.get(0);
                List<PermitType> permitTypes = new ArrayList<>(permitTypeRepository.findAll());
                List<SpaceType> spaceTypes = new ArrayList<> (spaceTypeRepository.findAll());
                List<Building> buildings = new ArrayList<> (buildingRepository.findAll());

                permitTypes.sort(Comparator.comparing(PermitType::getName));
                spaceTypes.sort(Comparator.comparing(SpaceType::getName));
                buildings.sort(Comparator.comparing(Building::getName));

                model.addAttribute("user", user);
                model.addAttribute("permitTypes", permitTypes);
                model.addAttribute("spaceTypes", spaceTypes);
                model.addAttribute("buildings", buildings);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        // Alerts
        if (successMessage != null) { model.addAttribute("successMessage", successMessage); }
        if (infoMessage != null) { model.addAttribute("infoMessage", infoMessage); }
        if (warningMessage != null) { model.addAttribute("warningMessage", warningMessage); }
        if (dangerMessage != null) { model.addAttribute("dangerMessage", dangerMessage); }

        return "user_preference/index";
    }

    @PostMapping("/edit")
    public String edit(User user,
                       RedirectAttributes redirectAttributes) {

        boolean duplicateEmail = false;
        boolean duplicateUsername = false;
        boolean success = false;

        Predicate emailPredicate = Predicates.and(
                Predicates.notEqual("userKey", user.getUserKey()),
                Predicates.equal("email", user.getEmail())
        );

        Predicate userNamePredicate = Predicates.and(
                Predicates.notEqual("userKey", user.getUserKey()),
                Predicates.equal("username", user.getUsername())
        );


        int emailPredicateCount = userRepository.countByPredicate(emailPredicate);
        int userNamePredicateCount = userRepository.countByPredicate(userNamePredicate);
        if(emailPredicateCount != 0) {
            duplicateEmail = true;
        } else if(userNamePredicateCount != 0) {
            duplicateUsername = true;
        } else {
            User existingUser = userRepository.findByKey(user.getUserKey());

            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setPreferredStartingAddress(user.getPreferredStartingAddress());
            existingUser.setPreferredDestinationBuilding(user.getPreferredDestinationBuilding());
            existingUser.setPreferredPermitTypes(user.getPreferredPermitTypes());
            existingUser.setPreferredSpaceTypes(user.getPreferredSpaceTypes());

            /*
            if(preferredPermitTypes == null) {
                existingUser.setPreferredPermitTypes(new HashSet<>());
            } else {
                existingUser.setPreferredPermitTypes(new HashSet<>(preferredPermitTypes));
            }

            if(preferredSpaceTypes == null) {
                existingUser.setPreferredSpaceTypes(new HashSet<> ());
            } else {
                existingUser.setPreferredSpaceTypes(new HashSet<>(preferredSpaceTypes));
            }
            */

            userRepository.save(existingUser);
            success = true;
        }

        if(success) {
            redirectAttributes.addAttribute(
                    "successMessage",
                    "Your information has been successfully updated."
            );
        }

        if(duplicateEmail) {
            redirectAttributes.addAttribute(
                    "dangerMessage",
                    "That e-mail address already exists."
            );
        }

        if(duplicateUsername) {
            redirectAttributes.addAttribute(
                    "dangerMessage",
                    "That username already exists."
            );
        }

        return "redirect:/user_preference/index";
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
        return "redirect:/user_preference/index";
    }
}
