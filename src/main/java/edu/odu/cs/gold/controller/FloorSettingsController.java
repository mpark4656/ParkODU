package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.repository.*;
import edu.odu.cs.gold.service.GarageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * This controller provides methods for returning html pages that allow
 * admins to configure floors.
 *
 * For actual manipulation of Floor models such as update, add, and delete,
 * see FloorRestController.java
 */
@Controller
@RequestMapping("/settings/floor")
public class FloorSettingsController {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private FloorStatisticRepository floorStatisticRepository;
    private SpaceTypeRepository spaceTypeRepository;
    private GarageService garageService;

    public FloorSettingsController(GarageRepository garageRepository,
                           FloorRepository floorRepository,
                           ParkingSpaceRepository parkingSpaceRepository,
                           FloorStatisticRepository floorStatisticRepository,
                           SpaceTypeRepository spaceTypeRepository,
                           GarageService garageService) {
        this.garageRepository = garageRepository;
        this.floorRepository = floorRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.floorStatisticRepository = floorStatisticRepository;
        this.spaceTypeRepository = spaceTypeRepository;
        this.garageService = garageService;
    }

    /**
     * This method returns "settings/floor/index" with a collection of all garage objects
     * added to the model.
     *
     * @param successMessage String
     * @param infoMessage String
     * @param warningMessage String
     * @param dangerMessage String
     * @param model String
     * @return String "settings/floor/index"
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

        return "settings/floor/index";
    }

    /**
     * This method returns "settings/floor/garage" with a Garage object and
     * a collection of Floor objects found in the garage.
     *
     * @param garageKey String
     * @param successMessage String
     * @param infoMessage String
     * @param warningMessage String
     * @param dangerMessage String
     * @param model Model
     * @return String "settings/floor/garage"
     */
    @GetMapping("garage/{garageKey}")
    public String garage(@PathVariable("garageKey") String garageKey,
                         @RequestParam(value = "successMessage", required = false) String successMessage,
                         @RequestParam(value = "infoMessage", required = false) String infoMessage,
                         @RequestParam(value = "warningMessage", required = false) String warningMessage,
                         @RequestParam(value = "dangerMessage", required = false) String dangerMessage,
                         Model model) {

        Garage garage = garageRepository.findByKey(garageKey);

        Predicate predicate = Predicates.and(
                Predicates.equal("garageKey", garage.getGarageKey())
        );

        List<Floor> floors = new ArrayList<>(floorRepository.findByPredicate(predicate));
        floors.sort(Comparator.comparing(Floor::getNumber));

        model.addAttribute("garage", garage);
        model.addAttribute("floors", floors);

        // Alerts
        if (successMessage != null) { model.addAttribute("successMessage", successMessage); }
        if (infoMessage != null) { model.addAttribute("infoMessage", infoMessage); }
        if (warningMessage != null) { model.addAttribute("warningMessage", warningMessage); }
        if (dangerMessage != null) { model.addAttribute("dangerMessage", dangerMessage); }

        return "settings/floor/garage";
    }

    /**
     * Method that returns the settings/floor/create.html template page with model attributes
     * @param successMessage String
     * @param infoMessage String
     * @param warningMessage String
     * @param dangerMessage String
     * @param garageKey String
     * @param model Model
     * @return String "settings/floor/create"
     */
    @GetMapping("/create/{garageKey}")
    public String create(@RequestParam(value = "successMessage", required = false) String successMessage,
                         @RequestParam(value = "infoMessage", required = false) String infoMessage,
                         @RequestParam(value = "warningMessage", required = false) String warningMessage,
                         @RequestParam(value = "dangerMessage", required = false) String dangerMessage,
                         @PathVariable("garageKey") String garageKey, Model model) {
        Garage garage = garageRepository.findByKey(garageKey);
        Floor floor = new Floor();
        floor.setGarageKey(garage.getGarageKey());
        model.addAttribute("floor", floor);
        model.addAttribute("garage", garage);

        // Alerts
        if (successMessage != null) { model.addAttribute("successMessage", successMessage); }
        if (infoMessage != null) { model.addAttribute("infoMessage", infoMessage); }
        if (warningMessage != null) { model.addAttribute("warningMessage", warningMessage); }
        if (dangerMessage != null) { model.addAttribute("dangerMessage", dangerMessage); }

        return "settings/floor/create";
    }

    /**
     * Method that creates a new floor
     * @param floor Floor
     * @param redirectAttributes RedirectAttributes
     * @return String redirection to previous page
     */
    @PostMapping("/create")
    public String create(Floor floor,
                         RedirectAttributes redirectAttributes) {

        boolean isSuccessful = false;
        boolean isDuplicate = false;

        if(floor.getGarageKey() == null || floor.getGarageKey().isEmpty()) {
            redirectAttributes.addAttribute(
                    "dangerMessage",
                    "The garage key cannot be null or empty.");
            return "redirect:/settings/floor/index";
        }

        Predicate predicate = Predicates.and(
                Predicates.equal("garageKey", floor.getGarageKey()),
                Predicates.equal("number", floor.getNumber())
        );

        int existingCount = floorRepository.countByPredicate(predicate);

        if(existingCount == 0) {
            floor.setLastUpdated(new Date());
            floorRepository.save(floor);

            for(int i = 0; i < floor.getTotalSpaces(); i++) {
                ParkingSpace parkingSpace = new ParkingSpace();
                parkingSpace.setGarageKey(floor.getGarageKey());
                parkingSpace.setLastUpdated(new Date());
                parkingSpace.setFloor(floor.getNumber());
                parkingSpace.setNumber(i + 1);
                parkingSpaceRepository.save(parkingSpace);
            }

            garageService.refresh(floor.getGarageKey());
            isSuccessful = true;
        } else {
            isDuplicate = true;
        }

        if(isSuccessful) {
            Garage garage = garageRepository.findByKey(floor.getGarageKey());
            redirectAttributes.addAttribute(
                    "successMessage",
                    "The floor, " +
                            floor.getNumber() +
                            ", was successfully created in garage " +
                            garage.getName());

            return "redirect:/settings/floor/garage/" + floor.getGarageKey();
        }

        if(isDuplicate) {
            Garage garage = garageRepository.findByKey(floor.getGarageKey());
            redirectAttributes.addAttribute(
                    "dangerMessage",
                    "The floor " +
                                floor.getNumber() +
                                " already exists in " +
                                garage.getName());

            return "redirect:/settings/floor/create/" + floor.getGarageKey();
        }

        return "redirect:/settings/floor/garage/" + floor.getGarageKey();
    }

    /**
     * Method that returns the "settings/floor/edit" template page
     * @param floorKey String
     * @param successMessage String
     * @param infoMessage String
     * @param warningMessage String
     * @param dangerMessage String
     * @param model Model
     * @return String "settings/floor/edit"
     */
    @GetMapping("/edit/{floorKey}")
    public String edit(@PathVariable("floorKey") String floorKey,
                       @RequestParam(value = "successMessage", required = false) String successMessage,
                       @RequestParam(value = "infoMessage", required = false) String infoMessage,
                       @RequestParam(value = "warningMessage", required = false) String warningMessage,
                       @RequestParam(value = "dangerMessage", required = false) String dangerMessage,
                       Model model) {

        Floor floor = floorRepository.findByKey(floorKey);
        model.addAttribute("floor", floor);

        // Alerts
        if (successMessage != null) { model.addAttribute("successMessage", successMessage); }
        if (infoMessage != null) { model.addAttribute("infoMessage", infoMessage); }
        if (warningMessage != null) { model.addAttribute("warningMessage", warningMessage); }
        if (dangerMessage != null) { model.addAttribute("dangerMessage", dangerMessage); }

        return "settings/floor/edit";
    }

    /**
     * Method to edit the current floor attributes
     * @param floor Floor
     * @param redirectAttributes RedirectAttributes
     * @return String redirection to previous page
     */
    @PostMapping("/edit")
    public String edit(Floor floor,
                       RedirectAttributes redirectAttributes) {
        boolean isSuccessful = false;
        boolean isDuplicate = false;

        Floor oldFloor = floorRepository.findByKey(floor.getFloorKey());

        try {
            if(floor.getNumber().equals(oldFloor.getNumber())) {
                Floor existingFloor = floorRepository.findByKey(floor.getFloorKey());
                existingFloor.setLastUpdated(new Date());
                existingFloor.setDescription(floor.getDescription());
                existingFloor.setNumber(floor.getNumber());
                floorRepository.save(existingFloor);
                isSuccessful = true;
            } else {
                Predicate predicate = Predicates.and(
                        Predicates.equal("number", floor.getNumber()),
                        Predicates.equal("garageKey", floor.getGarageKey())
                );

                int count = floorRepository.countByPredicate(predicate);

                if(count > 0) {
                    isDuplicate = true;
                }else {
                    Floor existingFloor = floorRepository.findByKey(floor.getFloorKey());
                    existingFloor.setLastUpdated(new Date());
                    existingFloor.setDescription(floor.getDescription());
                    existingFloor.setNumber(floor.getNumber());
                    floorRepository.save(existingFloor);
                    isSuccessful = true;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Alerts
        if (isSuccessful) {
            redirectAttributes.addAttribute(
                    "successMessage", "The floor " +
                            floor.getNumber() +
                            " was successfully updated.");
        }
        else if (isDuplicate) {
            redirectAttributes.addAttribute(
                    "dangerMessage",
                    "A floor with the number " +
                            floor.getNumber() +
                            " already exists.");
            return "redirect:/settings/floor/edit/" + floor.getFloorKey();
        }
        else {
            redirectAttributes.addAttribute(
                    "dangerMessage",
                    "An error occurred when attempting to update a floor.");
        }

        return "redirect:/settings/floor/garage/" + floor.getGarageKey();
    }

    /**
     * Method to remove a floor and delete any spaces on the floor
     * @param floorKey Strong
     * @param redirectAttributes RedirectAttributes
     * @return String redirection to previous page
     */
    @PostMapping("/delete")
    public String delete(@RequestParam("floorKey") String floorKey,
                         RedirectAttributes redirectAttributes) {

        if(floorKey == null || floorKey.isEmpty()) {
            redirectAttributes.addAttribute(
                    "dangerMessage",
                    "The floor key cannot be null or empty.");
            return "redirect:/settings/floor/index";
        }

        Floor existingFloor = floorRepository.findByKey(floorKey);

        if(existingFloor == null) {
            redirectAttributes.addAttribute(
                    "dangerMessage",
                    "The floor key was not found.");
            return "redirect:/settings/floor/index";
        }
        else {
            Predicate spacePredicate = Predicates.and(
                    Predicates.equal("garageKey", existingFloor.getGarageKey()),
                    Predicates.equal("floor", existingFloor.getNumber())
            );
            String garageKey = existingFloor.getGarageKey();

            parkingSpaceRepository.deleteByPredicate(spacePredicate);
            floorRepository.delete(floorKey);
            garageService.refresh(garageKey);

            redirectAttributes.addAttribute(
                    "successMessage",
                    "The floor was successfully deleted.");

            return "redirect:/settings/floor/garage/" + garageKey;
        }
    }
}
