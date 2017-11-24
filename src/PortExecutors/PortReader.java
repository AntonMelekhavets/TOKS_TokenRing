package PortExecutors;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class PortReader implements SerialPortEventListener {

	private SerialPort serialPort;
	private TextArea area;
	private byte[] data;
	private static byte myAdress;

	public PortReader(SerialPort serialPort, TextArea area, byte myAdress) {
		this.serialPort = serialPort;
		this.area = area;
		this.myAdress = myAdress;
	}

	public static void setMyAdress(byte adress) {
		myAdress = adress;
	}

	// Algorithm of reading data from COM port
	public void serialEvent(SerialPortEvent event) {
		if (event.isRXCHAR() && event.getEventValue() >= 0) {
			try {
				data = serialPort.readBytes();
				System.out.println(data.toString());
				if (checkOnToken()) {
					if (checkSource()) {
						ComPort.writeData(ComPort.getToken());
						return;
					}
					if (checkDestinition()) {
						Platform.runLater(new WindowChanger(area, getStringFromBytes(toByteArray(data))));
						ComPort.writeData(data);
					} else
						ComPort.writeData(data);
				} else {
					if (checkDataToSend()) {
						ComPort.writeData(ComPort.getDataToSend());
						ComPort.setNullDataToSend();
					} else
						ComPort.writeData(data);
				}
			} catch (SerialPortException | UnsupportedEncodingException | InterruptedException ex) {
				System.out.println(ex);
			}
		}
	}

	private boolean checkOnToken() {
		return data[1] != 0x00;//
	}

	private boolean checkDestinition() {
		return data[3] == myAdress;
	}

	private boolean checkSource() {
		return data[2] == myAdress;
	}

	private boolean checkDataToSend() {
		return ComPort.getDataToSend() != null;
	}

	private String getStringFromBytes(ArrayList<Byte> data) throws UnsupportedEncodingException {
		Byte[] dataIntoBytes = data.toArray(new Byte[data.size()]);
		return new String(ComPort.tobyte(dataIntoBytes), "UTF-8");
	}

	private ArrayList<Byte> toByteArray(byte[] array) {
		ArrayList<Byte> arrayOfByte = new ArrayList<Byte>();
		for (int i = 0; i < array.length; i++)
			arrayOfByte.add(array[i]);
		arrayOfByte.remove(0);
		arrayOfByte.remove(arrayOfByte.size() - 1);
		return arrayOfByte;
	}

}
