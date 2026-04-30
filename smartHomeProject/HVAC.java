package smartHomeProject;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class HVAC extends Devices{
	public enum Mode {
        HEAT,
        AC
    }

//    private Mode mode;
//	private int temp; 
	private ObjectProperty<Mode> mode = new SimpleObjectProperty<>();
	private IntegerProperty temp = new SimpleIntegerProperty();

	HVAC(String name, String deviceID) { //superclass to Devices()
		super(name, deviceID);
		mode.set(Mode.HEAT); //automatically set to heat
		temp.set(70); //automatically set to 70
	}
	
	
	public void setMode(Mode mode) {
	    this.mode.set(mode);
	}

	public void setTemp(int temp) {
	    if (temp >= 60 && temp <= 85) {
	        this.temp.set(temp);
	    } else {
	        System.out.println("Temperature must be within range 60-85");
	    }
	}
	
	
	public Mode getMode() {
	    return mode.get();
	}

	public int getTemp() {
	    return temp.get();
	}
	
	public ObjectProperty<Mode> modeProperty() {
	    return mode;
	}

	public IntegerProperty tempProperty() {
	    return temp;
	}
	
	@Override //stores the name and isOn status, but also adds the heatOrAC and temp statuses
	public String toString() {
	    return this.getName() + " (" + (this.isOn() ? "ON" : "OFF") + ")" + " (" + (this.getMode() == Mode.HEAT ? "HEAT " : "AC ") + this.getTemp() + ")";
	}

	
	
	
}
