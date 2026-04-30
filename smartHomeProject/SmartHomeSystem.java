package smartHomeProject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public class SmartHomeSystem {
	//private ArrayList<Devices> deviceList; //storage list for all devices
	private ObservableList<Devices> devices = FXCollections.observableArrayList();
	private ObservableList<Rule> rules = FXCollections.observableArrayList();
	private ObservableList<Room> rooms = FXCollections.observableArrayList();
	//private ArrayList<Room> roomList;
	private int simulatedTime = 0;

	public SmartHomeSystem() {
		System.out.println("NEW SYSTEM CREATED: " + this);
		 //Thread.dumpStack(); for debugging
	}
	
	//for creating smart home system instances to connect to controllers
	private static SmartHomeSystem instance;

	public static SmartHomeSystem getInstance() {
	    if (instance == null) {
	        instance = new SmartHomeSystem();
	    }
	    return instance;
	}

	//connects main menu controller to smart home system
	private MainMenuController controller;

	public void setController(MainMenuController controller) {
		this.controller = controller;
		System.out.println("Controller set for: " + this);

	}

	public ObservableList<Room> getRooms() {
		return rooms;
	}

	public void registerRoom(Room room) {
		rooms.add(room);
	}

	public void registerDevice(Devices device) { //adds new device to the deviceList array
		devices.add(device);
		
		 if (controller != null) {
		        Node node = controller.getHomeViewController().getNodeFromMap(device.getDeviceID());

		        device.setNode(node);

		        controller.refreshUI();
		    }
	}

	public ObservableList<Devices> getDevices() { //returns the deviceList array
		return devices;
	}

	public void addRule(Rule rule) {
		rules.add(rule);
		
		//sorts rule order by time, then specificity, then name
		rules.sort((r1, r2) -> {
			//time
			int timeCompare = Integer.compare(r1.getStartTime(), r2.getStartTime());
			if(timeCompare != 0) return timeCompare;
			
			//specificity
			int spec1 = r1.getPriority();
			int spec2 = r2.getPriority();
			
			int specCompare = Integer.compare(spec1,  spec2);
			if(specCompare != 0) return specCompare;
			
			//name
			return r1.getRuleName().compareToIgnoreCase(r2.getRuleName());
		});
	}

	public ObservableList<Rule> getRule() {
		return rules;
	}



	// executes the rule against current time
	public void executeRules(int simulatedTime) {

		List<Rule> activeRules = new ArrayList<>();

		for (Rule rule : rules) {

			//reset executed status once time has passed
			if(simulatedTime != rule.getStartTime()) rule.resetExecuted();

			if (simulatedTime == rule.getStartTime() && !rule.getHasExecuted()) {
				System.out.println(rule);
				activeRules.add(rule);
				rule.setAsExecuted();
			}
		}

		if(!activeRules.isEmpty()) {
			Platform.runLater(() -> applyRulesWithPriority(activeRules));
		}
	}

	//method that handles sorting rules by priority
	public void applyRulesWithPriority(List<Rule> rules) {

		Map<Devices, Action> finalActions = new HashMap<>();
		Map<Devices, Integer> devicePriority = new HashMap<>();
		Map<Devices, Rule> winningRules = new HashMap<>();

		for (Rule rule : rules) {

			List<Devices> affectedDevices = rule.getAffectedDevices(this);

			for (Devices device : affectedDevices) {
				
				
				for (Action action : rule.getActions()) {

				    if (!action.supports(device)) {
				        continue;
				    }

				    int currentPriority = rule.getPriority();
				    Integer existingPriority = devicePriority.get(device);

				    if (existingPriority == null) {

				        System.out.println("[NEW] " + rule.getRuleName() +
				                " sets " + device.getName());

				        Action newAction = null;

				        if (action instanceof BinaryAction binary) {
				            newAction = new BinaryAction(binary.isTurnOn(), binary.getTargetClass());
				        }

				        if (action instanceof HVACAction hvacAction) {
				            newAction = new HVACAction(
				                hvacAction.getMode(),
				                hvacAction.getTemp(),
				                hvacAction.isTurnOn()
				            );
				        }

				        finalActions.put(device, newAction);
				        devicePriority.put(device, currentPriority);
				        winningRules.put(device, rule);

				    } else if (currentPriority > existingPriority) {

				        System.out.println("[OVERRIDE - higher priority] " +
				                rule.getRuleName() + " overrides " +
				                winningRules.get(device).getRuleName() +
				                " for " + device.getName());

				        Action newAction = null;

				        if (action instanceof BinaryAction binary) {
				            newAction = new BinaryAction(binary.isTurnOn(), binary.getTargetClass());
				        }

				        if (action instanceof HVACAction hvacAction) {
				            newAction = new HVACAction(
				                hvacAction.getMode(),
				                hvacAction.getTemp(),
				                hvacAction.isTurnOn()
				            );
				        }

				        finalActions.put(device, newAction);
				        devicePriority.put(device, currentPriority);
				        winningRules.put(device, rule);

				    } else if (currentPriority == existingPriority) {

				        System.out.println("[OVERRIDE - same priority] " +
				                rule.getRuleName() + " overrides " +
				                winningRules.get(device).getRuleName() +
				                " for " + device.getName());

				        Action newAction = null;

				        if (action instanceof BinaryAction binary) {
				            newAction = new BinaryAction(binary.isTurnOn(), binary.getTargetClass());
				        }

				        if (action instanceof HVACAction hvacAction) {
				            newAction = new HVACAction(
				                hvacAction.getMode(),
				                hvacAction.getTemp(),
				                hvacAction.isTurnOn()
				            );
				        }

				        finalActions.put(device, newAction);
				        winningRules.put(device, rule);
				    } else {

				        System.out.println("[IGNORED] " +
				                rule.getRuleName() + " lost to " +
				                winningRules.get(device).getRuleName() +
				                " for " + device.getName());
				    }
				}

			}
		}

		//Final result summary
		System.out.println("\n=== FINAL ACTIONS ===");
		for (Map.Entry<Devices, Action> entry : finalActions.entrySet()) {
			Devices device = entry.getKey();
			Rule winner = winningRules.get(device);

			System.out.println(device.getName() +
					" controlled by " + winner.getRuleName());
		}


		//execute final actions
		for (Map.Entry<Devices, Action> entry : finalActions.entrySet()) {
		    Devices device = entry.getKey();
		    Action action = entry.getValue();

		    action.apply(device);
		}
		
		if(controller != null) {
			controller.refreshUI();
		}
	}

	//check for conflicts
	public List<String> checkConflicts(Rule newRule) {
		List<String> conflicts = new ArrayList<>();

		for(Rule existingRule : rules) {

			//checks rules with same start time
			if (existingRule.getStartTime() != newRule.getStartTime()) continue;

			//gets devices from both rules
			List<Devices> newDevices = newRule.getAffectedDevices(this);
			List<Devices> existingDevices = existingRule.getAffectedDevices(this);

			//checks if the new rule shared a device with the old rule
			for (Devices device : newDevices) {
				if (existingDevices.contains(device)) {

					//checks if the new rule conflicts actions with the old rule
					for (Action newAction : newRule.getActions()) {
						for (Action existingAction : existingRule.getActions()) {

							//creates error message
							if (actionsConflict(newAction, existingAction)) {
								conflicts.add(
										"Conflict with \"" + existingRule.getRuleName() +
										"\" for " + device.getName()
										);
							}
						}
					}
				}
			}
		}
		return conflicts;
	}

	//used to check if the actions conflict before alerting user
	private boolean actionsConflict(Action a1, Action a2) {

		//compares binary actions of two rules
		if (a1 instanceof BinaryAction b1 && a2 instanceof BinaryAction b2) {
			return b1.isTurnOn() != b2.isTurnOn();
		}

		//compares actions of two hvac rules
		if (a1 instanceof HVACAction h1 && a2 instanceof HVACAction h2) {
			return h1.isTurnOn() != h2.isTurnOn()
					|| h1.getTemp() != h2.getTemp()
					|| h1.getMode() != h2.getMode();
		}

		return false;
	}





	//timer (turns 1 real minute into 1 virtual, and 1 real minute into 1 virtual hour, so full virtual days are 24 real minutes)
	public void startSimulation() {

		Timer timer = new Timer();

		timer.scheduleAtFixedRate(new TimerTask() {
			int hour;
			int minute;

			@Override
			public void run() {

				simulatedTime++; //increased the time by one minute

				if(simulatedTime >= 1440) simulatedTime = 0; //reset the time after 24 minutes

				executeRules(simulatedTime); //runs executeRules, which checks if it is time to run commands

				//prints simulated time to console every real minute
				hour = simulatedTime / 60;
				minute = simulatedTime % 60;
				String timeString = String.format("%02d:%02d", hour, minute);
				if(simulatedTime % 60 == 0) System.out.println(timeString);

				Platform.runLater(() -> {
					if (controller != null) {
						controller.updateTimeLabel(hour, minute);
					}
				});


			}

		}, 0, 1000); //every second

	}


}