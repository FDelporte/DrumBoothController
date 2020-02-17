package be.webtechie.drumbooth.event;

import be.webtechie.drumbooth.led.LedCommand;
import com.pi4j.io.serial.Serial;
import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private final Serial serial;

    public EventManager(Serial serial) {
        this.serial = serial;
    }

    /**
     * The list with components to be notified of a new LedCommand received from Mosquitto.
     */
    private List<EventListener> eventListeners = new ArrayList<>();

    /**
     * Used by every component which wants to be notified of new events.
     *
     * @param eventListener {@link EventListener}
     */
    public void addListener(EventListener eventListener) {
        this.eventListeners.add(eventListener);
    }

    /**
     * Used by every component which wants to be send a {@link LedCommand} to the Arduino.
     *
     * @param ledCommand {@link LedCommand}
     */
    public void sendEvent(LedCommand ledCommand) {
        this.eventListeners.forEach(l -> l.onChange(ledCommand));

        try {
            // Write a text to the Arduino, as demo
            this.serial.writeln(ledCommand.toCommandString());
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
}
