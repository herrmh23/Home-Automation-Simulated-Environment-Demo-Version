package smartHomeProject;
import java.util.ArrayList;

public class Room {
    private String name;
    private ArrayList<Devices> roomDevices;

    public Room(String name) {
        this.name = name;
        this.roomDevices = new ArrayList<>();
    }

    // add device 
    public void addDevice(Devices device) {
        roomDevices.add(device);
    }

    // getters
    public String getName() {
        return name;
    }

    public ArrayList<Devices> getDevices() {
        return roomDevices;
    }


    @Override
    public String toString() {
        return "Room: " + name + " | Devices: " + roomDevices;
    }
}
