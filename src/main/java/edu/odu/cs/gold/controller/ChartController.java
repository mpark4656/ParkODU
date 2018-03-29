package edu.odu.cs.gold.controller;

import edu.odu.cs.gold.model.Chart;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/charts")
public class ChartController {

    @GetMapping({"","/","/index"})
    public String index(){

        return "charts/index";
    }

    @GetMapping({"/chart"})
    public String getChart(){

        return "charts/chart";
        }


}

