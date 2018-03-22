package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.FloorStatisticRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import edu.odu.cs.gold.service.GarageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This controller provides methods for returning html pages that allow
 * admins to configure garage.
 *
 * For actual manipulation of Garage models such as update, add, and delete,
 * see GarageRestController.java
 */
@Controller
@RequestMapping("/settings/garage")
public class GarageSettingsController {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private FloorStatisticRepository floorStatisticRepository;
    private GarageService garageService;

    public GarageSettingsController(GarageRepository garageRepository,
                            FloorRepository floorRepository,
                            ParkingSpaceRepository parkingSpaceRepository,
                            FloorStatisticRepository floorStatisticRepository,
                            GarageService garageService) {
        this.garageRepository = garageRepository;
        this.floorRepository = floorRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.floorStatisticRepository = floorStatisticRepository;
        this.garageService = garageService;
    }

    /**
     * This method returns "settings/garage/index" with a collection of all garage objects
     * added to the model.
     *
     * @param successMessage String
     * @param infoMessage String
     * @param warningMessage String
     * @param dangerMessage String
     * @param model Model
     * @return String "settings/garage/index"
     */
    @GetMapping({"","/","/index"})
    public String index(@RequestParam(value = "successMessage", required = false) String successMessage,
                        @RequestParam(value = "infoMessage", required = false) String infoMessage,
                        @RequestParam(value = "warningMessage", required = false) String warningMessage,
                        @RequestParam(value = "dangerMessage", required = false) String dangerMessage,
                        Model model) {

        List<Garage> garages = new ArrayList<>(garageRepository.findAll());
        garages.sort(Comparator.comparing(Garage::getName));
        model.addAttribute("garages", garages);

        // Alerts
        if (successMessage != null) { model.addAttribute("successMessage", successMessage); }
        if (infoMessage != null) { model.addAttribute("infoMessage", infoMessage); }
        if (warningMessage != null) { model.addAttribute("warningMessage", warningMessage); }
        if (dangerMessage != null) { model.addAttribute("dangerMessage", dangerMessage); }

        return "settings/garage/index";
    }

    /**
     * Method to return the settings/garage/create.html template page
     * @param model Model
     * @return String "settings/garage/create"
     */
    @GetMapping("/create")
    public String create(Model model) {
        Garage garage = new Garage();
        garage.generateGarageKey();
        model.addAttribute("garage", garage);
        return "settings/garage/create";
    }

    /**
     * Method to create a new garage
     * @param garage Garage
     * @param model Model
     * @param redirectAttributes RedirectAttributes
     * @return
     */
    @PostMapping("/create")
    public String create(Garage garage,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        boolean isSuccessful = false;
        boolean isDuplicate = false;
        try {
            Predicate predicate = Predicates.or(
                Predicates.equal("garageKey", garage.getGarageKey()),
                Predicates.equal("name", garage.getName())
            );
            int existingCount = garageRepository.countByPredicate(predicate);
            if (existingCount == 0) {
                garageRepository.save(garage);
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
            redirectAttributes.addAttribute("successMessage", "The Garage " + garage.getName() + " was successfully created.");
        }
        else if (isDuplicate) {
            model.addAttribute("dangerMessage", "A Garage with the name " + garage.getName() + " already exists.");
            model.addAttribute("garage", garage);
            return "settings/garage/create";
        }
        else {
            redirectAttributes.addAttribute("dangerMessage", "An error occurred when attempting to create a Garage.");
        }
        return "redirect:/settings/garage/index";
    }

    /**
     * Method to return the settings/garage/edit template
     * @param garageKey String
     * @param model Model
     * @return String "settings/garage/edit"
     */
    @GetMapping("/edit/{garageKey}")
    public String edit(@PathVariable("garageKey") String garageKey,
                       Model model) {
        Garage garage = garageRepository.findByKey(garageKey);
        model.addAttribute("garage", garage);
        return "settings/garage/edit";
    }

    /**
     * Method that modifies the attributes of currently existing garage
     * @param garage Garage
     * @param model Model
     * @param redirectAttributes RedirectAttributes
     * @return String redirection to previous page
     */
    @PostMapping("/edit")
    public String edit(Garage garage,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        boolean isSuccessful = false;
        boolean isDuplicate = false;

        try {
            Predicate predicate = Predicates.and(
                    Predicates.notEqual("garageKey", garage.getGarageKey()),
                    Predicates.equal("name", garage.getName())
            );
            int duplicateCount = garageRepository.countByPredicate(predicate);
            if (duplicateCount == 0) {
                Garage existingGarage = garageRepository.findByKey(garage.getGarageKey());
                existingGarage.setLatitude(garage.getLatitude());
                existingGarage.setLongitude(garage.getLongitude());
                existingGarage.setName(garage.getName());
                existingGarage.setDescription(garage.getDescription());
                existingGarage.setHeightDescription(garage.getHeightDescription());
                existingGarage.setAddress(garage.getAddress());
                existingGarage.setAddressOne(garage.getAddressOne());
                existingGarage.setAddressTwo(garage.getAddressTwo());
                existingGarage.setCity(garage.getCity());
                existingGarage.setState(garage.getState());
                existingGarage.setZipCode(garage.getZipCode());
                garageRepository.save(existingGarage);
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
            redirectAttributes.addAttribute("successMessage", "The Garage " + garage.getName() + " was successfully updated.");
        }
        else if (isDuplicate) {
            model.addAttribute("dangerMessage", "A Garage with the name " + garage.getName() + " already exists.");
            model.addAttribute("garage", garage);
            return "settings/garage/edit";
        }
        else {
            redirectAttributes.addAttribute("dangerMessage", "An error occurred when attempting to update a Garage.");
        }
        return "redirect:/settings/garage/index";
    }

    /**
     * Method that deletes an existing garage and all of the floor and parking spaces inside it
     * @param garageKey String
     * @param redirectAttributes RedirectAttributes
     * @return String "redirect:/settings/garage/index"
     */
    @PostMapping("/delete")
    public String delete(@RequestParam("garageKey") String garageKey,
                         RedirectAttributes redirectAttributes) {
        boolean isSuccessful = false;
        Garage garage = null;
        try {
            garage = garageRepository.findByKey(garageKey);
            if (garage != null) {
                Predicate predicate = Predicates.equal("garageKey", garageKey);
                parkingSpaceRepository.deleteByPredicate(predicate);
                floorRepository.deleteByPredicate(predicate);
                garageRepository.delete(garageKey);
                isSuccessful = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Alerts
        if (isSuccessful) {
            redirectAttributes.addAttribute("successMessage", "The Garage " + garage.getName() + " was successfully deleted.");
        }
        else {
            redirectAttributes.addAttribute("dangerMessage", "An error occurred when attempting to delete a Garage.");
        }
        return "redirect:/settings/garage/index";
    }
}
