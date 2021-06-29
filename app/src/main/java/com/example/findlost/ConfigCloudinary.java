package com.example.findlost;

import java.util.HashMap;
import java.util.Map;

public class ConfigCloudinary {
    private Map config;

    public ConfigCloudinary(){
        config = new HashMap();
        config.put("cloud_name", "dntacvap3");
        config.put("api_key","127881442217581");
        config.put("api_secret", "7j562Dpj5TGdEtQfJHeXJNmMxrA");
    }

    public Map getMap(){
        return config;
    }
}
