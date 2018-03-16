package edu.odu.cs.gold.controller;

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
     * Usage in settings/garage/index.html to access the collection of all garages
     * ${garages}
     *
     * @param model Model
     * @return String "settings/garage/index"
     */
    @GetMapping({"","/","/index"})
    public String index(Model model) {

        List<Garage> garages = new ArrayList<>(garageRepository.findAll());
        garages.sort(Comparator.comparing(Garage::getName));
        model.addAttribute("garages", garages);

        return "settings/garage/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        Garage garage = new Garage();
        garage.generateGarageKey();
        model.addAttribute("garage", garage);
        return "settings/garage/create";
    }

    @PostMapping("/create")
    public String create(Garage garage) {
        Garage existingGarage = null;
        try {
            existingGarage = garageRepository.findByKey(garage.getGarageKey());
            if (existingGarage == null) {
                garageRepository.save(garage);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/settings/garage/index";
    }

    @GetMapping("/edit/{garageKey}")
    public String edit(@PathVariable("garageKey") String garageKey,
                       Model model) {
        Garage garage = garageRepository.findByKey(garageKey);
        model.addAttribute("garage", garage);
        return "settings/garage/edit";
    }

    @PostMapping("/edit")
    public String edit(Garage garage) {
        Garage existingGarage = null;
        try {
            existingGarage = garageRepository.findByKey(garage.getGarageKey());
            existingGarage.setName(garage.getName());
            existingGarage.setDescription(garage.getDescription());
            existingGarage.setHeightDescription(garage.getHeightDescription());
            existingGarage.setAddressOne(garage.getAddressOne());
            existingGarage.setAddressTwo(garage.getAddressTwo());
            existingGarage.setCity(garage.getCity());
            existingGarage.setState(garage.getState());
            existingGarage.setZipCode(garage.getZipCode());
            garageRepository.save(existingGarage);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/settings/garage/index";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("garageKey") String garageKey) {
        try {
            garageRepository.delete(garageKey);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/settings/garage/index";
    }
}
