package smartHomeProject;

import java.util.HashMap;
import java.util.Map;
import javafx.animation.FillTransition;
import javafx.scene.effect.DropShadow;
import javafx.util.Duration;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class HomeViewController {

	private MainMenuController mainMenuController;

	public void setMainMenuController(MainMenuController controller) {
		this.mainMenuController = controller;
	}

	@FXML
	private Pane Room3Pane;

	@FXML
	private StackPane houseContainer;

	@FXML
	private Group houseGroup;

	@FXML
	private Pane landPane;

	@FXML
	private Pane room1Pane;

	@FXML
	private Rectangle room1Rectangle;

	@FXML
	private Pane room2Pane;

	@FXML
	private Rectangle room2Rectangle;

	@FXML
	private Rectangle room3Rectangle;

	@FXML
	private Pane room4Pane;

	@FXML
	private Rectangle room4Rectangle;

	@FXML
	private Rectangle room5Rectangle;

	@FXML
	private Rectangle room6Rectangle;

	@FXML
	private Circle light1;

	@FXML
	private Circle light2;

	@FXML
	private Circle light3;

	@FXML
	private Circle light4;

	@FXML
	private Circle light5;

	@FXML
	private Circle light6;

	@FXML
	private Circle light7;

	@FXML
	private Circle light8;

	@FXML
	private Circle light9;

	@FXML
	private Circle light10;

	@FXML
	private Circle light11;

	@FXML
	private Circle light12;

	@FXML
	private Circle light13;

	@FXML
	private Circle light14;

	@FXML
	private Circle lock1;
	
	@FXML
	private Rectangle lockSquare1;
	
	@FXML
	private Ellipse lockElipse1;
	
	@FXML
	private Group lock1Parts;

	@FXML
	private Rectangle hvac1;

	@FXML
	private Rectangle hvac2;
	
	private Map<String, Node> shapeMap = new HashMap<>();


	@FXML
	public void initialize() {
		
		//pair devices to their shapes
		shapeMap.put("light1", light1);
		shapeMap.put("light2", light2);
		shapeMap.put("light3", light3);
		shapeMap.put("light4", light4);
		shapeMap.put("light5", light5);
		shapeMap.put("light6", light6);
		shapeMap.put("light7", light7);
		shapeMap.put("light8", light8);
		shapeMap.put("light9", light9);
		shapeMap.put("light10", light10);
		shapeMap.put("light11", light11);
		shapeMap.put("light12", light12);
		shapeMap.put("light13", light13);
		shapeMap.put("light14", light14);
		shapeMap.put("hvac1", hvac1);
		shapeMap.put("hvac2", hvac2);
		shapeMap.put("lock1", lock1Parts);


		DoubleBinding scale = (DoubleBinding) Bindings.min(
				houseContainer.widthProperty().divide(456),
				houseContainer.heightProperty().divide(406)
				);

		houseGroup.scaleXProperty().bind(scale);
		houseGroup.scaleYProperty().bind(scale);


		light1.setOnMouseClicked(e -> onDeviceClicked(e));
		light2.setOnMouseClicked(e -> onDeviceClicked(e));
		light3.setOnMouseClicked(e -> onDeviceClicked(e));
		light4.setOnMouseClicked(e -> onDeviceClicked(e));
		light5.setOnMouseClicked(e -> onDeviceClicked(e));
		light6.setOnMouseClicked(e -> onDeviceClicked(e));
		light7.setOnMouseClicked(e -> onDeviceClicked(e));
		light8.setOnMouseClicked(e -> onDeviceClicked(e));
		light9.setOnMouseClicked(e -> onDeviceClicked(e));
		light10.setOnMouseClicked(e -> onDeviceClicked(e));
		light11.setOnMouseClicked(e -> onDeviceClicked(e));
		light12.setOnMouseClicked(e -> onDeviceClicked(e));
		light13.setOnMouseClicked(e -> onDeviceClicked(e));
		light14.setOnMouseClicked(e -> onDeviceClicked(e));
		lock1.setOnMouseClicked(e -> onDeviceClicked(e));
		hvac1.setOnMouseClicked(e -> onDeviceClicked(e));
		hvac2.setOnMouseClicked(e -> onDeviceClicked(e));


	} // close initialize


	public String deviceID = null;



	public void onDeviceClicked(MouseEvent event) {
		
		Node node = (Node) event.getSource();
		String id = node.getId();
		
		//adds deviceID to register device
		if(mainMenuController.devicePanelActive) {

		System.out.println(id + "clicked");

		if(mainMenuController != null) mainMenuController.setSelectedDeviceID(id);
		}
		
		//adds clicked devices to rule
		if(mainMenuController.rulePanelActive) {
			mainMenuController.getCreateRulesController().handleDeviceClickedFromHome(id);
			return;
		}
		
		//makes it so user can turn on/off devices manually by clicking
		if(!mainMenuController.devicePanelActive && 
				!mainMenuController.roomPanelActive && 
				!mainMenuController.rulePanelActive) {
			for(Devices d : mainMenuController.getSmartHomeSystem().getDevices()) {
				 if (d.getDeviceID().equals(id)) {
					 System.out.println("Device selected: "+ id);
					 if(d.isOn()) d.setOn(false);
					 else d.setOn(true);
				 } mainMenuController.getHomeViewController().updateDevice();
			}
			
			
		}


	}
	
	//gets nodes from hashmap
	public Node getNodeFromMap(String id) {
	    return shapeMap.get(id);
	}
	
	//one time loop for preloaded devices
	public void setupDeviceNodes() {
	    for (Devices d : mainMenuController.getSmartHomeSystem().getDevices()) {
	        d.setNode(shapeMap.get(d.getDeviceID()));
	    }
	}
	
	//update device colors
	public void updateDevice() {
	    for (Devices d : mainMenuController.getSmartHomeSystem().getDevices()) {

	        if (d instanceof Lights) {
	            Color targetColor = d.isOn() ? Color.YELLOW : Color.DARKSLATEGRAY;
	            animateColor(d.getNode(), targetColor);
	            applyLightEffect(d.getNode(), d.isOn());
	        }

	        if (d instanceof HVAC hvac) {
	            Color targetColor;

	            if (!hvac.isOn()) {
	                targetColor = Color.DARKSLATEGRAY;
	            } else {
	                targetColor = getHVACColor(hvac);
	            }

	            animateColor(hvac.getNode(), targetColor);
	            //applyHVACEffect(hvac);
	        }

	        if (d instanceof Lock) {
	            Color targetColor = d.isOn() ? Color.FIREBRICK : Color.LIGHTGREEN;
	            animateColor(d.getNode(), targetColor);
	            clearEffect(d.getNode());
	        }

	        System.out.println(d.getDeviceID() + " state: " + d.isOn());
	    }
	}
		
	//method to set the colors with custom input
	public void setColor(Node node, Color color) {
		if(node == null) return;
		
		if(node instanceof Shape shape) {
			if(shape.getStyleClass().contains("device-part")) {
				shape.setFill(color);
			}
		} else if(node instanceof Parent parent) {
			for(Node child : parent.getChildrenUnmodifiable()) {
				setColor(child, color);
			}
		}
	}
	
	//method to make hvac colors vary based on temp
	private Color getHVACColor(HVAC hvac) {
	    int temp = hvac.getTemp();

	    double ratio = (temp - 60) / 25.0; // 60 to 85 -> 0 to 1
	    ratio = Math.max(0, Math.min(1, ratio));

	    if (hvac.getMode() == HVAC.Mode.HEAT) {
	        // hotter = brighter red
	        return Color.hsb(
	            0,                  // red
	            1.0,                // full saturation
	            0.35 + (0.65 * ratio)
	        );
	    } else {
	        // colder = brighter blue
	        double coolRatio = 1.0 - ratio;

	        return Color.hsb(
	            240,                // blue
	            1.0,
	            0.35 + (0.65 * coolRatio)
	        );
	    }
	}

	//animated color changing
	public void animateColor(Node node, Color targetColor) {
	    if (node == null) return;

	    if (node instanceof Shape shape) {
	        if (!shape.getStyleClass().contains("device-part")) {
	            return;
	        }

	        Color startColor = shape.getFill() instanceof Color
	                ? (Color) shape.getFill()
	                : Color.DARKSLATEGRAY;

	        FillTransition ft = new FillTransition(
	                Duration.millis(400),
	                shape,
	                startColor,
	                targetColor
	        );
	        ft.play();

	    } else if (node instanceof Parent parent) {
	        for (Node child : parent.getChildrenUnmodifiable()) {
	            animateColor(child, targetColor);
	        }
	    }
	}
	//add hvac glow
	private void applyHVACEffect(HVAC hvac) {
	    Node node = hvac.getNode();
	    if (node == null) return;

	    if (!hvac.isOn()) {
	        clearEffect(node);
	        return;
	    }

	    int temp = hvac.getTemp();
	    double ratio = (temp - 60) / 25.0;
	    ratio = Math.max(0, Math.min(1, ratio));

	    double strength;
	    Color glowColor;

	    if (hvac.getMode() == HVAC.Mode.HEAT) {
	        strength = 0.3 + (0.7 * ratio);   // hotter heat = stronger glow
	        glowColor = Color.RED;
	    } else {
	        double coolRatio = 1.0 - ratio;   // colder AC = stronger glow
	        strength = 0.3 + (0.7 * coolRatio);
	        glowColor = Color.DEEPSKYBLUE;
	    }

	    DropShadow glow = new DropShadow();
	    glow.setRadius(10 + (15 * strength));
	    glow.setSpread(0.2 + (0.3 * strength));
	    glow.setColor(glowColor);

	    node.setEffect(glow);
	}
	
	//add light glow
	public void applyLightEffect(Node node, boolean isOn) {
	    if (node == null) return;

	    if (!isOn) {
	        clearEffect(node);
	        return;
	    }

	    DropShadow glow = new DropShadow();
	    glow.setColor(Color.GOLD);
	    glow.setRadius(18);
	    glow.setSpread(0.35);

	    node.setEffect(glow);
	}
	
	//removes effects
	public void clearEffect(Node node) {
	    if (node == null) return;

	    node.setEffect(null);

	    if (node instanceof Parent parent) {
	        for (Node child : parent.getChildrenUnmodifiable()) {
	            clearEffect(child);
	        }
	    }
	}
	
} //end class
