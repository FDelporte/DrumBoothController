package be.webtechie.drumbooth.event;

import be.webtechie.drumbooth.led.LedCommand;
import be.webtechie.drumbooth.relay.RelayCommand;
import be.webtechie.drumbooth.relay.Relay;
import be.webtechie.drumbooth.serial.SerialLink;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.util.Console;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

public class EventManager {

    private final Console console;
    private final Context pi4j;
    private final SerialLink serial;
    private final EnumMap<Relay, DigitalOutput> outputs;

    /**
     * The list with components to be notified of a new LedCommand received from Mosquitto.
     */
    private final List<EventListener> eventListeners = new ArrayList<>();
    private LedCommand lastLedCommand = null;

    public EventManager(Console console, String serialPort) {
        this.console = console;
        this.pi4j = Pi4J.newAutoContext();
        this.serial = new SerialLink(console, serialPort);

        // Configure the relay outputs
        this.outputs = new EnumMap<>(Relay.class);
        Stream.of(Relay.values()).forEach(this::initRelay);
    }

    private void initRelay(Relay relay) {
        try {
            var relayConfig = DigitalOutput.newConfigBuilder(pi4j)
                    .id(relay.name())
                    .name("LED Flasher")
                    .address(relay.getBcm())
                    .shutdown(DigitalState.LOW)
                    .initial(relay.getInitialState());
            var relayPin = pi4j.create(relayConfig);
            this.outputs.put(relay, relayPin);
            console.println("DigitalOutput initialized: " + relay.getBcm() + " with state " + relay.getInitialState());
        } catch (Exception e) {
            console.println("Can't initialize relay " + relay + ": " + e.getMessage());
        }
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
            console.println("Error while sending: " + ex.getMessage());
        }
    }

    public void sendRelayCommand(RelayCommand relayCommand) {
        this.eventListeners.forEach(l -> l.onRelayChange(relayCommand));
        var output = this.outputs.get(relayCommand.relay());
        if (output == null) {
            console.println("Can't execute relay command, relay output is not defined for " + relayCommand.relay());
        } else {
            output.setState(relayCommand.state().getValue().intValue());
            console.println("Relay " + relayCommand.relay() + " is set to " + relayCommand.state().getValue().intValue());
        }
    }

    /**
     * Set all relays off
     */
    public void setAllOff() {
        setRelays(Arrays.stream(Relay.values()).toList(), DigitalState.LOW);
    }

    /**
     * Set the state of the all the relays on the given boards.
     *
     * @param relays List of Enum Relay
     * @param state {@link DigitalState}
     */
    public void setRelays(List<Relay> relays, DigitalState state) {
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
