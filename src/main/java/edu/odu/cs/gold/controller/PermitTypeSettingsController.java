package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.*;
import edu.odu.cs.gold.repository.*;
import edu.odu.cs.gold.service.PermitTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/settings/permit_type")
public class PermitTypeSettingsController {

    private ParkingSpaceRepository parkingSpaceRepository;
    private PermitTypeRepository permitTypeRepository;
    private PermitTypeService permitTypeService;

    /**
     * Constructor
     * @param parkingSpaceRepository ParkingSpaceRepository
     * @param permitTypeRepository PermitTypeRepository
     */
    public PermitTypeSettingsController(ParkingSpaceRepository parkingSpaceRepository,
                                        PermitTypeRepository permitTypeRepository,
                                        PermitTypeService permitTypeService) {

        this.parkingSpaceRepository = parkingSpaceRepository;
        this.permitTypeRepository = permitTypeRepository;
        this.permitTypeService = permitTypeService;
    }

    /**
     * Returns the default index page with existing PermitType objects
     * @param model Model
     * @return String default index page
     */
    @GetMapping({"", "/", "/index"})
    public String index(@RequestParam(value = "successMessage", required = false) String successMessage,
                        @RequestParam(value = "infoMessage", required = false) String infoMessage,
                        @RequestParam(value = "warningMessage", required = false) String warningMessage,
                        @RequestParam(value = "dangerMessage", required = false) String dangerMessage,
                        Model model) {

        List<PermitType> permitTypes = new ArrayList<>(permitTypeRepository.findAll());

        model.addAttribute("permitTypes", permitTypes);

        // Alerts
        if (successMessage != null) { model.addAttribute("successMessage", successMessage); }
        if (infoMessage != null) { model.addAttribute("infoMessage", infoMessage); }
        if (warningMessage != null) { model.addAttribute("warningMessage", warningMessage); }
        if (dangerMessage != null) { model.addAttribute("dangerMessage", dangerMessage); }

        return "settings/permit_type/index";
    }

    /**
     * Returns the settings/permit_type/create.html page
     * @return String settings/permit_type/create.html
     */
    @GetMapping("/create")
    public String create() {
        return "settings/permit_type/create";
    }

    /**
     * Post method that creates a new permit type and redirects user back to the index
     * @param permitTypeName String
     * @param permitTypeDescription String
     * @return String redirection back to settings/permit_type/index
     */
    @PostMapping("/create")
    public String create(@RequestParam("permitTypeName") String permitTypeName,
                         @RequestParam("permitTypeDescription") String permitTypeDescription,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        // Do not accept null or empty permit type name
        if(permitTypeName == null || permitTypeName.trim().isEmpty()) {
            model.addAttribute(
                    "dangerMessage",
                    "The permit name must be specified.");
            return "settings/permit_type/create";
        }

        // Do not accept null or empty permit type description
        if(permitTypeDescription == null || permitTypeDescription.trim().isEmpty()) {
            model.addAttribute(
                    "dangerMessage",
                    "The permit description must be specified.");
            return "settings/permit_type/create";
        }

        // Remove any leading or trailing spaces just to be safe
        permitTypeName = permitTypeName.trim();
        permitTypeDescription = permitTypeDescription.trim();

        // Get the list of current existing permit types.
        List<PermitType> permitTypes = new ArrayList<>(permitTypeRepository.findAll());

        // If any of the existing permit types have the same name as the given name, do not create a new permit
        for(PermitType eachPermitType : permitTypes) {
            if(eachPermitType.getName().equals(permitTypeName)) {
                redirectAttributes.addAttribute(
                        "dangerMessage",
                        "The permit name " + permitTypeName + " already exists.");
                return "redirect:/settings/permit_type/index";
            }
        }

        // After passing all the checks, create this new permit and save it.
        try {
            // Create a new PermitType object. The unique key is auto-generated in the constructor.
            PermitType permitType = new PermitType(permitTypeName, permitTypeDescription);
            permitTypeRepository.save(permitType);
            redirectAttributes.addAttribute(
                    "successMessage",
                    permitType.getName() + " has been successfully created.");
            return "redirect:/settings/permit_type/index";
        } catch(Exception e) {
            e.printStackTrace();
            redirectAttributes.addAttribute("dangerMessage","Exception Raised");
            return "redirect:/settings/permit_type/index";
        }
    }

    /**
     * Update the description of the permit type
     * @param permitTypeKey String
     * @param permitDescription String
     * @return String
     */
    @PostMapping("/set_permit_description")
    @ResponseBody
    public String set_description(@RequestParam("permitTypeKey") String permitTypeKey,
                                  @RequestParam("permitDescription") String permitDescription) {

        PermitType permitType = permitTypeRepository.findByKey(permitTypeKey);

        // If the permit type does not exist
        if(permitType == null) {
            System.err.println("The specified permit type does not exist.");
            return "The specified permit type, " + permitTypeKey + "does not exist!";
        }

        if(permitDescription == null || permitDescription.trim().isEmpty()) {
            System.err.println("The specified permit description is null or empty");
            return "The specified permit description is null or empty!";
        }

        permitType.setDescription(permitDescription.trim());
        permitTypeRepository.save(permitType);

        permitTypeService.refresh(permitType.getPermitTypeKey());

        return "Update Successful";
    }

    /**
     * Update the name of the permit type
     * @param permitTypeKey String
     * @param permitName String
     * @return String
     */
    @PostMapping("/set_permit_name")
    @ResponseBody
    public String set_name(@RequestParam("permitTypeKey") String permitTypeKey,
                           @RequestParam("permitName") String permitName) {

        PermitType permitType = permitTypeRepository.findByKey(permitTypeKey);

        if(permitType == null) {
            System.err.println("The permit type does not exist");
            return "The specified permit type, " + permitTypeKey + "does not exist!";
        }

        if(permitName == null || permitName.trim().isEmpty()) {
            System.err.println("The specified permit name is null or empty");
            return "The specified permit name is null or empty!";
        }

        permitType.setName(permitName.trim());
        permitTypeRepository.save(permitType);

        permitTypeService.refresh(permitType.getPermitTypeKey());

        return "Update Successful";
    }

    /**
     * Delete the specified permit type. If existing parking spaces use the permit type, no action will be taken.
     * @param permitTypeKey String
     * @return String
     */
    @PostMapping("/delete")
    public String delete(@RequestParam("permitTypeKey") String permitTypeKey,
                         Model model) {

        if(permitTypeRepository.findByKey(permitTypeKey) != null) {
            // Find out if any of the parking spaces have this permitTypeKey
            Predicate predicate = Predicates.equal("permitTypeKey", permitTypeKey);
            List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findByPredicate(predicate);

            if(parkingSpaces.size() > 0) {

                model.addAttribute(
                        "dangerMessage",
                        "The specified parking permit is being used by existing parking spaces.");
                model.addAttribute("permitTypes", new ArrayList<>(permitTypeRepository.findAll()));
                return "settings/permit_type/index";
            }

            try {
                permitTypeRepository.delete(permitTypeKey);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            model.addAttribute("successMessage", "The permit " + permitTypeKey + " was successfully deleted");
        } else {
            model.addAttribute(
                    "dangerMessage",
                    "The specified parking permit is being used by existing parking spaces.");
            model.addAttribute("permitTypes", new ArrayList<>(permitTypeRepository.findAll()));
            return "settings/permit_type/index";
        }

        return "settings/permit_type/index";
    }
}