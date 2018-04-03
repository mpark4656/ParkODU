package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.PermitType;
import edu.odu.cs.gold.model.SpaceType;
import edu.odu.cs.gold.model.User;
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


    public UserPreferenceController(UserRepository userRepository,
                                    PermitTypeRepository permitTypeRepository,
                                    SpaceTypeRepository spaceTypeRepository) {
        this.userRepository = userRepository;
        this.permitTypeRepository = permitTypeRepository;
        this.spaceTypeRepository = spaceTypeRepository;
    }

    @GetMapping({"", "/", "/index"})
    public String index(Model model) {
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

                permitTypes.sort(Comparator.comparing(PermitType::getName));
                spaceTypes.sort(Comparator.comparing(SpaceType::getName));

                model.addAttribute("user", user);
                model.addAttribute("permitTypes", permitTypes);
                model.addAttribute("spaceTypes", spaceTypes);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return "user_preference/index";
    }

    @PostMapping("/edit")
    public String edit(@RequestParam("userKey") String userKey,
                       @RequestParam(name = "preferredPermitTypes", required = false) List<String> preferredPermitTypes,
                       @RequestParam(name = "preferredSpaceTypes", required = false) List<String> preferredSpaceTypes) {

        User user = userRepository.findByKey(userKey);

        if(preferredPermitTypes == null) {
            user.setPreferredPermitTypes(new HashSet<>());
        } else {
            user.setPreferredPermitTypes(new HashSet<>(preferredPermitTypes));
        }

        if(preferredSpaceTypes == null) {
            user.setPreferredSpaceTypes(new HashSet<> ());
        } else {
            user.setPreferredSpaceTypes(new HashSet<>(preferredSpaceTypes));
        }

        userRepository.save(user);

        return "redirect:/";
    }
}
