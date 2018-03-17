package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.*;
import edu.odu.cs.gold.repository.*;
import edu.odu.cs.gold.service.GarageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping({"", "/", "/index"})
    public String index(Model model) {

        List<Garage> garages = new ArrayList<>(garageRepository.findAll());
        garages.sort(Comparator.comparing(Garage::getName));

        List<Floor> floors = new ArrayList<>(floorRepository.findAll());
        floors.sort(Comparator.comparing(Floor::getNumber));

        model.addAttribute("garages", garages);
        model.addAttribute("floors", floors);

        return "settings/parking_space/index";
    }

    @GetMapping("/floor/{floorKey}")
    public String floor(@PathVariable("floorKey") String floorKey,
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

        model.addAttribute("garage", garage);
        model.addAttribute("floor", floor);
        model.addAttribute("parkingSpaces", parkingSpaces);
        model.addAttribute("permitTypes", permitTypes);
        model.addAttribute("spaceTypes", spaceTypes);

        return "settings/parking_space/floor";
    }

    @GetMapping("/create/{garageKey}/{floorKey}")
    public String create(@PathVariable("garageKey") String garageKey,
                         @PathVariable("floorKey") String floorKey,
                         Model model) {

        ParkingSpace parkingSpace = new ParkingSpace();
        Floor floor = floorRepository.findByKey(floorKey);

        parkingSpace.setGarageKey(garageKey);
        parkingSpace.setFloor(floor.getNumber());

        List<PermitType> permitTypes = new ArrayList<> (permitTypeRepository.findAll());
        List<SpaceType> spaceTypes = new ArrayList<> (spaceTypeRepository.findAll());

        model.addAttribute("parkingSpace", parkingSpace);
        model.addAttribute("permitTypes", permitTypes);
        model.addAttribute("spaceTypes", spaceTypes);

        return "settings/parking_space/create";
    }

    @PostMapping("/create")
    public String create(ParkingSpace parkingSpace) {
        ParkingSpace existingParkingSpace = null;

        if(parkingSpace.getGarageKey() == null || parkingSpace.getGarageKey().isEmpty()) {
            // Unable to create a new parking space. Required attributes are missing
            return "settings/parking_space/index";
        }

        try {
            existingParkingSpace = parkingSpaceRepository.findByKey(parkingSpace.getParkingSpaceKey());
            if (existingParkingSpace == null) {
                PermitType permitType = permitTypeRepository.findByKey(parkingSpace.getPermitTypeKey());
                SpaceType spaceType = spaceTypeRepository.findByKey(parkingSpace.getSpaceTypeKey());

                parkingSpace.setLastUpdated(new Date());
                parkingSpace.setPermitType(permitType.getName());
                parkingSpace.setSpaceType(spaceType.getName());

                parkingSpaceRepository.save(parkingSpace);
                garageService.refresh(parkingSpace.getGarageKey());   
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return "settings/parking_space/floor/";
    }

    /**
     * Method to update the space number of a parking space.
     * @param parkingSpaceKey String unique key of the parking space
     * @param spaceNumber String new space number
     * @return
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
                return "The space number " + spaceNumber + " already exists.";
            }
        }

        // This is a unique space number, so set the given parking space to the number and save it
        parkingSpace.setNumber(spaceNumber);
        parkingSpace.setLastUpdated(new Date());
        parkingSpaceRepository.save(parkingSpace);

        garageService.refresh(parkingSpace.getGarageKey());

        return parkingSpaceKey + "'s space number was set to " + spaceNumber;
    }


    /**
     * Method for setting the space type of the specified parking space
     * @param parkingSpaceKey String unique key of the parking space
     * @param spaceTypeKey String unique key of the space type
     * @return
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

        garageService.refresh(parkingSpace.getGarageKey());

        return parkingSpaceKey + "'s space type was set to " + spaceType.getName();
    }

    /**
     * Method for setting the permit type of the specified parking space
     * @param parkingSpaceKey String unique key of the parking space
     * @param permitTypeKey String unique key of the permit type
     * @return
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

        // Refresh the garage
        garageService.refresh(parkingSpace.getGarageKey());

        return parkingSpaceKey + "'s permit type was set to " + permitType.getName();
    }

    /**
     * Method for setting availability of a single space
     * @param parkingSpaceKey String
     * @param available Boolean
     * @return
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

        return parkingSpaceKey + "'s availability was set to " + available;
    }

    /**
     * This method deletes a parking space and redirects to the parking spaces list in
     * settings/parking_space/floor/{floorKey}
     *
     * @param parkingSpaceKey String
     * @return
     */
    @PostMapping("/delete")
    public String delete(@RequestParam("parkingSpaceKey") String parkingSpaceKey) {

        ParkingSpace parkingSpace = parkingSpaceRepository.findByKey(parkingSpaceKey);

        // Set up the predicate to find all floors that have the specified garage key and the floor number
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
            parkingSpaceRepository.delete(parkingSpaceKey);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Return back to the /settings/parking_space/floor page
        return "redirect:/settings/parking_space/floor/" + floorKey;
    }
}
