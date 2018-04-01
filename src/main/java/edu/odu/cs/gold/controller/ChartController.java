package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.FloorStatistic;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.service.FloorStatisticService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/charts")
public class ChartController {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private FloorStatisticService floorStatisticService;

    public ChartController(GarageRepository garageRepository,
                           FloorRepository floorRepository,
                           FloorStatisticService floorStatisticService) {
        this.garageRepository = garageRepository;
        this.floorRepository = floorRepository;
        this.floorStatisticService = floorStatisticService;
    }

    @GetMapping({"","/","/index"})
    public String index(Model model){

        List<Garage> garages = new ArrayList<>(garageRepository.findAll());
        garages.sort(Comparator.comparing(Garage::getName));

        model.addAttribute("garages", garages);
        return "charts/index";
    }

    @GetMapping("/chart")
    public String chart(@RequestParam("id") String garageKey,
                        @RequestParam("floorNumber") String floorNumber,
                        @RequestParam("date") String dateString,
                        @RequestParam("chartId") String chartId,
                        Model model) {

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);

            Predicate predicate = Predicates.and(
                    Predicates.equal("number", floorNumber),
                    Predicates.equal("garageKey", garageKey)
            );

            List<Floor> floors= floorRepository.findByPredicate(predicate);
            String floorKey = floors.get(0).getFloorKey();


            ArrayList<FloorStatistic> floorStatistics
                    = floorStatisticService.findFloorCapacityByDate(floorKey, date);

            // Sort Floors by Number
            floorStatistics.sort(Comparator.comparing(FloorStatistic::getTimestamp));

            // Build Chart Data
            StringBuilder dataString = new StringBuilder();
            StringBuilder labelString = new StringBuilder();
            for (FloorStatistic floorStatistic : floorStatistics) {
                dataString.append(floorStatistic.getCapacity() + ",");

                Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("America/New_York")); // creates a new calendar instance
                calendar.setTime(floorStatistic.getTimestamp());   // assigns calendar to given date

                if (calendar.get(Calendar.HOUR_OF_DAY) == 0) {
                    labelString.append("12am,");
                }
                else if (calendar.get(Calendar.HOUR_OF_DAY) == 12) {
                    labelString.append("12pm,");
                }
                else if (calendar.get(Calendar.HOUR_OF_DAY) < 12) {
                    labelString.append(calendar.get(Calendar.HOUR_OF_DAY) + "am,");
                }
                else {
                    labelString.append((calendar.get(Calendar.HOUR_OF_DAY) - 12) + "pm,");
                }
            }

            Garage garage = garageRepository.findByKey(garageKey);
            StringBuilder sb = new StringBuilder();

            sb.append("alert('STARTED'); ");
            sb.append("var ctx" + chartId + " = document.getElementById('floorStatisticChart" + chartId + "'); ");
            sb.append("var floorNumber" + chartId + " = '" + floorNumber + "'; ");
            sb.append("var garageName" + chartId + " = '" + garage.getName() + "'; ");
            sb.append("var title" + chartId + " = garageName" + chartId + " + ': Floor ' + floorNumber" + chartId + "; ");
            sb.append("var dataString" + chartId + " = '" + dataString + "'; ");
            sb.append("var data" + chartId + " = dataString" + chartId + ".split(','); ");
            sb.append("var labelString" + chartId + " = '" + labelString + "'; ");
            sb.append("var labels" + chartId + " = labelString" + chartId + ".split(','); ");
            sb.append("alert('ENDED'); ");
            sb.append("    var myChart" + chartId + " = new Chart(ctx" + chartId + ", { ");
            sb.append("            type: 'line', ");
            sb.append("    data: { ");
            sb.append("        labels: labels" + chartId + ", ");
            sb.append("                datasets: [{ ");
            sb.append("            label: 'Occupancy Rate (%)', ");
            sb.append("                    data: data" + chartId + ", ");
            sb.append("                    backgroundColor: [ ");
            sb.append("            'rgba(255, 99, 132, 0.2)', ");
            sb.append("                ], ");
            sb.append("            borderColor: [ ");
            sb.append("            'rgba(255,99,132,1)', ");
            sb.append("                ], ");
            sb.append("            borderWidth: 1 ");
            sb.append("        }] ");
            sb.append("    }, ");
            sb.append("    options: { ");
            sb.append("        spanGaps: true, ");
            sb.append("                scales: { ");
            sb.append("            yAxes: [{ ");
            sb.append("                ticks: { ");
            sb.append("                    beginAtZero:true ");
            sb.append("                } ");
            sb.append("            }] ");
            sb.append("        }, ");
            sb.append("        title: { ");
            sb.append("            display: true, ");
            sb.append("                    text: title" + chartId + " ");
            sb.append("        } ");
            sb.append("    } ");
            sb.append("    }); ");


            System.err.println(sb.toString());

            model.addAttribute("jsCode", sb.toString());
            model.addAttribute("garage", garage);
            model.addAttribute("dataString", dataString.toString());
            model.addAttribute("labelString", labelString.toString());
            model.addAttribute("floorNumber", floorNumber);
            model.addAttribute("date", date);
            model.addAttribute("chartId", chartId); // do not change

            return "charts/chart";
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "charts/chart";
    }


    /*
    @GetMapping("/chart/{garageKey}/{floorNumber}/{date}/{chartId}")
    public String chart(@PathVariable("garageKey") String garageKey,
                        @PathVariable("floorNumber") String floorNumber,
                        @PathVariable("date") @DateTimeFormat(pattern = "MM-dd-yyyy") Date date,
                        @PathVariable("chartId") String chartId,
                        Model model) {
        System.out.println(garageKey);
        System.out.println(floorNumber);
        System.out.println(date);
        System.out.println(chartId);
        model.addAttribute("garageKey", garageKey);
        model.addAttribute("floorNumber", floorNumber);
        model.addAttribute("date", date);
        model.addAttribute("chartId", chartId);
        return "charts/chart";
    }
    */

    /*
    @GetMapping("/chart/{garageKey}/{floorNumber}/{date}")
    public String chart(@PathVariable("garageKey") String garageKey,
                        @PathVariable("floorNumber") String floorNumber,
                        @PathVariable("date")
                        @DateTimeFormat(pattern = "yyyy-MM-dd")
                            Date date,
                        Model model) {
        Predicate predicate = Predicates.and(
                Predicates.equal("number", floorNumber),
                Predicates.equal("garageKey", garageKey)
        );

        List<Floor> floors= floorRepository.findByPredicate(predicate);
        String floorKey = floors.get(0).getFloorKey();


        ArrayList<FloorStatistic> floorStatistics
                = floorStatisticService.findFloorCapacityByDate(floorKey, date);

        // Sort Floors by Number
        floorStatistics.sort(Comparator.comparing(FloorStatistic::getTimestamp));

        // Build Chart Data
        StringBuilder dataString = new StringBuilder();
        StringBuilder labelString = new StringBuilder();
        for (FloorStatistic floorStatistic : floorStatistics) {
            dataString.append(floorStatistic.getCapacity() + ",");

            Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("America/New_York")); // creates a new calendar instance
            calendar.setTime(floorStatistic.getTimestamp());   // assigns calendar to given date

            if (calendar.get(Calendar.HOUR_OF_DAY) == 0) {
                labelString.append("12am,");
            }
            else if (calendar.get(Calendar.HOUR_OF_DAY) == 12) {
                labelString.append("12pm,");
            }
            else if (calendar.get(Calendar.HOUR_OF_DAY) < 12) {
                labelString.append(calendar.get(Calendar.HOUR_OF_DAY) + "am,");
            }
            else {
                labelString.append((calendar.get(Calendar.HOUR_OF_DAY) - 12) + "pm,");
            }
        }

        Garage garage = garageRepository.findByKey(garageKey);
        model.addAttribute("garage", garage);
        model.addAttribute("dataString", dataString.toString());
        model.addAttribute("labelString", labelString.toString());
        return "charts/chart";
    }
    */
}

