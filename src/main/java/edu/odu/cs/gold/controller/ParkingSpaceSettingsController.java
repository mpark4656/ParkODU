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

    /**
     *
     * @param parkingSpaceKey
     * @param spaceTypeKey
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
     * Set
     * @param parkingSpaceKey
     * @param permitTypeKey
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
        parkingSpace.setLastUpdated(new Date());
        parkingSpaceRepository.save(parkingSpace);

        garageService.refresh(parkingSpace.getGarageKey());

        return parkingSpaceKey + "'s permit type was set to " + permitType.getName();
    }

    /**
     * PostRequest for setting availability of a single space
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
     *
     * @param parkingSpaceKey
     * @return
     */
    @PostMapping("/delete")
    public String delete(@RequestParam("parkingSpaceKey") String parkingSpaceKey) {
        try {
            parkingSpaceRepository.delete(parkingSpaceKey);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/settings/parking_space/index";
    }
}
