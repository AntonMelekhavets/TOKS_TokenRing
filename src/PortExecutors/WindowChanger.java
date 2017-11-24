package PortExecutors;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

//Class for changing of JavaFX application
public class WindowChanger implements Runnable {
	private TextArea area;
	private String data;

	public WindowChanger(TextArea area, String data) {
		this.area = area;
		this.data = data;
	}

	@FXML
	public void run() {
		area.setText("ToMe:" + data + "\n");
	}
}
