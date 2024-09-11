package be.webtechie.drumbooth.event;

import be.webtechie.drumbooth.led.LedCommand;
import be.webtechie.drumbooth.relay.RelayCommand;
import be.webtechie.drumbooth.relay.definition.Relay;
import be.webtechie.drumbooth.relay.definition.State;
import be.webtechie.drumbooth.serial.SerialLink;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.util.Console;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventManager {

    private final Console console;
    private final Context pi4j;
    private final SerialLink serial;

    /**
     * The list with components to be notified of a new LedCommand received from Mosquitto.
     */
    private final List<EventListener> eventListeners = new ArrayList<>();
    private LedCommand lastLedCommand = null;

    public EventManager(Console console, String serialPort) {
        this.console = console;
        this.pi4j = Pi4J.newAutoContext();
        this.serial = new SerialLink(console, serialPort);
    }

    /**
     * Convert the value to HEX byte string.
     *
     * @param value Numeric value
     * @return String value in HEX format
     */
    private static String toHexString(int value) {
        return String.format("0x%02X", value).substring(0, 4);
    }

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
        this.eventListeners.forEach(l -> l.onLedStripChange(ledCommand));

        try {
            serial.write(ledCommand.toCommandString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            console.println("Error while sending: {}", ex.getMessage());
        }
    }

    public void sendRelayCommand(RelayCommand relayCommand) {
        this.eventListeners.forEach(l -> l.onRelayChange(relayCommand));

        // TODO execute relays state

        console.println("{} set to {}",
                relayCommand.relay(), relayCommand.state());
    }

    /**
     * Set all relays off
     */
    public void setAllOff() {
        setRelays(Arrays.stream(Relay.values()).toList(), State.STATE_OFF);
    }

    /**
     * Set the state of the all the relays on the given boards.
     *
     * @param relays List of Enum Relay
     * @param state  Enum State
     */
    public void setRelays(List<Relay> relays, State state) {
        for (Relay relay : relays) {
            this.sendRelayCommand(new RelayCommand(relay, state));
        }
    }

    /**
     * @return The last {@link LedCommand} sent to the Arduino.
     */
    public LedCommand getLastLedCommand() {
        return this.lastLedCommand;
    }
}
