package smartHomeProject;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;

public class Devices {
	//private boolean isOn; //boolean value that sets a device to on or off
	private BooleanProperty isOn = new SimpleBooleanProperty();
	private String name; //name of the device for storage and control purposes
	private String deviceID;
	private Node node;
	
	Devices(String name, String deviceID){ //constructor
		isOn.set(false);;
		this.name = name;
		this.deviceID = deviceID;
	}
	public boolean isOn() {
	    return isOn.get();
	}

	public void setOn(boolean value) {
	    isOn.set(value);
	}

	public BooleanProperty isOnProperty() {
	    return isOn;
	}
	
	public String getName() {  //returns device name
        return name;
    }
	
	public String getDeviceID() {
		return deviceID;
	}
	
	public String getDeviceType() {
		return deviceID.replaceAll("\\d+$", "");
	}
	
	public void setNode(Node node) {
		this.node = node;
	}
	
	public Node getNode() {
		return node;
	}
	
	@Override //this makes it so the devices in the ArrayList are stored by their name and status
			 //otherwise it stores them as the device type with a bunch of storage location numbers
	public String toString() {
	    return name + " (" + (isOn.getValue() ? "ON" : "OFF") + ")";
	}
	
}
