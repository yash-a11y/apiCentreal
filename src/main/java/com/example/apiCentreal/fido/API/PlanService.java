package com.example.apiCentreal.fido.API;


import com.example.apiCentreal.fido.API.modules.Plans;
import com.example.apiCentreal.fido.API.obj.plan;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PlanService {


    Plans plans = new Plans();



    public ArrayList<plan> getPlans() {

        return  plans.plans();

    }
}
