package com.example.apiCentreal;



import com.example.apiCentreal.fido.API.PlanService;
import com.example.apiCentreal.fido.API.obj.plan;
import com.example.apiCentreal.rogers.models.Plan;
import com.example.apiCentreal.rogers.service.WebScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "plans/api/v.1")
public class controller {

    @Autowired
    private PlanService service;

    @Autowired
    private WebScraperService webScraperService;

    @Autowired
    private com.example.apiCentreal.fido.API.deviceSrv deviceSrv;

    @GetMapping("/home")
    void welcome()
    {
      System.out.println("welcome");
    }
    @GetMapping(path = "/fidoplans")
     ArrayList<plan> useropt()
    {
        return service.getPlans();
    }

    @GetMapping("/rogersplans")
    public List<Plan> getPlans() {
        return webScraperService.scrapePlans();
    }


//    @GetMapping(path = "/device")
//    ArrayList<device> device()
//    {
//        return deviceSrv.getDevice();
//    }




}
