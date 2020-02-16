package be.webtechie.drumbooth.serial;

import be.webtechie.drumbooth.led.LedCommand;
import com.pi4j.io.serial.Serial;

/**
 * Send a command to the serial device
 */
public class SerialSender {

    public static void sendData(Serial serial, LedCommand ledCommand) {
        try {
            // Write a text to the Arduino, as demo
            serial.writeln(ledCommand.toCommandString());
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
}