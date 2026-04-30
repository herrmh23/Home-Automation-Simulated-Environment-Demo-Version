package smartHomeProject;
import java.util.ArrayList;
import java.util.List;

public class Rule {

	//allows selection of target type when rule is selected
	public enum TargetType {
		HOUSE,
		ROOM,
		DEVICE
	}

	private String ruleName;
	private int startTimeHours; // 0-23 hours
	private int startTimeMin; //0 - 59 min 
	private TargetType targetType = TargetType.DEVICE;
	private ArrayList<Room> targetRooms = new ArrayList<>();
	private boolean hasExecuted = false;
	private ArrayList<Devices> targetDevices = new ArrayList<>();


	private ArrayList<Action> actions = new ArrayList<>();

	public Rule(String ruleName, int startTimeHours, int startTimeMin) {
		this.ruleName = ruleName;
		this.startTimeHours = startTimeHours;
		this.startTimeMin = startTimeMin;

		this.actions = new ArrayList<>();
	}

	
	public void addAction(Action action) {
		actions.add(action);
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public String getRuleName() {
		return ruleName;
	}

	public int getStartTime() {
		return startTimeHours * 60 + startTimeMin;
	}
	
	public int getHours() {
		return startTimeHours;
	}

	public int getMinutes() {
		return startTimeMin;
	}
	
	public void setTargetType(TargetType type) {
		this.targetType = type;
	}
	
	public TargetType getTargetType() {
		return targetType;
	}
	
	public ArrayList<Room> getTargetRooms() {
	    return targetRooms;
	}

	public ArrayList<Devices> getTargetDevices() {
	    return targetDevices;
	}

	public void addTargetRoom(Room room) {
		targetRooms.add(room);
	}
	
	//used to handle conflicting rules
	//Rules will execute by order of specificity house < room < device
	public int getPriority() {
	    switch (targetType) {
	        case HOUSE: return 1;
	        case ROOM: return 2;
	        case DEVICE: return 3;
	    }
	    return 0;
	}

//	public void execute(SmartHomeSystem system) {
//
//		//device rules
//		if (targetType == TargetType.DEVICE) {
//			for (Action action : actions) {
//				action.execute();
//			}
//			return;
//		}
//
//		// ROOM / HOUSE rules
//		List<Devices> devices = getAffectedDevices(system);
//
//
//		for (Devices device : devices) {
//			for (Action action : actions) {
//
//				if (action instanceof BinaryAction binary) {
//					new BinaryAction(device, binary.isTurnOn()).execute();
//				}
//
//				if (action instanceof HVACAction hvacAction && device instanceof HVAC hvac) {
//					new HVACAction(
//							hvac,
//							hvacAction.getMode(),
//							hvacAction.getTemp(),
//							hvacAction.isTurnOn()
//							).execute();
//				}
//			}
//		}
//	}

	//makes the rules display more desciptively in the list view
	public List<Devices> getAffectedDevices(SmartHomeSystem system) {
		List<Devices> result = new ArrayList<>();

		switch (targetType) {
		case HOUSE:
			result.addAll(system.getDevices());
			break;
		case ROOM:
			for (Room room : targetRooms) {
				result.addAll(room.getDevices());
			}
			break;
		case DEVICE:
		        result.addAll(targetDevices);
			break;
		}

		System.out.println("Targets: " + targetDevices); // for testing
		return result;
	}


	public boolean getHasExecuted() {
		return hasExecuted;
	}
	
	public void setAsExecuted() {
		hasExecuted = true;
	}

	public void resetExecuted() {
		hasExecuted = false;
	}


	public void addTargetDevice(Devices device) {
	    targetDevices.add(device);
	}

	
	public String getFormattedTime() {
	    int hour = startTimeHours;
	    String amPm = (hour >= 12) ? "PM" : "AM";

	    int displayHour = hour % 12;
	    if (displayHour == 0) displayHour = 12;

	    return String.format("%d:%02d %s", displayHour, startTimeMin, amPm);
	}

	public String getTargetDescription() {
	    switch (targetType) {
	        case HOUSE:
	            return "entire house";

	        case ROOM:
	            if (targetRooms.isEmpty()) return "selected rooms";

	            StringBuilder roomText = new StringBuilder();
	            String separator = "";

	            for (Room room : targetRooms) {
	                roomText.append(separator).append(room.getName());
	                separator = ", ";
	            }

	            return "room(s): " + roomText;

	        case DEVICE:
	            if (targetDevices.isEmpty()) return "selected devices";

	            StringBuilder deviceText = new StringBuilder();
	            String deviceSeparator = "";

	            for (Devices device : targetDevices) {
	                deviceText.append(deviceSeparator).append(device.getName());
	                deviceSeparator = ", ";
	            }

	            return "device(s): " + deviceText;
	    }

	    return "";
	}

	public String getActionDescription() {
	    StringBuilder actionText = new StringBuilder();
	    String separator = "";

	    for (Action action : actions) {
	        if (action instanceof BinaryAction binary) {
	            if (binary.getTargetClass() == Lights.class) {
	                actionText.append(separator)
	                          .append("turn lights ")
	                          .append(binary.isTurnOn() ? "ON" : "OFF");
	            } else if (binary.getTargetClass() == Lock.class) {
	                actionText.append(separator)
	                          .append(binary.isTurnOn() ? "lock doors" : "unlock doors");
	            }
	        }

	        if (action instanceof HVACAction hvacAction) {
	            if (!hvacAction.isTurnOn()) {
	                actionText.append(separator).append("turn HVAC OFF");
	            } else {
	                actionText.append(separator)
	                          .append("set HVAC to ")
	                          .append(hvacAction.getMode())
	                          .append(" at ")
	                          .append(hvacAction.getTemp())
	                          .append("°");
	            }
	        }

	        separator = " | ";
	    }

	    return actionText.toString();
	}

	public String getFullDescription() {
	    return ruleName + "\n"
	            + "Time: " + getFormattedTime() + "\n"
	            + "Target: " + getTargetDescription() + "\n"
	            + "Action: " + getActionDescription();
	}


} // close
