package be.webtechie.drumbooth.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.pi4j.util.Console;

import java.io.IOException;

public class SerialLink {

    private final Console console;
    private final String portPath;
    private SerialPort port = null;

    public SerialLink(Console console, String portPath) {
        this.console = console;
        this.portPath = portPath;
        openPort();
    }

    private void openPort() {
        if (port != null) {
            console.println("Closing " + portPath);
            port.closePort();
        }
        try {
            port = null; //set to null in case getCommPort throws, port will remain null.
            port = SerialPort.getCommPort(this.portPath);
            port.setBaudRate(115_200);
            port.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
            port.openPort(0, 8192, 8192);
            console.println("Opening " + portPath);
        } catch (Exception e) {
            console.println("Could not open serial port " + e.getMessage());
        }
    }

    private void closePort() {
        if (port != null) {
            console.println("Closing " + portPath);
            try {
                // Make sure all pending commands are sent before closing the port
                port.getOutputStream().flush();
            } catch (IOException e) {
                console.println("Error while flushing the data: " + e.getMessage());
            }
            port.closePort();
        }
    }

    public void write(byte[] data) {
        int lastErrorCode = port != null ? port.getLastErrorCode() : 0;
        int lastErrorLocation = port != null ? port.getLastErrorLocation() : 0;
        boolean isOpen = port != null && port.isOpen();
        if (port == null || !isOpen || lastErrorCode != 0) {
            console.println("Port was open:" + isOpen + ", last error:" + lastErrorCode + " " + lastErrorLocation);
            openPort();
        }
        port.writeBytes(data, data.length);
    }
}