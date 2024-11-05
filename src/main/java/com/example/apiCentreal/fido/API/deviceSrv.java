package com.example.apiCentreal.fido.API;


import com.example.apiCentreal.fido.API.modules.Device;
import com.example.apiCentreal.fido.API.obj.device;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class deviceSrv {


    private Device device = new Device();


    public ArrayList<device> getDevice()
    {
        try {
            return device.getDevices();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
