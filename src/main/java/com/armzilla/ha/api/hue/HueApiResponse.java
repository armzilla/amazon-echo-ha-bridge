package com.armzilla.ha.api.hue;

import com.armzilla.ha.api.hue.DeviceResponse;

import java.util.Map;

/**
 * Created by arm on 4/14/15.
 */
public class HueApiResponse {
    private Map<String, DeviceResponse> lights;

    public Map<String, DeviceResponse> getLights() {
        return lights;
    }

    public void setLights(Map<String, DeviceResponse> lights) {
        this.lights = lights;
    }
}
