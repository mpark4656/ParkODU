package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.*;
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

@Controller
@RequestMapping("/settings/parking_space")
public class ParkingSpaceSettingsController {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private PermitTypeRepository permitTypeRepository;
    private SpaceTypeRepository spaceTypeRepository;
    private GarageService garageService;

    /**
     * Constructor
     * @param garageRepository GarageRepository
     * @param floorRepository FloorRepository
     * @param parkingSpaceRepository ParkingSpaceRepository
     * @param permitTypeRepository PermitTypeRepository
     * @param spaceTypeRepository SpaceTypeRepository
     * @param garageService GarageService
     */
    public ParkingSpaceSettingsController(GarageRepository garageRepository,
                                          FloorRepository floorRepository,
                                          ParkingSpaceRepository parkingSpaceRepository,
                                          PermitTypeRepository permitTypeRepository,
                                          SpaceTypeRepository spaceTypeRepository,
                                          GarageService garageService) {
        this.garageRepository = garageRepository;
        this.floorRepository = floorRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.permitTypeRepository = permitTypeRepository;
        this.spaceTypeRepository = spaceTypeRepository;
        this.garageService = garageService;
    }

    /**
     * Return the default index page with garages model and floors model
     *
     * @param successMessage String
     * @param infoMessage String
     * @param warningMessage String
     * @param dangerMessage String
     * @param model Model
     * @return String "settings/parking_space/index"
     */
    @GetMapping({"", "/", "/index"})
    public String index(@RequestParam(value = "successMessage", required = false) String successMessage,
                        @RequestParam(value = "infoMessage", required = false) String infoMessage,
                        @RequestParam(value = "warningMessage", required = false) String warningMessage,
                        @RequestParam(value = "dangerMessage", required = false) String dangerMessage,
                        Model model) {

        List<Garage> garages = new ArrayList<>(garageRepository.findAll());
        garages.sort(Comparator.comparing(Garage::getName));

        List<Floor> floors = new ArrayList<>(floorRepository.findAll());
        floors.sort(Comparator.comparing(Floor::getNumber));

        model.addAttribute("garages", garages);
        model.addAttribute("floors", floors);

        // Alerts
        if (successMessage != null) { model.addAttribute("successMessage", successMessage); }
        if (infoMessage != null) { model.addAttribute("infoMessage", infoMessage); }
        if (warningMessage != null) { model.addAttribute("warningMessage", warningMessage); }
        if (dangerMessage != null) { model.addAttribute("dangerMessage", dangerMessage); }

        return "settings/parking_space/index";
    }

    /**
     * Return settings/parking_space/floor.index with garage, floor, parkingSpaces, permitTypes,
     * and spaceTypes models
     *
     * @param successMessage String
     * @param infoMessage String
     * @param warningMessage String
     * @param dangerMessage String
     * @param floorKey String
     * @param model Model
     * @return String "settings/parking_space/floor"
     */
    @GetMapping("/floor/{floorKey}")
    public String floor(@RequestParam(value = "successMessage", required = false) String successMessage,
                        @RequestParam(value = "infoMessage", required = false) String infoMessage,
                        @RequestParam(value = "warningMessage", required = false) String warningMessage,
                        @RequestParam(value = "dangerMessage", required = false) String dangerMessage,
                        @PathVariable("floorKey") String floorKey,
                        Model model) {

        Floor floor = floorRepository.findByKey(floorKey);
        Garage garage = garageRepository.findByKey(floor.getGarageKey());

        Predicate predicate = Predicates.and(
                Predicates.equal("garageKey", floor.getGarageKey()),
                Predicates.equal("floor", floor.getNumber())
        );

        List<ParkingSpace> parkingSpaces = new ArrayList<>(parkingSpaceRepository.findByPredicate(predicate));
        parkingSpaces.sort(Comparator.comparing(ParkingSpace::getNumber));

        // Get the Permit Types and Space types currently in the repository
        List<PermitType> permitTypes = new  ArrayList<> (permitTypeRepository.findAll());
        List<SpaceType> spaceTypes = new ArrayList<> (spaceTypeRepository.findAll());

        spaceTypes.sort(Comparator.comparing(SpaceType::getName));
        permitTypes.sort(Comparator.comparing(PermitType::getName));

        model.addAttribute("garage", garage);
        model.addAttribute("floor", floor);
        model.addAttribute("parkingSpaces", parkingSpaces);
        model.addAttribute("permitTypes", permitTypes);
        model.addAttribute("spaceTypes", spaceTypes);

        // Alerts
        if (successMessage != null) { model.addAttribute("successMessage", successMessage); }
        if (infoMessage != null) { model.addAttribute("infoMessage", infoMessage); }
        if (warningMessage != null) { model.addAttribute("warningMessage", warningMessage); }
        if (dangerMessage != null) { model.addAttribute("dangerMessage", dangerMessage); }

        return "settings/parking_space/floor";
    }

    /**
     * Return the settings/parking_space/create.html page
     * Models include parkingSpace, permitTypes, and spaceTypes
     *
     * @param successMessage String
     * @param infoMessage String
     * @param warningMessage String
     * @param dangerMessage String
     * @param garageKey String
     * @param floorKey String
     * @param model Model
     * @return String "settings/parking_space/create"
     */
    @GetMapping("/create/{garageKey}/{floorKey}")
    public String create(@RequestParam(value = "successMessage", required = false) String successMessage,
                         @RequestParam(value = "infoMessage", required = false) String infoMessage,
                         @RequestParam(value = "warningMessage", required = false) String warningMessage,
                         @RequestParam(value = "dangerMessage", required = false) String dangerMessage,
                         @PathVariable("garageKey") String garageKey,
                         @PathVariable("floorKey") String floorKey,
                         Model model) {

        ParkingSpace parkingSpace = new ParkingSpace();
        Floor floor = floorRepository.findByKey(floorKey);

        parkingSpace.setGarageKey(garageKey);
        parkingSpace.setFloor(floor.getNumber());

        List<PermitType> permitTypes = new ArrayList<> (permitTypeRepository.findAll());
        List<SpaceType> spaceTypes = new ArrayList<> (spaceTypeRepository.findAll());

        spaceTypes.sort(Comparator.comparing(SpaceType::getName));
        permitTypes.sort(Comparator.comparing(PermitType::getName));

        model.addAttribute("floor", floor);
        model.addAttribute("parkingSpace", parkingSpace);
        model.addAttribute("permitTypes", permitTypes);
        model.addAttribute("spaceTypes", spaceTypes);

        // Alerts
        if (successMessage != null) { model.addAttribute("successMessage", successMessage); }
        if (infoMessage != null) { model.addAttribute("infoMessage", infoMessage); }
        if (warningMessage != null) { model.addAttribute("warningMessage", warningMessage); }
        if (dangerMessage != null) { model.addAttribute("dangerMessage", dangerMessage); }

        return "settings/parking_space/create";
    }

    /**
     * Post method to create a new parking space
     * User is redirected back to settings/parking_space/floor.html
     *
     * @param parkingSpace ParkingSpace
     * @param redirectAttributes RedirectAttributes
     * @return String redirection to previous page
     */
    @PostMapping("/create")
    public String create(ParkingSpace parkingSpace,
                         RedirectAttributes redirectAttributes) {

        boolean isSuccessful = false;
        boolean isDuplicateSpaceKey = false;
        boolean exceptionRaised = false;

        ParkingSpace existingParkingSpace = parkingSpaceRepository.findByKey(parkingSpace.getParkingSpaceKey());

        // Create predicate to find the floorKey of the floor that this parking space is located on
        Predicate predicate = Predicates.and(
                Predicates.equal("garageKey", parkingSpace.getGarageKey()),
                Predicates.equal("number", parkingSpace.getFloor())
        );

        // findByPredicate() method always returns a list, but we know that there is only one floor that
        // meets the predicate's criteria (There should be only one)
        List<Floor> floors = new ArrayList<>(floorRepository.findByPredicate(predicate));

        // Get the floor key from the floor
        String floorKey = floors.get(0).getFloorKey();

        try {
            // If the existingParkingSpace is null, it means the space key is unique
            if (existingParkingSpace == null) {

                // Find all parking spaces that share the same garage key and floor level
                Predicate spacePredicate = Predicates.and(
                        Predicates.equal("garageKey", parkingSpace.getGarageKey()),
                        Predicates.equal("floor", parkingSpace.getFloor())
                );

                List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findByPredicate(spacePredicate);

                // Check to see if there are duplicate space numbers on the same floor of the same garage
                for (ParkingSpace eachParkingSpace : parkingSpaces) {
                    if (eachParkingSpace.getNumber().equals(parkingSpace.getNumber())) {
                        redirectAttributes.addAttribute(
                                "dangerMessage",
                                "The space number, " +
                                        parkingSpace.getNumber() +
                                        ", already exists.");
                        return "redirect:/settings/parking_space/create/" +
                                parkingSpace.getGarageKey() + "/" +
                                floorKey;
                    }
                }

                // Obtain the permit type and the space type
                PermitType permitType = permitTypeRepository.findByKey(parkingSpace.getPermitTypeKey());
                SpaceType spaceType = spaceTypeRepository.findByKey(parkingSpace.getSpaceTypeKey());

                // Set the rest of the attributes
                parkingSpace.setPermitType(permitType.getName());
                parkingSpace.setSpaceType(spaceType.getName());
                parkingSpace.setLastUpdated(new Date());

                parkingSpaceRepository.save(parkingSpace);
                garageService.refresh(parkingSpace.getGarageKey());
                isSuccessful = true;
            }
            else {
                isDuplicateSpaceKey = true;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            exceptionRaised = true;
        }


        if(isSuccessful) {
            redirectAttributes.addAttribute(
                    "successMessage",
                    "The space number, " +
                            parkingSpace.getNumber() +
                            ", has been successfully created.");

            return "redirect:/settings/parking_space/floor/" + floorKey;
        }

        if(isDuplicateSpaceKey) {
            redirectAttributes.addAttribute(
                    "dangerMessage",
                    "The specified parking space key already exists.");
            return "redirect:/settings/parking_space/create/" +
                    parkingSpace.getGarageKey() + "/" +
                    floorKey;
        }

        if(exceptionRaised) {
            redirectAttributes.addAttribute(
                    "dangerMessage",
                    "Unknown error has occurred.");
        }

        return "redirect:/settings/parking_space/index";
    }

    /**
     * Method to update the space number of a parking space.
     * @param parkingSpaceKey String unique key of the parking space
     * @param spaceNumber String new space number
     * @return String
     */
    @PostMapping("/set_space_number")
    @ResponseBody
    public String setSpaceNumber(@RequestParam("parkingSpaceKey") String parkingSpaceKey,
                                 @RequestParam("spaceNumber") Integer spaceNumber) {

        ParkingSpace parkingSpace = parkingSpaceRepository.findByKey(parkingSpaceKey);

        // Need to find out whether there is a parking space on the same floor with the same space number.
        // Business rule dictates that the space number has to be unique on the same floor of the same garage.
        Predicate predicate = Predicates.and(
                Predicates.equal("garageKey", parkingSpace.getGarageKey()),
                Predicates.equal("floor", parkingSpace.getFloor())
        );

        // Get all the parking spaces
        List<ParkingSpace> parkingSpaces = new ArrayList<> (parkingSpaceRepository.findByPredicate(predicate));

        // Check to see if the given space number already exists
        for(ParkingSpace eachParkingSpace : parkingSpaces) {
            if(eachParkingSpace.getNumber().equals(spaceNumber)) {
                return "The number, " + spaceNumber.toString() + ", already exists.";
            }
        }

        // This is a unique space number, so set the given parking space to the number and save it
        parkingSpace.setNumber(spaceNumber);
        parkingSpace.setLastUpdated(new Date());
        parkingSpaceRepository.save(parkingSpace);

        return "The space number was set to " + parkingSpace.getNumber();
    }

    /**
     * Method for setting the space type of the specified parking space
     * @param parkingSpaceKey String unique key of the parking space
     * @param spaceTypeKey String unique key of the space type
     * @return String
     */
    @PostMapping("/set_space_type")
    @ResponseBody
    public String setSpaceType(@RequestParam("parkingSpaceKey") String parkingSpaceKey,
                               @RequestParam("spaceTypeKey") String spaceTypeKey) {

        ParkingSpace parkingSpace = parkingSpaceRepository.findByKey(parkingSpaceKey);
        SpaceType spaceType = spaceTypeRepository.findByKey(spaceTypeKey);

        // Remember that updating the space type requires 2 actions
        // 1. Update parkingSpace.permitType (Permit Type Display name)
        // 2. Update parkingSpace.permitTypeKey (Permit Type Key)
        parkingSpace.setSpaceType(spaceType.getName());
        parkingSpace.setSpaceTypeKey(spaceType.getSpaceTypeKey());
        parkingSpace.setLastUpdated(new Date());
        parkingSpaceRepository.save(parkingSpace);

        return "The space type of the space number " + parkingSpace.getNumber() + " was set to " + spaceType.getName();
    }

    /**
     * Method for setting the permit type of the specified parking space
     * @param parkingSpaceKey String unique key of the parking space
     * @param permitTypeKey String unique key of the permit type
     * @return String
     */
    @PostMapping("/set_permit_type")
    @ResponseBody
    public String setPermitType(@RequestParam("parkingSpaceKey") String parkingSpaceKey,
                                @RequestParam("permitTypeKey") String permitTypeKey) {

        ParkingSpace parkingSpace = parkingSpaceRepository.findByKey(parkingSpaceKey);
        PermitType permitType = permitTypeRepository.findByKey(permitTypeKey);

        // Remember that updating the permit type requires 2 actions
        // 1. Update parkingSpace.permitType (Permit Type Display name)
        // 2. Update parkingSpace.permitTypeKey (Permit Type Key)
        parkingSpace.setPermitType(permitType.getName());
        parkingSpace.setPermitTypeKey(permitType.getPermitTypeKey());

        // Set the last updated date
        parkingSpace.setLastUpdated(new Date());

        // Save to repository
        parkingSpaceRepository.save(parkingSpace);

        return "The permit type of the space number " + parkingSpace.getNumber() + " was set to " + permitType.getName();
    }

    /**
     * Method for setting availability of a single space
     * @param parkingSpaceKey String
     * @param available Boolean
     * @return String
     */
    @PostMapping("/set_availability")
    @ResponseBody
    public String setAvailability(@RequestParam("parkingSpaceKey") String parkingSpaceKey,
                                  @RequestParam("available") Boolean available) {

        ParkingSpace parkingSpace = parkingSpaceRepository.findByKey(parkingSpaceKey);
        parkingSpace.setAvailable(available);
        parkingSpace.setLastUpdated(new Date());
        parkingSpaceRepository.save(parkingSpace);

        garageService.refresh(parkingSpace.getGarageKey());

        if(available){
            return "The space number " + parkingSpace.getNumber() + " was set to available";
        }
        else{
            return "The space number " + parkingSpace.getNumber() + " was set to unavailable";
        }
    }

    /**
     * This method deletes a parking space and redirects to the parking spaces list in
     * settings/parking_space/floor/{floorKey}
     *
     * @param parkingSpaceKey String
     * @param redirectAttributes RedirectAttributes
     * @return String
     */
    @PostMapping("/delete")
    public String delete(@RequestParam("parkingSpaceKey") String parkingSpaceKey,
                         RedirectAttributes redirectAttributes) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findByKey(parkingSpaceKey);
        String floorKey = "";

        try {
            // Set up the predicate to find all floors that have the specified garage key and the floor number
            Predicate predicate = Predicates.and(
                    Predicates.equal("garageKey", parkingSpace.getGarageKey()),
                    Predicates.equal("number", parkingSpace.getFloor())
            );

            // findByPredicate() method always returns a list, but we know that there is only one floor that
            // meets the predicate's criteria (There should be only one)
            List<Floor> floors = new ArrayList<>(floorRepository.findByPredicate(predicate));

            // Get the floor key from the floor
            floorKey = floors.get(0).getFloorKey();
            parkingSpaceRepository.delete(parkingSpaceKey);
            garageService.refresh(parkingSpace.getGarageKey());
            redirectAttributes.addAttribute(
                    "successMessage",
                    "The space number "+ parkingSpace.getNumber() + " was successfully deleted.");
        }
        catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addAttribute("dangerMessage","Unknown error has occurred.");
        }

        // Return back to the /settings/parking_space/floor page
        return "redirect:/settings/parking_space/floor/" + floorKey;
    }
}
