package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.*;
import edu.odu.cs.gold.repository.*;
import edu.odu.cs.gold.service.SpaceTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/settings/space_type")
public class SpaceTypeSettingsController {

    private ParkingSpaceRepository parkingSpaceRepository;
    private SpaceTypeRepository spaceTypeRepository;
    private SpaceTypeService spaceTypeService;

    /**
     * Constructor
     * @param parkingSpaceRepository ParkingSpaceRepository
     * @param spaceTypeRepository SpaceTypeRepository
     */
    public SpaceTypeSettingsController(ParkingSpaceRepository parkingSpaceRepository,
                                       SpaceTypeRepository spaceTypeRepository,
                                       SpaceTypeService spaceTypeService) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.spaceTypeRepository = spaceTypeRepository;
        this.spaceTypeService = spaceTypeService;
    }

    /**
     * Returns the default index page with existing SpaceType objects
     * @param model Model
     * @return String default index page
     */
    @GetMapping({"", "/", "/index"})
    public String index(@RequestParam(value = "successMessage", required = false) String successMessage,
                        @RequestParam(value = "infoMessage", required = false) String infoMessage,
                        @RequestParam(value = "warningMessage", required = false) String warningMessage,
                        @RequestParam(value = "dangerMessage", required = false) String dangerMessage,
                        Model model) {

        List<SpaceType> spaceTypes = new ArrayList<>(spaceTypeRepository.findAll());
        model.addAttribute("spaceTypes", spaceTypes);

        // Alerts
        if (successMessage != null) { model.addAttribute("successMessage", successMessage); }
        if (infoMessage != null) { model.addAttribute("infoMessage", infoMessage); }
        if (warningMessage != null) { model.addAttribute("warningMessage", warningMessage); }
        if (dangerMessage != null) { model.addAttribute("dangerMessage", dangerMessage); }
        return "settings/space_type/index";
    }

    /**
     * Returns the settings/space_type/create.html page
     * @return String settings/space_type/create.html
     */
    @GetMapping("/create")
    public String create(@RequestParam(value = "successMessage", required = false) String successMessage,
                         @RequestParam(value = "infoMessage", required = false) String infoMessage,
                         @RequestParam(value = "warningMessage", required = false) String warningMessage,
                         @RequestParam(value = "dangerMessage", required = false) String dangerMessage,
                         Model model) {

        // Alerts
        if (successMessage != null) { model.addAttribute("successMessage", successMessage); }
        if (infoMessage != null) { model.addAttribute("infoMessage", infoMessage); }
        if (warningMessage != null) { model.addAttribute("warningMessage", warningMessage); }
        if (dangerMessage != null) { model.addAttribute("dangerMessage", dangerMessage); }

        return "settings/space_type/create";
    }

    /**
     * Post method that creates a new space type and redirects user back to the index
     * @param spaceTypeName String
     * @param spaceTypeDescription String
     * @return String redirection back to settings/space_type/index
     */
    @PostMapping("/create")
    public String create(@RequestParam("spaceTypeName") String spaceTypeName,
                         @RequestParam("spaceTypeDescription") String spaceTypeDescription) {

        // Do not accept null or empty space type name
        if(spaceTypeName == null || spaceTypeName.trim().isEmpty()) {
            System.err.println("The space type name cannot be null or empty!");
            return "redirect:/settings/space_type/index";
        }

        // Do not accept null or empty space type description
        if(spaceTypeDescription == null || spaceTypeDescription.trim().isEmpty()) {
            System.err.println("The space type description cannot be null or empty!");
            return "redirect:/settings/space_type/index";
        }

        // Remove any leading or trailing spaces just to be safe
        spaceTypeName = spaceTypeName.trim();
        spaceTypeDescription = spaceTypeDescription.trim();

        // Get the list of current existing space types.
        List<SpaceType> spaceTypes = new ArrayList<>(spaceTypeRepository.findAll());

        // If any of the existing space types have the same name as the given name, do not create a new space
        for(SpaceType eachSpaceType : spaceTypes) {
            if(eachSpaceType.getName().equals(spaceTypeName)) {
                System.err.println("The specified space name already exists!");
                return "redirect:/settings/space_type/index";
            }
        }

        // After passing all the checks, create this new space and save it.
        try {
            // Create a new SpaceType object. The unique key is auto-generated in the constructor.
            SpaceType spaceType = new SpaceType(spaceTypeName, spaceTypeDescription);
            spaceTypeRepository.save(spaceType);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return "redirect:/settings/space_type/index";
    }

    /**
     * Update the description of the space type
     * @param spaceTypeKey String
     * @param spaceDescription String
     * @return String
     */
    @PostMapping("/set_space_description")
    @ResponseBody
    public String set_description(@RequestParam("spaceTypeKey") String spaceTypeKey,
                                  @RequestParam("spaceDescription") String spaceDescription) {

        SpaceType spaceType = spaceTypeRepository.findByKey(spaceTypeKey);

        // If the space type does not exist
        if(spaceType == null) {
            System.err.println("The specified space type does not exist.");
            return "The specified space type, " + spaceTypeKey + "does not exist!";
        }

        if(spaceDescription == null || spaceDescription.trim().isEmpty()) {
            System.err.println("The specified space description is null or empty");
            return "The specified space description is null or empty!";
        }

        spaceType.setDescription(spaceDescription.trim());
        spaceTypeRepository.save(spaceType);

        spaceTypeService.refresh(spaceType.getSpaceTypeKey());

        System.err.println("The space type description was successfully updated.");
        return "The description of the space type, " + spaceTypeKey + " was successfully updated";
    }

    /**
     * Update the name of the space type
     * @param spaceTypeKey String
     * @param spaceName String
     * @return String
     */
    @PostMapping("/set_space_name")
    @ResponseBody
    public String set_name(@RequestParam("spaceTypeKey") String spaceTypeKey,
                           @RequestParam("spaceName") String spaceName) {

        SpaceType spaceType = spaceTypeRepository.findByKey(spaceTypeKey);

        if(spaceType == null) {
            System.err.println("The space type does not exist");
            return "The specified space type, " + spaceTypeKey + "does not exist!";
        }

        if(spaceName == null || spaceName.trim().isEmpty()) {
            System.err.println("The specified space name is null or empty");
            return "The specified space name is null or empty!";
        }

        spaceType.setName(spaceName.trim());
        spaceTypeRepository.save(spaceType);

        spaceTypeService.refresh(spaceType.getSpaceTypeKey());

        System.err.println("The space type name was successfully updated.");
        return "The name of the space type, " + spaceTypeKey + " was successfully updated";
    }

    /**
     * Delete the specified space type. If existing parking spaces use the space type, no action will be taken.
     * @param spaceTypeKey String
     * @return String
     */
    @PostMapping("/delete")
    public String delete(@RequestParam("spaceTypeKey") String spaceTypeKey) {

        if(spaceTypeRepository.findByKey(spaceTypeKey) != null) {
            // Find out if any of the parking spaces have this spaceTypeKey
            Predicate predicate = Predicates.equal("spaceTypeKey", spaceTypeKey);
            List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findByPredicate(predicate);

            if(parkingSpaces.size() > 0) {
                System.err.println("The specified parking space is being used by existing parking spaces.");
                return "redirect:/settings/space_type/index";
            }

            try {
                spaceTypeRepository.delete(spaceTypeKey);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            System.err.println("The space type " + spaceTypeKey + " was successfully deleted");
        } else {
            System.err.println("The space type " + spaceTypeKey + " does not exist");
        }

        return "redirect:/settings/space_type/index";
    }
}