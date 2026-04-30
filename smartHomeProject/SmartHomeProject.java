package smartHomeProject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


//this is the driver -----------------------
public class SmartHomeProject extends Application{ 

	@Override
	public void start(Stage stage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
		Scene scene = new Scene(loader.load());
		
		MainMenuController controller = loader.getController();

		stage.setTitle("Smart Home System");
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.show();

		


		//start up for smart home ––––––––––––––––––––––––––––––––––––––––––––––

		SmartHomeSystem system = SmartHomeSystem.getInstance();
		controller.setSystem(system);

		//starts the clock
		system.startSimulation();

		
		//practice devices
		
		Room room1 = new Room("KITCHEN");
		system.registerRoom(room1);
		
		Room room2 = new Room("LIVING ROOM");
		system.registerRoom(room2);
		
		Room room3 = new Room("HALLWAY");
		system.registerRoom(room3);
		
		
		Lights device1 = new Lights("kitchen light 1", "light2");
		system.registerDevice(device1);
		room1.addDevice(device1);
		
		Lights device2 = new Lights("kitchen light 2", "light3");
		system.registerDevice(device2);
		room1.addDevice(device2);
		
		Lights device3 = new Lights("kitchen light 3", "light5");
		system.registerDevice(device3);
		room1.addDevice(device3);
		
		Lights device4 = new Lights("kitchen light 4", "light6");
		system.registerDevice(device4);
		room1.addDevice(device4);

		Lock device5 = new Lock("front door lock", "lock1");
		system.registerDevice(device5);
		room2.addDevice(device5);
		device5.setOn(true);

		HVAC device6 = new HVAC("main hvac", "hvac1");
		system.registerDevice(device6);
		device6.setMode(HVAC.Mode.AC);
		device6.setTemp(65);
		room2.addDevice(device6);
		
		Lights device7 = new Lights("living room light 1", "light1");
		system.registerDevice(device7);
		room2.addDevice(device7);
		
		Lights device8 = new Lights("living room light 2", "light4");
		system.registerDevice(device8);
		room2.addDevice(device8);
		
		Lights device9 = new Lights("hallway light 1", "light7");
		system.registerDevice(device9);
		room3.addDevice(device9);
		
		Lights device10 = new Lights("hallway light 2", "light8");
		system.registerDevice(device10);
		room3.addDevice(device10);
		
		Lights device11 = new Lights("hallway light 3", "light9");
		system.registerDevice(device11);
		room3.addDevice(device11);
		
		System.out.println(system.getDevices());
		
		// Create bedtime rule at 10:00 PM
		Rule bedtimeRule = new Rule("Bedtime", 22, 30);

		// Target entire house (simplest way)
		bedtimeRule.setTargetType(Rule.TargetType.HOUSE);

		// ---- Actions ----

		// Turn OFF all lights
		bedtimeRule.addAction(new BinaryAction(false, Lights.class));

		// Lock doors
		bedtimeRule.addAction(new BinaryAction(true, Lock.class));

		// Set HVAC to HEAT 70 and turn ON
		bedtimeRule.addAction(new HVACAction(HVAC.Mode.HEAT, 70, true));

		// Add rule to system
		system.addRule(bedtimeRule);

	}

	public static void main(String[] args) {




		launch(args); // THIS starts JavaFX -----------

	} //end of main

} //end of class
