package junk;

import java.util.List;

public abstract class Page {
    private List<Device> devices;

    public List<Device> getDevices() {
        return devices;
    }

    protected abstract List<DeviceRequirement> getDeviceRequirements();

}
