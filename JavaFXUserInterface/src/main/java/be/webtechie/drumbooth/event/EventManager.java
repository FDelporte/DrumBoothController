package be.webtechie.drumbooth.event;

import be.webtechie.drumbooth.led.LedCommand;
import com.pi4j.io.serial.Serial;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class EventManager {

    private static Logger logger = Logger.getLogger(EventManager.class);

    private final Serial serial;
    private LedCommand lastLedCommand = null;

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
    public void sendSerialCommand(LedCommand ledCommand) {
        this.lastLedCommand = ledCommand;
        this.eventListeners.forEach(l -> l.onChange(ledCommand));

        try {
            this.serial.writeln(ledCommand.toCommandString());
        } catch (Exception ex) {
            logger.error("Error while sending : " + ex.getMessage());
        }
    }

    /**
     * @return The last {@link LedCommand} sent to the Arduino.
     */
    public LedCommand getLastLedCommand() {
        return this.lastLedCommand;
    }
}
