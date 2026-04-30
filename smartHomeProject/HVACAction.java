package smartHomeProject;

public class HVACAction extends Action {
    private HVAC.Mode mode;
    private int temp;
    private boolean turnOn;

    public HVACAction(HVAC.Mode mode, int temp, boolean turnOn) {
        this.mode = mode;
        this.temp = temp;
        this.turnOn = turnOn;
    }

    @Override
    public boolean supports(Devices device) {
        return device instanceof HVAC;
    }

    @Override
    public void apply(Devices device) {
        if (device instanceof HVAC hvac) {
            hvac.setMode(mode);
            hvac.setTemp(temp);
            hvac.setOn(turnOn);
        }
    }

    public HVAC.Mode getMode() { return mode; }
    public int getTemp() { return temp; }
    public boolean isTurnOn() { return turnOn; }
}


//package smartHomeProject;
//
//public class HVACAction extends Action {
//    private HVAC.Mode mode;
//    private int temp;
//    private boolean turnOn;
//
//    public HVACAction(HVAC.Mode mode, int temp, boolean turnOn) {
//        this.mode = mode;
//        this.temp = temp;
//        this.turnOn = turnOn;
//    }
//
//    @Override
//    public void apply(Devices device) {
//        if (device instanceof HVAC hvac) {
//            hvac.setMode(mode);
//            hvac.setTemp(temp);
//            hvac.setOn(turnOn);
//        }
//    }
//
//    public HVAC.Mode getMode() { return mode; }
//    public int getTemp() { return temp; }
//    public boolean isTurnOn() { return turnOn; }
//}
//
////import smartHomeProject.HVAC.Mode;
////
////public class HVACAction extends Action{
////	private HVAC.Mode mode;
////	private int temp;
////	boolean turnOn;
////	
////	public HVACAction(HVAC.Mode mode, int temp, boolean turnOn) {
////		super();
////		this.mode = mode;
////		this.temp = temp;
////		this.turnOn = turnOn;
////	}
////	
////	
////	@Override
////	public void execute() {
////		((HVAC) device).setMode(mode);
////		((HVAC) device).setTemp(temp);
////		((HVAC) device).setOn(turnOn);
//		
//	//}
//	
////	@Override
////	public String toString() {
////	    HVAC hvac = (HVAC) device;
////
////	    if (!turnOn) {
////	        return hvac.getName() + " → OFF (Mode: " + mode + ", Temp: " + temp + ")";
////	    }
////
////	    return hvac.getName() + " → ON (Mode: " + mode + ", Temp: " + temp + ")";
////	}
//
////
////	public Mode getMode() {
////		return mode;
////	}
////
////
////	public int getTemp() {
////		return temp;
////	}
////
////
////	public boolean isTurnOn() {
////		return turnOn;
////	}
////
////}
