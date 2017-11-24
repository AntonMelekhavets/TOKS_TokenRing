
import java.io.IOException;

import GUI.Controller;
import PortExecutors.ComPort;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jssc.SerialPortException;

public class TOKS_LAB5 extends Application {

	private Stage baseStage;
	private BorderPane rootForWindow;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage baseStage) throws Exception {
		this.baseStage = baseStage;
		showChatWindows();
	}

	public void showChatWindows() throws IOException, SerialPortException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(TOKS_LAB5.class.getResource("GUI/Window.fxml"));
		AnchorPane chatOverwiew = (AnchorPane) loader.load();
		Scene scene = new Scene(chatOverwiew);
		baseStage.setScene(scene);
		baseStage.show();
		baseStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				try {
					ComPort.getGettingPort().closePort();
					ComPort.getSetterPort().closePort();
				} catch (SerialPortException | NullPointerException ex) {
					// TODO Auto-generated catch block
					baseStage.close();
					ex.printStackTrace();
				}
			}
		});
		Controller controller = loader.getController();
	}

}
