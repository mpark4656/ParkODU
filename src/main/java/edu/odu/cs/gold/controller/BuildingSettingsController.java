package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Building;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.BuildingRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This controller provides methods for returning html pages that allow
 * admins to configure building.
 *
 * For actual manipulation of Building models such as update, add, and delete,
 * see BuildingRestController.java
 */
@Controller
@RequestMapping("/settings/building")
public class BuildingSettingsController {

    private BuildingRepository buildingRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;

    public BuildingSettingsController(BuildingRepository buildingRepository,
                                    FloorRepository floorRepository,
                                    ParkingSpaceRepository parkingSpaceRepository) {
        this.buildingRepository = buildingRepository;
        this.floorRepository = floorRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    /**
     * This method returns "settings/building/index" with a collection of all building objects
     * added to the model.
     *
     * @param successMessage String
     * @param infoMessage String
     * @param warningMessage String
     * @param dangerMessage String
     * @param model Model
     * @return String "settings/building/index"
     */
    @GetMapping({"","/","/index"})
    public String index(@RequestParam(value = "successMessage", required = false) String successMessage,
                        @RequestParam(value = "infoMessage", required = false) String infoMessage,
                        @RequestParam(value = "warningMessage", required = false) String warningMessage,
                        @RequestParam(value = "dangerMessage", required = false) String dangerMessage,
                        Model model) {

        List<Building> buildings = new ArrayList<>(buildingRepository.findAll());
        buildings.sort(Comparator.comparing(Building::getName));
        model.addAttribute("buildings", buildings);

        // Alerts
        if (successMessage != null) { model.addAttribute("successMessage", successMessage); }
        if (infoMessage != null) { model.addAttribute("infoMessage", infoMessage); }
        if (warningMessage != null) { model.addAttribute("warningMessage", warningMessage); }
        if (dangerMessage != null) { model.addAttribute("dangerMessage", dangerMessage); }

        return "settings/building/index";
    }

    /**
     * Method to return the settings/building/create.html template page
     * @param model Model
     * @return String "settings/building/create"
     */
    @GetMapping("/create")
    public String create(Model model) {
        Building building = new Building();
        model.addAttribute("building", building);
        return "settings/building/create";
    }

    /**
     * Method to create a new building
     * @param building Building
     * @param model Model
     * @param redirectAttributes RedirectAttributes
     * @return
     */
    @PostMapping("/create")
    public String create(Building building,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        boolean isSuccessful = false;
        boolean isDuplicate = false;
        try {
            Predicate predicate = Predicates.or(
                    Predicates.equal("buildingKey", building.getBuildingKey()),
                    Predicates.equal("name", building.getName())
            );
            int existingCount = buildingRepository.countByPredicate(predicate);
            if (existingCount == 0) {
                buildingRepository.save(building);
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
            redirectAttributes.addAttribute("successMessage", "The Building " + building.getName() + " was successfully created.");
        }
        else if (isDuplicate) {
            model.addAttribute("dangerMessage", "A Building with the name " + building.getName() + " already exists.");
            model.addAttribute("building", building);
            return "settings/building/create";
        }
        else {
            redirectAttributes.addAttribute("dangerMessage", "An error occurred when attempting to create a Building.");
        }
        return "redirect:/settings/building/index";
    }

    /**
     * Method to return the settings/building/edit template
     * @param buildingKey String
     * @param model Model
     * @return String "settings/building/edit"
     */
    @GetMapping("/edit/{buildingKey}")
    public String edit(@PathVariable("buildingKey") String buildingKey,
                       Model model) {
        Building building = buildingRepository.findByKey(buildingKey);
        model.addAttribute("building", building);
        return "settings/building/edit";
    }

    /**
     * Method that modifies the attributes of currently existing building
     * @param building Building
     * @param model Model
     * @param redirectAttributes RedirectAttributes
     * @return String redirection to previous page
     */
    @PostMapping("/edit")
    public String edit(Building building,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        boolean isSuccessful = false;
        boolean isDuplicate = false;

        try {
            Predicate predicate = Predicates.and(
                    Predicates.notEqual("buildingKey", building.getBuildingKey()),
                    Predicates.equal("name", building.getName())
            );
            int duplicateCount = buildingRepository.countByPredicate(predicate);
            if (duplicateCount == 0) {
                Building existingBuilding = buildingRepository.findByKey(building.getBuildingKey());
                existingBuilding.setLatitude(building.getLatitude());
                existingBuilding.setLongitude(building.getLongitude());
                existingBuilding.setName(building.getName());
                existingBuilding.setDescription(building.getDescription());
                existingBuilding.setAddress(building.getAddress());

                buildingRepository.save(existingBuilding);
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
            redirectAttributes.addAttribute("successMessage", "The Building " + building.getName() + " was successfully updated.");
        }
        else if (isDuplicate) {
            model.addAttribute("dangerMessage", "A Building with the name " + building.getName() + " already exists.");
            model.addAttribute("building", building);
            return "settings/building/edit";
        }
        else {
            redirectAttributes.addAttribute("dangerMessage", "An error occurred when attempting to update a Building.");
        }
        return "redirect:/settings/building/index";
    }

    /**
     * Method that deletes an existing building and all of the floor and parking spaces inside it
     * @param buildingKey String
     * @param redirectAttributes RedirectAttributes
     * @return String "redirect:/settings/building/index"
     */
    @PostMapping("/delete")
    public String delete(@RequestParam("buildingKey") String buildingKey,
                         RedirectAttributes redirectAttributes) {
        boolean isSuccessful = false;
        Building building = null;
        try {
            building = buildingRepository.findByKey(buildingKey);
            if (building != null) {
                buildingRepository.delete(buildingKey);
                isSuccessful = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Alerts
        if (isSuccessful) {
            redirectAttributes.addAttribute("successMessage", "The Building " + building.getName() + " was successfully deleted.");
        }
        else {
            redirectAttributes.addAttribute("dangerMessage", "An error occurred when attempting to delete a Building.");
        }
        return "redirect:/settings/building/index";
    }
}
