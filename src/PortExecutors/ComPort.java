package PortExecutors;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JOptionPane;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import GUI.Controller;
import jssc.SerialPort;
import jssc.SerialPortException;

public class ComPort {
	private static SerialPort gettingPort, setterPort;
	private static Byte startEndFlag = (byte) 0xA7;
	private static Byte accessTokenControl = 0x00;
	private static Byte accessPackageControl = 0x10;
	private static byte[] dataToSend = null;

	// Creation of token
	public static byte[] getToken() {
		return new byte[] { startEndFlag, accessTokenControl, startEndFlag };
	}

	// Creation of package
	public static void createPackage(String data, Byte source, Byte destinition) {
		ArrayList<Byte> packData = new ArrayList<Byte>(
				Arrays.asList(startEndFlag, accessPackageControl, source, destinition));
		packData.addAll(toByteList(data.getBytes()));
		packData.add(startEndFlag);
		dataToSend = tobyte(packData.toArray(new Byte[packData.size()]));
	}

	// Initialization of COM ports
	public static void initializeGetPort(Controller controller, String port) throws SerialPortException {
		gettingPort = new SerialPort(port);
		gettingPort.openPort();
		gettingPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
		gettingPort.addEventListener(new PortReader(gettingPort, controller.getArea(), (byte) 0x01));
	}

	public static void initializeSetPort(Controller controller, String port) throws SerialPortException {
		setterPort = new SerialPort(port);
		setterPort.openPort();
		setterPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
	}

	public static void writeData(byte[] data)
			throws SerialPortException, UnsupportedEncodingException, InterruptedException {
		setterPort.writeBytes(data);
	}

	public static byte[] getDataToSend() {
		return dataToSend;
	}

	public static ArrayList<Byte> toByteList(byte[] arrayByte) {
		ArrayList<Byte> list = new ArrayList<Byte>();
		for (int i = 0; i < arrayByte.length; i++) {
			list.add(arrayByte[i]);
		}
		return list;
	}

	public static byte[] tobyte(Byte[] arrayByte) {
		byte[] array = new byte[arrayByte.length];
		for (int i = 0; i < arrayByte.length; i++) {
			array[i] = arrayByte[i];
		}
		return array;
	}

	public static void setNullDataToSend() {
		dataToSend = null;
	}

	public static SerialPort getGettingPort() {
		return gettingPort;
	}

	public static SerialPort getSetterPort() {
		return setterPort;
	}

}
