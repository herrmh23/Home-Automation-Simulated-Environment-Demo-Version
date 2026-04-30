package smartHomeProject;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class RegisterDeviceController {

	private MainMenuController mainMenuController;

	public void setMainMenuController(MainMenuController controller) {
		this.mainMenuController = controller;
	}

	@FXML
	private Label registerDeviceLabel;

	@FXML
	private Button closeButton;

	@FXML
	private Button deleteDeviceButton;

	@FXML
	private TextField deviceIDTextField;

	@FXML
	private ListView<Devices> deviceListView;

	@FXML
	private TextField deviceNameTextField;

	@FXML
	private Label errorLabel;

	@FXML
	private Button registerDeviceButton;

	@FXML
	private VBox registerDeviceVBox;

	@FXML
	private Label deviceNameLabel;

	@FXML
	private Label roomSelectionLabel;

	@FXML
	private Label deviceIDLabel;

	@FXML
	private ComboBox<Room> roomSelectionComboBox;


	public void initialize() {


		registerDeviceLabel.styleProperty().bind(
				Bindings.concat(
						"-fx-text-fill: #F2F2F2; ",
						"-fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
						registerDeviceVBox.widthProperty().multiply(0.1))
				);

		deviceNameLabel.styleProperty().bind( //–––––––––
				Bindings.concat(
						"-fx-text-fill: #F2F2F2; ",
						"-fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
						registerDeviceVBox.widthProperty().multiply(0.05))
				);

		deviceNameTextField.prefHeightProperty().bind(
				registerDeviceVBox.heightProperty().multiply(0.05)
				);

		deviceIDLabel.styleProperty().bind(
				Bindings.concat(
						"-fx-text-fill: #F2F2F2; ",
						"-fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
						registerDeviceVBox.widthProperty().multiply(0.05)) //––––––––––
				);

		deviceIDTextField.prefHeightProperty().bind(
				registerDeviceVBox.heightProperty().multiply(0.05)
				);

		roomSelectionLabel.styleProperty().bind(
				Bindings.concat(
						"-fx-text-fill: #F2F2F2; ",
						"-fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
						registerDeviceVBox.widthProperty().multiply(0.05)) //–––––––––
				);

		roomSelectionComboBox.prefHeightProperty().bind(
				registerDeviceVBox.heightProperty().multiply(0.05)
				);

		registerDeviceButton.prefHeightProperty().bind(
				registerDeviceVBox.heightProperty().multiply(0.05)
				);

		closeButton.prefHeightProperty().bind(
				registerDeviceVBox.heightProperty().multiply(0.05)
				);

		deleteDeviceButton.prefHeightProperty().bind(
				registerDeviceVBox.heightProperty().multiply(0.05)
				);

		//closes right side pannel
		closeButton.setOnAction(e -> mainMenuController.cancelAnything());


		//makes it so the drop down menu only shows room name, and number of devices
		roomSelectionComboBox.setCellFactory(cb -> new ListCell<>() {
			@Override
			protected void updateItem(Room room, boolean empty) {
				super.updateItem(room, empty);
				setText(empty || room == null ? null : room.getName() + " [Devices: " + room.getDevices().size() + "]");
			}
		});

		roomSelectionComboBox.setButtonCell(new ListCell<>() {
			@Override
			protected void updateItem(Room room, boolean empty) {
				super.updateItem(room, empty);
				setText(empty || room == null ? null : room.getName());
			}
		});



		//register device button –––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––
		registerDeviceButton.setOnAction(e -> {
			String name = deviceNameTextField.getText().trim().toLowerCase();
			String ID = deviceIDTextField.getText().trim().toLowerCase();
			Boolean exists = false;

			//gets the type of device from the deviceID
			String type = ID.replaceAll("\\d+$", "");
			System.out.println(type);

			for(Devices d: mainMenuController.getSmartHomeSystem().getDevices()) {
				if(d.getName().equalsIgnoreCase(name)) {
					exists = true;

					//output name exists error message
					errorLabel.setText("Device " + name + " already exists");
					errorLabel.styleProperty().bind(
							Bindings.concat(
									"-fx-text-fill: red; -fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
									registerDeviceVBox.widthProperty().multiply(0.05))
							);
					break;
				}
				else if(d.getDeviceID().equals(ID)) {
					exists = true;

					//output name exists error message
					errorLabel.setText("DeviceID " + ID + " already registered");
					errorLabel.styleProperty().bind(
							Bindings.concat(
									"-fx-text-fill: red; -fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
									registerDeviceVBox.widthProperty().multiply(0.05))
							);
				}
			} //close for loop

			if (roomSelectionComboBox.getValue() == null) {
				errorLabel.setText("Please select a room");
				errorLabel.styleProperty().bind(
						Bindings.concat(
								"-fx-text-fill: red; -fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
								registerDeviceVBox.widthProperty().multiply(0.05))
						);
				return;
			}

			if (deviceNameTextField.getText().isEmpty()) {
				errorLabel.setText("Please give device a name");
				errorLabel.styleProperty().bind(
						Bindings.concat(
								"-fx-text-fill: red; -fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
								registerDeviceVBox.widthProperty().multiply(0.05))
						);
				return;
			}

			if (deviceIDTextField.getText().isEmpty()) {
				errorLabel.setText("Please select a device to register");
				errorLabel.styleProperty().bind(
						Bindings.concat(
								"-fx-text-fill: red; -fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
								registerDeviceVBox.widthProperty().multiply(0.05))
						);
				return;
			}

			if(!exists && !name.isEmpty() && !ID.isEmpty() && roomSelectionComboBox.getValue() != null) {
				if(type.equals("light")) {
					Lights light = new Lights(name, ID);
					mainMenuController.getSmartHomeSystem().registerDevice(light);
					roomSelectionComboBox.getValue().addDevice(light);
					mainMenuController.getRegisterRoomController().refreshRoomList();
					deviceNameTextField.clear();
					deviceIDTextField.clear();
					roomSelectionComboBox.getSelectionModel().clearSelection();

					errorLabel.setText("Light device " + name + " registered");
					errorLabel.styleProperty().bind(
							Bindings.concat(
									"-fx-text-fill: green; -fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
									registerDeviceVBox.widthProperty().multiply(0.05))
							);
				} else if(type.equals("hvac")) {
					HVAC hvac = new HVAC(name, ID);
					mainMenuController.getSmartHomeSystem().registerDevice(hvac);
					mainMenuController.getRegisterRoomController().refreshRoomList();
					roomSelectionComboBox.getValue().addDevice(hvac);
					deviceNameTextField.clear();
					deviceIDTextField.clear();
					roomSelectionComboBox.getSelectionModel().clearSelection();

					errorLabel.setText("HVAC device " + name + " registered");
					errorLabel.styleProperty().bind(
							Bindings.concat(
									"-fx-text-fill: green; -fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
									registerDeviceVBox.widthProperty().multiply(0.05))
							);
				} else if(type.equals("lock")) {
					Lock lock = new Lock(name, ID);
					mainMenuController.getSmartHomeSystem().registerDevice(lock);
					roomSelectionComboBox.getValue().addDevice(lock);
					mainMenuController.getRegisterRoomController().refreshRoomList();
					deviceNameTextField.clear();
					deviceIDTextField.clear();
					roomSelectionComboBox.getSelectionModel().clearSelection();

					errorLabel.setText("Lock device " + name + " registered");
					errorLabel.styleProperty().bind(
							Bindings.concat(
									"-fx-text-fill: green; -fx-font-family: 'Comic Sans MS'; -fx-font-size: ",
									registerDeviceVBox.widthProperty().multiply(0.05))
							);
				}

			} 
			mainMenuController.getHomeViewController().setupDeviceNodes();
			mainMenuController.getHomeViewController().updateDevice();
		} 

		
		
				); //close register device button
		
		deleteDeviceButton.setOnAction(e -> {
			Devices selected = deviceListView.getSelectionModel().getSelectedItem();
			if(selected != null) {
				selected.setOn(false);
				mainMenuController.getHomeViewController().applyLightEffect(selected.getNode(), false);
				mainMenuController.getHomeViewController().animateColor(selected.getNode(), Color.web("#9e9e9e"));
				mainMenuController.getSmartHomeSystem().getDevices().remove(selected);
				for(Room r : mainMenuController.getSmartHomeSystem().getRooms()) {
					if(r.getDevices().contains(selected)) r.getDevices().remove(selected);
				}
			}
		}
				);
		
		
		deviceListView.setCellFactory(lv -> new ListCell<>() {

		    @Override
		    protected void updateItem(Devices device, boolean empty) {
		        super.updateItem(device, empty);

		        textProperty().unbind();

		        if (empty || device == null) {
		            setText(null);
		            return;
		        }

		        StringExpression status;

		     // Special case for Locks
		     if (device instanceof Lock) {
		         status = Bindings.when(device.isOnProperty())
		                 .then("LOCKED")
		                 .otherwise("UNLOCKED");
		     } else {
		         status = Bindings.when(device.isOnProperty())
		                 .then("ON")
		                 .otherwise("OFF");
		     }

		     StringExpression base = Bindings.concat(
		         device.getName(), " (", device.getDeviceID(), ") - ",
		         status
		     );
		      
		        

		        // HVAC special case
		        if (device instanceof HVAC hvac) {

		            textProperty().bind(
		                Bindings.concat(
		                    base,
		                    " (",
		                    hvac.modeProperty().asString(),
		                    " ",
		                    hvac.tempProperty().asString(),
		                    "°)"
		                )
		            );

		        } else {
		            textProperty().bind(base);
		        }
		    }
		});

		applyDarkTheme();
		//styles scroll bar
				Platform.runLater(() -> {
				    styleScrollBars(deviceListView);
				});
	} //close initialize



	public void setDeviceID(String id) {
		deviceIDTextField.setText(id);
	}

	public void closeButton() {
		deviceNameTextField.clear();
		deviceIDTextField.clear();
		roomSelectionComboBox.getSelectionModel().clearSelection();
		errorLabel.setText(null);
	}

	//errases error message
	public void clearErrorMessage() {
		errorLabel.setText(null);
	}

	public void initData() {
		roomSelectionComboBox.setItems(mainMenuController.getSmartHomeSystem().getRooms());
		deviceListView.setItems(mainMenuController.getSmartHomeSystem().getDevices());
	}

	
	//create dark theme
	private void applyDarkTheme() {
	    String panel = "#1E1E1E";
	    String card = "#252525";
	    String border = "#3A3A3A";
	    String text = "#F2F2F2";
	    String muted = "#B8B8B8";
	    String selected = "#2F4F6B";

	    registerDeviceVBox.setStyle(
	            "-fx-background-color: " + panel + ";" +
	            "-fx-padding: 14;"
	    );

	    // Do NOT style bound labels here

	    styleInput(deviceNameTextField, card, border, text);
	    styleInput(deviceIDTextField, card, border, text);

	    roomSelectionComboBox.setStyle(
	            "-fx-background-color: " + card + ";" +
	            "-fx-text-fill: " + text + ";" +
	            "-fx-border-color: " + border + ";" +
	            "-fx-background-radius: 8;" +
	            "-fx-border-radius: 8;" +
	            "-fx-font-family: 'Comic Sans MS';"
	    );

	    styleComboBoxCells(text, card, selected);
	    styleDeviceListView(panel, card, border, text, muted, selected);

	    styleDarkButton(registerDeviceButton, "#8C64D8", "#FFFFFF");
	    styleDarkButton(deleteDeviceButton, "#A94442", "#FFFFFF");
	    styleDarkButton(closeButton, "#444444", "#F2F2F2");
	}
	
	private void styleComboBoxCells(String text, String card, String selected) {
	    roomSelectionComboBox.setCellFactory(cb -> new ListCell<>() {
	        @Override
	        protected void updateItem(Room room, boolean empty) {
	            super.updateItem(room, empty);

	            if (empty || room == null) {
	                setText(null);
	                setStyle("-fx-background-color: " + card + "; -fx-text-fill: " + text + ";");
	            } else {
	                setText(room.getName() + " [Devices: " + room.getDevices().size() + "]");
	                setStyle("-fx-background-color: " + card + "; -fx-text-fill: " + text + "; -fx-font-family: 'Comic Sans MS';");
	            }
	        }

	        @Override
	        public void updateSelected(boolean isSelected) {
	            super.updateSelected(isSelected);
	            if (!isEmpty()) {
	                if (isSelected) {
	                    setStyle("-fx-background-color: " + selected + "; -fx-text-fill: " + text + "; -fx-font-family: 'Comic Sans MS';");
	                } else {
	                    setStyle("-fx-background-color: " + card + "; -fx-text-fill: " + text + "; -fx-font-family: 'Comic Sans MS';");
	                }
	            }
	        }
	    });

	    roomSelectionComboBox.setButtonCell(new ListCell<>() {
	        @Override
	        protected void updateItem(Room room, boolean empty) {
	            super.updateItem(room, empty);
	            setText(empty || room == null ? null : room.getName());
	            setStyle("-fx-background-color: " + card + "; -fx-text-fill: " + text + "; -fx-font-family: 'Comic Sans MS';");
	        }
	    });
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
	
	private void styleDeviceListView(String panel, String card, String border, String text, String muted, String selected) {
	    deviceListView.setStyle(
	            "-fx-control-inner-background: " + panel + ";" +
	            "-fx-background-color: " + panel + ";" +
	            "-fx-border-color: " + border + ";" +
	            "-fx-background-radius: 10;" +
	            "-fx-border-radius: 10;"
	    );

	    deviceListView.setCellFactory(lv -> new ListCell<>() {
	        @Override
	        protected void updateItem(Devices device, boolean empty) {
	            super.updateItem(device, empty);

	            textProperty().unbind();

	            if (empty || device == null) {
	                setText(null);
	                setStyle("-fx-background-color: transparent; -fx-text-fill: " + text + ";");
	                return;
	            }

	            StringExpression status;

	            if (device instanceof Lock) {
	                status = Bindings.when(device.isOnProperty())
	                        .then("LOCKED")
	                        .otherwise("UNLOCKED");
	            } else {
	                status = Bindings.when(device.isOnProperty())
	                        .then("ON")
	                        .otherwise("OFF");
	            }

	            StringExpression base = Bindings.concat(
	                    device.getName(), " (", device.getDeviceID(), ") - ",
	                    status
	            );

	            if (device instanceof HVAC hvac) {
	                textProperty().bind(
	                        Bindings.concat(
	                                base,
	                                " (",
	                                hvac.modeProperty().asString(),
	                                " ",
	                                hvac.tempProperty().asString(),
	                                "°)"
	                        )
	                );
	            } else {
	                textProperty().bind(base);
	            }

	            if (isSelected()) {
	                setStyle(
	                        "-fx-background-color: " + selected + ";" +
	                        "-fx-text-fill: " + text + ";" +
	                        "-fx-font-family: 'Comic Sans MS';"
	                );
	            } else {
	                setStyle(
	                        "-fx-background-color: " + card + ";" +
	                        "-fx-text-fill: " + text + ";" +
	                        "-fx-font-family: 'Comic Sans MS';"
	                );
	            }
	        }

	        @Override
	        public void updateSelected(boolean isSelected) {
	            super.updateSelected(isSelected);
	            if (!isEmpty()) {
	                if (isSelected()) {
	                    setStyle(
	                            "-fx-background-color: " + selected + ";" +
	                            "-fx-text-fill: " + text + ";" +
	                            "-fx-font-family: 'Comic Sans MS';"
	                    );
	                } else {
	                    setStyle(
	                            "-fx-background-color: " + card + ";" +
	                            "-fx-text-fill: " + text + ";" +
	                            "-fx-font-family: 'Comic Sans MS';"
	                    );
	                }
	            }
	        }
	    });
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
	
	private void styleScrollBars(Node node) {
	    node.lookupAll(".scroll-bar").forEach(scroll -> {
	        scroll.setStyle(
	                "-fx-background-color: transparent;"
	        );
	    });

	    node.lookupAll(".scroll-bar .track").forEach(track -> {
	        track.setStyle(
	                "-fx-background-color: #1E1E1E;" +
	                "-fx-background-radius: 8;"
	        );
	    });

	    node.lookupAll(".scroll-bar .thumb").forEach(thumb -> {
	        thumb.setStyle(
	                "-fx-background-color: #3A3A3A;" +
	                "-fx-background-radius: 8;"
	        );
	    });

	    node.lookupAll(".scroll-bar .thumb:hover").forEach(thumb -> {
	        thumb.setStyle(
	                "-fx-background-color: #5A5A5A;" +
	                "-fx-background-radius: 8;"
	        );
	    });
	}
	
	
}// close class


