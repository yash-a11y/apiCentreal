package com.example.apiCentreal;



import com.example.apiCentreal.fido.API.PlanService;
import com.example.apiCentreal.fido.API.obj.plan;
import com.example.apiCentreal.koodo.KoodoPlan;
import com.example.apiCentreal.koodo.KoodoScraperService;
import com.example.apiCentreal.rogers.models.Plan;
import com.example.apiCentreal.rogers.service.WebScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "plans/api/v.1")
public class controller {

    @Autowired
    private PlanService service;

    @Autowired
    private centralservice centralservice;

    @Autowired
    private WebScraperService webScraperService;

    @Autowired
    private com.example.apiCentreal.fido.API.deviceSrv deviceSrv;
    @Autowired
    private  KoodoScraperService koodoScraperService;

    @GetMapping("/home")
    void welcome()
    {
      System.out.println("welcome");
    }
    @GetMapping(path = "/fidoplans")
     ArrayList<plan> getFidoPlans()
    {
        return service.getPlans();
    }

    @GetMapping("/rogersplans")
    public List<Plan> getRogersPlans() {
        return webScraperService.scrapePlans();
    }

    @GetMapping("/ranking")
    public List<String> getRogersPlans(@RequestParam("keyword") String key) {
        return centralservice.getRanking(key);
    }


    @GetMapping("/koodo")
    public List<KoodoPlan> getKoodoPlans() {
        return koodoScraperService.scrapePrepaidPlans();
    }



//    @GetMapping(path = "/device")
//    ArrayList<device> device()
//    {
//        return deviceSrv.getDevice();
//    }




}
