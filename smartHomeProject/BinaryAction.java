package smartHomeProject;

public class BinaryAction extends Action {
    private boolean turnOn;
    private Class<? extends Devices> targetClass;

    public BinaryAction(boolean turnOn, Class<? extends Devices> targetClass) {
        this.turnOn = turnOn;
        this.targetClass = targetClass;
    }

    public boolean isTurnOn() {
        return turnOn;
    }

    public Class<? extends Devices> getTargetClass() {
        return targetClass;
    }

    @Override
    public boolean supports(Devices device) {
        return targetClass.isInstance(device);
    }

    @Override
    public void apply(Devices device) {
        if (supports(device)) {
            device.setOn(turnOn);
        }
    }
}

//package smartHomeProject;
//
//public class BinaryAction extends Action {
//    private boolean turnOn;
//
//    public BinaryAction(boolean turnOn) {
//        this.turnOn = turnOn;
//    }
//
//    public boolean isTurnOn() {
//        return turnOn;
//    }
//
//    @Override
//    public void apply(Devices device) {
//        device.setOn(turnOn);
//    }
//}

//public class BinaryAction extends Action {
//	private boolean turnOn;
//	
//	public BinaryAction( boolean turnOn) {
//		super();
//		this.turnOn = turnOn;
//	}
//	
//	public boolean isTurnOn() {
//	    return turnOn;
//	}
//
//	@Override
//	public void execute() {
//		// TODO Auto-generated method stub
//		
//	}
	
//	@Override
//	public void execute() {
//		device.setOn(turnOn);
//	}

	
//	@Override
//	public String toString() {
//	    return device.getName() + " → " + (turnOn ? "ON" : "OFF");
//	}
	

