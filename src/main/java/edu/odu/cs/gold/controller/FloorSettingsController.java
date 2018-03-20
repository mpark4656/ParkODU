package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.model.SpaceType;
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
     * Usage in settings/floor/index.html to access the collection of all garage
     * ${garage}

     * @param model Model
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

    @GetMapping("/create/{garageKey}")
    public String create(@PathVariable("garageKey") String garageKey, Model model) {
        Garage garage = garageRepository.findByKey(garageKey);
        Floor floor = new Floor();
        floor.setGarageKey(garage.getGarageKey());
        model.addAttribute("floor", floor);
        model.addAttribute("garage", garage);
        return "settings/floor/create";
    }

    @PostMapping("/create")
    public String create(Floor floor,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        boolean isSuccessful = false;
        boolean isDuplicate = false;

        System.err.println(floor.toString());

        if(floor.getGarageKey() == null || floor.getGarageKey().isEmpty()) {
            List<Garage> garages = new ArrayList<>(garageRepository.findAll());
            garages.sort(Comparator.comparing(Garage::getName));
            model.addAttribute("dangerMessage", "The garage key cannot be null or empty.");
            model.addAttribute("garages", garages);
            return "settings/floor/index";
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
                    "The floor was successfully created in garage " + garage.getName());
        }

        if(isDuplicate) {
            Garage garage = garageRepository.findByKey(floor.getGarageKey());
            redirectAttributes.addAttribute(
                    "dangerMessage",
                    "The specified floor already exists in " + garage.getName());
        }

        return "redirect:/settings/floor/garage/" + floor.getGarageKey();
    }

    /**
     * This method returns "settings/floor/garage" with a Garage object and
     * a collection of Floor objects found in the garage.
     *
     * Uage in settings/floor/garage.html to access the collection of floors and the garage
     * ${garage}
     * ${floors}
     *
     * @param garageKey String unique identifier of the garage being queried
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

    @PostMapping("/delete")
    public String delete(@RequestParam("floorKey") String floorKey,
                         Model model) {

        if(floorKey == null || floorKey.isEmpty()) {
            List<Garage> garages = new ArrayList<>(garageRepository.findAll());
            garages.sort(Comparator.comparing(Garage::getName));
            model.addAttribute("dangerMessage", "The floor key cannot be null or empty.");
            model.addAttribute("garages", garages);
            return "settings/floor/index";
        }

        Floor existingFloor = floorRepository.findByKey(floorKey);

        if(existingFloor == null) {
            List<Garage> garages = new ArrayList<>(garageRepository.findAll());
            garages.sort(Comparator.comparing(Garage::getName));
            model.addAttribute("dangerMessage", "The floor key was not found.");
            model.addAttribute("garages", garages);
            return "settings/floor/index";
        }
        else {
            Predicate predicate = Predicates.and(
                    Predicates.equal("garageKey", existingFloor.getGarageKey()),
                    Predicates.equal("floor", existingFloor.getNumber())
            );
            String garageKey = existingFloor.getGarageKey();

            parkingSpaceRepository.deleteByPredicate(predicate);
            floorRepository.delete(floorKey);

            garageService.refresh(garageKey);
            Garage garage = garageRepository.findByKey(garageKey);
            model.addAttribute("garage", garage);

            Predicate floorPredicate = Predicates.equal("garageKey", garageKey);
            List<Floor> floors = new ArrayList<>(floorRepository.findByPredicate(floorPredicate));
            model.addAttribute("floors", floors);

            floors.sort(Comparator.comparing(Floor::getNumber));
            model.addAttribute("successMessage", "The floor key was deleted.");
            return "settings/floor/garage";
        }
    }
}
