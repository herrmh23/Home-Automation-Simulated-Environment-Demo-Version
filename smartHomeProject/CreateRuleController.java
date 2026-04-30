package smartHomeProject;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.ListChangeListener;

public class CreateRuleController {

	private MainMenuController mainMenuController;

	public void setMainMenuController(MainMenuController controller) {
		this.mainMenuController = controller;
	}

	//room and device list observable lists
	private ObservableList<Room> roomList;
	private ObservableList<Devices> deviceList;

	//connect these room and device lists to the actually created lists
	public void setData(ObservableList<Room> rooms, ObservableList<Devices> devices) {
		this.roomList = rooms;
		this.deviceList = devices;
	}

	@FXML
	private Label HVACModeLabel;

	@FXML
	private ToggleGroup HVACMode;

	@FXML
	private RadioButton HVACOffRadioButton;

	@FXML
	private HBox HVACRulesHBox;

	@FXML
	private TextField HVACTempTextField;

	@FXML
	private Label HVACTemperatureLabel;

	@FXML
	private RadioButton acRadioButton;

	@FXML
	private Button closeButton;

	@FXML
	private Button createRuleButton;

	@FXML
	private Label createRuleLabel;

	@FXML
	private VBox createRuleVBox;

	@FXML
	private RadioButton deviceRuleRadioButton;

	@FXML
	private Label errorLabel;

	@FXML
	private RadioButton headRadioButton;

	@FXML
	private RadioButton houseRuleRadioButton;

	@FXML
	private ToggleGroup lightsOnOff;

	@FXML
	private RadioButton lockRadioButton;

	@FXML
	private RadioButton unlockRadioButton;

	@FXML
	private ToggleGroup setLockStatus;

	@FXML
	private RadioButton offRadioButton;

	@FXML
	private RadioButton onRadioButton;

	@FXML
	private RadioButton roomRuleRadioButton;

	@FXML
	private HBox ruleActivationTimeHBox;

	@FXML
	private Label ruleActivationTimeLabel;

	@FXML
	private TextField hoursTextField;

	@FXML
	private Label colonLabel;

	@FXML
	private TextField minutesTextField;

	@FXML
	private RadioButton amRadioButton;

	@FXML
	private RadioButton pmRadioButton;

	@FXML
	private ToggleGroup amPM;

	@FXML
	private Label ruleNameLabel;

	@FXML
	private TextField ruleNameTextField;

	@FXML
	private ToggleGroup ruleRangeSelection;

	@FXML
	private ListView<Object> ruleTargetSelectionListView;

	@FXML
	private HBox setDevicesToHBox;

	@FXML
	private Label setDevicesToLabel;

	@FXML
	private HBox setLockHBox;

	@FXML
	private Label setLockLabel;

	private ArrayList<RadioButton> radioButtons;

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}
	
	public boolean getEditMode() {
		return editMode;
	}


	public void initialize() {

		//sets all device type controls to not visible by default
		setLightOptionsVisible(false);
		setLockOptionsVisible(false);
		setHVACOptionsVisible(false);

		//sets the list view to not visible and not managed by default. Also sets selection to mutliple to allow for
		//multiple devices or rooms to be selected at once
		ruleTargetSelectionListView.setVisible(false);
		ruleTargetSelectionListView.setManaged(false);
		ruleTargetSelectionListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		//modifies the list view cells to:
		//appear better format wise
		//include check boxes to make multiple element selection smoother (no extra keyboard buttons needed)
		ruleTargetSelectionListView.setCellFactory(lv -> new ListCell<>() {

			private final CheckBox checkBox = new CheckBox();

			{

				checkBox.setOnAction(e -> {
					if (getItem() == null) return;

					var selectionModel = getListView().getSelectionModel();

					if (checkBox.isSelected()) {
						selectionModel.select(getIndex());
					} else {
						selectionModel.clearSelection(getIndex());
					}
				});


				setOnMousePressed(event -> {
					if (getItem() == null) return;

					var selectionModel = getListView().getSelectionModel();

					if (selectionModel.isSelected(getIndex())) {
						selectionModel.clearSelection(getIndex());
						checkBox.setSelected(false);
					} else {
						selectionModel.select(getIndex());
						checkBox.setSelected(true);
					}

					event.consume(); 
				});
			}

			@Override
			protected void updateItem(Object item, boolean empty) {
				super.updateItem(item, empty);

				if (empty || item == null) {
					setGraphic(null);
					return;
				}


				if (item instanceof Room room) {
					checkBox.setText(room.getName());
				} else if (item instanceof Devices device) {
					checkBox.setText(device.getName());
				}


				checkBox.setSelected(
						getListView().getSelectionModel().isSelected(getIndex())
						);

				// Make cursor a hand when hovering over row
				setOnMouseEntered(e -> setCursor(Cursor.HAND));
				setOnMouseExited(e -> setCursor(Cursor.DEFAULT));


				checkBox.setCursor(Cursor.HAND);


				checkBox.setOnMousePressed(e -> e.consume());

				setGraphic(checkBox);
			}
		});


		radioButtons = new ArrayList<>(List.of(
				acRadioButton,
				HVACOffRadioButton,
				deviceRuleRadioButton,
				houseRuleRadioButton,
				roomRuleRadioButton,
				headRadioButton,
				lockRadioButton,
				unlockRadioButton,
				onRadioButton,
				offRadioButton,
				amRadioButton,
				pmRadioButton
				));

		createRuleLabel.styleProperty().bind(
				Bindings.concat(
						"-fx-text-fill: #F2F2F2; ",
						"-fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
						createRuleVBox.widthProperty().multiply(0.1))
				);

		ruleNameLabel.styleProperty().bind( //–––––––––
				Bindings.concat(
						"-fx-text-fill: #F2F2F2; ",
						"-fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
						createRuleVBox.widthProperty().multiply(0.05))
				);

		ruleActivationTimeLabel.styleProperty().bind( //–––––––––
				Bindings.concat(
						"-fx-text-fill: #F2F2F2; ",
						"-fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
						createRuleVBox.widthProperty().multiply(0.05))
				);

		setDevicesToLabel.styleProperty().bind( //–––––––––
				Bindings.concat(
						"-fx-text-fill: #F2F2F2; ",
						"-fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
						createRuleVBox.widthProperty().multiply(0.05))
				);

		setLockLabel.styleProperty().bind( //–––––––––
				Bindings.concat(
						"-fx-text-fill: #F2F2F2; ",
						"-fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
						createRuleVBox.widthProperty().multiply(0.05))
				);

		HVACModeLabel.styleProperty().bind( //–––––––––
				Bindings.concat(
						"-fx-text-fill: #F2F2F2; ",
						"-fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
						createRuleVBox.widthProperty().multiply(0.05))
				);

		HVACTemperatureLabel.styleProperty().bind( //–––––––––
				Bindings.concat(
						"-fx-text-fill: #F2F2F2; ",
						"-fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
						createRuleVBox.widthProperty().multiply(0.05))
				);

		ruleNameTextField.prefHeightProperty().bind(
				createRuleVBox.heightProperty().multiply(0.05)
				);

		HVACTempTextField.prefHeightProperty().bind(
				createRuleVBox.heightProperty().multiply(0.05)
				);

		createRuleButton.prefHeightProperty().bind(
				createRuleVBox.heightProperty().multiply(0.05)
				);

		closeButton.prefHeightProperty().bind(
				createRuleVBox.heightProperty().multiply(0.05)
				);

		for (RadioButton rb : radioButtons) {
			rb.styleProperty().bind(
					Bindings.concat(
							"-fx-font-family: 'Comic Sans MS'; ",
							"-fx-font-size: ",
							createRuleVBox.widthProperty().multiply(0.04)
							)
					);
		}

		//format rule time inputs
		DoubleExpression timeHeight = createRuleVBox.heightProperty().multiply(0.05);

		hoursTextField.prefHeightProperty().bind(timeHeight);
		minutesTextField.prefHeightProperty().bind(timeHeight);
		colonLabel.prefHeightProperty().bind(timeHeight);

		hoursTextField.prefWidthProperty().bind(createRuleVBox.widthProperty().multiply(0.1));
		minutesTextField.prefWidthProperty().bind(createRuleVBox.widthProperty().multiply(0.1));

		ruleActivationTimeHBox.setAlignment(Pos.CENTER_LEFT);
		ruleActivationTimeHBox.setSpacing(5);

		colonLabel.styleProperty().bind(
				Bindings.concat(
						"-fx-text-fill: #F2F2F2; ",
						"-fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
						createRuleVBox.heightProperty().multiply(0.04)
						)
				);

		hoursTextField.setAlignment(Pos.CENTER);
		minutesTextField.setAlignment(Pos.CENTER);
		colonLabel.setAlignment(Pos.CENTER);

		// Colon smaller
		colonLabel.prefWidthProperty().bind(createRuleVBox.widthProperty().multiply(0.03));

		//closes right side pannel
		closeButton.setOnAction(e -> mainMenuController.cancelAnything());


		//sets list view based on rule type radio button selection
		ruleRangeSelection.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {

			if (newToggle == null) return;

			//protects against issues if system hasn't started yet
			if (mainMenuController == null || mainMenuController.getSmartHomeSystem() == null) {
				return;
			}

			RadioButton selected = (RadioButton) newToggle;

			if (selected == houseRuleRadioButton) {

				//sets list view to not visible
				ruleTargetSelectionListView.setVisible(false);
				ruleTargetSelectionListView.setManaged(false);
				//sets all device options visible automatically
				setLightOptionsVisible(true);
				setLockOptionsVisible(true);
				setHVACOptionsVisible(true);

			} else if (selected == roomRuleRadioButton) {

				//sets list view to visible
				ruleTargetSelectionListView.setVisible(true);
				ruleTargetSelectionListView.setManaged(true);

				//sets list view to room list
				ruleTargetSelectionListView.setItems(
						(ObservableList) mainMenuController.getRooms()
						);

				//sets all device options invisible automatically
				setLightOptionsVisible(false);
				setLockOptionsVisible(false);
				setHVACOptionsVisible(false);



			} else if (selected == deviceRuleRadioButton) {

				//sets list view visible
				ruleTargetSelectionListView.setVisible(true);
				ruleTargetSelectionListView.setManaged(true);

				//sets list view to devices list
				ruleTargetSelectionListView.setItems(
						(ObservableList) mainMenuController.getDevices()
						);

				//sets all device options invisible automatically
				setLightOptionsVisible(false);
				setLockOptionsVisible(false);
				setHVACOptionsVisible(false);
			}
		});

		//sets hvac temp options to invisible if off is selected
		HVACMode.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {

			if (newToggle == HVACOffRadioButton) {
				setHVACTempOptionsVisible(false);
			} else {
				setHVACTempOptionsVisible(true);
			}
		});

		//event listener that shows or removes device type options based on selected items from list view
		ruleTargetSelectionListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Object>) change -> {

			boolean hasLight = false;
			boolean hasHVAC = false;
			boolean hasLock = false;


			for (Object obj : ruleTargetSelectionListView.getSelectionModel().getSelectedItems()) {

				//if device rule is selected
				if(obj instanceof Devices device) {

					switch(device.getDeviceType()) {

					case "light":
						hasLight = true;
						break;

					case "hvac":
						hasHVAC = true;
						break;

					case "lock":
						hasLock = true;
						break;
					}
				}

				//if room rule is selected
				if(obj  instanceof Room) {
					for(Devices d: ((Room) obj).getDevices()) { //get all devices from selected rooms
						switch(d.getDeviceType()) {

						case "light":
							hasLight = true;
							break;

						case "hvac":
							hasHVAC = true;
							break;

						case "lock":
							hasLock = true;
							break;
						}
					}

				}

				//set the options visible or not visible
				setLightOptionsVisible(hasLight);
				setHVACOptionsVisible(hasHVAC);
				setLockOptionsVisible(hasLock);
			}
		});

		//event listener for when createRuleButton is clicked to create the rule : createRuleButton Function
		createRuleButton.setOnAction(e -> { //–––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––

			
			
			//capitalized the first letter of every word in a rule name
			String name = ruleNameTextField.getText();
			name = name.toLowerCase();
			String words[] = name.split("\\s+");
			
			for(int i = 0; i < words.length; i++) {
				if(!words[i].isEmpty()) {
					words[i] = words[i].substring(0,1).toUpperCase() + words[i].substring(1);
				}
			}
			
			name = String.join(" ", words).trim();
			
			//Checkers to ensure valid inputs
			if(name == null || name.isEmpty()) {
				errorLabel.setText("Please enter a rule name");
				setErrorLabelColor("red");
				return;
			}
			
			if(!houseRuleRadioButton.isSelected() && 
					!deviceRuleRadioButton.isSelected() &&
					!roomRuleRadioButton.isSelected()) {
				errorLabel.setText("Please select a target scope");
				setErrorLabelColor("red");
				return;
			}

			//checks if time input is valid
			String hoursText = hoursTextField.getText();
			String minutesText = minutesTextField.getText();

			if (hoursText.isEmpty() || minutesText.isEmpty()) {
			    errorLabel.setText("Please enter a valid time");
			    setErrorLabelColor("red");
			    return;
			}
			
			

			int hours, minutes;

			//sets time values to hours and minutes if inputs are valid
			try {
			    hours = Integer.parseInt(hoursText);
			    minutes = Integer.parseInt(minutesText);
			} catch (NumberFormatException ex) {
			    errorLabel.setText("Time must be numbers");
			    setErrorLabelColor("red");
			    return;
			}
			
			if(hours > 12 || hours < 0 || minutes > 59 || minutes <0) {
				errorLabel.setText("Invalid time");
				setErrorLabelColor("red");
				return;
			}
			
			//factors in am/pm
			if(pmRadioButton.isSelected() && hours != 12) hours += 12; //12 pm is 12
			if(amRadioButton.isSelected() && hours == 12) hours = 0; //midnight should be 0:00
			
			if((deviceRuleRadioButton.isSelected() || roomRuleRadioButton.isSelected()) && 
					ruleTargetSelectionListView.getSelectionModel().getSelectedItems().isEmpty()) {
				errorLabel.setText("Please select rule targets");
				setErrorLabelColor("red");
				return;
			}

			if(setDevicesToHBox.isVisible() && !onRadioButton.isSelected() && !offRadioButton.isSelected()) {
				errorLabel.setText("Please select light state");
				setErrorLabelColor("red");
				return;
			}

			if(setLockHBox.isVisible() && !lockRadioButton.isSelected() && !unlockRadioButton.isSelected()) {
				errorLabel.setText("Please select lock state");
				setErrorLabelColor("red");
				return;
			}

			if(HVACRulesHBox.isVisible() && !headRadioButton.isSelected() && !acRadioButton.isSelected() 
					&& !HVACOffRadioButton.isSelected()) {
				errorLabel.setText("Please select HVAC state");
				setErrorLabelColor("red");
				return;
			}

			if (HVACTempTextField.isVisible()) {

			    String tempText = HVACTempTextField.getText();

			    if (tempText.isEmpty()) {
			        errorLabel.setText("Please enter HVAC temp");
			        setErrorLabelColor("red");
			        return;
			    }

			    int temp;
			    try {
			        temp = Integer.parseInt(tempText);
			    } catch (NumberFormatException ex) {
			        errorLabel.setText("HVAC temp must be a number");
			        setErrorLabelColor("red");
			        return;
			    }

			    if (temp < 60 || temp > 85) {
			        errorLabel.setText("HVAC temp must be 60º - 85º");
			        setErrorLabelColor("red");
			        return;
			    }
			}

			Rule rule = new Rule(name, hours, minutes);

			//step 2: select the rule type
			if (houseRuleRadioButton.isSelected()) {
				rule.setTargetType(Rule.TargetType.HOUSE);
			}
			else if (roomRuleRadioButton.isSelected()) {
				rule.setTargetType(Rule.TargetType.ROOM);
			}
			else {
				rule.setTargetType(Rule.TargetType.DEVICE);
			}

		
			
			// step 3A: set targets
			if (roomRuleRadioButton.isSelected()) {
			    for (Object obj : ruleTargetSelectionListView.getSelectionModel().getSelectedItems()) {
			        if (obj instanceof Room room) {
			            rule.addTargetRoom(room);
			        }
			    }
			}
			else if (deviceRuleRadioButton.isSelected()) {
			    for (Object obj : ruleTargetSelectionListView.getSelectionModel().getSelectedItems()) {
			        if (obj instanceof Devices device) {
			            rule.addTargetDevice(device);
			            System.out.println("Adding target: " + device.getDeviceID());
			        }
			    }
			}
			// HOUSE rules do not need explicit targets because Rule.getAffectedDevices()
			// already uses all system devices for HOUSE.

			// step 3B: add actions once per device type
			if (setDevicesToHBox.isVisible()) {
			    boolean setOn = onRadioButton.isSelected();
			    rule.addAction(new BinaryAction(setOn, Lights.class));
			}

			if (setLockHBox.isVisible()) {
			    boolean locked = lockRadioButton.isSelected();
			    rule.addAction(new BinaryAction(locked, Lock.class));
			}

			if (HVACRulesHBox.isVisible()) {
			    if (HVACOffRadioButton.isSelected()) {
			        rule.addAction(new HVACAction(HVAC.Mode.HEAT, 70, false));
			    } else {
			        HVAC.Mode mode = acRadioButton.isSelected() ? HVAC.Mode.AC : HVAC.Mode.HEAT;
			        int temp = Integer.parseInt(HVACTempTextField.getText());
			        rule.addAction(new HVACAction(mode, temp, true));
			    }
			}


			

			//checks for conflicting rule
			List<String> conflicts = mainMenuController.getSmartHomeSystem().checkConflicts(rule);

			//sets error message to alert user if new rule conflicts an existing rule
			if(!conflicts.isEmpty()) {
				errorLabel.setText("Conflicts detected:\n" + String.join("\n", conflicts));
				setErrorLabelColor("orange");
			} 
			else {
				errorLabel.setText("Rule " + name + " created!");
				setErrorLabelColor("green");
			}

			//if in edit mode, remove old rule, create new one. Otherwise just add new rule
			if (editMode && ruleBeingEdited != null) {
			    mainMenuController.getSmartHomeSystem().getRule().remove(ruleBeingEdited);
			    
			    for(Rule r : mainMenuController.getSmartHomeSystem().getRule())
					if(r.getRuleName().equalsIgnoreCase(name)) {
						errorLabel.setText("Rule " + name + " already exists");
						setErrorLabelColor("red");
						return;
					}
			    
			    mainMenuController.getSmartHomeSystem().addRule(rule);
			    exitEditMode();
			} else {
				
				for(Rule r : mainMenuController.getSmartHomeSystem().getRule())
					if(r.getRuleName().equalsIgnoreCase(name)) {
						errorLabel.setText("Rule " + name + " already exists");
						setErrorLabelColor("red");
						return;
					}
				
			    mainMenuController.getSmartHomeSystem().addRule(rule);
			}

			closeButton();
			//prints to console for testing purposes
			System.out.println("New rule: " + rule.getRuleName() + " " + rule.getStartTime() + rule.getActions());
		});// close create rule button createRuleButton Function–––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––


		applyDarkTheme();
	} // close initialize –––––––––––––––––––––––––––––––––

	//methods to set options visible or not
	public void setLightOptionsVisible(boolean b) {
		setDevicesToLabel.setVisible(b);
		setDevicesToLabel.setManaged(b);
		setDevicesToHBox.setVisible(b);
		setDevicesToHBox.setManaged(b);
	}

	public void setLockOptionsVisible(boolean b) {
		setLockLabel.setVisible(b);
		setLockLabel.setManaged(b);
		setLockHBox.setVisible(b);
		setLockHBox.setManaged(b);
	}

	public void setHVACOptionsVisible(boolean b) {
		HVACModeLabel.setVisible(b);
		HVACModeLabel.setManaged(b);
		HVACRulesHBox.setVisible(b);
		HVACRulesHBox.setManaged(b);
		HVACTemperatureLabel.setVisible(b);
		HVACTemperatureLabel.setManaged(b);
		HVACTempTextField.setVisible(b);
		HVACTempTextField.setManaged(b);
	}

	public void setHVACTempOptionsVisible(boolean b) {
		HVACTemperatureLabel.setVisible(b);
		HVACTemperatureLabel.setManaged(b);
		HVACTempTextField.setVisible(b);
		HVACTempTextField.setManaged(b);
	}

	//erases error message
	public void clearErrorMessage() {
		errorLabel.setText(null);
	}
	

	//clears everything
	public void closeButton() {
		
		//turns off edit mode
		editMode = false;
	    ruleBeingEdited = null;
	    createRuleLabel.setText("Create Rule");
	    createRuleButton.setText("Create Rule");
	    closeButton.setText("Close");
	    
	    //clears all fields
		ruleNameTextField.clear();
		ruleRangeSelection.selectToggle(null);
		hoursTextField.clear();
		minutesTextField.clear();
		amPM.selectToggle(amRadioButton);
		lightsOnOff.selectToggle(null);
		setLockStatus.selectToggle(null);
		HVACMode.selectToggle(null);
		HVACTempTextField.clear();
		ruleTargetSelectionListView.getSelectionModel().clearSelection();
		errorLabel.setText(null);
		setLightOptionsVisible(false);
		setLockOptionsVisible(false);
		setHVACOptionsVisible(false);
		ruleTargetSelectionListView.setVisible(false);
		ruleTargetSelectionListView.setManaged(false);
	}
	
	//only clears form fields
	private void clearFormFieldsOnly() {
	    ruleNameTextField.clear();
	    ruleRangeSelection.selectToggle(null);
	    hoursTextField.clear();
	    minutesTextField.clear();
	    amPM.selectToggle(amRadioButton);
	    lightsOnOff.selectToggle(null);
	    setLockStatus.selectToggle(null);
	    HVACMode.selectToggle(null);
	    HVACTempTextField.clear();
	    ruleTargetSelectionListView.getSelectionModel().clearSelection();
	    errorLabel.setText(null);
	    setLightOptionsVisible(false);
	    setLockOptionsVisible(false);
	    setHVACOptionsVisible(false);
	    ruleTargetSelectionListView.setVisible(false);
	    ruleTargetSelectionListView.setManaged(false);
	}

	//method to change errorLabel color
	public void setErrorLabelColor(String color) {
		errorLabel.styleProperty().unbind();
		
		errorLabel.styleProperty().bind(
				Bindings.concat(
						"-fx-text-fill: ", color, "; -fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
						createRuleVBox.widthProperty().multiply(0.05))
				);
	}

	
	public void handleDeviceClickedFromHome(String deviceId) {
	    if (deviceId == null) return;

	    // Only allow this when building a DEVICE rule
	    if (!deviceRuleRadioButton.isSelected()) {
	        return;
	    }

	    // Make sure the device list is showing
	    ruleTargetSelectionListView.setVisible(true);
	    ruleTargetSelectionListView.setManaged(true);
	    ruleTargetSelectionListView.setItems((ObservableList) mainMenuController.getDevices());

	    for (Object obj : ruleTargetSelectionListView.getItems()) {
	        if (obj instanceof Devices device && device.getDeviceID().equals(deviceId)) {

	            var selectionModel = ruleTargetSelectionListView.getSelectionModel();
	            int index = ruleTargetSelectionListView.getItems().indexOf(obj);

	            if (selectionModel.isSelected(index)) {
	                selectionModel.clearSelection(index);
	            } else {
	                selectionModel.select(index);
	            }

	            ruleTargetSelectionListView.scrollTo(index);
	            break;
	        }
	    }
	}
	
	
	//used for edit rule functionality
		private Rule ruleBeingEdited = null;
		private boolean editMode = false;

		//changes the text and buttons to edit rule buttons
		public void startEditMode(Rule rule) {
			
			if(rule == null) return;
			
			editMode = true;
			ruleBeingEdited = rule;
			
			createRuleLabel.setText("Edit Rule");
			createRuleButton.setText("Confirm Changes");
			closeButton.setText("Cancel");
			
			loadRuleIntoFields(rule);
		}

		//calls all laod rules methods
		public void loadRuleIntoFields(Rule rule) {
			if(rule == null) return;
			
			clearFormFieldsOnly();
			
			ruleNameTextField.setText(rule.getRuleName());
			loadTime(rule);
			loadTargetType(rule);
			loadTargets(rule);
			loadActions(rule);
		}
		
		//gets selected rules time
		private void loadTime(Rule rule) {
		    int totalMinutes = rule.getStartTime();
		    int hour24 = totalMinutes / 60;
		    int minute = totalMinutes % 60;

		    boolean isPM = hour24 >= 12;
		    int hour12 = hour24 % 12;
		    if (hour12 == 0) hour12 = 12;

		    hoursTextField.setText(String.valueOf(hour12));
		    minutesTextField.setText(String.format("%02d", minute));

		    if (isPM) {
		        amPM.selectToggle(pmRadioButton);
		    } else {
		        amPM.selectToggle(amRadioButton);
		    }
		}
		
		//gets selected rules target type and adds it
		private void loadTargetType(Rule rule) {
		    switch (rule.getTargetType()) {
		        case HOUSE:
		            ruleRangeSelection.selectToggle(houseRuleRadioButton);
		            break;
		        case ROOM:
		            ruleRangeSelection.selectToggle(roomRuleRadioButton);
		            break;
		        case DEVICE:
		            ruleRangeSelection.selectToggle(deviceRuleRadioButton);
		            break;
		    }
		}
		
		//gets the targets form the selected rule
		private void loadTargets(Rule rule) {
		    var selectionModel = ruleTargetSelectionListView.getSelectionModel();
		    selectionModel.clearSelection();

		    if (rule.getTargetType() == Rule.TargetType.ROOM) {
		        ruleTargetSelectionListView.setItems((ObservableList) mainMenuController.getRooms());

		        for (Room room : rule.getTargetRooms()) {
		            int index = ruleTargetSelectionListView.getItems().indexOf(room);
		            if (index >= 0) {
		                selectionModel.select(index);
		            }
		        }
		    }

		    if (rule.getTargetType() == Rule.TargetType.DEVICE) {
		        ruleTargetSelectionListView.setItems((ObservableList) mainMenuController.getDevices());

		        for (Devices device : rule.getTargetDevices()) {
		            int index = ruleTargetSelectionListView.getItems().indexOf(device);
		            if (index >= 0) {
		                selectionModel.select(index);
		            }
		        }
		    }
		}
		
		//gets actions from rule and sets their inputs
		private void loadActions(Rule rule) {
		    lightsOnOff.selectToggle(null);
		    setLockStatus.selectToggle(null);
		    HVACMode.selectToggle(null);
		    HVACTempTextField.clear();

		    for (Action action : rule.getActions()) {
		        if (action instanceof BinaryAction binary) {
		            if (binary.getTargetClass() == Lights.class) {
		                if (binary.isTurnOn()) {
		                    lightsOnOff.selectToggle(onRadioButton);
		                } else {
		                    lightsOnOff.selectToggle(offRadioButton);
		                }
		            }

		            if (binary.getTargetClass() == Lock.class) {
		                if (binary.isTurnOn()) {
		                    setLockStatus.selectToggle(lockRadioButton);
		                } else {
		                    setLockStatus.selectToggle(unlockRadioButton);
		                }
		            }
		        }

		        if (action instanceof HVACAction hvacAction) {
		            if (!hvacAction.isTurnOn()) {
		                HVACMode.selectToggle(HVACOffRadioButton);
		            } else {
		                if (hvacAction.getMode() == HVAC.Mode.AC) {
		                    HVACMode.selectToggle(acRadioButton);
		                } else {
		                    HVACMode.selectToggle(headRadioButton);
		                }

		                HVACTempTextField.setText(String.valueOf(hvacAction.getTemp()));
		            }
		        }
		    }
		}
		
		//resets back to normal create rule
		private void exitEditMode() {
		    editMode = false;
		    ruleBeingEdited = null;
		    createRuleLabel.setText("Create Rule");
		    createRuleButton.setText("Create Rule");
		    closeButton.setText("Close");
		}
	
	
	//creates the dark theme
	private void applyDarkTheme() {
	    String panel = "#1E1E1E";
	    String card = "#252525";
	    String border = "#3A3A3A";
	    String text = "#F2F2F2";

	    createRuleVBox.setStyle(
	            "-fx-background-color: " + panel + ";" +
	            "-fx-padding: 14;"
	    );

	    // Do NOT style labels here — their styleProperty is already bound above

	    styleInput(ruleNameTextField, card, border, text);
	    styleInput(hoursTextField, card, border, text);
	    styleInput(minutesTextField, card, border, text);
	    styleInput(HVACTempTextField, card, border, text);

	    styleListView(ruleTargetSelectionListView, panel, border, text);
	    styleRuleTargetCells();

	    styleDarkButton(createRuleButton, "#5AA9FF", "#FFFFFF");
	    styleDarkButton(closeButton, "#444444", "#F2F2F2");

	    styleRadioButton(deviceRuleRadioButton, text);
	    styleRadioButton(houseRuleRadioButton, text);
	    styleRadioButton(roomRuleRadioButton, text);
	    styleRadioButton(headRadioButton, text);
	    styleRadioButton(acRadioButton, text);
	    styleRadioButton(HVACOffRadioButton, text);
	    styleRadioButton(lockRadioButton, text);
	    styleRadioButton(unlockRadioButton, text);
	    styleRadioButton(onRadioButton, text);
	    styleRadioButton(offRadioButton, text);
	    styleRadioButton(amRadioButton, text);
	    styleRadioButton(pmRadioButton, text);
	}
	
	private void styleRuleTargetCells() {
	    ruleTargetSelectionListView.setCellFactory(lv -> new ListCell<>() {

	        private final CheckBox checkBox = new CheckBox();

	        {
	            checkBox.setStyle(
	                    "-fx-text-fill: #F2F2F2;" +
	                    "-fx-font-family: 'Comic Sans MS';"
	            );

	            checkBox.setOnAction(e -> {
	                if (getItem() == null) return;

	                var selectionModel = getListView().getSelectionModel();

	                if (checkBox.isSelected()) {
	                    selectionModel.select(getIndex());
	                } else {
	                    selectionModel.clearSelection(getIndex());
	                }
	            });

	            setOnMousePressed(event -> {
	                if (getItem() == null) return;

	                var selectionModel = getListView().getSelectionModel();

	                if (selectionModel.isSelected(getIndex())) {
	                    selectionModel.clearSelection(getIndex());
	                    checkBox.setSelected(false);
	                } else {
	                    selectionModel.select(getIndex());
	                    checkBox.setSelected(true);
	                }

	                event.consume();
	            });
	        }

	        @Override
	        protected void updateItem(Object item, boolean empty) {
	            super.updateItem(item, empty);

	            if (empty || item == null) {
	                setGraphic(null);
	                setText(null);
	                setStyle("-fx-background-color: transparent;");
	                return;
	            }

	            if (item instanceof Room room) {
	                checkBox.setText(room.getName());
	            } else if (item instanceof Devices device) {
	                checkBox.setText(device.getName());
	            }

	            checkBox.setSelected(
	                    getListView().getSelectionModel().isSelected(getIndex())
	            );

	            checkBox.setCursor(Cursor.HAND);
	            checkBox.setOnMousePressed(e -> e.consume());

	            if (isSelected()) {
	                setStyle("-fx-background-color: #2F4F6B;");
	            } else {
	                setStyle("-fx-background-color: #252525;");
	            }

	            setGraphic(checkBox);
	        }

	        @Override
	        public void updateSelected(boolean selected) {
	            super.updateSelected(selected);

	            if (!isEmpty()) {
	                if (selected) {
	                    setStyle("-fx-background-color: #2F4F6B;");
	                } else {
	                    setStyle("-fx-background-color: #252525;");
	                }
	            }
	        }
	    });
	}
	
	private void styleRadioButton(RadioButton rb, String textColor) {
	    rb.styleProperty().unbind();
	    rb.styleProperty().bind(
	            Bindings.concat(
	                    "-fx-text-fill: ", textColor, "; ",
	                    "-fx-font-family: 'Comic Sans MS'; ",
	                    "-fx-font-size: ",
	                    createRuleVBox.widthProperty().multiply(0.04)
	            )
	    );
	}
	
	private void styleInput(javafx.scene.control.TextField field, String bg, String border, String text) {
	    field.setStyle(
	            "-fx-background-color: " + bg + ";" +
	            "-fx-text-fill: " + text + ";" +
	            "-fx-prompt-text-fill: #888888;" +
	            "-fx-border-color: " + border + ";" +
	            "-fx-border-radius: 8;" +
	            "-fx-background-radius: 8;" +
	            "-fx-font-family: 'Comic Sans MS';"
	    );
	}
	
	private <T> void styleListView(ListView<T> listView, String bg, String border, String text) {
	    listView.setStyle(
	            "-fx-control-inner-background: " + bg + ";" +
	            "-fx-background-color: " + bg + ";" +
	            "-fx-border-color: " + border + ";" +
	            "-fx-background-radius: 10;" +
	            "-fx-border-radius: 10;" +
	            "-fx-text-fill: " + text + ";"
	    );
	}
	
	private void styleDarkButton(Button button, String bgColor, String textColor) {
	    String normalStyle =
	            "-fx-background-color: " + bgColor + ";" +
	            "-fx-text-fill: " + textColor + ";" +
	            "-fx-font-family: 'Comic Sans MS';" +
	            "-fx-font-size: 14px;" +
	            "-fx-font-weight: bold;" +
	            "-fx-background-radius: 10;" +
	            "-fx-border-radius: 10;" +
	            "-fx-cursor: hand;" +
	            "-fx-padding: 8 12 8 12;";

	    String hoverStyle =
	            "-fx-background-color: derive(" + bgColor + ", 15%);" +
	            "-fx-text-fill: " + textColor + ";" +
	            "-fx-font-family: 'Comic Sans MS';" +
	            "-fx-font-size: 14px;" +
	            "-fx-font-weight: bold;" +
	            "-fx-background-radius: 10;" +
	            "-fx-border-radius: 10;" +
	            "-fx-cursor: hand;" +
	            "-fx-padding: 8 12 8 12;";

	    button.setStyle(normalStyle);

	    button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
	    button.setOnMouseExited(e -> button.setStyle(normalStyle));
	}
	
}
