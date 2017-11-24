package GUI;

import java.io.UnsupportedEncodingException;

import PortExecutors.ComPort;
import PortExecutors.PortReader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import jssc.SerialPortException;

public class Controller {
	@FXML
	private static Button sendButton;
	@FXML
	private ChoiceBox<String> getPortChoice;
	@FXML
	private ChoiceBox<String> sendPortChoice;
	@FXML
	private ChoiceBox<String> recipientChoice;
	@FXML
	private TextArea area;
	@FXML
	private TextField field;
	@FXML
	private ChoiceBox<String> myAddress;

	private static Byte source;
	private Byte destinition;
	private byte[] data = null;

	@FXML
	public void initialize() throws SerialPortException {
		Controller controller = this;
		myAddress.setItems(FXCollections.observableArrayList("1", "2", "3", "4"));
		myAddress.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				source = number2.byteValue();
				PortReader.setMyAdress(source);
				if (source == 3)
					try {
						ComPort.writeData(ComPort.getToken());
					} catch (UnsupportedEncodingException | SerialPortException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
		recipientChoice.setItems(FXCollections.observableArrayList("1", "2", "3", "4"));
		recipientChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				destinition = number2.byteValue();
			}
		});
		ObservableList<String> listOfComPorts = FXCollections.observableArrayList("COM1", "COM2", "COM3", "COM4",
				"COM5", "COM6", "COM7", "COM8", "COM9", "COM10");
		sendPortChoice.setItems(listOfComPorts);
		sendPortChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				try {
					ComPort.initializeSetPort(controller, sendPortChoice.getItems().get(number2.intValue()));
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		getPortChoice.setItems(listOfComPorts);
		getPortChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				try {
					ComPort.initializeGetPort(controller, getPortChoice.getItems().get(number2.intValue()));
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@FXML
	public void sendData() throws UnsupportedEncodingException, SerialPortException, InterruptedException {
		ComPort.createPackage(field.getText(), source, destinition);
	}

	public TextArea getArea() {
		return area;
	}
	
	public static Byte getMyAdress() {
		return source;
	}

	public ChoiceBox<String> getGetPortChoice() {
		return getPortChoice;
	}

	public ChoiceBox<String> getSendPortChoice() {
		return sendPortChoice;
	}

	public ChoiceBox<String> getRecipientChoice() {
		return recipientChoice;
	}
}
