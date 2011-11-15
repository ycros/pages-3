package junk;

import java.util.Map;

public class DeviceRequirement {
    Class<Device> deviceClass;
    int amount;
    Map<String, String> parameters;

    public DeviceRequirement(Class<Device> deviceClass) {
        this(deviceClass, null);
    }

    public DeviceRequirement(Class<Device> deviceClass, Map<String, String> parameters) {
        this(deviceClass, 1, parameters);
    }

    public DeviceRequirement(Class<Device> deviceClass, int amount, Map<String, String> parameters) {
        this.deviceClass = deviceClass;
        this.amount = amount;
        this.parameters = parameters;
    }
}
