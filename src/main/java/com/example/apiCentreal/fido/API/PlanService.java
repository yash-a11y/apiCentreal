package com.example.apiCentreal.fido.API;


import com.example.apiCentreal.fido.API.modules.Plan;
import com.example.apiCentreal.fido.API.modules.Plans;
import com.example.apiCentreal.fido.API.obj.plan;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PlanService {



    Plans prepaidPlans = new Plans();
    Plan plans = new Plan();


    public ArrayList<plan> getPrepaidPlans() {

        return  prepaidPlans.plans();

    }

    public ArrayList<plan> getPlans() {

        return plans.plans();

    }
}
